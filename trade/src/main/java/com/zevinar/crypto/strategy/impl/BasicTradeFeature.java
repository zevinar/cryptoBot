package com.zevinar.crypto.strategy.impl;

import static org.apache.commons.collections4.CollectionUtils.isEmpty;

import java.util.List;

import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.math.NumberUtils;
import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.Trade;

import com.zevinar.crypto.exchange.dto.IOpenTransaction;
import com.zevinar.crypto.exchange.dto.OpenTransactionImpl;
import com.zevinar.crypto.interfcaes.AbstractStrategyFeature;
import com.zevinar.crypto.utils.Constants;
import com.zevinar.crypto.utils.enums.TransactionTypeEnum;

public class BasicTradeFeature extends  AbstractStrategyFeature{

	private double bidDiscount = 0.16;
	private double sellProfit = 0.2;
	double wantedBuyPrice = 0, actualBuyPrice = 0, wantedSellPrice = 0, actualSellPrice = 0;
	protected void reset(){
		wantedBuyPrice = 0; actualBuyPrice = 0; wantedSellPrice = 0; actualSellPrice = 0;
	}
	@Override
	public boolean performDecision(List<Trade> dataList) {
		boolean continueAnalysis = true;
		if (!isEmpty(dataList)) {
			final Trade lastTrade = dataList.get(NumberUtils.INTEGER_ZERO);
			CurrencyPair coinType = lastTrade.getCurrencyPair();
			List<IOpenTransaction> openTransactions = exchangeHandler.getOpenTransactions();
			// TODO   crypto   change   to   getopenorders
			boolean hasOpenTransactions = !isEmpty(openTransactions);
			boolean hasCoinsForTrade = false;

			hasCoinsForTrade = exchangeHandler.getCoinBalance(coinType.base)
					* lastTrade.getPrice().doubleValue() >= Constants.MIN_CASH_FOR_TRADE_USD;
			// TODO crypto calculate getMinCoinForTrade using Xchange api
			boolean hasCashForTrade = false;
			hasCashForTrade = exchangeHandler.getCoinBalance(Currency.USD) >= Constants.MIN_CASH_FOR_TRADE_USD;
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
				OpenTransactionImpl sellRequest = null;
				sellRequest = new OpenTransactionImpl(coinType.base, TransactionTypeEnum.SELL, wantedSellPrice,
						exchangeHandler.getCoinBalance(coinType.base));
				LOG.info("Strategy Posting Sell: {}", sellRequest);

				exchangeHandler.postTransactionRequest(sellRequest);

			} else if (hasCashForTrade) {
				actualSellPrice = wantedSellPrice;
				final Trade Trade = lastTrade;
				double lastCoinPrice = Trade.getPrice().doubleValue();
				wantedBuyPrice = (actualSellPrice > NumberUtils.DOUBLE_ZERO)
						? (NumberUtils.DOUBLE_ONE - bidDiscount) * actualSellPrice
						: (NumberUtils.DOUBLE_ONE - bidDiscount) * lastCoinPrice;
				final OpenTransactionImpl buyRequest = new OpenTransactionImpl(coinType.base, TransactionTypeEnum.BUY,
						wantedBuyPrice, 100);
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
		// TODO crypto openTransaction might be null
		switch (openTransaction.getTransactionType()) {
		case BUY:
			break;
		case SELL:
			break;
		default:
			throw new NotImplementedException(
					String.format("Transaction Type Not Implemented For : %s", openTransaction.getTransactionType()));
		}
	}
	public void setBidDiscount(double bidDiscount) {
		this.bidDiscount = bidDiscount;
	}
	public void setSellProfit(double sellProfit) {
		this.sellProfit = sellProfit;
	}
	@Override
	public String toString() {
		return "BasicTradeFeature [bidDiscount=" + bidDiscount + ", sellProfit=" + sellProfit + "]";
	}


	

}
