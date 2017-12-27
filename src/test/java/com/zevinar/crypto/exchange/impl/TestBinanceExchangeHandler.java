package com.zevinar.crypto.exchange.impl;

import java.util.List;

import org.junit.Test;

import com.zevinar.crypto.exchange.interfcaes.ICoinTransaction;
import com.zevinar.crypto.utils.enums.CoinTypeEnum;

public class TestBinanceExchangeHandler {
	@Test
	public void testGetSingleCoinQuotes() {
		BinanceExchangeHandler handler = new BinanceExchangeHandler();
		long currentTimeMillis = System.currentTimeMillis();
		long day = 24*60*60*1000;
		long fromTime = currentTimeMillis - 3 * day;
		long toTime = currentTimeMillis - 2 * day;
//		List<ICoinTransaction> singleCoinQuotes = handler.getSingleCoinTransactions(CoinTypeEnum.LTC, fromTime, toTime);
//		Assert.assertTrue(singleCoinQuotes.size() > 0);
	}
}
