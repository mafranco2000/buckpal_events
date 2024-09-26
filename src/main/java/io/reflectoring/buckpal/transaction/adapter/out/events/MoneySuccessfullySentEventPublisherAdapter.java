package io.reflectoring.buckpal.transaction.adapter.out.events;

import io.reflectoring.buckpal.common.MoneySuccessfullySentEvent;
import io.reflectoring.buckpal.transaction.application.port.out.MoneySuccessfullySentEventPublisherPort;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class MoneySuccessfullySentEventPublisherAdapter implements MoneySuccessfullySentEventPublisherPort {
    private final ApplicationEventPublisher applicationEventPublisher;
    @Override
    public void publishMoneySuccessfullySentEvent(MoneySuccessfullySentEvent moneySuccessfullySentEvent) {
        applicationEventPublisher.publishEvent(moneySuccessfullySentEvent);
    }
}
