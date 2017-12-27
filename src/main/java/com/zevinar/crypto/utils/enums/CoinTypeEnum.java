package com.zevinar.crypto.utils.enums;

public enum CoinTypeEnum {
	BTC("Bit Coin", "BTCUSDT"), LTC("Lite Coin", "LTCUSDT"), ETH("Etherum", "ETHUSDT"), DSH("Dash", null);
	
	private String coinName;
	private String httpQuerySymbol;
	
	private CoinTypeEnum(String coinName, String httpQuerySymbol){
		this.coinName = coinName;
		this.httpQuerySymbol = httpQuerySymbol;
	}
	
	public String getCoinName() {
		return coinName;
	}

	public String getHttpQuerySymbol() {
		return httpQuerySymbol;
	}
}
