package com.zevinar.crypto.exchange.impl;

import java.util.List;

import org.knowm.xchange.Exchange;
import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.binance.BinanceExchange;
import org.knowm.xchange.currency.Currency;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zevinar.crypto.exchange.interfcaes.IBaseExchangeHandler;
import com.zevinar.crypto.exchange.dto.IOpenTransaction;
import com.zevinar.crypto.exchange.dto.ITransactionResult;
import com.zevinar.crypto.utils.enums.ExchangeDetailsEnum;

public class BinanceExchangeHandler implements IBaseExchangeHandler{
	private static final Logger LOG = LoggerFactory.getLogger(BinanceExchangeHandler.class);

	//TODO init as spring DI
	private static final Exchange INSTANCE= ExchangeFactory.INSTANCE.createExchange(BinanceExchange.class.getName());

	protected static Exchange getExchange() {
		return INSTANCE;
	}

	@Override
	public ExchangeDetailsEnum getExchangeDetails() {
		return ExchangeDetailsEnum.BNC;
	}

	@Override
	public Double getTransactionFee() {
		return 0.02;
	}


	@Override
	public List<IOpenTransaction> getOpenTransactions() {
		// TODO mshitrit implement for functional
		return null;
	}

	@Override
	public ITransactionResult postTransactionRequest(IOpenTransaction request) {
		// TODO mshitrit implement for functional
		return null;
	}

	@Override
	public double getCoinBalance(Currency coinType) {
		// TODO mshitrit implement for functional
		return 0;
	}

	@Override
	public double getCurrentCashUSD() {
		// TODO mshitrit implement for functional
		return 0;
	}

	

}
