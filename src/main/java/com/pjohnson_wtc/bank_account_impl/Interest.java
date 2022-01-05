package com.pjohnson_wtc.bank_account_impl;

import java.math.BigDecimal;

public interface Interest {

	BigDecimal baseInterestRate = new BigDecimal(4.15);
	
	BigDecimal generateInterestRate();
}
