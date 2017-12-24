package com.zevinar.crypto.utils.enums;

public enum ExchangeDetailsEnum {
	WEX("Bit Coin"), BNC("Binance");

	private String exchangeName;

	private ExchangeDetailsEnum(String coinName) {
		this.exchangeName = coinName;
	}

	public String getExchangeName() {
		return exchangeName;
	}
}
