package io.reflectoring.buckpal.transaction.application.service;

import io.reflectoring.buckpal.transaction.application.port.in.GetAccountBalanceUseCase;
import io.reflectoring.buckpal.transaction.application.port.out.LoadAccountPort;
import io.reflectoring.buckpal.transaction.domain.Money;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Component
@Transactional
public class GetAccountBalanceService implements GetAccountBalanceUseCase {

	private final LoadAccountPort loadAccountPort;

	@Override
	public Money getAccountBalance(GetAccountBalanceQuery query) {
		return loadAccountPort.loadAccount(query.accountId(), LocalDateTime.now())
				.calculateBalance();
	}
}
