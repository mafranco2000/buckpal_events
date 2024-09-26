package io.reflectoring.buckpal.notification.application.port.in;

import io.reflectoring.buckpal.common.MoneySuccessfullySentEvent;

public interface MoneySuccessfullySentNotificationPort {

    void onApplicationEvent(MoneySuccessfullySentEvent moneySuccessfullySentEvent);
}
