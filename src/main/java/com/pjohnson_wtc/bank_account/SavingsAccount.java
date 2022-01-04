package com.pjohnson_wtc.bank_account;

import java.math.BigDecimal;

import com.pjohnson_wtc.bank_account_impl.Interest;

public class SavingsAccount extends Account implements Interest {

	public SavingsAccount(String name, long socialSecurityNumber, BigDecimal balance) {
		super(name, socialSecurityNumber, balance);
		this.accountNumber = generateAccountNumber("1", socialSecurityNumber);
		this.interestRate = generateInterestRate();
	}
	
	public BigDecimal generateInterestRate() {
		return baseInterestRate.subtract(baseInterestRate.multiply(new BigDecimal(0.25)));
	}
}
