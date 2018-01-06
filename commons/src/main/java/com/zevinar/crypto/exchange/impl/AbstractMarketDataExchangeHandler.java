package com.zevinar.crypto.exchange.impl;

import static com.zevinar.crypto.utils.FunctionalCodeUtils.methodRunner;

import java.util.List;
import java.util.Optional;

import org.knowm.xchange.Exchange;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.OrderBook;
import org.knowm.xchange.dto.marketdata.Ticker;
import org.knowm.xchange.dto.marketdata.Trade;
import org.knowm.xchange.dto.marketdata.Trades;
import org.knowm.xchange.service.marketdata.MarketDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zevinar.crypto.exchange.interfcaes.IMarketDataExchangeHandler;
import com.zevinar.crypto.utils.enums.ExchangeEnum;

//can only get market data
public abstract class AbstractMarketDataExchangeHandler implements IMarketDataExchangeHandler {
	private static final Logger LOG = LoggerFactory.getLogger(AbstractMarketDataExchangeHandler.class);

	// TODO crypto init as spring DI
	public static Exchange INSTANCE = null;
	protected static MarketDataService marketDataService = null;

	protected static Exchange getExchange() {
		return INSTANCE;
	}

	// TODO crypto init as spring DI
	public static void init() {

		marketDataService = getExchange().getMarketDataService();
	}

	public ExchangeEnum getExchangeType() {
		return ExchangeEnum.fromString(INSTANCE.getDefaultExchangeSpecification().getExchangeName());
	}

	public List<Trade> getTradesWithCache(CurrencyPair currencyPair, Long fromId, Long fromTime, Long toTime,
			Long limit) {
		final String cacheKey = CacheHandler.INSTANCE.buildCacheKey(fromTime, getExchangeType(), currencyPair);
		Optional<List<Trade>> optionalRec = CacheHandler.INSTANCE.getRecords(cacheKey);
		return optionalRec.orElseGet(
				CacheHandler.INSTANCE.fillCache(cacheKey, getTrades(currencyPair, fromId, fromTime, toTime, limit)));
	}

	@Override
	public List<Trade> getTrades(CurrencyPair currencyPair, Long fromId, Long fromTime, Long toTime, Long limit) {
		Trades trades = methodRunner(() -> marketDataService.getTrades(currencyPair, fromId, fromTime, toTime, limit));
		return trades.getTrades();
		// get all
		// by
		// default
		// ,works
		// different
		// in
		// each
		// echange
	}

	@Override
	public Ticker getTicker(CurrencyPair pair) {
		return methodRunner(() -> marketDataService.getTicker(pair));
	}

	@Override
	public OrderBook getOrderBook(CurrencyPair pair, Object... args) {
		return methodRunner(() -> marketDataService.getOrderBook(pair)); // works
																			// different
																			// in
																			// each
		// echange
	}

	public Double getTradingFee() {// TODO crypto get real fee (might be
									// different between exchanges, coins, trade
									// size etc...)
		return 0.02;
	}

}
