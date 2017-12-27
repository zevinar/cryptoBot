package com.zevinar.crypto.exchange.impl;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.zevinar.crypto.exchange.interfcaes.ICoinTransaction;
import com.zevinar.crypto.utils.enums.CoinTypeEnum;

public class TestBinanceExchangeHandler {
	@Test
	public void testGetSingleCoinQuotes() {
		BinanceExchangeHandler handler = new BinanceExchangeHandler();
		long currentTimeMillis = System.currentTimeMillis();
		long fromTime = currentTimeMillis - 60*60*1000;
		List<ICoinTransaction> singleCoinQuotes = handler.getSingleCoinTransactions(CoinTypeEnum.LTC, fromTime);
		Assert.assertTrue(singleCoinQuotes.size() > 0);
		//1514322346604
		//1514322348542
//		/String serverTime = HttpClient.CLIENT.doGet("https://api.binance.com/api/v1/time").getBody();
//		System.out.println(String.format(" ServerTime:%s , MachineTime:%s", serverTime, currentTimeMillis));
	}
}
