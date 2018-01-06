package com.zevinar.crypto.exchange.impl;

import static org.apache.commons.collections4.CollectionUtils.isEmpty;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.map.HashedMap;
import org.apache.commons.lang3.math.NumberUtils;
import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.Trade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zevinar.crypto.exchange.dto.IOpenTransaction;
import com.zevinar.crypto.exchange.impl.realexchange.BinanceExchangeHandler;
import com.zevinar.crypto.utils.FunctionalCodeUtils;
import com.zevinar.crypto.utils.enums.ExchangeEnum;
import com.zevinar.crypto.utils.enums.TransactionTypeEnum;

public class SimExchangeHandler extends AbstractTradeExchangeHandler {
	private static final Logger LOG = LoggerFactory.getLogger(SimExchangeHandler.class);

	List<IOpenTransaction> openTransactionsList = new ArrayList<>();
	Map<Currency, Double> coinBalanceMap = new HashedMap<>();
	private double currentCashUSD = 100;

	@Override
	public ExchangeEnum getExchangeType() {
		return ExchangeEnum.BINANCE;
	}


	@Override
	public void postTransactionRequest(IOpenTransaction request) {
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

	}


	public List<IOpenTransaction> getOpenTransactions() {
		return openTransactionsList;
	}//TODO crypto change to getopenorders

	@Override
	public Double getCoinBalance(Currency coinType) {
		double balance = NumberUtils.DOUBLE_ZERO;
		if (coinType.equals(Currency.USD))
			return currentCashUSD;
		if (coinBalanceMap.containsKey(coinType)) {
			balance = coinBalanceMap.get(coinType);
		}

		return balance;
	}





	@Override
	public List<Trade> getTradesWithCache(CurrencyPair currencyPair, Long fromId, Long fromTime, Long toTime, Long limit)  {
		final BinanceExchangeHandler binanceExchangeHandler = new BinanceExchangeHandler();
		return FunctionalCodeUtils.methodRunner( () -> binanceExchangeHandler.getTradesWithCache(currencyPair, null,fromTime, toTime,null));
	}


	public void feedData(List<Trade> dataList) {
		if (!isEmpty(dataList) && !isEmpty(openTransactionsList)) {
			Trade Trade = dataList.get(NumberUtils.INTEGER_ZERO);
			IOpenTransaction openTransaction = openTransactionsList.get(NumberUtils.INTEGER_ZERO);
			if (openTransaction.getTransactionType() == TransactionTypeEnum.BUY
					&& openTransaction.getCoinUsdPrice() >= Trade.getPrice().doubleValue()) {
				openTransactionsList.remove(openTransaction);
				LOG.info("Transaction Performed {}", openTransaction);
				double coinAmountBought = (1 - getTradingFee() ) * openTransaction.getTransactionAmount() / openTransaction.getCoinUsdPrice();
				coinBalanceMap.put(openTransaction.getCoinType(), coinAmountBought);

			} else if (openTransaction.getTransactionType() == TransactionTypeEnum.SELL
					&& openTransaction.getCoinUsdPrice() <= Trade.getPrice().doubleValue()) {
				currentCashUSD +=  (1 - getTradingFee() ) * openTransaction.getCoinUsdPrice() * openTransaction.getTransactionAmount();
				openTransactionsList.remove(openTransaction);
				LOG.info("Transaction Performed {} Current Cache is: {}", openTransaction, currentCashUSD);
			}
		}
	}

	@Override
	public Double getTradingFee() {

		return 0.02;
	}

}
