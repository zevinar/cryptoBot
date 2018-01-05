package com.zevinar.crypto.exchange.interfcaes;

import java.util.List;

import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.Trade;

public interface IStrategy {
	int getStrategySampleRateInSec();
	CurrencyPair getCoinOfIntrest();
	boolean analyzeData(List<Trade> data);
	void init(IExchangeHandler exchangeHandler);
}
