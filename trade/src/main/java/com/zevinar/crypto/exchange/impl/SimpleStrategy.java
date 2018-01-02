package com.zevinar.crypto.exchange.impl;

import static org.apache.commons.collections4.CollectionUtils.isEmpty;

import java.util.List;

import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zevinar.crypto.bl.impl.StrategySimulator;
import com.zevinar.crypto.exchange.interfcaes.ICoinTransaction;
import com.zevinar.crypto.exchange.interfcaes.IExchangeHandler;
import com.zevinar.crypto.exchange.interfcaes.IOpenTransaction;
import com.zevinar.crypto.exchange.interfcaes.IStrategy;
import com.zevinar.crypto.utils.Constants;
import com.zevinar.crypto.utils.enums.CoinTypeEnum;

public class SimpleStrategy implements IStrategy {
	private static final Logger LOG = LoggerFactory.getLogger(SimpleStrategy.class);
	private IExchangeHandler exchangeHandler;
	private static final double BID_DISCOUNT = 0.8;
	@Override
	public int getStrategySampleRateInSec() {
		return 60;
	}

	@Override
	public CoinTypeEnum getCoinOfIntrest() {
		return CoinTypeEnum.LTC;
	}

	@Override
	public boolean analyzeData(List<ICoinTransaction> data) {
		boolean continueAnalysis = true;
		if(!isEmpty(data)){
			CoinTypeEnum coinType = data.get(NumberUtils.INTEGER_ZERO).getTransactionCoinType();
			boolean isBroke = exchangeHandler.getCoinBalance(coinType) <= coinType.getMinCoinForTrade() && isEmpty(exchangeHandler.getOpenTransactions()) && exchangeHandler.getCurrentCashUSD() <  Constants.MIN_CASH_FOR_TRADE_USD;
			if( isBroke ){
				LOG.debug("Strategy is Bankrupt");
				continueAnalysis = false;
				
			}
			else if ( isEmpty(exchangeHandler.getOpenTransactions())){
				//First Initilazation
				final ICoinTransaction iCoinTransaction = data.get(NumberUtils.INTEGER_ZERO);
				double lastCoinPrice = iCoinTransaction.getTransactionPrice();
				double wantedBuyPrice = BID_DISCOUNT * lastCoinPrice;
				LOG.info("Posting Buy: {} At Price: {}", iCoinTransaction.getTransactionCoinType().getCoinName(), wantedBuyPrice);
				exchangeHandler.postBuy(iCoinTransaction.getTransactionCoinType(), wantedBuyPrice);
				
			}
			else{
				//Open Buy
				
				//Open Sell
			}
			
		}
		return continueAnalysis;
		
	}

	@Override
	public void init(IExchangeHandler exchangeHandler) {
		this.exchangeHandler = exchangeHandler;
	}

}
