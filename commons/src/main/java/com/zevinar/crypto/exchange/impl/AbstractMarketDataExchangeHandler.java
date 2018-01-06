package com.zevinar.crypto.exchange.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import com.zevinar.crypto.exchange.interfcaes.IMarketDataExchangeHandler;
import com.zevinar.crypto.utils.enums.ExchangeEnum;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.OrderBook;
import org.knowm.xchange.dto.marketdata.Ticker;
import org.knowm.xchange.dto.marketdata.Trade;
import org.knowm.xchange.service.marketdata.MarketDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;

//can only get market data
public abstract class AbstractMarketDataExchangeHandler implements IMarketDataExchangeHandler {
	private static final Logger LOG = LoggerFactory.getLogger(AbstractMarketDataExchangeHandler.class);

	//TODO init as spring DI
	public static  Exchange INSTANCE=null;
	protected static MarketDataService marketDataService=null;

	protected static Exchange getExchange() {
		return INSTANCE;
	}

	//TODO init as spring DI
	public static void init() {

		marketDataService=getExchange().getMarketDataService();
	}


	public ExchangeEnum getExchangeType() {
		return  ExchangeEnum.fromString( INSTANCE.getDefaultExchangeSpecification().getExchangeName());
	}

	public abstract List<Trade>  getTradesWithCache(CurrencyPair currencyPair, Long fromId, Long fromTime, Long toTime, Long limit) throws IOException;

	@Override
	public List<Trade> getTrades(CurrencyPair currencyPair,Long fromId, Long fromTime, Long toTime, Long limit) throws IOException {
		return marketDataService.getTrades(currencyPair).getTrades();//get all by default ,works different in each echange
	}

	@Override
	public Ticker getTicker(CurrencyPair pair) throws IOException {
		Ticker ticker= marketDataService.getTicker(pair);
		return ticker;
	}

	@Override
	public OrderBook getOrderBook(CurrencyPair pair, Object... args) throws IOException {
		return marketDataService.getOrderBook(pair);  //works different in each echange
	}

	public Double getTradingFee() {//TODO  get real fee (might be different between exchanges, coins, trade size etc...)
		return 0.02;
	}

}
