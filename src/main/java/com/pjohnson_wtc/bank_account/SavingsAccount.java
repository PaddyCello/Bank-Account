package com.pjohnson_wtc.bank_account;

import java.math.BigDecimal;

import com.pjohnson_wtc.bank_account_impl.Interest;

public class SavingsAccount extends Account implements Interest {
	
	private BankingTool depositBox;

	public SavingsAccount(String name, long socialSecurityNumber, BigDecimal balance, String accountType) {
		super(name, socialSecurityNumber, balance, accountType);
		this.accountNumber = generateAccountNumber(accountType, socialSecurityNumber);
		this.interestRate = generateInterestRate();
		this.depositBox = new BankingTool(100, 999);
	}
	
	//Calculate interest rate for savings account
	public BigDecimal generateInterestRate() {
		return baseInterestRate.subtract(baseInterestRate.multiply(new BigDecimal(0.25)));
	}
	
	//toString - not inherited from Account as info specific to account type
	public String toString() {
		return "Name: " + name + ", Savings Account Number: " + accountNumber + ", Deposit Box Number: " + depositBox.getIdNumber() + ", Balance: " + balance;
	}
	
	//Necessary getters
	public BankingTool getDepositBox() {
		return depositBox;
	}
}
