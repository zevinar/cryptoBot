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

public class CexioExchangeHandler extends AbstractMarketDataExchangeHandler {
	private static final Logger LOG = LoggerFactory.getLogger(CexioExchangeHandler.class);



	public  CexioExchangeHandler()
	{
		INSTANCE= ExchangeFactory.INSTANCE.createExchange(ExchangeEnum.CEXIO.getExchangeName());
		init();
	}

	public  List<Trade> getTradesWithCache(CurrencyPair currencyPair, Long fromId, Long fromTime, Long toTime, Long limit) throws IOException {
		final String cacheKey = CacheHandler.INSTANCE.buildCacheKey(fromTime, getExchangeType(), currencyPair);
		Optional< List<Trade>> optionalRec =  CacheHandler.INSTANCE.getRecords(cacheKey);
		return optionalRec.orElseGet( CacheHandler.INSTANCE.fillCache(cacheKey, getTrades(currencyPair,fromId, fromTime, toTime,limit)));
	}



	//Cex has only from tradeid or max
	public  List<Trade> getTrades(CurrencyPair coinType, Long fromId, Long fromTime, Long toTime, Long limit) throws IOException {
		return marketDataService.getTrades(coinType, fromId  ).getTrades();
	}



}
