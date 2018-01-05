package com.zevinar.crypto.exchange.interfcaes;

import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.currency.CurrencyPair;

public interface ICoinQuote {
	CurrencyPair getCoinType();
	Double getUSDollarBuy();
	Double getUSDollarSell();
}
