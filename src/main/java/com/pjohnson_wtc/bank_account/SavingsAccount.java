package com.pjohnson_wtc.bank_account;

import java.math.BigDecimal;

public class SavingsAccount extends Account {

	public SavingsAccount(String name, long socialSecurityNumber, BigDecimal balance) {
		super(name, socialSecurityNumber, balance);
		this.accountNumber = 1;
	}
}
