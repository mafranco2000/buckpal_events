package io.reflectoring.buckpal.transaction.application.port.out;

import io.reflectoring.buckpal.transaction.domain.Account;
import io.reflectoring.buckpal.transaction.domain.Account.AccountId;

import java.time.LocalDateTime;

public interface LoadAccountPort {

	Account loadAccount(AccountId accountId, LocalDateTime baselineDate);
}
