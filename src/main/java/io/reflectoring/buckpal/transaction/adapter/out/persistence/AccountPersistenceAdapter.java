package io.reflectoring.buckpal.transaction.adapter.out.persistence;

import io.reflectoring.buckpal.transaction.application.port.out.LoadAccountPort;
import io.reflectoring.buckpal.transaction.application.port.out.UpdateAccountStatePort;
import io.reflectoring.buckpal.transaction.domain.Account;
import io.reflectoring.buckpal.transaction.domain.Account.AccountId;
import io.reflectoring.buckpal.transaction.domain.Activity;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Component
class AccountPersistenceAdapter implements
		LoadAccountPort,
		UpdateAccountStatePort {

	private final SpringDataAccountRepository accountRepository;
	private final ActivityRepository activityRepository;
	private final AccountMapper accountMapper;

	@Override
	public Account loadAccount(
					AccountId accountId,
					LocalDateTime baselineDate) {

		AccountJpaEntity account =
				accountRepository.findById(accountId.getValue())
						.orElseThrow(EntityNotFoundException::new);

		List<ActivityJpaEntity> activities =
				activityRepository.findByOwnerSince(
						accountId.getValue(),
						baselineDate);

		Long withdrawalBalance = activityRepository
				.getWithdrawalBalanceUntil(
						accountId.getValue(),
						baselineDate)
				.orElse(0L);

		Long depositBalance = activityRepository
				.getDepositBalanceUntil(
						accountId.getValue(),
						baselineDate)
				.orElse(0L);

		return accountMapper.mapToDomainEntity(
				account,
				activities,
				withdrawalBalance,
				depositBalance);

	}

	@Override
	public void updateActivities(Account account) {
		for (Activity activity : account.getActivityWindow().getActivities()) {
			if (activity.getId() == null) {
				activityRepository.save(accountMapper.mapToJpaEntity(activity));
			}
		}
	}

}
