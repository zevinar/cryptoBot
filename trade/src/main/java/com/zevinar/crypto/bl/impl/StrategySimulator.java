package com.zevinar.crypto.bl.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zevinar.crypto.exchange.impl.SimExchangeHandler;
import com.zevinar.crypto.exchange.impl.SimpleStrategy;
import com.zevinar.crypto.exchange.interfcaes.ICoinTransaction;
import com.zevinar.crypto.exchange.interfcaes.IStrategy;
import com.zevinar.crypto.utils.FunctionalCodeUtils;
import com.zevinar.crypto.utils.FunctionalCodeUtils.RunnableThrows;
import com.zevinar.crypto.utils.enums.CoinTypeEnum;

public class StrategySimulator {
	private static final Logger LOG = LoggerFactory.getLogger(StrategySimulator.class);
	public static final int MIN_IN_MS = 60 * 1000;
	public static final int HOUR_IN_MS = 60 * MIN_IN_MS;
	public static final int DAY_IN_MS = 24 * HOUR_IN_MS;
	// Sim Params
	private int numOfDays = 23;
	private int sleepDuration = 3000;

	public static void main(String[] args) {
		StrategySimulator simulator = new StrategySimulator();
		SimExchangeHandler exchangeHandler = new SimExchangeHandler();
		SimpleStrategy strategy = new SimpleStrategy();
		simulator.setNumOfDays(7);
		strategy.init(exchangeHandler);
		simulator.runSimulation(strategy, exchangeHandler);
		

	}

	public void runSimulation(IStrategy strategy, SimExchangeHandler simExchangeHandler) {
		long currentTimeMillis = System.currentTimeMillis();
		CoinTypeEnum strategyCryptoCoinn = strategy.getCoinOfIntrest();
		long numOfHours = numOfDays * 24;
		for (int i = 0; i < numOfHours; i++) {
			final long startTime = currentTimeMillis - (numOfHours - i) * HOUR_IN_MS;
			final long endTime = currentTimeMillis - (numOfHours - i - 1) * HOUR_IN_MS ;
			List<ICoinTransaction> fullDayTransactionsList = simExchangeHandler.getSingleCoinTransactions(
					strategyCryptoCoinn, startTime, endTime);
			List<List<ICoinTransaction>> subSetDataForStrategyCallback = breakDownHourlyData(fullDayTransactionsList,
					strategy.getStrategySampleRateInSec(), startTime);
			subSetDataForStrategyCallback.stream().flatMap( List::stream).findFirst().ifPresent(trans -> LOG.debug("Current Quote is : {}", trans));
			subSetDataForStrategyCallback.stream().forEach(dataList -> {
				simExchangeHandler.feedData(dataList);
				strategy.analyzeData(dataList);
			});
			// Sleep in order not to overload Binance API
			FunctionalCodeUtils.methodRunner((RunnableThrows<InterruptedException>) () -> Thread.sleep(sleepDuration));

		}

	}

	List<List<ICoinTransaction>> breakDownHourlyData(List<ICoinTransaction> fullHourTransactionsList,
			int strategySampleRateInSec, long startTime) {

		List<Predicate<ICoinTransaction>> predicateList = new ArrayList<>();
		for (long i = startTime; i <= HOUR_IN_MS + startTime; i = i + strategySampleRateInSec * 1000) {
			long startTimeCurr = i;
			long endTimeCurr = startTimeCurr + strategySampleRateInSec * 1000;
			Predicate<ICoinTransaction> timeFilter = coinTrans -> coinTrans.getTransactionTime() >= startTimeCurr
					&& coinTrans.getTransactionTime() < endTimeCurr;
			predicateList.add(timeFilter);

		}
		// Each Element Represent Data Segment during strategySampleRateInSec
		final Stream<List<ICoinTransaction>> streamByStrategySample = predicateList.stream().map(
				currPredicate -> fullHourTransactionsList.stream().filter(currPredicate).collect(Collectors.toList()));
		return streamByStrategySample.collect(Collectors.toList());
	}

	public void setNumOfDays(int numOfDays) {
		this.numOfDays = numOfDays;
	}

	public void setSleepDuration(int sleepDuration) {
		this.sleepDuration = sleepDuration;
	}
}
