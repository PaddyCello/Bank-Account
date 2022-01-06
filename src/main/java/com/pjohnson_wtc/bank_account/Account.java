package com.pjohnson_wtc.bank_account;

import java.math.BigDecimal;
import java.util.Random;

public class Account {
	
	String name;
	long socialSecurityNumber;
	long accountNumber;
	BigDecimal balance;
	static int uniqueAccountNumBit = 10000;
	BigDecimal interestRate;
	
	public Account(String name, long socialSecurityNumber, BigDecimal balance, String accountType) {
		this.name = name;
		this.socialSecurityNumber = socialSecurityNumber;
		this.balance = balance;
		this.accountNumber = generateAccountNumber(accountType, socialSecurityNumber);
	}
	
	//Method for generating account number
	long generateAccountNumber(String accountType, long socialSecurityNumber) {
		int randomBit = new Random().nextInt(999 - 100) + 100;
		return Long.parseLong(accountType + (socialSecurityNumber % 100) + uniqueAccountNumBit++ + randomBit);
	}
	
	//WTCET-37 - NEW
	public BigDecimal deposit(BigDecimal amount) {
		setBalance(balance.add(amount));
		return balance;
	}
	
	public void setBalance(BigDecimal balance) {
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
	
	public BigDecimal getInterestRate() {
		return interestRate;
	}
}
