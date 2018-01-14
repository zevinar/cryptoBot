package com.zevinar.crypto.exchange.interfcaes;

import java.util.List;

import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.OrderBook;
import org.knowm.xchange.dto.marketdata.Ticker;
import org.knowm.xchange.dto.marketdata.Trade;

public interface IMarketDataExchangeHandler extends IExchangeInfo {

	//market data api

	OrderBook getOrderBook(CurrencyPair pair, Object... args) ;

	Ticker getTicker(CurrencyPair pair);

	List<Trade> getTrades(CurrencyPair coinType, Long fromId, Long fromTime, Long toTime, Long limit);



}
