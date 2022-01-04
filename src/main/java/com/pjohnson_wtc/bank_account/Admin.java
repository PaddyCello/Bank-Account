package com.pjohnson_wtc.bank_account;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Admin {
	
	private List<Account> accounts = null;
	
	public List<List<String>> createAccount() {
		
		List<List<String>> accountApplicants = readFromCsv(this.getClass().getClassLoader().getResourceAsStream("NewBankAccounts.csv"));
		
		accounts = new ArrayList<Account>();
		
		for (List<String> applicant : accountApplicants) {
			if (applicant.get(2).equals("Savings")) {
				Account newAccount = new SavingsAccount();
				accounts.add(newAccount);
			} else {
				Account newAccount = new CheckingAccount();
				accounts.add(newAccount);
			}
		}
		
		return accountApplicants;
	}
	
	private List<List<String>> readFromCsv(InputStream filepath) {
		List<List<String>> accountApplicants = new ArrayList<List<String>>();
		try (BufferedReader br = new BufferedReader(new InputStreamReader(filepath))) {
		String line;
		while ((line = br.readLine()) != null) {
			String[] values = line.split(",");
			accountApplicants.add(Arrays.asList(values));
		}
		return accountApplicants;
		} catch (IOException ioe) {
			ioe.printStackTrace();
			return null;
		}
	}
	
	public List<Account> getAccounts() {
		return accounts;
	}

	public static void main(String[] args) {

	}

}
