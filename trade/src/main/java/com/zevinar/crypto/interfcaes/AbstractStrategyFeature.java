package com.zevinar.crypto.interfcaes;

import com.zevinar.crypto.exchange.interfcaes.ITradeExchangeHandler;

public abstract class AbstractStrategyFeature implements IStrategyFeature{
	protected ITradeExchangeHandler exchangeHandler;

	public void init(ITradeExchangeHandler exchangeHandler){
		this.exchangeHandler = exchangeHandler;
	}
}
