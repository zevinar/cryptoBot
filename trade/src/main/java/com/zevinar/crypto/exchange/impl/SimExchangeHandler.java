package com.zevinar.crypto.exchange.impl;

import static org.apache.commons.collections4.CollectionUtils.isEmpty;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.zevinar.crypto.exchange.interfcaes.IExchangeHandler;
import org.apache.commons.collections4.map.HashedMap;
import org.apache.commons.lang3.math.NumberUtils;
import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.Trade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zevinar.crypto.exchange.dto.IOpenTransaction;
import com.zevinar.crypto.exchange.dto.ITransactionResult;
import com.zevinar.crypto.utils.enums.ExchangeDetailsEnum;
import com.zevinar.crypto.utils.enums.TransactionTypeEnum;

public class SimExchangeHandler implements IExchangeHandler {
	private static final Logger LOG = LoggerFactory.getLogger(SimExchangeHandler.class);

	ITransactionResult fakeResult = new ITransactionResult() {
	};
	List<IOpenTransaction> openTransactionsList = new ArrayList<>();
	Map<Currency, Double> coinBalanceMap = new HashedMap<>();
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
		Currency coinType = request.getCoinType();

		if (request.getTransactionType() == TransactionTypeEnum.BUY) {

			if (currentCashUSD < request.getTransactionAmount()) {
				LOG.error("Illegal Transaction Current Cash {}, Transaction: {}", currentCashUSD, request);
			} else {
				currentCashUSD = currentCashUSD - request.getTransactionAmount();
				openTransactionsList.add(request);
			}
		} else {
			if (request.getTransactionAmount() > getCoinBalance(coinType)) {
				LOG.error("Illegal Transaction Current Coin Amount {}, Transaction: {}", getCoinBalance(coinType),
						request);
			} else {
				coinBalanceMap.put(coinType,getCoinBalance(coinType) - request.getTransactionAmount());
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
	public double getCoinBalance(Currency coinType) {
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
	public List<Trade> getSingleCoinTransactions(CurrencyPair coinType, long fromTime, long toTime) throws IOException {
		return BinanceTradeExchangeHandler.getSingleCoinTransactionsWithCache(coinType, fromTime, toTime);
	}

	public void feedData(List<Trade> dataList) {
		if (!isEmpty(dataList) && !isEmpty(openTransactionsList)) {
			Trade Trade = dataList.get(NumberUtils.INTEGER_ZERO);
			IOpenTransaction openTransaction = openTransactionsList.get(NumberUtils.INTEGER_ZERO);
			if (openTransaction.getTransactionType() == TransactionTypeEnum.BUY
					&& openTransaction.getCoinUsdPrice() >= Trade.getPrice().doubleValue()) {
				openTransactionsList.remove(openTransaction);
				LOG.info("Transaction Performed {}", openTransaction);
				double coinAmountBought = (1 - getTransactionFee() ) * openTransaction.getTransactionAmount() / openTransaction.getCoinUsdPrice();
				coinBalanceMap.put(openTransaction.getCoinType(), coinAmountBought);

			} else if (openTransaction.getTransactionType() == TransactionTypeEnum.SELL
					&& openTransaction.getCoinUsdPrice() <= Trade.getPrice().doubleValue()) {
				currentCashUSD +=  (1 - getTransactionFee() ) * openTransaction.getCoinUsdPrice() * openTransaction.getTransactionAmount();
				openTransactionsList.remove(openTransaction);
				LOG.info("Transaction Performed {} Current Cache is: {}", openTransaction, currentCashUSD);
			}
		}
	}

}
