package com.zevinar.crypto.exchange.impl;

import static org.apache.commons.collections4.CollectionUtils.isEmpty;

import java.util.List;

import com.zevinar.crypto.exchange.dto.OpenTransactionImpl;
import com.zevinar.crypto.exchange.interfcaes.IExchangeHandler;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.math.NumberUtils;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.Trade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zevinar.crypto.exchange.dto.IOpenTransaction;
import com.zevinar.crypto.exchange.interfcaes.IStrategy;
import com.zevinar.crypto.utils.Constants;
import com.zevinar.crypto.utils.enums.TransactionTypeEnum;

public class SimpleStrategy implements IStrategy {
	private static final Logger LOG = LoggerFactory.getLogger(SimpleStrategy.class);
	private IExchangeHandler exchangeHandler;
	private double bidDiscount = 0.16;
	private double sellProfit = 0.2;
	@Override
	public int getStrategySampleRateInSec() {
		return 60;
	}

	@Override
	public CurrencyPair getCoinOfIntrest() {
		return new CurrencyPair("LTC","USD");
	}
	double wantedBuyPrice = 0, actualBuyPrice = 0, wantedSellPrice = 0, actualSellPrice = 0;
	@Override
	public boolean analyzeData(List<Trade> dataList) {
		
		boolean continueAnalysis = true;
		if (!isEmpty(dataList)) {
			CurrencyPair coinType = dataList.get(NumberUtils.INTEGER_ZERO).getCurrencyPair();
			List<IOpenTransaction> openTransactions = exchangeHandler.getOpenTransactions();
			boolean hasOpenTransactions = !isEmpty(openTransactions);
			boolean hasCoinsForTrade = exchangeHandler.getCoinBalance(coinType.base) >= 0.1; // coinType.getMinCoinForTrade();
			//TODO calculate getMinCoinForTrade using Xchange api
			boolean hasCashForTrade = exchangeHandler.getCurrentCashUSD() >= Constants.MIN_CASH_FOR_TRADE_USD;
			boolean isBroke = !hasCoinsForTrade && !hasOpenTransactions && !hasCashForTrade;
			if (isBroke) {
				LOG.debug("Strategy is Bankrupt");
				return false;

			}
			if (hasOpenTransactions) {
				handleOpenTransactions(openTransactions);
			} else if (hasCoinsForTrade) {
				actualBuyPrice = wantedBuyPrice;
			
				wantedSellPrice = (NumberUtils.DOUBLE_ONE + sellProfit) * actualBuyPrice;
				final OpenTransactionImpl sellRequest =
						new OpenTransactionImpl(coinType.base, TransactionTypeEnum.SELL, wantedSellPrice, exchangeHandler.getCoinBalance(coinType.base));
				LOG.info("Strategy Posting Sell: {}", sellRequest);
				exchangeHandler.postTransactionRequest(sellRequest);


			} else if (hasCashForTrade) {
				actualSellPrice = wantedSellPrice;
				final Trade Trade = dataList.get(NumberUtils.INTEGER_ZERO);
				double lastCoinPrice = Trade.getPrice().doubleValue();
				wantedBuyPrice = (actualSellPrice > NumberUtils.DOUBLE_ZERO) ? (NumberUtils.DOUBLE_ONE - bidDiscount) * actualSellPrice: (NumberUtils.DOUBLE_ONE - bidDiscount) * lastCoinPrice;
				final OpenTransactionImpl buyRequest = new OpenTransactionImpl(coinType.base, TransactionTypeEnum.BUY, wantedBuyPrice, 100);
				LOG.info("Strategy Posting Buy: {} ", buyRequest);
				exchangeHandler.postTransactionRequest(buyRequest);

			} else {
				LOG.error("Illegal State at analyzeData method ");
				throw new UnsupportedOperationException("Illegal State at analyzeData method");
			}

		}
		return continueAnalysis;

	}

	private void handleOpenTransactions(List<IOpenTransaction> openTransactions) {
		 
		IOpenTransaction openTransaction = openTransactions.get(NumberUtils.INTEGER_ZERO);
		if (openTransaction!=null) {
			switch (openTransaction.getTransactionType()) {
				case BUY:
					break;
				case SELL:
					break;
				default:
					throw new NotImplementedException(String.format("Transaction Type Not Implemented For : %s",
							openTransaction.getTransactionType()));
			}
		}
		throw new NotImplementedException(String.format("Transaction Type Not Implemented For : %s",
				openTransaction.getTransactionType()));
	}

	@Override
	public void init(IExchangeHandler exchangeHandler) {
		this.exchangeHandler = exchangeHandler;
	}

	public void setBidDiscount(double bidDiscount) {
		this.bidDiscount = bidDiscount;
	}

	public void setSellProfit(double sellProfit) {
		this.sellProfit = sellProfit;
	}

}
