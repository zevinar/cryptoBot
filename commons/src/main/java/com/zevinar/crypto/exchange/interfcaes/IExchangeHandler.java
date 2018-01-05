package com.zevinar.crypto.exchange.interfcaes;

import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.Trade;

import java.io.IOException;
import java.util.List;

public interface IExchangeHandler extends IBaseExchangeHandler {
	/**
	 * Transactions of coin type starting from fromTime until currentTime
	 * @param coinType
	 * @param fromTime
	 * @return
	 */
	List<Trade> getSingleCoinTransactions(CurrencyPair coinType, long fromTime, long toTime) throws IOException;

	

	

	
}

	
