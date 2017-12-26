package com.zevinar.crypto.exchange.interfcaes;

import java.util.List;

import com.zevinar.crypto.utils.enums.CoinTypeEnum;
import com.zevinar.crypto.utils.enums.ExchangeDetailsEnum;

public interface IExchangeHandler {
	ExchangeDetailsEnum getExchangeDetails();
	/**
	 * List Of Current Quotes of all coins supported in exchange in US Dollar.
	 * @return
	 */
	List<ICoinQuote> getALlCoinsQuotes();
	
	/**
	 * Transaction fee, for example for 1% return 0.01.<br>
	 * @return
	 */
	Double getTransactionFee();
	
	Double getMoveCoinFee();
	
	/**
	 * Quotes of coin type starting from fromTime until currentTime
	 * @param coinType
	 * @param fromTime
	 * @return
	 */
	List<ICoinQuote> getSingleCoinQuotes(CoinTypeEnum coinType, long fromTime);
}
