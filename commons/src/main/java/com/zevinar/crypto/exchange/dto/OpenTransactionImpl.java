package com.zevinar.crypto.exchange.dto;

import com.zevinar.crypto.utils.enums.TransactionTypeEnum;
import org.knowm.xchange.currency.Currency;

//TODO crypto change to Xchange data type
public class OpenTransactionImpl implements IOpenTransaction{
	private Currency coinType;
	private TransactionTypeEnum transactionType;
	private double price;
	private double amount;

	public OpenTransactionImpl(Currency coinType, TransactionTypeEnum transactionType, double price, double amount){
		this.coinType = coinType;
		this.transactionType = transactionType;
		this.price = price;
		this.amount = amount;
	}
	@Override
	public Currency getCoinType() {
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
