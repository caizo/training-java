package org.pmv.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class Bank {
    private final String name;
    private List<Account> accounts = new ArrayList<>();

    public void transfer(Account rootAccount, Account destinationAccount, BigDecimal amount){
        rootAccount.debit(amount);
        destinationAccount.credit(amount);
    }

    public void addAccount(Account account){
        accounts.add(account);
        account.setBank(this);
    }
}
