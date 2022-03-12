package org.pmv.model;

import lombok.Data;
import org.pmv.model.exceptions.InsufficientBalanceException;

import java.math.BigDecimal;

import static org.pmv.messages.Messages.INSUFFICIENT_BALANCE;
@Data
public class Account {

    private String person;
    private BigDecimal balance;
    private Bank bank;

    public void debit(BigDecimal amount){
        BigDecimal newBalance = this.balance.subtract(amount);
        // si el nuevo balance es menor que 0
        if(newBalance.compareTo(BigDecimal.ZERO) < 0){
            throw new InsufficientBalanceException(INSUFFICIENT_BALANCE);
        } else {
            this.balance = newBalance;
        }
    }
    public void credit(BigDecimal amount){
        this.balance = this.balance.add(amount);
    }

    public Account() {
    }

    public Account(String person, BigDecimal balance) {
        this.person = person;
        this.balance = balance;
    }
}
