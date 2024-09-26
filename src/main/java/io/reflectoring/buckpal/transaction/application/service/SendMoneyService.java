package io.reflectoring.buckpal.transaction.application.service;

import io.reflectoring.buckpal.common.MoneySuccessfullySentEvent;
import io.reflectoring.buckpal.transaction.application.port.in.SendMoneyCommand;
import io.reflectoring.buckpal.transaction.application.port.in.SendMoneyUseCase;
import io.reflectoring.buckpal.transaction.application.port.out.AccountLock;
import io.reflectoring.buckpal.transaction.application.port.out.LoadAccountPort;
import io.reflectoring.buckpal.transaction.application.port.out.MoneySuccessfullySentEventPublisherPort;
import io.reflectoring.buckpal.transaction.application.port.out.UpdateAccountStatePort;
import io.reflectoring.buckpal.transaction.domain.Account;
import io.reflectoring.buckpal.transaction.domain.Account.AccountId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Component
@Transactional
public class SendMoneyService implements SendMoneyUseCase {

	private final LoadAccountPort loadAccountPort;
	private final AccountLock accountLock;
	private final UpdateAccountStatePort updateAccountStatePort;
	private final MoneyTransferProperties moneyTransferProperties;
	private final MoneySuccessfullySentEventPublisherPort moneySuccessfullySentEventPublisherPort;

	@Override
	public boolean sendMoney(SendMoneyCommand command) {

		checkThreshold(command);

		LocalDateTime baselineDate = LocalDateTime.now().minusDays(10);

		Account sourceAccount = loadAccountPort.loadAccount(
				command.sourceAccountId(),
				baselineDate);

		Account targetAccount = loadAccountPort.loadAccount(
				command.targetAccountId(),
				baselineDate);

		AccountId sourceAccountId = sourceAccount.getId()
				.orElseThrow(() -> new IllegalStateException("expected source account ID not to be empty"));
		AccountId targetAccountId = targetAccount.getId()
				.orElseThrow(() -> new IllegalStateException("expected target account ID not to be empty"));

		accountLock.lockAccount(sourceAccountId);
		if (!sourceAccount.withdraw(command.money(), targetAccountId)) {
			accountLock.releaseAccount(sourceAccountId);
			return false;
		}

		accountLock.lockAccount(targetAccountId);
		if (!targetAccount.deposit(command.money(), sourceAccountId)) {
			accountLock.releaseAccount(sourceAccountId);
			accountLock.releaseAccount(targetAccountId);
			return false;
		}

		updateAccountStatePort.updateActivities(sourceAccount);
		updateAccountStatePort.updateActivities(targetAccount);

		accountLock.releaseAccount(sourceAccountId);
		accountLock.releaseAccount(targetAccountId);

		MoneySuccessfullySentEvent moneySuccessfullySentEvent = new MoneySuccessfullySentEvent(this, sourceAccountId, command.money());
		moneySuccessfullySentEventPublisherPort.publishMoneySuccessfullySentEvent(moneySuccessfullySentEvent);

		return true;
	}

	private void checkThreshold(SendMoneyCommand command) {
		if(command.money().isGreaterThan(moneyTransferProperties.getMaximumTransferThreshold())){
			throw new ThresholdExceededException(moneyTransferProperties.getMaximumTransferThreshold(), command.money());
		}
	}

}




