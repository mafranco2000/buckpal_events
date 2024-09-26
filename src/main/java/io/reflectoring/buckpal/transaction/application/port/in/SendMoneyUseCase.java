package io.reflectoring.buckpal.transaction.application.port.in;

public interface SendMoneyUseCase {

	boolean sendMoney(SendMoneyCommand command);

}
