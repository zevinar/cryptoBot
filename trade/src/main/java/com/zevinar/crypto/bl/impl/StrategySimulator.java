package com.zevinar.crypto.bl.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.Trade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zevinar.crypto.exchange.SimExchangeHandler;
import com.zevinar.crypto.exchange.SimpleStrategy;
import com.zevinar.crypto.exchange.interfcaes.IStrategy;
import com.zevinar.crypto.utils.DateUtils;
import com.zevinar.crypto.utils.FunctionalCodeUtils;
import com.zevinar.crypto.utils.FunctionalCodeUtils.RunnableThrows;

public class StrategySimulator {
	private static final Logger LOG = LoggerFactory.getLogger(StrategySimulator.class);
	// Sim Params
	private int numOfDays = 23;
	private int sleepDuration = 3000;

	public static void main(String[] args) {
		StrategySimulator simulator = new StrategySimulator();
		SimExchangeHandler exchangeHandler = new SimExchangeHandler();
		SimpleStrategy strategy = new SimpleStrategy();
		simulator.setNumOfDays(2);
		strategy.init(exchangeHandler);
		simulator.runSimulation(strategy, exchangeHandler);

	}

	public void runSimulation(IStrategy strategy, SimExchangeHandler simExchangeHandler) {
		long currentTimeMillis = DateUtils.roundToClosetHour(System.currentTimeMillis());
		CurrencyPair strategyCryptoCoinn = strategy.getCoinOfIntrest();
		long numOfHours = calculateNumOfHours();
		for (int i = 0; i < numOfHours; i++) {
			final long startTime = currentTimeMillis - (numOfHours - i) * DateUtils.HOUR_IN_MS;
			final long endTime = currentTimeMillis - (numOfHours - i - 1) * DateUtils.HOUR_IN_MS;
			List<Trade> fullHourTransactionsList = null;
			fullHourTransactionsList = simExchangeHandler.getTradesWithCache(strategyCryptoCoinn, null, startTime,
					endTime, null);
			List<List<Trade>> subSetDataForStrategyCallback = breakDownHourlyData(fullHourTransactionsList,
					strategy.getStrategySampleRateInSec(), startTime);
			if (i % 24 == 0) {
				subSetDataForStrategyCallback.stream().flatMap(List::stream).findFirst()
						.ifPresent(trans -> LOG.info("Current Quote is : {}", trans));
			}
			subSetDataForStrategyCallback.stream().forEach(dataList -> {
				simExchangeHandler.feedData(dataList);
				strategy.analyzeData(dataList);
			});
			// Sleep in order not to overload Binance API
			FunctionalCodeUtils.methodRunner((RunnableThrows<InterruptedException>) () -> Thread.sleep(sleepDuration));

		}
		simExchangeHandler.printStatus();

	}

	long calculateNumOfHours() {
		return numOfDays * 24L;
	}

	List<List<Trade>> breakDownHourlyData(List<Trade> fullHourTransactionsList, int strategySampleRateInSec,
			long startTime) {

		List<Predicate<Trade>> predicateList = new ArrayList<>();
		for (long i = startTime; i <= DateUtils.HOUR_IN_MS + startTime; i = i + strategySampleRateInSec * 1000) {
			long startTimeCurr = i;
			long endTimeCurr = startTimeCurr + strategySampleRateInSec * 1000;
			Predicate<Trade> timeFilter = coinTrans -> coinTrans.getTimestamp().getTime() >= startTimeCurr
					&& coinTrans.getTimestamp().getTime() < endTimeCurr;
			predicateList.add(timeFilter);

		}
		// Each Element Represent Data Segment during strategySampleRateInSec
		final Stream<List<Trade>> streamByStrategySample = predicateList.stream().map(
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
