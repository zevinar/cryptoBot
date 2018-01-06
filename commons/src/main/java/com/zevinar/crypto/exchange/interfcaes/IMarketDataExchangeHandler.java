package com.zevinar.crypto.exchange.interfcaes;

import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.OrderBook;
import org.knowm.xchange.dto.marketdata.Ticker;
import org.knowm.xchange.dto.marketdata.Trade;

import java.io.IOException;
import java.util.List;

public interface IMarketDataExchangeHandler extends IExchangeHandler  {

	//market data api

	OrderBook getOrderBook(CurrencyPair pair, Object... args) throws IOException ;

	Ticker getTicker(CurrencyPair pair) throws IOException;

	List<Trade> getTrades(CurrencyPair coinType, Long fromId, Long fromTime, Long toTime, Long limit) throws IOException;



}
