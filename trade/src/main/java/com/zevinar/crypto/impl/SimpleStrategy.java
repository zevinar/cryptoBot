package com.zevinar.crypto.impl;

import java.util.List;

import org.knowm.xchange.currency.CurrencyPair;

import com.zevinar.crypto.interfcaes.AbstractSimulationStrategy;
import com.zevinar.crypto.interfcaes.IStrategyFeature;
import com.zevinar.crypto.strategy.impl.BasicTradeFeature;

public class SimpleStrategy extends AbstractSimulationStrategy {
	public SimpleStrategy() {
		strategyFeatures.add(new BasicTradeFeature());
	}

	@Override
	public CurrencyPair getCoinOfIntrest() {
		return new CurrencyPair("LTC", "USDT");
	}

	@Override
	public List<IStrategyFeature> getFeatures() {
		return strategyFeatures;
	}

}
