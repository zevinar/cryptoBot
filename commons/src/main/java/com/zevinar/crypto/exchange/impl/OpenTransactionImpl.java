package com.zevinar.crypto.exchange.impl;

import com.zevinar.crypto.exchange.interfcaes.IOpenTransaction;
import com.zevinar.crypto.utils.enums.CoinTypeEnum;
import com.zevinar.crypto.utils.enums.TransactionTypeEnum;

public class OpenTransactionImpl implements IOpenTransaction{
	private CoinTypeEnum coinType;
	private TransactionTypeEnum transactionType;
	private double price;
	private double amount;

	public OpenTransactionImpl(CoinTypeEnum coinType, TransactionTypeEnum transactionType, double price, double amount){
		this.coinType = coinType;
		this.transactionType = transactionType;
		this.price = price;
		this.amount = amount;
	}
	@Override
	public CoinTypeEnum getCoinType() {
		return coinType;
	}

	@Override
	public TransactionTypeEnum getTransactionType() {
		return transactionType;
	}

	@Override
	public double getCoinUsdPrice() {
		return price;
	}
	@Override
	public double getTransactionAmount() {
		return amount;
	}
	@Override
	public String toString() {
		return "OpenTransactionImpl [coinType=" + coinType + ", transactionType=" + transactionType + ", price=" + price
				+ ", amount=" + amount + "]";
	}

}
