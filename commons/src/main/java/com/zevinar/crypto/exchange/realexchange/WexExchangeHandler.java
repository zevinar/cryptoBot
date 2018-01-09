package com.zevinar.crypto.exchange.realexchange;

import static com.zevinar.crypto.utils.FunctionalCodeUtils.methodRunner;

import java.util.List;

import com.zevinar.crypto.exchange.AbstractMarketDataExchangeHandler;
import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.Trade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zevinar.crypto.utils.enums.ExchangeEnum;

public class WexExchangeHandler extends AbstractMarketDataExchangeHandler {
	private static final Logger LOG = LoggerFactory.getLogger(WexExchangeHandler.class);

	public WexExchangeHandler() {

		INSTANCE = ExchangeFactory.INSTANCE.createExchange(ExchangeEnum.WEX.getExchangeName());

	}


	// WEX does not get dates, only limit
	@Override
	public List<Trade> getTrades(CurrencyPair coinType, Long fromId, Long fromTime, Long toTime, Long limit) {
		if (fromTime==null)
			return methodRunner(() -> marketDataService.getTrades(coinType).getTrades());
		else return methodRunner(() -> marketDataService.getTrades(coinType,limit).getTrades());
	}

}
