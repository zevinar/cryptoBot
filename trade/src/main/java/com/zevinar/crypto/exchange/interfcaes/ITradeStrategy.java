package com.zevinar.crypto.exchange.interfcaes;

import java.util.List;

import com.zevinar.crypto.exchange.interfcaes.ICoinTransaction;
import com.zevinar.crypto.utils.enums.CoinTypeEnum;

public interface ITradeStrategy {
	int getStrategySampleRateInSec();
	CoinTypeEnum getCoinOfIntrest();
	void analyzeData(List<ICoinTransaction> data);
	void init(IExchangeHandlerForStrategy exchangeActionHandler, double initialCashUsd);
}
