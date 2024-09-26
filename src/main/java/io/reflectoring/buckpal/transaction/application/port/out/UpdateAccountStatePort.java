package io.reflectoring.buckpal.transaction.application.port.out;

import io.reflectoring.buckpal.transaction.domain.Account;

public interface UpdateAccountStatePort {

	void updateActivities(Account account);

}
