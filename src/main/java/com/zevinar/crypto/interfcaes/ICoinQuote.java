package com.zevinar.crypto.interfcaes;

import com.zevinar.crypto.utils.enums.CoinTypeEnum;

public interface ICoinQuote {
	CoinTypeEnum getCoinType();
	Double getUSDollarQuote();
}
