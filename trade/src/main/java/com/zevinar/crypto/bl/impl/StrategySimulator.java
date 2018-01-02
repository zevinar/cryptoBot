package com.zevinar.crypto.bl.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zevinar.crypto.exchange.impl.BinanceExchangeHandler;
import com.zevinar.crypto.exchange.interfcaes.ICoinTransaction;
import com.zevinar.crypto.exchange.interfcaes.IExchangeHandler;
import com.zevinar.crypto.exchange.interfcaes.IStrategy;
import com.zevinar.crypto.utils.FunctionalCodeUtils;
import com.zevinar.crypto.utils.FunctionalCodeUtils.RunnableThrows;
import com.zevinar.crypto.utils.enums.CoinTypeEnum;

public class StrategySimulator {
	private static final Logger LOG = LoggerFactory.getLogger(StrategySimulator.class);
	public static final int DAY_IN_MS = 24 * 60 * 60 * 1000;
	IExchangeHandler exchangeHandler = new BinanceExchangeHandler();
	// Sim Params
	private static final int NUM_OF_DAYS = 30;
	private static final double INITIAL_CASH_USD = 100;

	public void runSimulation(IStrategy strategy) {
		long currentTimeMillis = System.currentTimeMillis();
		CoinTypeEnum strategyCryptoCoinn = strategy.getCoinOfIntrest();
		strategy.init(exchangeHandler);
		for (int i = 0; i < NUM_OF_DAYS; i++) {

			final long startTime = currentTimeMillis - (NUM_OF_DAYS - i) * DAY_IN_MS;
			List<ICoinTransaction> fullDayTransactionsList = exchangeHandler.getSingleCoinTransactions(
					strategyCryptoCoinn, startTime, currentTimeMillis - (NUM_OF_DAYS - i + 1) * DAY_IN_MS);
			List<List<ICoinTransaction>> subSetDataForStrategyCallback = breakDownDailyData(fullDayTransactionsList,
					strategy.getStrategySampleRateInSec(), startTime);
			subSetDataForStrategyCallback.stream().forEach(strategy::analyzeData);
			// Sleep in order not to overload Binance API
			FunctionalCodeUtils.methodRunner((RunnableThrows<InterruptedException>) () -> Thread.sleep(10000));

		}

	}

	List<List<ICoinTransaction>> breakDownDailyData(List<ICoinTransaction> fullDayTransactionsList,
			int strategySampleRateInSec, long startTime) {
		
		List<Predicate<ICoinTransaction>> predicateList = new ArrayList<>();
		for (long i = startTime; i <= DAY_IN_MS + startTime; i = i
				+ strategySampleRateInSec * 1000) {
			long startTimeCurr = i;
			long endTimeCurr = startTimeCurr + strategySampleRateInSec * 1000;
			Predicate<ICoinTransaction> timeFilter = coinTrans -> coinTrans.getTransactionTime() >= startTimeCurr
					&& coinTrans.getTransactionTime() < endTimeCurr;
			predicateList.add(timeFilter);

		}
		//Each Element Represent Data Segment during strategySampleRateInSec
		final Stream<List<ICoinTransaction>> streamByStrategySample = predicateList.stream().map( currPredicate -> fullDayTransactionsList.stream().filter(currPredicate).collect(Collectors.toList()));
		return streamByStrategySample.collect(Collectors.toList());
	}
}
