package com.zevinar.crypto.exchange.impl;

import static org.apache.commons.collections4.CollectionUtils.isEmpty;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.map.HashedMap;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zevinar.crypto.bl.impl.BinanceTradeExchangeHandler;
import com.zevinar.crypto.exchange.interfcaes.ICoinTransaction;
import com.zevinar.crypto.exchange.interfcaes.IExchangeHandler;
import com.zevinar.crypto.exchange.interfcaes.IOpenTransaction;
import com.zevinar.crypto.exchange.interfcaes.ITransactionResult;
import com.zevinar.crypto.utils.enums.CoinTypeEnum;
import com.zevinar.crypto.utils.enums.ExchangeDetailsEnum;
import com.zevinar.crypto.utils.enums.TransactionTypeEnum;

public class SimExchangeHandler implements IExchangeHandler {
	private static final Logger LOG = LoggerFactory.getLogger(SimExchangeHandler.class);

	ITransactionResult fakeResult = new ITransactionResult() {
	};
	List<IOpenTransaction> openTransactionsList = new ArrayList<>();
	Map<CoinTypeEnum, Double> coinBalanceMap = new HashedMap<>();
	private double currentCashUSD = 100;

	@Override
	public ExchangeDetailsEnum getExchangeDetails() {
		return ExchangeDetailsEnum.WEX;
	}

	@Override
	public Double getTransactionFee() {
		return 0.02;
	}

	@Override
	public ITransactionResult postTransactionRequest(IOpenTransaction request) {
		CoinTypeEnum coinType = request.getCoinType();

		if (request.getTransactionType() == TransactionTypeEnum.BUY) {

			if (currentCashUSD < request.getTransactionAmount()) {
				LOG.error("Illegal Transaction Current Cash {}, Transaction: {}", currentCashUSD, request);
			} else {
				currentCashUSD = currentCashUSD - request.getTransactionAmount();
				openTransactionsList.add(request);
			}
		} else {
			if (request.getTransactionAmount() > coinBalanceMap.get(coinType)) {
				LOG.error("Illegal Transaction Current Coin Amount {}, Transaction: {}", coinBalanceMap.get(coinType),
						request);
			} else {
				coinBalanceMap.put(coinType, coinBalanceMap.get(coinType) - request.getTransactionAmount());
				openTransactionsList.add(request);
			}
		}
		return fakeResult;
	}

	@Override
	public List<IOpenTransaction> getOpenTransactions() {
		return openTransactionsList;
	}

	@Override
	public double getCoinBalance(CoinTypeEnum coinType) {
		double balance = NumberUtils.DOUBLE_ZERO;
		if (coinBalanceMap.containsKey(coinType)) {
			balance = coinBalanceMap.get(coinType);
		}
		return balance;
	}

	@Override
	public double getCurrentCashUSD() {
		return currentCashUSD;
	}

	@Override
	public List<ICoinTransaction> getSingleCoinTransactions(CoinTypeEnum coinType, long fromTime, long toTime) {
		return BinanceTradeExchangeHandler.getSingleCoinTransactionsWithCache(coinType, fromTime, toTime);
	}

	public void feedData(List<ICoinTransaction> dataList) {
		if (!isEmpty(dataList) && !isEmpty(openTransactionsList)) {
			ICoinTransaction iCoinTransaction = dataList.get(NumberUtils.INTEGER_ZERO);
			IOpenTransaction openTransaction = openTransactionsList.get(NumberUtils.INTEGER_ZERO);
			if (openTransaction.getTransactionType() == TransactionTypeEnum.BUY
					&& openTransaction.getCoinUsdPrice() >= iCoinTransaction.getTransactionPrice()) {
				openTransactionsList.remove(openTransaction);
				LOG.info("Transaction Performed {}", openTransaction);
				double coinAmountBought = (1 - getTransactionFee() ) * openTransaction.getTransactionAmount() / openTransaction.getCoinUsdPrice();
				coinBalanceMap.put(openTransaction.getCoinType(), coinAmountBought);

			} else if (openTransaction.getTransactionType() == TransactionTypeEnum.SELL
					&& openTransaction.getCoinUsdPrice() <= iCoinTransaction.getTransactionPrice()) {
				currentCashUSD +=  (1 - getTransactionFee() ) * openTransaction.getCoinUsdPrice() * openTransaction.getTransactionAmount();
				openTransactionsList.remove(openTransaction);
				LOG.info("Transaction Performed {} Current Cache is: {}", openTransaction, currentCashUSD);
			}
		}
	}

}
