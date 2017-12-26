package com.zevinar.crypto.exchange.impl;

import org.junit.Test;

import com.zevinar.crypto.utils.HttpClient;

public class TestBinanceExchangeHandler {
	@Test
	public void testGetSingleCoinQuotes() {
		BinanceExchangeHandler handler = new BinanceExchangeHandler();
		long currentTimeMillis = System.currentTimeMillis();
		long fromTime = currentTimeMillis - 60*60*1000;
//		List<ICoinQuote> singleCoinQuotes = handler.getSingleCoinQuotes(CoinTypeEnum.LTC, fromTime);
		//1514322346604
		//1514322348542
//		/String serverTime = HttpClient.CLIENT.doGet("https://api.binance.com/api/v1/time").getBody();
//		System.out.println(String.format(" ServerTime:%s , MachineTime:%s", serverTime, currentTimeMillis));
	}
}
