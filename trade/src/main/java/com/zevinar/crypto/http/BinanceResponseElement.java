package com.zevinar.crypto.http;

import com.zevinar.crypto.exchange.interfcaes.ICoinTransaction;
import com.zevinar.crypto.utils.enums.CoinTypeEnum;

public class BinanceResponseElement implements ICoinTransaction {
	// Transaction Time
	private long T;
	// Price
	private double p;
	// Quantity
	private double q;

	// private long f;
	// private long l;
	// private long a;
	// private boolean m;
	// private boolean M;

	private CoinTypeEnum coinType;

	@Override
	public long getTransactionTime() {
		return T;
	}

	@Override
	public double getTransactionQuantity() {
		return q;
	}

	@Override
	public double getTransactionPrice() {
		return p;
	}

	@Override
	public CoinTypeEnum getTransactionCoinType() {
		return coinType;
	}

	@Override
	public void setCoinType(CoinTypeEnum coinType) {
		this.coinType = coinType;
	}
}
