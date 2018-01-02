package com.zevinar.crypto.exchange.interfcaes;

import com.zevinar.crypto.utils.enums.TransactionTypeEnum;

/**
 * Represents an open Transaction in an Exchange.<br>
 * @author ms172g
 *
 */
public interface IOpenTransaction{
	TransactionTypeEnum getTransactionType();
}