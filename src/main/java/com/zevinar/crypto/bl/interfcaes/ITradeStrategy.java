package com.zevinar.crypto.bl.interfcaes;

import java.util.List;

import com.zevinar.crypto.exchange.interfcaes.ICoinTransaction;
import com.zevinar.crypto.exchange.interfcaes.IExchangeActionsHandler;
import com.zevinar.crypto.utils.enums.CoinTypeEnum;

public interface ITradeStrategy {
	int getStrategySampleRateInSec();
	CoinTypeEnum getCoinOfIntrest();
	void getDataCallbackMethod(List<ICoinTransaction> data);
	void init(IExchangeActionsHandler exchangeActionHandler, double initialCashUsd);
}
