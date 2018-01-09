package com.zevinar.crypto.interfcaes;

import java.util.List;

import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.Trade;

import com.zevinar.crypto.exchange.interfcaes.ITradeExchangeHandler;

public interface IStrategy {
	int getStrategySampleRateInSec();
	CurrencyPair getCoinOfIntrest();
	boolean analyzeData(List<Trade> data);
	void init(ITradeExchangeHandler exchangeHandler);
}
