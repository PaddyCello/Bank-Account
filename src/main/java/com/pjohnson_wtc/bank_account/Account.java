package com.pjohnson_wtc.bank_account;

import java.math.BigDecimal;

public class Account {
	
	String name;
	long socialSecurityNumber;
	long accountNumber;
	BigDecimal balance;
	
	public Account(String name, long socialSecurityNumber, BigDecimal balance) {
		this.name = name;
		this.socialSecurityNumber = socialSecurityNumber;
		this.balance = balance;
		
	}
	
	public String getName() {
		return name;
	}
	
	public long getAccountNumber() {
		return accountNumber;
	}
	
	public BigDecimal getBalance() {
		return balance;
	}
}
