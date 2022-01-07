package com.pjohnson_wtc.bank_account;

import java.math.BigDecimal;

import com.pjohnson_wtc.bank_account_impl.Interest;

public class CheckingAccount extends Account implements Interest {
	
	BankingTool debitCard;

	public CheckingAccount(String name, long socialSecurityNumber, BigDecimal balance, String accountType) {
		
		super(name, socialSecurityNumber, balance, accountType);
		this.accountNumber = generateAccountNumber(accountType, socialSecurityNumber);
		this.interestRate = generateInterestRate();
		this.debitCard = new BankingTool(100000000000L, 999999999999L);
	}
	
	//Calculate interest rate for checking account
	public BigDecimal generateInterestRate() {
		return baseInterestRate.multiply(new BigDecimal(0.15));
	}

	//toString - not inherited from Account as info specific to account type
		public String toString() {
			return "Name: " + name + ", Checking Account Number: " + accountNumber + ", Debit Card Number: " + debitCard.getIdNumber() + ", Balance: " + balance;
		}
	
	//Necessary getters
	public BankingTool getDebitCard() {
		return debitCard;
	}
}
