package com.pjohnson_wtc.bank_account;

import java.util.Random;

public class BankingTool {
	
	long idNumber;
	int accessCode;
	
	public BankingTool() {
		accessCode = generateAccessCode();
	}
	
	int generateAccessCode() {
		return new Random().nextInt(999-100) + 100;
	}
	
	int getAccessCode() {
		return accessCode;
	}
}
