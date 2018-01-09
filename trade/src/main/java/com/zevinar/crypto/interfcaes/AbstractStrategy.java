package com.zevinar.crypto.interfcaes;

import java.util.List;

import org.knowm.xchange.dto.marketdata.Trade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zevinar.crypto.exchange.interfcaes.ITradeExchangeHandler;

public abstract class AbstractStrategy implements IStrategy{
	protected static final Logger LOG = LoggerFactory.getLogger(AbstractStrategy.class);
	protected ITradeExchangeHandler exchangeHandler;
	protected List<IStrategyFeature> strategyFeatures;
	
	
	@Override
	public final void init() {
		this.exchangeHandler = getExchangeHandler();
		this.strategyFeatures = getFeatures();
		strategyFeatures.stream().forEach(strategyFeature -> strategyFeature.init(exchangeHandler));
	}
	
	@Override
	public final boolean  analyzeData(List<Trade> dataList) {
		strategyFeatures.stream().forEach(strategyFeature -> strategyFeature.performDecision(dataList));
		return true;
	}
}
