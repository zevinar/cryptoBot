package com.zevinar.crypto.exchange.impl;

import static com.zevinar.crypto.bl.impl.StrategySimulator.DAY_IN_MS;
import static com.zevinar.crypto.bl.impl.StrategySimulator.HOUR_IN_MS;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.io.IOException;
import java.util.List;

import com.zevinar.crypto.exchange.impl.realexchange.BinanceExchangeHandler;
import com.zevinar.crypto.utils.FunctionalCodeUtils;

import org.junit.Test;

import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.Trade;

public class BinanceExchangeHandlerTest {
	@Test
	public void testGetSingleCoinQuotes() {
		BinanceExchangeHandler handler = new BinanceExchangeHandler();
		long currentTimeMillis = System.currentTimeMillis();
		// Max 23 Days Back
		int daysBack = 0;
		// long fromTime = currentTimeMillis - 60 * 60 * 1000;
		long fromTime = currentTimeMillis - (daysBack * DAY_IN_MS + 2 * HOUR_IN_MS);
		long toTime = currentTimeMillis - (daysBack * DAY_IN_MS + HOUR_IN_MS);
		// Last Hour
		List<Trade> singleCoinQuotes = FunctionalCodeUtils
				.methodRunner(() -> handler.getTrades(new CurrencyPair("LTC", "USDT"), null, fromTime, toTime, null));
		assertThat(singleCoinQuotes.size() > 0, is(true));

	}
}
