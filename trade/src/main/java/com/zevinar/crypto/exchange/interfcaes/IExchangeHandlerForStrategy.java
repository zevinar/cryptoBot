package com.zevinar.crypto.exchange.interfcaes;

import java.util.List;

import com.zevinar.crypto.utils.enums.CoinTypeEnum;

public interface IExchangeHandlerForStrategy extends IBaseExchangeHandler {
	List<ICoinTransaction> getSingleCoinTransactions(CoinTypeEnum coinType, long fromTime, long toTime);

}
