package com.zevinar.crypto.exchange.impl;

import java.util.List;

import com.zevinar.crypto.exchange.interfcaes.ICoinTransaction;
import com.zevinar.crypto.exchange.interfcaes.IExchangeHandler;
import com.zevinar.crypto.exchange.interfcaes.IOpenTransaction;
import com.zevinar.crypto.exchange.interfcaes.ITransactionResult;
import com.zevinar.crypto.utils.enums.CoinTypeEnum;
import com.zevinar.crypto.utils.enums.ExchangeDetailsEnum;

public class SimExchangeHandler implements IExchangeHandler{
//TODO mshitrit continue here
	@Override
	public ExchangeDetailsEnum getExchangeDetails() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Double getTransactionFee() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ITransactionResult postBuy(CoinTypeEnum coinType, double wantedBuyPrice) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ITransactionResult postSell(CoinTypeEnum coinType, double wantedSellPrice) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<IOpenTransaction> getOpenTransactions() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double getCoinBalance(CoinTypeEnum coinType) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getCurrentCashUSD() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<ICoinTransaction> getSingleCoinTransactions(CoinTypeEnum coinType, long fromTime, long toTime) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void feedData(List<ICoinTransaction> dataList) {
		
	}

}
