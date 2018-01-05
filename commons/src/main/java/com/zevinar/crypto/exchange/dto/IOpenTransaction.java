package com.zevinar.crypto.exchange.dto;

import com.zevinar.crypto.utils.enums.TransactionTypeEnum;
import org.knowm.xchange.currency.Currency;

/**
 * Represents an open Transaction in an Exchange.<br>
 * @author ms172g
 *
 */
public interface IOpenTransaction{
	Currency getCoinType();
	TransactionTypeEnum getTransactionType();
	double getCoinUsdPrice();
	double getTransactionAmount();
}