package com.zevinar.crypto.exchange.interfcaes;

import java.util.List;

import com.zevinar.crypto.utils.enums.CoinTypeEnum;

public interface IExchangeHandlerForSimulator extends IBaseExchangeHandler {
	/**
	 * Transactions of coin type starting from fromTime until currentTime
	 * @param coinType
	 * @param fromTime
	 * @return
	 */
	List<ICoinTransaction> getSingleCoinTransactions(CoinTypeEnum coinType, long fromTime);
	
	}
