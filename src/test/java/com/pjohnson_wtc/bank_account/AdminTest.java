package com.pjohnson_wtc.bank_account;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.stream.Collectors;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AdminTest {
	
	@Mock
	Account account;
	
	@InjectMocks
	Admin admin;

	@Test
	public void testCreateAccount() {
		assertEquals(15, admin.createAccount("NewBankAccounts.csv").size());
	}
	@Test
	public void testCreateAccount_addsToAccounts() {
		admin.createAccount("NewBankAccounts.csv");
		assertEquals(15, admin.getAccounts().size());
	}
	@Test
	public void testCreateAccount_addsBothCheckingAndSavings() {
		assertEquals(1, (long)(admin.createAccount("NewBankAccounts.csv").get(0) / 10000000000L));
		assertEquals(2, (long)(admin.createAccount("NewBankAccounts.csv").get(1) / 10000000000L));
	}
	@Test
	public void testCreateAccount_passesCsvDataToConstructors() {
		admin.createAccount("NewBankAccounts.csv");
		assertEquals("Arielle Duncan", admin.getAccounts().get(0).getName());
		assertEquals(new BigDecimal(1500), admin.getAccounts().get(4).getBalance());
	}
	@Test
	public void testCreateAccount_acctNoIncludesSSNDigits() {
		assertEquals(145, (long)(admin.createAccount("NewBankAccounts.csv").get(4) / 100000000));
	}
	@Test
	public void testCreateAccount_interestRatesSetForAccounts() {
		admin.createAccount("NewBankAccounts.csv");
		assertEquals(0.6225, admin.getAccounts().get(1).getInterestRate());
		assertEquals(3.1125, admin.getAccounts().get(0).getInterestRate());
	}
	@Test
	public void testCreateAccount_createsAccessCode_Savings() {
		admin.createAccount("NewBankAccounts.csv");
		SavingsAccount sa = (SavingsAccount)admin.getAccounts().get(0);
		assertTrue((sa.getDepositBox().getAccessCode() > 99) && (sa.getDepositBox().getAccessCode() < 1000));
	}
	@Test
	public void testCreateAccount_createsAccessCode_Checking() {
		admin.createAccount("NewBankAccounts.csv");
		CheckingAccount ca = (CheckingAccount)admin.getAccounts().get(1);
		assertTrue((ca.getDebitCard().getAccessCode() > 99) && (ca.getDebitCard().getAccessCode() < 1000));
	}
}
