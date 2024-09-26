package io.reflectoring.buckpal.transaction.adapter.in.ui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.router.Route;
import io.reflectoring.buckpal.common.MoneySuccessfullySentEvent;
import io.reflectoring.buckpal.transaction.application.port.in.GetAccountBalanceUseCase;
import io.reflectoring.buckpal.transaction.application.port.in.SendMoneyCommand;
import io.reflectoring.buckpal.transaction.application.port.in.SendMoneyUseCase;
import io.reflectoring.buckpal.transaction.domain.Account;
import io.reflectoring.buckpal.transaction.domain.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
@Route("")
public class MainView extends VerticalLayout implements ApplicationListener<MoneySuccessfullySentEvent> {
    private final long sourceAccount = 1;
    @Autowired
    private SendMoneyUseCase sendMoneyUseCase;
    @Autowired
    private GetAccountBalanceUseCase getAccountBalanceUseCase;
    private NumberField currentBalanceField;
    private GetAccountBalanceUseCase.GetAccountBalanceQuery getAccountBalanceQuery;

    public MainView() {

        getAccountBalanceQuery = new GetAccountBalanceUseCase.GetAccountBalanceQuery(new Account.AccountId(sourceAccount));

        // set up some Vaadin UI elements
        currentBalanceField = new NumberField();
        Div currentBalancePrefix = new Div();
        currentBalancePrefix.setText("Current balance: ");
        currentBalanceField.setPrefixComponent(currentBalancePrefix);
        currentBalanceField.setReadOnly(true);

        NumberField amountOfMoneyField = new NumberField();
        amountOfMoneyField.setLabel("Amount of money to send");
        amountOfMoneyField.setValue(0.0);
        Div dollarPrefix = new Div();
        dollarPrefix.setText("$");
        amountOfMoneyField.setPrefixComponent(dollarPrefix);

        IntegerField targetAccountField = new IntegerField ();
        targetAccountField.setLabel("Target account Id");

        // set up a button to initiate a money send by calling the SendMoneyUseCase passing a SendMoneyCommand to it
        Button sendMoneyButton = new Button("Send money");
        sendMoneyButton.addClickListener(clickEvent -> {
            SendMoneyCommand sendMoneyCommand = new SendMoneyCommand(
                    new Account.AccountId(sourceAccount),
                    new Account.AccountId(targetAccountField.getValue().longValue()),
                    Money.of(amountOfMoneyField.getValue().longValue()));
            sendMoneyUseCase.sendMoney(sendMoneyCommand);
            Notification.show("Send request submitted.", 5000, Notification.Position.TOP_CENTER);
        });

        //  set up a button to show or hide the current balance
        Button showHideBalanceButton = new Button("Show/Hide balance");
        showHideBalanceButton.addClickListener(clickEvent -> {
            if (currentBalanceField.isEmpty()) {
                Money currentBalance = getAccountBalance();
                currentBalanceField.setValue(currentBalance.getAmount().doubleValue());
            } else {
                currentBalanceField.setValue(null);
            }

        });

        // set up the UI layout
        setAlignItems(FlexComponent.Alignment.CENTER);
        HorizontalLayout accountBalanceDisplay = new HorizontalLayout();
        accountBalanceDisplay.setPadding(true);
        accountBalanceDisplay.add(showHideBalanceButton, currentBalanceField);
        HorizontalLayout sendMoneyForm = new HorizontalLayout();
        sendMoneyForm.setPadding(true);
        sendMoneyForm.add(amountOfMoneyField, targetAccountField, sendMoneyButton);
        add("BuckPal - Account Id: " + sourceAccount);
        add(accountBalanceDisplay);
        add(sendMoneyForm);
    }

    private Money getAccountBalance() {
        return getAccountBalanceUseCase.getAccountBalance(getAccountBalanceQuery);
    }

    @Override
    public void onApplicationEvent(MoneySuccessfullySentEvent moneySuccessfullySentEvent) {
        getUI().ifPresent(ui -> ui.access(() -> {
            if (!currentBalanceField.isEmpty()) {
                Money currentBalance = getAccountBalance();
                currentBalanceField.setValue(currentBalance.getAmount().doubleValue());
            }
            Notification.show("Money successfully sent. Current balance updated.", 5000, Notification.Position.TOP_CENTER);
        }));
    }
}
