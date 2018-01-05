package com.zevinar.crypto.bl.impl;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.math.NumberUtils;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.mockito.Mockito;

import com.zevinar.crypto.exchange.impl.SimExchangeHandler;
import com.zevinar.crypto.exchange.impl.SimpleStrategy;
import com.zevinar.crypto.exchange.interfcaes.ICoinTransaction;
import com.zevinar.crypto.utils.enums.CoinTypeEnum;

public class StrategySimulatorTest {
	StrategySimulator simulator = new StrategySimulator();

	@Test
	public void testBreakHourlyDailyData() {
		int strategySampleRateInSec = 15;

		// 27.12.2017 15:55
		long startTime = 1514382933050L;
		// First Segment
		ICoinTransaction firstTransaction = buildTransaction(startTime + 2000);
		ICoinTransaction secondTransaction = buildTransaction(startTime + 3000);
		// Last Segment
		ICoinTransaction thirdTransaction = buildTransaction(startTime + StrategySimulator.HOUR_IN_MS);
		List<ICoinTransaction> fullHourTransactionsList = Arrays.asList(firstTransaction, secondTransaction,
				thirdTransaction);

		List<List<ICoinTransaction>> breakDownHourlyData = simulator.breakDownHourlyData(fullHourTransactionsList,
				strategySampleRateInSec, startTime);
		final int minutes = breakDownHourlyData.size() / (60 / strategySampleRateInSec);
		MatcherAssert.assertThat(minutes, Matchers.is(60));
		// First Segment
		List<ICoinTransaction> firstSegment = breakDownHourlyData.get(NumberUtils.INTEGER_ZERO);
		MatcherAssert.assertThat(firstSegment.size(), Matchers.is(2));
		MatcherAssert.assertThat(firstSegment, Matchers.contains(firstTransaction, secondTransaction));

		// Second Segment
		List<ICoinTransaction> lastSegment = breakDownHourlyData
				.get(breakDownHourlyData.size() - NumberUtils.INTEGER_ONE);
		MatcherAssert.assertThat(lastSegment.size(), Matchers.is(NumberUtils.INTEGER_ONE));
		MatcherAssert.assertThat(lastSegment, Matchers.contains(thirdTransaction));

		// Whole List
		List<ICoinTransaction> allElementsFlat = breakDownHourlyData.stream().flatMap(e -> e.stream())
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
		Mockito.doReturn(buildTransactionList()).when(exchangeHandler)
				.getSingleCoinTransactions(Mockito.any(CoinTypeEnum.class), Mockito.anyLong(), Mockito.anyLong());
		simulator.runSimulation(strategy, exchangeHandler);
		assertThat(((int) (exchangeHandler.getCurrentCashUSD() * 100))/100.0 , is(15.24));
		assertThat(exchangeHandler.getOpenTransactions().size(), is(1));

	}

	private List<ICoinTransaction> buildTransactionList() {
		long minInMS = 60 * 1000;
		long hourInMs = 60 * minInMS;
		final long startTime = System.currentTimeMillis() - StrategySimulator.DAY_IN_MS;
		List<ICoinTransaction> list = new ArrayList<>();
		list.add(buildTransaction(startTime + 2000, 250, CoinTypeEnum.LTC));
		list.add(buildTransaction(startTime + 2 * minInMS, 248, CoinTypeEnum.LTC));
		list.add(buildTransaction(startTime + 4 * minInMS, 221, CoinTypeEnum.LTC));
		list.add(buildTransaction(startTime + 6 * minInMS, 199, CoinTypeEnum.LTC));
		list.add(buildTransaction(startTime + hourInMs + 2 * minInMS, 180, CoinTypeEnum.LTC));
		list.add(buildTransaction(startTime + hourInMs + 4 * minInMS, 189, CoinTypeEnum.LTC));
		list.add(buildTransaction(startTime + hourInMs + 6 * minInMS, 200, CoinTypeEnum.LTC));
		list.add(buildTransaction(startTime + 2 * hourInMs, 239, CoinTypeEnum.LTC));
		list.add(buildTransaction(startTime + 3 * hourInMs , 244, CoinTypeEnum.LTC));
		list.add(buildTransaction(startTime + 4 * hourInMs , 270, CoinTypeEnum.LTC));
		list.add(buildTransaction(startTime + 5 * hourInMs , 215, CoinTypeEnum.LTC));
		list.add(buildTransaction(startTime + 6 * hourInMs , 202, CoinTypeEnum.LTC));
		list.add(buildTransaction(startTime + 7 * hourInMs , 196, CoinTypeEnum.LTC));
		return list;
	}

	private ICoinTransaction buildTransaction(long time) {
		ICoinTransaction coinTransaction = Mockito.mock(ICoinTransaction.class);
		Mockito.when(coinTransaction.getTransactionTime()).thenReturn(time);
		return coinTransaction;
	}

	private ICoinTransaction buildTransaction(long time, double price, CoinTypeEnum coinType) {
		ICoinTransaction coinTransaction = Mockito.mock(ICoinTransaction.class);
		Mockito.when(coinTransaction.getTransactionTime()).thenReturn(time);
		Mockito.when(coinTransaction.getTransactionPrice()).thenReturn(price);
		Mockito.when(coinTransaction.getTransactionCoinType()).thenReturn(coinType);
		return coinTransaction;
	}
}
