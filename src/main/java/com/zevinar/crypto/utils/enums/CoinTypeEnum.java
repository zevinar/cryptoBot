package com.zevinar.crypto.utils.enums;

public enum CoinTypeEnum {
	BTC("Bit Coin"), LTC("Lite Coin"), ETH("Etherum"), DSH("Dash");
	
	private String coinName;
	
	private CoinTypeEnum(String coinName){
		this.coinName = coinName;
	}
	
	public String getCoinName() {
		return coinName;
	}
}
