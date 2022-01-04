package com.pjohnson_wtc.bank_account;

import static org.junit.Assert.*;

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
		assertEquals(15, admin.createAccount().size());
	}
	@Test
	public void testCreateAccount_addsToAccounts() {
		admin.createAccount();
		assertEquals(15, admin.getAccounts().size());
	}
	@Test
	public void testCreateAccount_addsBothCheckingAndSavings() {
		admin.createAccount();
		assertEquals(1, admin.getAccounts().get(0).getAccountNumber());
		assertEquals(2, admin.getAccounts().get(1).getAccountNumber());
	}

}
