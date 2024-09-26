package io.reflectoring.buckpal.common;

import io.reflectoring.buckpal.transaction.application.port.in.PositiveMoney;
import io.reflectoring.buckpal.transaction.domain.Account;
import io.reflectoring.buckpal.transaction.domain.Money;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import static io.reflectoring.buckpal.common.validation.Validation.validate;

@Getter
public class MoneySuccessfullySentEvent extends ApplicationEvent {

    public void setSourceAccountId(Account.AccountId sourceAccountId) {
        this.sourceAccountId = sourceAccountId;
    }

    public void setMoney(Money money) {
        this.money = money;
    }


    @NotNull Account.AccountId sourceAccountId;
    @NotNull @PositiveMoney
    Money money;

    public MoneySuccessfullySentEvent(Object source, Account.AccountId sourceAccountId, Money money) {
        super(source);
        this.sourceAccountId = sourceAccountId;
        this.money = money;
        validate(this);
    }
}
