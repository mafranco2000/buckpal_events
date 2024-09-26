package io.reflectoring.buckpal.notification.adapter.in.events;

import io.reflectoring.buckpal.common.MoneySuccessfullySentEvent;
import io.reflectoring.buckpal.notification.application.handler.MoneySuccessfullySentNotificationHandler;
import io.reflectoring.buckpal.notification.application.port.in.MoneySuccessfullySentNotificationPort;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class MoneySuccessfullySentNotificationAdapter implements MoneySuccessfullySentNotificationPort, ApplicationListener<MoneySuccessfullySentEvent> {
    private final MoneySuccessfullySentNotificationHandler moneySuccessfullySentNotificationHandler;
        @Override
        public void onApplicationEvent(MoneySuccessfullySentEvent moneySuccessfullySentEvent) {
            moneySuccessfullySentNotificationHandler.handleEvent(moneySuccessfullySentEvent);
        }
}
