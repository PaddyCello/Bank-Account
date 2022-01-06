package com.pjohnson_wtc.bank_account;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Account {
	
	Logger logger = Logger.getLogger("com.pjohnson_wtc.bank_account.account");
	
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
	public BigDecimal deposit(double amount) {
		
		if (amount <= 0) {
			logger.log(Level.WARNING, "You must deposit a positive amount");
			return balance;
		}
		setBalance(balance.add(new BigDecimal(Math.floor(amount * 100) / 100)));
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
