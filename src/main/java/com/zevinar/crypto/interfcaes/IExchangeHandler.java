package com.zevinar.crypto.interfcaes;

import java.util.List;

import com.zevinar.crypto.utils.enums.ExchangeDetailsEnum;

public interface IExchangeHandler {
	ExchangeDetailsEnum getExchangeDetails();
	List<ICoinQuote> getQuotes();
	
	/**
	 * Transaction fee, for example for 1% return 0.01.<br>
	 * @return
	 */
	Double getTransactionFee();
	
	Double getMoveCoinFee();
}
