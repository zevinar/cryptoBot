package com.zevinar.crypto.utils.enums;

public enum CoinTypeEnum {
	BTC("Bit Coin", 0.1), LTC("Lite Coin", 0.1), ETH("Etherum", 0.1), DSH("Dash", 0.1);
	
	private String coinName;
	private double minCoinForTrade;
	
	private CoinTypeEnum(String coinName, double minCoinnForTrade){
		this.coinName = coinName;
		this.minCoinForTrade = minCoinnForTrade;
	}
	
	public String getCoinName() {
		return coinName;
	}


	public double getMinCoinForTrade() {
		return minCoinForTrade;
	}
}
