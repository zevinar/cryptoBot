package com.zevinar.crypto.interfcaes.exchange;

import com.zevinar.crypto.utils.enums.CoinTypeEnum;

public interface ICoinQuote {
	CoinTypeEnum getCoinType();
	Double getUSDollarQuote();
}
