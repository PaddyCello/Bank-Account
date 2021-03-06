package com.pjohnson_wtc.bank_account;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.math.RoundingMode;

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
	public void testCreateAccountsFromCsv() {
		assertEquals(15, admin.createAccountsFromCsv("NewBankAccounts.csv").size());
	}
	@Test
	public void testCreateAccountsFromCsv_addsToAccounts() {
		admin.createAccountsFromCsv("NewBankAccounts.csv");
		assertEquals(15, admin.getAccounts().size());
	}
	@Test
	public void testCreateAccountsFromCsv_addsBothCheckingAndSavings() {
		assertEquals(1, (long)(admin.createAccountsFromCsv("NewBankAccounts.csv").get(0) / 10000000000L));
		assertEquals(2, (long)(admin.createAccountsFromCsv("NewBankAccounts.csv").get(1) / 10000000000L));
	}
	@Test
	public void testCreateAccountsFromCsv_passesCsvDataToConstructors() {
		admin.createAccountsFromCsv("NewBankAccounts.csv");
		assertEquals("Arielle Duncan", admin.getAccounts().get(0).getName());
		assertEquals(new BigDecimal(1500), admin.getAccounts().get(4).getBalance());
	}
	@Test
	public void testCreateAccountsFromCsv_acctNoIncludesSSNDigits() {
		assertEquals(145, (long)(admin.createAccountsFromCsv("NewBankAccounts.csv").get(4) / 100000000));
	}
	@Test
	public void testCreateAccountsFromCsv_interestRatesSetForAccounts() {
		admin.createAccountsFromCsv("NewBankAccounts.csv");
		assertEquals(new BigDecimal(0.6225).setScale(4, RoundingMode.FLOOR), admin.getAccounts().get(1).getInterestRate().setScale(4, RoundingMode.FLOOR));
		assertEquals(new BigDecimal(3.1125).setScale(4, RoundingMode.CEILING), admin.getAccounts().get(0).getInterestRate().setScale(4, RoundingMode.FLOOR));
	}
	@Test
	public void testCreateAccountsFromCsv_createsAccessCode_Savings() {
		admin.createAccountsFromCsv("NewBankAccounts.csv");
		SavingsAccount sa = (SavingsAccount)admin.getAccounts().get(0);
		assertTrue((sa.getDepositBox().getAccessCode() > 999) && (sa.getDepositBox().getAccessCode() < 10000));
	}
	@Test
	public void testCreateAccountsFromCsv_createsAccessCode_Checking() {
		admin.createAccountsFromCsv("NewBankAccounts.csv");
		CheckingAccount ca = (CheckingAccount)admin.getAccounts().get(1);
		assertTrue((ca.getDebitCard().getAccessCode() > 999) && (ca.getDebitCard().getAccessCode() < 10000));
	}
	@Test
	public void testCreateAccountsFromCsv_createsIDNum_Savings() {
		admin.createAccountsFromCsv("NewBankAccounts.csv");
		SavingsAccount sa = (SavingsAccount)admin.getAccounts().get(0);
		assertTrue((sa.getDepositBox().getIdNumber() > 99) && (sa.getDepositBox().getIdNumber() < 1000));
	}
	@Test
	public void testCreateAccountsFromCsv_createsIDNum_Checking() {
		admin.createAccountsFromCsv("NewBankAccounts.csv");
		CheckingAccount ca = (CheckingAccount)admin.getAccounts().get(1);
		assertTrue((ca.getDebitCard().getIdNumber() > 99999999999L) && (ca.getDebitCard().getIdNumber() < 1000000000000L));
	}
	@Test
	public void testShowInfo_savings() {
		admin.createAccountsFromCsv("NewBankAccounts.csv");
		SavingsAccount sa = (SavingsAccount)admin.getAccounts().get(0);
		assertEquals("Name: Arielle Duncan, Savings Account Number: " + sa.getAccountNumber() + ", Deposit Box Number: " + sa.getDepositBox().getIdNumber() + ", Balance: 1000", admin.showInfo(sa.getAccountNumber(), sa.getDepositBox().getAccessCode()));
	}
	@Test
	public void testShowInfo_checking() {
		admin.createAccountsFromCsv("NewBankAccounts.csv");
		CheckingAccount ca = (CheckingAccount)admin.getAccounts().get(1);
		assertEquals("Name: Marybeth Sanders, Checking Account Number: " + ca.getAccountNumber() + ", Debit Card Number: " + ca.getDebitCard().getIdNumber() + ", Balance: 2500", admin.showInfo(ca.getAccountNumber(), ca.getDebitCard().getAccessCode()));
	}
	@Test
	public void testShowInfo_wrongAccountNumber() {
		admin.createAccountsFromCsv("NewBankAccounts.csv");
		assertNull(admin.showInfo(30025643212L, 123));
	}
	@Test
	public void testShowInfo_wrongAccessCode() {
		admin.createAccountsFromCsv("NewBankAccounts.csv");
		assertNull(admin.showInfo(admin.getAccounts().get(0).getAccountNumber(), 123));
	}
	@Test
	public void testDepositIntoAccount() {
		admin.createAccountsFromCsv("NewBankAccounts.csv");
		admin.depositIntoAccount(admin.getAccounts().get(0).getAccountNumber(), 200);
		assertEquals(new BigDecimal(1200), admin.getAccounts().get(0).getBalance());
	}
	@Test
	public void testDepositIntoAccount_negativeDeposit() {
		admin.createAccountsFromCsv("NewBankAccounts.csv");
		admin.depositIntoAccount(admin.getAccounts().get(0).getAccountNumber(), -200);
		assertEquals(new BigDecimal(1000), admin.getAccounts().get(0).getBalance());
	}
	@Test
	public void testDepositIntoAccount_tooManyDecimalPlaces() {
		admin.createAccountsFromCsv("NewBankAccounts.csv");
		admin.depositIntoAccount(admin.getAccounts().get(0).getAccountNumber(), 200.005);
		assertEquals(new BigDecimal(1200), admin.getAccounts().get(0).getBalance());
	}
	@Test
	public void testWithdrawFromAccount() {
		admin.createAccountsFromCsv("NewBankAccounts.csv");
		SavingsAccount sa = (SavingsAccount)admin.getAccounts().get(0);
		admin.withdrawFromAccount(sa.getAccountNumber(), sa.getDepositBox().getIdNumber(), sa.getDepositBox().getAccessCode(), 200);
		assertEquals(new BigDecimal(800), admin.getAccounts().get(0).getBalance());
	}
	@Test
	public void testWithdrawFromAccount_negativeWithrawal() {
		admin.createAccountsFromCsv("NewBankAccounts.csv");
		SavingsAccount sa = (SavingsAccount)admin.getAccounts().get(0);
		admin.withdrawFromAccount(sa.getAccountNumber(), sa.getDepositBox().getIdNumber(), sa.getDepositBox().getAccessCode(), -200);
		assertEquals(new BigDecimal(1000), admin.getAccounts().get(0).getBalance());
	}
	@Test
	public void testWithdrawFromAccount_insufficientFunds() {
		admin.createAccountsFromCsv("NewBankAccounts.csv");
		SavingsAccount sa = (SavingsAccount)admin.getAccounts().get(0);
		admin.withdrawFromAccount(sa.getAccountNumber(), sa.getDepositBox().getIdNumber(), sa.getDepositBox().getAccessCode(), 1200);
		assertEquals(new BigDecimal(1000), admin.getAccounts().get(0).getBalance());
	}
	@Test
	public void testWithdrawFromAccount_tooManyDecimalPlaces() {
		admin.createAccountsFromCsv("NewBankAccounts.csv");
		SavingsAccount sa = (SavingsAccount)admin.getAccounts().get(0);
		admin.withdrawFromAccount(sa.getAccountNumber(), sa.getDepositBox().getIdNumber(), sa.getDepositBox().getAccessCode(), 200.005);
		assertEquals(new BigDecimal(800), admin.getAccounts().get(0).getBalance());
	}
	@Test
	public void testTransferBetweenAccounts( ) {
		admin.createAccountsFromCsv("NewBankAccounts.csv");
		SavingsAccount sa = (SavingsAccount)admin.getAccounts().get(0);
		Account recipientAccount = admin.getAccounts().get(1);
		admin.transferBetweenAccounts(sa.getAccountNumber(), recipientAccount.getAccountNumber(), sa.getDepositBox().getIdNumber(), sa.getDepositBox().getAccessCode(), 200.005);
		assertEquals(new BigDecimal(800), admin.getAccounts().get(0).getBalance());
		assertEquals(new BigDecimal(2700), admin.getAccounts().get(1).getBalance());
	}
	@Test
	public void testTransferBetweenAccounts_invalidDestinationAccount() {
		admin.createAccountsFromCsv("NewBankAccounts.csv");
		SavingsAccount sa = (SavingsAccount)admin.getAccounts().get(0);
		admin.transferBetweenAccounts(sa.getAccountNumber(), 30234034502L, sa.getDepositBox().getIdNumber(), sa.getDepositBox().getAccessCode(), 200.005);
		assertEquals(new BigDecimal(1000), admin.getAccounts().get(0).getBalance());
	}
}
