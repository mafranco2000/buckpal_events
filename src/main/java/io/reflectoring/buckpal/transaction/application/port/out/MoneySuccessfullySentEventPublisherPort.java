package io.reflectoring.buckpal.transaction.application.port.out;

import io.reflectoring.buckpal.common.MoneySuccessfullySentEvent;

public interface MoneySuccessfullySentEventPublisherPort {

    void publishMoneySuccessfullySentEvent(MoneySuccessfullySentEvent moneySuccessfullySentEvent);
}
