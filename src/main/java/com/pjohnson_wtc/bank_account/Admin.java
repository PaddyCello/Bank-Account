package com.pjohnson_wtc.bank_account;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Admin {
	
	public List<List<String>> createAccount() {
		
		return readFromCsv(this.getClass().getClassLoader().getResourceAsStream("NewBankAccounts.csv"));
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

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
