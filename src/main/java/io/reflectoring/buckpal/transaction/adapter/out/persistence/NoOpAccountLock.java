package io.reflectoring.buckpal.transaction.adapter.out.persistence;

import io.reflectoring.buckpal.transaction.application.port.out.AccountLock;
import io.reflectoring.buckpal.transaction.domain.Account.AccountId;
import org.springframework.stereotype.Component;

@Component
class NoOpAccountLock implements AccountLock {

	@Override
	public void lockAccount(AccountId accountId) {
		// do nothing
	}

	@Override
	public void releaseAccount(AccountId accountId) {
		// do nothing
	}

}
