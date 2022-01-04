package com.pjohnson_wtc.bank_account;

import java.math.BigDecimal;

import com.pjohnson_wtc.bank_account_impl.Interest;

public class CheckingAccount extends Account implements Interest {

	public CheckingAccount(String name, long socialSecurityNumber, BigDecimal balance) {
		super(name, socialSecurityNumber, balance);
		this.accountNumber = generateAccountNumber("2", socialSecurityNumber);
		this.interestRate = generateInterestRate();
	}
	
	public BigDecimal generateInterestRate() {
		return baseInterestRate.multiply(new BigDecimal(0.15));
	}
}
