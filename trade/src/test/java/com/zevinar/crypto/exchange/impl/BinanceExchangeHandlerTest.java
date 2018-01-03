package com.zevinar.crypto.exchange.impl;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import com.zevinar.crypto.bl.impl.BinanceTradeExchangeHandler;
import com.zevinar.crypto.exchange.interfcaes.ICoinTransaction;
import com.zevinar.crypto.utils.enums.CoinTypeEnum;

public class BinanceExchangeHandlerTest {
	@Test @Ignore
	public void testGetSingleCoinQuotes() {
		BinanceTradeExchangeHandler handler = new BinanceTradeExchangeHandler();
		long currentTimeMillis = System.currentTimeMillis();
//		long day = 24*60*60*1000;
		long fromTime = currentTimeMillis - 60 * 60 * 1000;
		long toTime = currentTimeMillis;
		//Last Hour
		List<ICoinTransaction> singleCoinQuotes = handler.getSingleCoinTransactions(CoinTypeEnum.LTC, fromTime, toTime);
		assertThat(singleCoinQuotes.size() > 0, is(true));

	}
}
