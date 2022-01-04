package com.pjohnson_wtc.bank_account;

import java.math.BigDecimal;

public class CheckingAccount extends Account {

	public CheckingAccount(String name, long socialSecurityNumber, BigDecimal balance) {
		super(name, socialSecurityNumber, balance);
		this.accountNumber = 2;
	}
}
