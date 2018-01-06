package com.zevinar.crypto.exchange.interfcaes;

import com.zevinar.crypto.utils.enums.ExchangeEnum;

public interface IExchangeHandler {

	ExchangeEnum getExchangeType() ;

	Double getTradingFee();//TODO

}
