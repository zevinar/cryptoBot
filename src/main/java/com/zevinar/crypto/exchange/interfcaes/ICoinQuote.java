package com.zevinar.crypto.exchange.interfcaes;

import com.zevinar.crypto.utils.enums.CoinTypeEnum;

public interface ICoinQuote {
	CoinTypeEnum getCoinType();
	Double getUSDollarBuy();
	Double getUSDollarSell();
}
