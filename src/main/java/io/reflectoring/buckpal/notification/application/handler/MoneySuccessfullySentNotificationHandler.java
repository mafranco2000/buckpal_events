package io.reflectoring.buckpal.notification.application.handler;

import io.reflectoring.buckpal.common.MoneySuccessfullySentEvent;
import org.springframework.stereotype.Component;

@Component
public class MoneySuccessfullySentNotificationHandler {

    public void handleEvent(MoneySuccessfullySentEvent moneySuccessfullySentEvent) {
        System.out.println("Successfully sent " + moneySuccessfullySentEvent.getMoney() + " from account " + moneySuccessfullySentEvent.getSourceAccountId() + " at timestamp " + moneySuccessfullySentEvent.getTimestamp());
    }
}
