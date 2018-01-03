package com.zevinar.crypto.exchange.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zevinar.crypto.exchange.interfcaes.IBaseExchangeHandler;
import com.zevinar.crypto.exchange.interfcaes.IOpenTransaction;
import com.zevinar.crypto.exchange.interfcaes.ITransactionResult;
import com.zevinar.crypto.utils.enums.CoinTypeEnum;
import com.zevinar.crypto.utils.enums.ExchangeDetailsEnum;

public class BinanceExchangeHandler implements IBaseExchangeHandler{
	private static final Logger LOG = LoggerFactory.getLogger(BinanceExchangeHandler.class);

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
	public double getCoinBalance(CoinTypeEnum coinType) {
		// TODO mshitrit implement for functional
		return 0;
	}

	@Override
	public double getCurrentCashUSD() {
		// TODO mshitrit implement for functional
		return 0;
	}

	

}
