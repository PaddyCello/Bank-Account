package com.pjohnson_wtc.bank_account;

import java.util.Random;

public class BankingTool {
	
	private long idNumber;
	private int accessCode;
	
	public BankingTool(long min, long max) {
		this.idNumber = generateIdNumber(min, max);
		this.accessCode = generateAccessCode();
	}
	
	//Generate 4-digit PIN
	private int generateAccessCode() {
		return new Random().nextInt(9999 - 1000) + 1000;
	}
	
	//Generate 3-digit deposit box number or 12-digit debit card number depending on input
	private long generateIdNumber(long min, long max) {
		return (long)(Math.random() * (max - min)) + min;
	}
	
	
	//Necessary getters
	public long getIdNumber() {
		return idNumber;
	}
	
	public int getAccessCode() {
		return accessCode;
	}
}
