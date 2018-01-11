package com.zevinar.crypto.exchange;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.knowm.xchange.currency.Currency.LTC;

import java.util.List;

import org.junit.Test;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.Trade;

import com.zevinar.crypto.exchange.realexchange.BinanceExchangeHandler;
import com.zevinar.crypto.utils.DateUtils;
import com.zevinar.crypto.utils.FunctionalCodeUtils;

public class BinanceExchangeHandlerTest {
	@Test
	public void testGetSingleCoinQuotes() {
		BinanceExchangeHandler handler = new BinanceExchangeHandler();
		long currentTimeMillis = System.currentTimeMillis();
		// Max 23 Days Back
		int daysBack = 0;
		// long fromTime = currentTimeMillis - 60 * 60 * 1000;
		long fromTime = currentTimeMillis - (daysBack * DateUtils.DAY_IN_MS + 2 * DateUtils.HOUR_IN_MS);
		long toTime = currentTimeMillis - (daysBack * DateUtils.DAY_IN_MS + DateUtils.HOUR_IN_MS);
		// Last Hour
		List<Trade> singleCoinQuotes = FunctionalCodeUtils
				.methodRunner(() -> handler.getTrades(new CurrencyPair(LTC.getCurrencyCode(), "USDT"), null, fromTime, toTime, null));
		assertThat(singleCoinQuotes.size() > 0, is(true));

	}



}
