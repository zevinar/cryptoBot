package com.zevinar.crypto.exchange.interfcaes;

import com.zevinar.crypto.utils.enums.CoinTypeEnum;
import com.zevinar.crypto.utils.enums.TransactionTypeEnum;

/**
 * Represents an open Transaction in an Exchange.<br>
 * @author ms172g
 *
 */
public interface IOpenTransaction{
	CoinTypeEnum getCoinType();
	TransactionTypeEnum getTransactionType();
	double getCoinUsdPrice();
	double getTransactionAmount();
}