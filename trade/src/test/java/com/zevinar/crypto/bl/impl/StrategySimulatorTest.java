package com.zevinar.crypto.bl.impl;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.math.NumberUtils;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.Trade;
import org.mockito.Mockito;

import com.zevinar.crypto.exchange.SimExchangeHandler;
import com.zevinar.crypto.exchange.SimpleStrategy;
import com.zevinar.crypto.utils.DateUtils;

public class StrategySimulatorTest {
	StrategySimulator simulator = new StrategySimulator();

	@Test
	public void testBreakHourlyDailyData() {
		int strategySampleRateInSec = 15;

		// 27.12.2017 15:55
		long startTime = 1514382933050L;
		// First Segment
		Trade firstTransaction = buildTransaction(startTime + 2000);
		Trade secondTransaction = buildTransaction(startTime + 3000);
		// Last Segment
		Trade thirdTransaction = buildTransaction(startTime + DateUtils.HOUR_IN_MS);
		List<Trade> fullHourTransactionsList = Arrays.asList(firstTransaction, secondTransaction, thirdTransaction);

		List<List<Trade>> breakDownHourlyData = simulator.breakDownHourlyData(fullHourTransactionsList,
				strategySampleRateInSec, startTime);
		final int minutes = breakDownHourlyData.size() / (60 / strategySampleRateInSec);
		MatcherAssert.assertThat(minutes, Matchers.is(60));
		// First Segment
		List<Trade> firstSegment = breakDownHourlyData.get(NumberUtils.INTEGER_ZERO);
		MatcherAssert.assertThat(firstSegment.size(), Matchers.is(2));
		MatcherAssert.assertThat(firstSegment, Matchers.contains(firstTransaction, secondTransaction));

		// Second Segment
		List<Trade> lastSegment = breakDownHourlyData.get(breakDownHourlyData.size() - NumberUtils.INTEGER_ONE);
		MatcherAssert.assertThat(lastSegment.size(), Matchers.is(NumberUtils.INTEGER_ONE));
		MatcherAssert.assertThat(lastSegment, Matchers.contains(thirdTransaction));

		// Whole List
		List<Trade> allElementsFlat = breakDownHourlyData.stream().flatMap(e -> e.stream())
				.collect(Collectors.toList());
		MatcherAssert.assertThat(allElementsFlat.size(), Matchers.is(3));
	}

	@Test
	public void testRunSimulation() {
		simulator.setNumOfDays(1);
		simulator.setSleepDuration(0);
		SimExchangeHandler exchangeHandler = Mockito.spy(new SimExchangeHandler());
		SimpleStrategy strategy = new SimpleStrategy();
		strategy.setBidDiscount(0.2);
		strategy.setSellProfit(0.2);
		strategy.init(exchangeHandler);

		doReturn(buildTransactionList()).when(exchangeHandler).getTradesWithCache(Mockito.any(CurrencyPair.class),
				Mockito.isNull(), Mockito.anyLong(), Mockito.anyLong(), Mockito.isNull());
		simulator.runSimulation(strategy, exchangeHandler);
		assertThat(((int) (exchangeHandler.getCoinBalance(Currency.USD) * 100)) / 100.0, is(15.24));
		assertThat(exchangeHandler.getOpenTransactions().size(), is(1));

	}

	@Test
	public void testRunSimulationLive() {
		StrategySimulator simulator = Mockito.spy(new StrategySimulator());
		doReturn(1L).when(simulator).calculateNumOfHours();
		SimExchangeHandler exchangeHandler = Mockito.spy(new SimExchangeHandler());
		SimpleStrategy strategy = new SimpleStrategy();
		strategy.setBidDiscount(0.2);
		strategy.setSellProfit(0.2);
		strategy.init(exchangeHandler);

		simulator.runSimulation(strategy, exchangeHandler);
		verify(exchangeHandler, Mockito.times(1)).getTradesWithCache(Mockito.any(CurrencyPair.class), Mockito.eq(null),
				Mockito.anyLong(), Mockito.anyLong(), Mockito.eq(null));
		verify(exchangeHandler, Mockito.times(1)).printStatus();

	}

	private List<Trade> buildTransactionList() {
		long minInMS = 60 * 1000;
		long hourInMs = 60 * minInMS;
		final long startTime = System.currentTimeMillis() - DateUtils.DAY_IN_MS;
		List<Trade> list = new ArrayList<>();
		list.add(buildTransaction(startTime + 2000, 250, new CurrencyPair("LTC", "USDT")));
		list.add(buildTransaction(startTime + 2 * minInMS, 248, new CurrencyPair("LTC", "USDT")));
		list.add(buildTransaction(startTime + 4 * minInMS, 221, new CurrencyPair("LTC", "USDT")));
		list.add(buildTransaction(startTime + 6 * minInMS, 199, new CurrencyPair("LTC", "USDT")));
		list.add(buildTransaction(startTime + hourInMs + 2 * minInMS, 180, new CurrencyPair("LTC", "USDT")));
		list.add(buildTransaction(startTime + hourInMs + 4 * minInMS, 189, new CurrencyPair("LTC", "USDT")));
		list.add(buildTransaction(startTime + hourInMs + 6 * minInMS, 200, new CurrencyPair("LTC", "USDT")));
		list.add(buildTransaction(startTime + 2 * hourInMs, 239, new CurrencyPair("LTC", "USDT")));
		list.add(buildTransaction(startTime + 3 * hourInMs, 244, new CurrencyPair("LTC", "USDT")));
		list.add(buildTransaction(startTime + 4 * hourInMs, 270, new CurrencyPair("LTC", "USDT")));
		list.add(buildTransaction(startTime + 5 * hourInMs, 215, new CurrencyPair("LTC", "USDT")));
		list.add(buildTransaction(startTime + 6 * hourInMs, 202, new CurrencyPair("LTC", "USDT")));
		list.add(buildTransaction(startTime + 7 * hourInMs, 196, new CurrencyPair("LTC", "USDT")));
		return list;
	}

	private Trade buildTransaction(long time) {
		Trade coinTransaction = Mockito.mock(Trade.class);
		Mockito.when(coinTransaction.getTimestamp()).thenReturn(new Date(time));
		return coinTransaction;
	}

	private Trade buildTransaction(long time, double price, CurrencyPair coinType) {
		Trade coinTransaction = Mockito.mock(Trade.class);
		Mockito.when(coinTransaction.getTimestamp()).thenReturn(new Date(time));
		Mockito.when(coinTransaction.getPrice()).thenReturn(new BigDecimal(price));
		Mockito.when(coinTransaction.getCurrencyPair()).thenReturn(coinType);
		return coinTransaction;
	}
}
