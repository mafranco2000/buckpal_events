package io.reflectoring.buckpal.transaction.application.port.in;

import io.reflectoring.buckpal.transaction.domain.Account.AccountId;
import io.reflectoring.buckpal.transaction.domain.Money;
import jakarta.validation.constraints.NotNull;

import static io.reflectoring.buckpal.common.validation.Validation.validate;

public record SendMoneyCommand(
        @NotNull AccountId sourceAccountId,
        @NotNull AccountId targetAccountId,
        @NotNull @PositiveMoney Money money
) {

    public SendMoneyCommand(
            AccountId sourceAccountId,
            AccountId targetAccountId,
            Money money) {
        this.sourceAccountId = sourceAccountId;
        this.targetAccountId = targetAccountId;
        this.money = money;
        validate(this);
    }

}
