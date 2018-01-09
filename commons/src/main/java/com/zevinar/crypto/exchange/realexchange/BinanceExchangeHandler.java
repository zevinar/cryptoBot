package com.zevinar.crypto.exchange.realexchange;

import com.zevinar.crypto.exchange.AbstractMarketDataExchangeHandler;
import org.knowm.xchange.ExchangeFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zevinar.crypto.utils.enums.ExchangeEnum;

public class BinanceExchangeHandler extends AbstractMarketDataExchangeHandler {
	private static final Logger LOG = LoggerFactory.getLogger(BinanceExchangeHandler.class);

	public BinanceExchangeHandler() {
		INSTANCE = ExchangeFactory.INSTANCE.createExchange(ExchangeEnum.BINANCE.getExchangeName());
		init();
	}



}
