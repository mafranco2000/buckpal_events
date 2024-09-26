package io.reflectoring.buckpal.transaction.application.port.out;

import io.reflectoring.buckpal.transaction.domain.Account;

public interface AccountLock {

	void lockAccount(Account.AccountId accountId);

	void releaseAccount(Account.AccountId accountId);

}
