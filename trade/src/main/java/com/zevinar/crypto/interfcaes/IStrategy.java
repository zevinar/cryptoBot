package com.zevinar.crypto.interfcaes;

import java.util.List;

import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.Trade;

import com.zevinar.crypto.exchange.interfcaes.ITradeExchangeHandler;

public interface IStrategy {
	CurrencyPair getCoinOfIntrest();

	boolean analyzeData(List<Trade> data);

	List<IStrategyFeature> getFeatures();
	
	ITradeExchangeHandler getExchangeHandler();

	void init();

	default int getStrategySampleRateInSec() {
		return 15;
	}

}
