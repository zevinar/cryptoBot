package com.zevinar.crypto.exchange.interfcaes;

import com.zevinar.crypto.utils.enums.ExchangeEnum;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.meta.ExchangeMetaData;

import java.util.List;

public interface IExchangeHandler {

	ExchangeEnum getExchangeType() ;

	Double getTradingFee();//TODO crypto

	Exchange getExchange();


	List<CurrencyPair> getExchangeSymbols();

	ExchangeMetaData getExchangeMetaData();

}
