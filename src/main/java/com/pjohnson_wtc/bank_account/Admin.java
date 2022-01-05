package com.pjohnson_wtc.bank_account;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Admin {
	
	private static Logger logger = Logger.getLogger("com.pjohnson_wtc.bank_account.admin");
	
	//List of all accounts
	private List<Account> accounts = null;
	
	//Method for creating accounts from csv file
	public List<Long> createAccount(String filename) {
		
		//Call readFromCsv() to read csv file and format into List of Lists
		List<List<String>> accountApplicants = readFromCsv(this.getClass().getClassLoader().getResourceAsStream(filename));
		
		//Initialize accounts to a new ArrayList
		accounts = new ArrayList<Account>();
		
		//Create list for storing and returning account numbers
		List<Long> accountNums = new ArrayList<Long>();
		
		//Loop through list of account applicants
		for (List<String> applicant : accountApplicants) {
			
			//Skip to next line if validation fails
			if (!validateApplicant(applicant)) continue;

			//Declare local variable for saving new Account objects
			Account newAccount = finallyCreateAccount(applicant);
			
			//Add new Account to Admin.accounts and account number list
			accounts.add(newAccount);
			accountNums.add(newAccount.getAccountNumber());
		}
		
		logger.log(Level.INFO, accountNums.size() + " accounts created");
		
		//Return list of account numbers
		return accountNums;
	}
	
	//Method for reading data from csv file
	private List<List<String>> readFromCsv(InputStream filepath) {
		
		//Create new ArrayList of Lists of Strings for storing formatted data
		List<List<String>> accountApplicants = new ArrayList<List<String>>();
		
		//Read through csv file, line by line
		try (BufferedReader br = new BufferedReader(new InputStreamReader(filepath))) {
			
		//Store current line in local variable
		String line;
		
		//For each line, split the String at each comma and store the results in an array of Strings
		while ((line = br.readLine()) != null) {
			
			String[] values = line.split(",");
			
			//Convert this array of Strings to a List and to accountApplicants
			accountApplicants.add(Arrays.asList(values));
		}
		
		//Return formatted list of data
		return accountApplicants;
		
		} catch (IOException ioe) {
			
			logger.log(Level.WARNING, "Could not read file", ioe.toString());
			return null;
		}
	}
	
	//Method for validating input from csv
	private boolean validateApplicant(List<String> applicant) {
		
		//Return false if account type is neither Savings nor Checking
		if (!(applicant.get(2).equals("Savings") || applicant.get(2).equals("Checking"))) {
			
			logger.log(Level.WARNING, "Valid account type required in column 3");
			return false;
		}
		
		//Return false if parsing numeric values from SSN or balance throws an Exception
		try {
			Long.parseLong(applicant.get(1));
			Double.parseDouble(applicant.get(3));
			
		} catch (NumberFormatException nfe) {
			logger.log(Level.WARNING, "Columns 2 and 4 must both be numbers", nfe.toString());
			return false;
		}
		
		//Return false if SSN is too short to provide digits for account number
		if (Long.parseLong(applicant.get(1)) < 10) {
			logger.log(Level.WARNING, "Invalid social security number");
			return false;
		}
		
		//Return false if opening balance is less than or equal to zero
		if (Double.parseDouble(applicant.get(3)) <= 0) {
			logger.log(Level.WARNING, "Cannot open an account without funds");
			return false;
		}
		
		//If all is good, return true
		return true;
	}
	
	//Method for creating a single account
	private Account finallyCreateAccount(List<String> applicant) {
		
		//Save name, SSN and starting balance to local variables, parsing to correct data types as needed
		String name = applicant.get(0);
		long socialSecurityNumber = Long.parseLong(applicant.get(1));
		BigDecimal balance = new BigDecimal(applicant.get(3));
		
		//Create new SavingsAccount or CheckingAccount depending on type specified in csv
		return (applicant.get(2).equals("Savings")) ? new SavingsAccount(name, socialSecurityNumber, balance, "1") : 
													new CheckingAccount(name, socialSecurityNumber, balance, "2");
	}
	
	//Necessary getters
	public List<Account> getAccounts() {
		return accounts;
	}

}
