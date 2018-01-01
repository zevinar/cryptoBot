package com.zevinar.crypto.exchange.interfcaes;

import java.util.List;

import com.zevinar.crypto.exchange.interfcaes.ICoinTransaction;
import com.zevinar.crypto.utils.enums.CoinTypeEnum;

public interface IStrategy {
	int getStrategySampleRateInSec();
	CoinTypeEnum getCoinOfIntrest();
	boolean analyzeData(List<ICoinTransaction> data);
	void init(IExchangeHandler exchangeHandler, double initialCashUsd);
}
