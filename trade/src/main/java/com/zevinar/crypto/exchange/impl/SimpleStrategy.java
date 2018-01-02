package com.zevinar.crypto.exchange.impl;

import static org.apache.commons.collections4.CollectionUtils.isEmpty;

import java.util.List;

import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zevinar.crypto.exchange.interfcaes.ICoinTransaction;
import com.zevinar.crypto.exchange.interfcaes.IExchangeHandler;
import com.zevinar.crypto.exchange.interfcaes.IOpenTransaction;
import com.zevinar.crypto.exchange.interfcaes.IStrategy;
import com.zevinar.crypto.utils.Constants;
import com.zevinar.crypto.utils.enums.CoinTypeEnum;

public class SimpleStrategy implements IStrategy {
	private static final Logger LOG = LoggerFactory.getLogger(SimpleStrategy.class);
	private IExchangeHandler exchangeHandler;
	private static final double BID_DISCOUNT = 0.2;
	private static final double SELL_PROFIT = 0.2;
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
		double wantedBuyPrice = 0, actualBuyPrice = 0, wantedSellPrice = 0, actualSellPrice = 0;
		boolean continueAnalysis = true;
		if (!isEmpty(data)) {
			CoinTypeEnum coinType = data.get(NumberUtils.INTEGER_ZERO).getTransactionCoinType();
			List<IOpenTransaction> openTransactions = exchangeHandler.getOpenTransactions();
			boolean hasOpenTransactions = !isEmpty(openTransactions);
			boolean hasCoinsForTrade = exchangeHandler.getCoinBalance(coinType) >= coinType.getMinCoinForTrade();
			boolean hasCashForTrade = exchangeHandler.getCurrentCashUSD() >= Constants.MIN_CASH_FOR_TRADE_USD;
			boolean isBroke = !hasCoinsForTrade && !hasOpenTransactions && !hasCashForTrade;
			if (isBroke) {
				LOG.debug("Strategy is Bankrupt");
				return false;

			}
			if (hasOpenTransactions) {
				IOpenTransaction openTransaction = openTransactions.stream().findFirst().get();
				switch (openTransaction.getTransactionType()) {
				case BUY:
					break;
				case SELL:
					break;
				default:
					throw new NotImplementedException(String.format("Transaction Type Not Implemented For : %s",
							openTransaction.getTransactionType()));
				}
			} else if (hasCoinsForTrade) {
				actualBuyPrice = wantedBuyPrice;
				LOG.info("Bought: {} At Price: {}", coinType.getCoinName(),
						actualBuyPrice);
				wantedSellPrice = (NumberUtils.DOUBLE_ONE - SELL_PROFIT) * actualBuyPrice;
				LOG.info("Posting Sell: {} At Price: {}", coinType.getCoinName(),
						wantedSellPrice);
				exchangeHandler.postSell(coinType, wantedSellPrice);

			} else if (hasCashForTrade) {
				actualSellPrice = wantedSellPrice;
				if(  actualSellPrice > 0 ) {
					LOG.info("Sold: {} At Price: {}", coinType.getCoinName(),
							actualSellPrice);
				}
				final ICoinTransaction iCoinTransaction = data.get(NumberUtils.INTEGER_ZERO);
				double lastCoinPrice = iCoinTransaction.getTransactionPrice();
				wantedBuyPrice = (actualSellPrice > NumberUtils.DOUBLE_ZERO) ? (NumberUtils.DOUBLE_ONE - BID_DISCOUNT) * actualSellPrice: (NumberUtils.DOUBLE_ONE - BID_DISCOUNT) * lastCoinPrice;
				LOG.info("Posting Buy: {} At Price: {}", coinType.getCoinName(),
						wantedBuyPrice);
				exchangeHandler.postBuy(iCoinTransaction.getTransactionCoinType(), wantedBuyPrice);

			} else {
				LOG.error("Illegal State at analyzeData method ");
				throw new UnsupportedOperationException("Illegal State at analyzeData method");
			}

		}
		return continueAnalysis;

	}

	@Override
	public void init(IExchangeHandler exchangeHandler) {
		this.exchangeHandler = exchangeHandler;
	}

}
