package com.zevinar.crypto.exchange.interfcaes;

import com.zevinar.crypto.exchange.impl.AbstractMarketDataExchangeHandler;

import java.util.List;

public interface IExchangeHandlerForArbitrage extends IExchangeHandler {
	Double getMoveCoinFee();
	/**
	 * List Of Current Quotes of all coins supported in exchange in US Dollar.
	 * @return
	 */
	List<ICoinQuote> getAllCoinsQuotes();
}
