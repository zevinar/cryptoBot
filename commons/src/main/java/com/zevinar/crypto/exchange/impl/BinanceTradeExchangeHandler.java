package com.zevinar.crypto.exchange.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zevinar.crypto.exchange.impl.CacheHandler;
import com.zevinar.crypto.exchange.interfcaes.IExchangeHandler;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.Trade;
import org.knowm.xchange.dto.marketdata.Trades;
import org.knowm.xchange.service.marketdata.MarketDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class BinanceTradeExchangeHandler extends  BinanceExchangeHandler implements IExchangeHandler {
	private static final Logger LOG = LoggerFactory.getLogger(BinanceTradeExchangeHandler.class);
	private static Gson gson = new GsonBuilder().setPrettyPrinting().create();
	//TODO init as spring DI
	private static MarketDataService marketDataService=getExchange().getMarketDataService();



	@Override
	public List<Trade> getSingleCoinTransactions(CurrencyPair currencyPair, long fromTime, long toTime) throws IOException {
		return getSingleCoinTransactionsWithCache(currencyPair, null, fromTime, toTime,null);
	}

	public static List<Trade> getSingleCoinTransactionsWithCache(CurrencyPair currencyPair, Long fromTime, Long toTime) throws IOException {
		Optional< List<Trade>> optionalRec =  CacheHandler.INSTANCE.getRecords(currencyPair, fromTime, toTime);
		return optionalRec.orElseGet( CacheHandler.INSTANCE.fillCache(getRecordsFromExchange(currencyPair,null, fromTime, toTime,null).getTrades()));

	}

	public static List<Trade> getSingleCoinTransactionsWithCache(CurrencyPair currencyPair,Long fromId, Long fromTime, Long toTime,Long limit) throws IOException {
		Optional< List<Trade>> optionalRec =  CacheHandler.INSTANCE.getRecords(currencyPair, fromTime, toTime);
		return optionalRec.orElseGet( CacheHandler.INSTANCE.fillCache(getRecordsFromExchange(currencyPair,fromId, fromTime, toTime,limit).getTrades()));

	}

	private static Trades getRecordsFromExchange(CurrencyPair coinType,Long fromId, Long fromTime, Long toTime,Long limit) throws IOException {
		Trades trades = marketDataService.getTrades(coinType, fromId,  fromTime,  toTime, limit);
		return trades;
	}

	

}
