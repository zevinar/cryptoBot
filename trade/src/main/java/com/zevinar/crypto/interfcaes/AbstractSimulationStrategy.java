package com.zevinar.crypto.interfcaes;

import com.zevinar.crypto.exchange.interfcaes.ITradeExchangeHandler;
import com.zevinar.crypto.exchange.realexchange.SimExchangeHandler;

public abstract class AbstractSimulationStrategy extends AbstractStrategy {
	private SimExchangeHandler handler;
	protected AbstractSimulationStrategy(){
		SimExchangeHandler handler = new SimExchangeHandler();
		this.handler = handler;
	}
	public final ITradeExchangeHandler getExchangeHandler(){
		return handler;
	}
	public final SimExchangeHandler getSimExchangeHandler(){
		return handler;
	}
	//For Testing Only
	public void setSimExchangeHandler(SimExchangeHandler handler){
		this.handler = handler;
	}
}
