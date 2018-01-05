package com.zevinar.crypto.exchange.impl;

import static com.zevinar.crypto.bl.impl.StrategySimulator.DAY_IN_MS;
import static com.zevinar.crypto.bl.impl.StrategySimulator.HOUR_IN_MS;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.util.List;

import org.junit.Test;

import com.zevinar.crypto.bl.impl.BinanceTradeExchangeHandler;
import com.zevinar.crypto.exchange.interfcaes.ICoinTransaction;
import com.zevinar.crypto.utils.enums.CoinTypeEnum;

public class BinanceExchangeHandlerTest {
	@Test
	public void testGetSingleCoinQuotes() {
		BinanceTradeExchangeHandler handler = new BinanceTradeExchangeHandler();
		long currentTimeMillis = System.currentTimeMillis();
		//Max 23 Days Back
		int daysBack = 0;
		// long fromTime = currentTimeMillis - 60 * 60 * 1000;
		long fromTime = currentTimeMillis - (daysBack * DAY_IN_MS + 2 * HOUR_IN_MS );
		long toTime = currentTimeMillis - (daysBack * DAY_IN_MS + HOUR_IN_MS);
		// Last Hour
		List<ICoinTransaction> singleCoinQuotes = handler.getSingleCoinTransactions(CoinTypeEnum.LTC, fromTime, toTime);
		assertThat(singleCoinQuotes.size() > 0, is(true));

	}
}
