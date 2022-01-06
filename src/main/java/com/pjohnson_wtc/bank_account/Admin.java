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
	public List<Long> createAccountsFromCsv(String filename) {
		
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
			Account newAccount = createAccount(applicant);
			
			//Add new Account to Admin.accounts and account number list
			accounts.add(newAccount);
			accountNums.add(newAccount.getAccountNumber());
		}
		
		logger.log(Level.INFO, accountNums.size() + " accounts created");
		
		//Return list of account numbers
		return accountNums;
	}
	
	//WTCET-36 - NEW until 62
	//Method for showing info from a given account number and access code
	public String showInfo(long accountNumber, int accessCode) {
		
		//Create a new Account object to hold the result of searching for the account that goes with the account number provided
		Account accountToFind = findAccountByNumber(accountNumber);
		
		//Return early if no account found, otherwise return the results of checking to see if the access code is valid
		return (accountToFind == null) ? null : checkAccessCode(accountToFind, accessCode);	
	}
	
	//WTCET-37 - NEW until 73
	//Method for depositing an amount of money into an account - takes a double instead of a BigDecimal to minimize risk of NumberFormatException
	public BigDecimal depositIntoAccount(long accountNumber, double amount) {
		
		//Create a new Account object to hold the result of searching for the account that goes with the account number provided
		Account accountToFind = findAccountByNumber(accountNumber);
		
		//Return early if no account found, otherwise return the results of checking to see if the amount can be deposited
		return (accountToFind == null) ? null : accountToFind.deposit(amount);
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
		
		try {
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
		
		//Handle exception if any of the fields are null, and return false
		} catch (NullPointerException npe) {
			
			logger.log(Level.WARNING, "Cannot have empty fields", npe.toString());
			return false;
		}
	}
	
	//Method for creating a single account
	private Account createAccount(List<String> applicant) {
		
		//Save name, SSN and starting balance to local variables, parsing to correct data types as needed
		String name = applicant.get(0);
		long socialSecurityNumber = Long.parseLong(applicant.get(1));
		BigDecimal balance = new BigDecimal(applicant.get(3));
		
		//Create new SavingsAccount or CheckingAccount depending on type specified in csv
		return (applicant.get(2).equals("Savings")) ? new SavingsAccount(name, socialSecurityNumber, balance, "1") : 
													new CheckingAccount(name, socialSecurityNumber, balance, "2");
	}
	
	//WTCET-36 - NEW until 209
	//Method for finding account with account number provided
	private Account findAccountByNumber(long accountNumber) {
		
		//Return null if account number is out of bounds
		if (accountNumber < 10000000000L || accountNumber > 29999999999L) {
			
			logger.log(Level.WARNING, "Invalid account number");
			return null;
		}
		
		//Initialize a new Account object to null
		Account accountToFind = null;
		
		//Loop through the accounts ArrayList checking for a match; if one is found, assign accountToFind to it and break the loop
		for (Account account: accounts) {
			
			if (account.getAccountNumber() == accountNumber) {
				accountToFind = account;
				break;
			}
		}
		
		if (accountToFind == null) logger.log(Level.WARNING, "Account does not exist");
		
		//Return result - either an account or null
		return accountToFind;
	}
	
	//Method for checking if access code is valid for the account found
	private String checkAccessCode(Account accountToFind, int accessCode) {
		
		//Initialize a String to null
		String returnData = null;
		
		//If account number starts with 1, our account will be a savings account
		if (accountToFind.getAccountNumber() < 20000000000L) {
			SavingsAccount savings = (SavingsAccount)accountToFind;
			
			//If the access code is valid, assign our string to the data that would be returned by showInfo
			if (savings.getDepositBox().getAccessCode() == accessCode) {
				returnData = savings.toString();
			} 
			
			//If account number starts with 2, our account will be a checking account
		} else {
			CheckingAccount checking = (CheckingAccount)accountToFind;
			
			if (checking.getDebitCard().getAccessCode() == accessCode) {
				returnData = checking.toString();
			}
		}
		
		if (returnData == null) logger.log(Level.WARNING, "Invalid access code");
		
		//Return the value of the String - either info on a savings or checking account, or null
		return returnData;
	}
	
	//Necessary getters
	public List<Account> getAccounts() {
		return accounts;
	}

}
