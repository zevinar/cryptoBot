package com.zevinar.crypto.exchange.impl.realexchange;

import com.zevinar.crypto.exchange.impl.AbstractMarketDataExchangeHandler;
import com.zevinar.crypto.exchange.impl.CacheHandler;
import com.zevinar.crypto.utils.enums.ExchangeEnum;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.Trade;
import org.knowm.xchange.dto.marketdata.Trades;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class WexExchangeHandler extends AbstractMarketDataExchangeHandler {
	private static final Logger LOG = LoggerFactory.getLogger(WexExchangeHandler.class);


	public  WexExchangeHandler() {

		INSTANCE= ExchangeFactory.INSTANCE.createExchange(ExchangeEnum.WEX.getExchangeName());
		init();
	}

	public  List<Trade> getTradesWithCache(CurrencyPair currencyPair, Long fromId, Long fromTime, Long toTime, Long limit) throws IOException {
		Optional< List<Trade>> optionalRec =  CacheHandler.INSTANCE.getRecords(currencyPair, fromTime, toTime);//TODO crypto make sure cache is exchange specific
		return optionalRec.orElseGet( CacheHandler.INSTANCE.fillCache(getTrades(currencyPair,fromId, fromTime, toTime,limit)));
	}


	//WEX does not get dates, only limit
	@Override
	public   List<Trade>  getTrades(CurrencyPair coinType, Long fromId, Long fromTime, Long toTime, Long limit) throws IOException {
		return marketDataService.getTrades(coinType, limit).getTrades();
	}





	

}
