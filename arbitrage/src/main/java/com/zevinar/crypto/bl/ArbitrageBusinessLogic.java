package com.zevinar.crypto.bl;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import  com.zevinar.crypto.utils.datastruct.Wrapper;
import com.zevinar.crypto.bl.impl.DealImpl;
import com.zevinar.crypto.bl.interfcaes.IDeal;
import com.zevinar.crypto.exchange.interfcaes.ICoinQuote;
import com.zevinar.crypto.exchange.interfcaes.IExchangeHandlerForArbitrage;
import com.zevinar.crypto.utils.enums.CoinTypeEnum;
 
public final class ArbitrageBusinessLogic {

	private ArbitrageBusinessLogic(){
		throw new UnsupportedOperationException("Utility class do not instantiate");
	}
	/**
	 * Best Deal 
	 * @param firstExchange
	 * @param secondExchange
	 * @return
	 */
	public static IDeal calculateBestArbitrage(IExchangeHandlerForArbitrage firstExchange, IExchangeHandlerForArbitrage secondExchange) {
		List<ICoinQuote> quotesFirst = firstExchange.getAllCoinsQuotes();
		List<ICoinQuote> quotesSecond = secondExchange.getAllCoinsQuotes();
		
		Map<CoinTypeEnum, ICoinQuote> firstQuoteMap = quotesFirst.stream()
				.collect(Collectors.toMap(ICoinQuote::getCoinType, Function.identity()));
		Map<CoinTypeEnum, ICoinQuote> secondQuoteMap = quotesSecond.stream()
				.collect(Collectors.toMap(ICoinQuote::getCoinType, Function.identity()));
		
		Comparator<ICoinQuote> coinTypeComparator = (q1, q2) -> q1.getCoinType().compareTo(q2.getCoinType());
		
		List<ICoinQuote> filteredFirst = quotesFirst.stream()
				.filter(quote -> secondQuoteMap.containsKey(quote.getCoinType())).sorted(coinTypeComparator)
				.collect(Collectors.toList());
		List<ICoinQuote> filteredSecond = quotesSecond.stream()
				.filter(quote -> firstQuoteMap.containsKey(quote.getCoinType())).sorted(coinTypeComparator)
				.collect(Collectors.toList());
		
		Wrapper<IDeal> bestWrapper = new Wrapper<>(IDeal.getDummyDeal());
		for (int i = 0; i < filteredFirst.size(); i++) {
			for (int j = i + 1; j < filteredFirst.size(); j++) {
				updateBestDeal(filteredFirst.get(i), filteredSecond.get(i), filteredSecond.get(j), filteredFirst.get(j),
						firstExchange, secondExchange, bestWrapper);
				updateBestDeal(filteredFirst.get(j), filteredSecond.get(j), filteredSecond.get(i), filteredFirst.get(i),
						firstExchange, secondExchange, bestWrapper);
				updateBestDeal(filteredSecond.get(j), filteredFirst.get(j), filteredFirst.get(i), filteredSecond.get(i),
						firstExchange, secondExchange, bestWrapper);
				updateBestDeal(filteredSecond.get(i), filteredFirst.get(i), filteredFirst.get(j), filteredSecond.get(j),
						firstExchange, secondExchange, bestWrapper);

			}

		}

		return bestWrapper.getInnerElement();
	}

	private static void updateBestDeal(ICoinQuote buyFirstExchange, ICoinQuote sellSecondExchange,
			ICoinQuote buySecondExchange, ICoinQuote sellFirstExchange, IExchangeHandlerForArbitrage firstExchange, IExchangeHandlerForArbitrage secondExchange, Wrapper<IDeal> bestWrapper) {
		IDeal current = calculateDeal(buyFirstExchange, sellSecondExchange, buySecondExchange, sellFirstExchange, firstExchange, secondExchange);
		if (current.compareTo(bestWrapper.getInnerElement()) > 0) {
			bestWrapper.setInnerElement(current);
		}
	}
	protected static IDeal calculateDeal(ICoinQuote buyFirstExchange, ICoinQuote sellSecondExchange,
			ICoinQuote buySecondExchange, ICoinQuote sellFirstExchange, IExchangeHandlerForArbitrage firstExchange, IExchangeHandlerForArbitrage secondExchange) {
		final BigDecimal percentageAfterFirstExchangeTransactionFee = BigDecimal.valueOf(1 - firstExchange.getTransactionFee());
		final BigDecimal percentageAfterSecondExchangeTransactionFee = BigDecimal.valueOf(1 - secondExchange.getTransactionFee());
		
		BigDecimal initialCashSpent = new BigDecimal(buyFirstExchange.getUSDollarBuy()) ;
		BigDecimal numCoinBoughtFirst =  initialCashSpent.multiply(percentageAfterFirstExchangeTransactionFee).divide(initialCashSpent, 0);
		BigDecimal numCoinSentSecond = numCoinBoughtFirst.multiply(BigDecimal.valueOf(1 - firstExchange.getMoveCoinFee()));
		BigDecimal priceCoinSoldSecond = numCoinSentSecond.multiply(BigDecimal.valueOf(sellSecondExchange.getUSDollarSell())).multiply(percentageAfterSecondExchangeTransactionFee);
		BigDecimal numOfCoinsConvertedSecond = priceCoinSoldSecond.multiply(percentageAfterSecondExchangeTransactionFee).divide(BigDecimal.valueOf(buySecondExchange.getUSDollarBuy()), 0);
		BigDecimal numOfCoinsSentFirst = numOfCoinsConvertedSecond.multiply(BigDecimal.valueOf(1 - secondExchange.getMoveCoinFee()));
		BigDecimal priceCoinSoldFirst = numOfCoinsSentFirst.multiply(BigDecimal.valueOf(sellFirstExchange.getUSDollarSell())).multiply(percentageAfterFirstExchangeTransactionFee);
		
		double profit = priceCoinSoldFirst.subtract(initialCashSpent).doubleValue();
		final IDeal deal = new DealImpl(buyFirstExchange, sellSecondExchange, buySecondExchange, sellFirstExchange, firstExchange, secondExchange, profit);
		return deal;
	}
}
