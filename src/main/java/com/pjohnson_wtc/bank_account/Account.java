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
	
	//WTCET-37 - NEW until 53
	//Method for depositing money into account
	public BigDecimal deposit(double amount) {
		
		//Return early if validation check for amount fails
		if (!validAmount(amount)) return balance;
		
		//Otherwise, set balance to existing value plus deposit amount, truncated to two decimal places, and return
		setBalance(balance.add(new BigDecimal(Math.floor(amount * 100) / 100)));
		
		logger.log(Level.INFO, "Deposit successful");
		return balance;
	}
	
	//WTCET-38 - NEW
	public BigDecimal withdraw(double amount) {
		
		//Return early if validation check for amount fails
		if (!validAmount(amount)) return balance;
		if (!sufficientFunds(amount)) return balance;
		
		setBalance(balance.subtract(new BigDecimal(amount)));
		return balance;
	}
	
	boolean validAmount(double amount) {
		
		//Return false if deposit amount is less than or equal to zero
		if (amount <= 0) {
			logger.log(Level.WARNING, "You must provide a positive amount");
			return false;
		}
		return true;
	}
	
	boolean sufficientFunds(double amount) {
		if (balance.compareTo(new BigDecimal(amount)) < 0) {
			logger.log(Level.WARNING, "Insufficient funds");
			return false;
		}
		return true;
	}
	
	//Necessary setters
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
	
	//Necessary getters
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
