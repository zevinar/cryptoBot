package com.zevinar.crypto.bl.impl;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.math.NumberUtils;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;

import com.zevinar.crypto.exchange.impl.SimExchangeHandler;
import com.zevinar.crypto.exchange.impl.SimpleStrategy;
import com.zevinar.crypto.exchange.interfcaes.ICoinTransaction;
import com.zevinar.crypto.utils.enums.CoinTypeEnum;

public class StrategySimulatorTest {
	StrategySimulator simulator = new StrategySimulator();

	@Test
	public void testBreakDownDailyData() {
		int strategySampleRateInSec = 15;

		// 27.12.2017 15:55
		long startTime = 1514382933050L;
		// First Segment
		ICoinTransaction firstTransaction = buildTransaction(startTime + 2000);
		ICoinTransaction secondTransaction = buildTransaction(startTime + 3000);
		// Last Segment
		ICoinTransaction thirdTransaction = buildTransaction(startTime + StrategySimulator.DAY_IN_MS);
		List<ICoinTransaction> fullDayTransactionsList = Arrays.asList(firstTransaction, secondTransaction,
				thirdTransaction);

		List<List<ICoinTransaction>> breakDownDailyData = simulator.breakDownDailyData(fullDayTransactionsList,
				strategySampleRateInSec, startTime);
		final int hours = breakDownDailyData.size() * strategySampleRateInSec / 3600;
		MatcherAssert.assertThat(hours, Matchers.is(24));
		// First Segment
		List<ICoinTransaction> firstSegment = breakDownDailyData.get(NumberUtils.INTEGER_ZERO);
		MatcherAssert.assertThat(firstSegment.size(), Matchers.is(2));
		MatcherAssert.assertThat(firstSegment, Matchers.contains(firstTransaction, secondTransaction));

		// Second Segment
		List<ICoinTransaction> lastSegment = breakDownDailyData
				.get(breakDownDailyData.size() - NumberUtils.INTEGER_ONE);
		MatcherAssert.assertThat(lastSegment.size(), Matchers.is(NumberUtils.INTEGER_ONE));
		MatcherAssert.assertThat(lastSegment, Matchers.contains(thirdTransaction));

		// Whole List
		List<ICoinTransaction> allElementsFlat = breakDownDailyData.stream().flatMap(e -> e.stream())
				.collect(Collectors.toList());
		MatcherAssert.assertThat(allElementsFlat.size(), Matchers.is(3));
	}
	
	@Test @Ignore
	public void testRunSimulation() {
		simulator.setNumOfDays(1);
		SimExchangeHandler exchangeHandler = Mockito.spy(new SimExchangeHandler());
		SimpleStrategy strategy = new SimpleStrategy();
		strategy.init(exchangeHandler);
		Mockito.doReturn(buildTransactionList()).when(exchangeHandler.getSingleCoinTransactions(Mockito.any(CoinTypeEnum.class), Mockito.anyLong(), Mockito.anyLong()));
		simulator.runSimulation(strategy, exchangeHandler);
		
	}

	private List<ICoinTransaction> buildTransactionList() {
		final long startTime = System.currentTimeMillis() - StrategySimulator.DAY_IN_MS;
		ICoinTransaction firstTransaction = buildTransaction(startTime + 2000);
		return null;
	}

	private ICoinTransaction buildTransaction(long time) {
		ICoinTransaction coinTransaction = Mockito.mock(ICoinTransaction.class);
		Mockito.when(coinTransaction.getTransactionTime()).thenReturn(time);
		return coinTransaction;
	}
	
	private ICoinTransaction buildTransaction(long time, double quantity, double price, CoinTypeEnum coinType) {
		 
		
		ICoinTransaction coinTransaction = Mockito.mock(ICoinTransaction.class);
		Mockito.when(coinTransaction.getTransactionTime()).thenReturn(time);
//		Mockito.when(coinTransaction.getTransactionQuantity()).thenReturn(time);
		return coinTransaction;
	}
}
