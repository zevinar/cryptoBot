package com.zevinar.crypto.exchange.interfcaes;

import com.zevinar.crypto.utils.enums.CoinTypeEnum;

public interface ICoinTransaction {
	long getTransactionTime();
	double getTransactionQuantity();
	double getTransactionPrice();
	CoinTypeEnum getTransactionCoinType();
	void setCoinType(CoinTypeEnum coinType);
}
