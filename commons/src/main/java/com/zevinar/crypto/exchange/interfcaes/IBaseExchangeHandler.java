package com.zevinar.crypto.exchange.interfcaes;

import java.util.List;

import com.zevinar.crypto.exchange.dto.IOpenTransaction;
import com.zevinar.crypto.exchange.dto.ITransactionResult;
import com.zevinar.crypto.utils.enums.ExchangeDetailsEnum;
import org.knowm.xchange.currency.Currency;

public interface IBaseExchangeHandler {
	
	ExchangeDetailsEnum getExchangeDetails();

	/**
	 * Transaction fee, for example for 1% return 0.01.<br>
	 * 
	 * @return
	 */
	Double getTransactionFee();

	ITransactionResult postTransactionRequest(IOpenTransaction request);
	
	List<IOpenTransaction> getOpenTransactions();

	double getCoinBalance(Currency coinType);
	
	double getCurrentCashUSD();

}
