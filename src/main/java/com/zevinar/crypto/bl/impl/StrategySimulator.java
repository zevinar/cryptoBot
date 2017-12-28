package com.zevinar.crypto.bl.impl;

import java.util.List;

import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zevinar.crypto.bl.interfcaes.ITradeStrategy;
import com.zevinar.crypto.exchange.impl.BinanceExchangeHandler;
import com.zevinar.crypto.exchange.interfcaes.ICoinTransaction;
import com.zevinar.crypto.exchange.interfcaes.IExchangeActionsHandler;
import com.zevinar.crypto.exchange.interfcaes.IExchangeInfoHandler;
import com.zevinar.crypto.utils.FunctionalCodeUtils;
import com.zevinar.crypto.utils.FunctionalCodeUtils.RunnableThrows;
import com.zevinar.crypto.utils.enums.CoinTypeEnum;

public class StrategySimulator {
	private static final Logger LOG = LoggerFactory.getLogger(StrategySimulator.class);
	IExchangeInfoHandler exchangeHandler = new BinanceExchangeHandler();
	//TODO mshitrit implement fake one to use in simulation
	IExchangeActionsHandler exchangeActionHandler = Mockito.mock(IExchangeActionsHandler.class);
	// Sim Params
	private static final int NUM_OF_DAYS = 30;
	private static final double INITIAL_CASH_USD = 100;

	public void runSimulation(ITradeStrategy strategy) {
		long currentTimeMillis = System.currentTimeMillis();
		CoinTypeEnum strategyCryptoCoinn = strategy.getCoinOfIntrest();
		strategy.init(exchangeActionHandler, INITIAL_CASH_USD );
		for (int i = 0; i < NUM_OF_DAYS; i++) {
			int dayInMS = 24 * 60 * 60 * 1000;
			List<ICoinTransaction> fullDayTransactionsList = exchangeHandler.getSingleCoinTransactions(strategyCryptoCoinn,
					currentTimeMillis - (NUM_OF_DAYS - i) * dayInMS,
					currentTimeMillis - (NUM_OF_DAYS - i + 1) * dayInMS);
			List<List<ICoinTransaction>> subSetDataForStrategyCallback = breakDailyData(fullDayTransactionsList, strategy.getStrategySampleRateInSec());
			subSetDataForStrategyCallback.stream().forEach( strategy::getDataCallbackMethod);
			//Sleep in order not to overload Binance API
			FunctionalCodeUtils.methodRunner( (RunnableThrows<InterruptedException>) () -> Thread.sleep(10000));
			
		}

	}

	private List<List<ICoinTransaction>> breakDailyData(List<ICoinTransaction> fullDayTransactionsList,
			int strategySampleRateInSec) {
		// TODO mshitrit implement
		return null;
	}
}
