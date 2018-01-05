package com.zevinar.crypto.http;

import java.util.Calendar;

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

	@Override
	public String toString() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(T);

		int mYear = calendar.get(Calendar.YEAR);
		int mMonth = calendar.get(Calendar.MONTH) + 1;
		int mDay = calendar.get(Calendar.DAY_OF_MONTH);
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		String timeFormated = String.format("%s-%s-%s %s:%s", mDay, mMonth, mYear, hour, minute);
		return "ICoinTransaction [Time=" + timeFormated + ", Price=" + p + ", coinType=" + coinType + "]";
	}
}
