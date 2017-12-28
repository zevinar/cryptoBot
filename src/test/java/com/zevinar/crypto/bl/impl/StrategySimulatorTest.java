package com.zevinar.crypto.bl.impl;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.math.NumberUtils;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.mockito.Mockito;

import com.zevinar.crypto.exchange.interfcaes.ICoinTransaction;

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
		ICoinTransaction thirdTransaction = buildTransaction(startTime + StrategySimulator.DAY_IN_MS - 1000);
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
		
		
		/*List<ICoinTransaction> allElementsFlat = breakDownDailyData.stream().flatMap(e -> e.stream()).collect(Collectors.toList());
		System.out.println(allElementsFlat.size());
		MatcherAssert.assertThat(allElementsFlat, Matchers.contains(firstTransaction, secondTransaction));
		MatcherAssert.assertThat(allElementsFlat, Matchers.contains(thirdTransaction));*/
		//TODO mshitrit check this
		List<ICoinTransaction> lastSegment = breakDownDailyData.get(breakDownDailyData.size() - NumberUtils.INTEGER_ONE);
		MatcherAssert.assertThat(lastSegment.size(), Matchers.is(NumberUtils.INTEGER_ONE));
		MatcherAssert.assertThat(lastSegment, Matchers.contains(thirdTransaction));
		
		

	}

	private ICoinTransaction buildTransaction(long time) {
		ICoinTransaction coinTransaction = Mockito.mock(ICoinTransaction.class);
		Mockito.when(coinTransaction.getTransactionTime()).thenReturn(time);
		return coinTransaction;
	}
}
