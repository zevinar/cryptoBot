package com.zevinar.crypto.interfcaes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zevinar.crypto.exchange.interfcaes.ITradeExchangeHandler;

public abstract class AbstractStrategyFeature implements IStrategyFeature{
	protected ITradeExchangeHandler exchangeHandler;
	protected static final Logger LOG = LoggerFactory.getLogger(AbstractStrategyFeature.class);

	public void init(ITradeExchangeHandler exchangeHandler){
		this.exchangeHandler = exchangeHandler;
	}
}
