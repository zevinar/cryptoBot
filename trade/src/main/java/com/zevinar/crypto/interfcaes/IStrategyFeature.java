package com.zevinar.crypto.interfcaes;

import java.util.List;

import org.knowm.xchange.dto.marketdata.Trade;

import com.zevinar.crypto.exchange.interfcaes.ITradeExchangeHandler;

public interface IStrategyFeature {
	void init(ITradeExchangeHandler exchangeHandler);
	boolean performDecision(List<Trade> dataList);
}
