package io.reflectoring.buckpal.transaction.application.port.in;

import io.reflectoring.buckpal.transaction.domain.Account.AccountId;
import io.reflectoring.buckpal.transaction.domain.Money;

public interface GetAccountBalanceUseCase {

	Money getAccountBalance(GetAccountBalanceQuery query);

	record GetAccountBalanceQuery(AccountId accountId) {
	}
}
