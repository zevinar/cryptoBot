package com.zevinar.crypto.exchange.interfcaes;

import java.util.List;

public interface IExchangeHandlerForArbitrage extends IExchangeInfo {
	Double getMoveCoinFee();
	/**
	 * List Of Current Quotes of all coins supported in exchange in US Dollar.
	 * @return
	 */
	List<ICoinQuote> getAllCoinsQuotes();
}
