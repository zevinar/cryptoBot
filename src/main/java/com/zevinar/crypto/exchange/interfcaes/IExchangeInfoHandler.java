package com.zevinar.crypto.exchange.interfcaes;

import java.util.List;

import com.zevinar.crypto.utils.enums.CoinTypeEnum;

public interface IExchangeInfoHandler extends IBaseExchangeHandler {

	/**
	 * List Of Current Quotes of all coins supported in exchange in US Dollar.
	 * @return
	 */
	List<ICoinQuote> getALlCoinsQuotes();
	
	/**
	 * Transactions of coin type starting from fromTime until currentTime
	 * @param coinType
	 * @param fromTime
	 * @return
	 */
	List<ICoinTransaction> getSingleCoinTransactions(CoinTypeEnum coinType, long fromTime);
	
	List<ICoinTransaction> getSingleCoinTransactions(CoinTypeEnum coinType, long fromTime, long toTime);
}
