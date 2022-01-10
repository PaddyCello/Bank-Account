package com.pjohnson_wtc.bank_account;

import java.math.BigDecimal;
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

	//Method for depositing money into account
	public BigDecimal deposit(double amount) {
		
		//Return early if validation check for amount fails
		if (!validAmount(amount)) return balance;
		
		//Otherwise, set balance to existing value plus deposit amount, truncated to two decimal places, and return
		setBalance(balance.add(new BigDecimal(Math.floor(amount * 100) / 100)));
		
		logger.log(Level.INFO, "Deposit successful");
		return balance;
	}

	//Method for withdrawing money from an account
	public BigDecimal withdraw(double amount) {
		
		//Return early if validation check for amount fails
		if (!validAmount(amount)) return balance;
		
		//Return early if the account does not hold sufficient funds for withdrawal
		if (!sufficientFunds(amount)) return balance;
		
		//Otherwise, subtract withdrawal amount (truncated to two decimal places) and return new balance
		setBalance(balance.subtract(new BigDecimal(Math.floor(amount * 100) / 100)));
		
		logger.log(Level.INFO, "Withdrawal successful");
		return balance;
	}
	
	//Method for checking amount for withdrawal or deposit is positive
	boolean validAmount(double amount) {
		
		//Return false if deposit amount is less than or equal to zero
		if (amount <= 0) {
			logger.log(Level.WARNING, "You must provide a positive amount");
			return false;
		}
		
		//Otherwise, return true
		return true;
	}
	
	//Method for checking that account has sufficient funds to cover withdrawal
	boolean sufficientFunds(double amount) {
		
		//Return false if amount to be withdrawn is greater than balance of account
		if (balance.compareTo(new BigDecimal(amount)) < 0) {
			logger.log(Level.WARNING, "Insufficient funds");
			return false;
		}
		
		//Otherwise, return true
		return true;
	}
	
	//WTCET-39 - REFACTORED until 92. Setter does not need to be public as balance only updated via deposit, withdraw and transfer
	//Necessary setters
	void setBalance(BigDecimal balance) {
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
