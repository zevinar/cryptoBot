package com.zevinar.crypto.bl;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.zevinar.crypto.impl.bl.DealImpl;
import com.zevinar.crypto.interfcaes.bl.IDeal;
import com.zevinar.crypto.interfcaes.exchange.ICoinQuote;
import com.zevinar.crypto.interfcaes.exchange.IExchangeHandler;
import com.zevinar.crypto.utils.datastruct.Wrapper;
import com.zevinar.crypto.utils.enums.CoinTypeEnum;

public class CryptoBusinessLogic {
	/**
	 * Best Deal 
	 * @param first
	 * @param second
	 * @return
	 */
	public static IDeal calculateBestArbitrage(IExchangeHandler first, IExchangeHandler second) {
		List<ICoinQuote> quotesFirst = first.getQuotes();
		List<ICoinQuote> quotesSecond = second.getQuotes();
		Map<CoinTypeEnum, ICoinQuote> firstQuoteMap = quotesFirst.stream()
				.collect(Collectors.toMap(quote -> quote.getCoinType(), Function.identity()));
		Map<CoinTypeEnum, ICoinQuote> secondQuoteMap = quotesSecond.stream()
				.collect(Collectors.toMap(quote -> quote.getCoinType(), Function.identity()));
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
						first, second, bestWrapper);
				updateBestDeal(filteredFirst.get(j), filteredSecond.get(j), filteredSecond.get(i), filteredFirst.get(i),
						first, second, bestWrapper);
				updateBestDeal(filteredSecond.get(j), filteredFirst.get(j), filteredFirst.get(i), filteredSecond.get(i),
						first, second, bestWrapper);
				updateBestDeal(filteredSecond.get(i), filteredFirst.get(i), filteredFirst.get(j), filteredSecond.get(j),
						first, second, bestWrapper);

			}

		}

		return bestWrapper.getInnerElement();
	}

	private static void updateBestDeal(ICoinQuote buyFirstExchange, ICoinQuote sellSecondExchange,
			ICoinQuote buySecondExchange, ICoinQuote sellFirstExchange, IExchangeHandler first, IExchangeHandler second, Wrapper<IDeal> bestWrapper) {
		IDeal current = calculateDeal(buyFirstExchange, sellSecondExchange, buySecondExchange, sellFirstExchange, first, second);
		if (current.compareTo(bestWrapper.getInnerElement()) > 0) {
			bestWrapper.setInnerElement(current);
		}
	}

	private static IDeal calculateDeal(ICoinQuote buyFirstExchange, ICoinQuote sellSecondExchange,
			ICoinQuote buySecondExchange, ICoinQuote sellFirstExchange, IExchangeHandler first, IExchangeHandler second) {
		Double priceSpent = buyFirstExchange.getUSDollarQuote() /(1 - first.getTransactionFee()) / ( 1 - first.getMoveCoinFee());
		Double numberOfSecondCoinUnitsBought = sellSecondExchange.getUSDollarQuote() * ( 1 - second.getTransactionFee() ) / buySecondExchange.getUSDollarQuote();
		Double priceEarned = numberOfSecondCoinUnitsBought * (1 - second.getMoveCoinFee()) * (1 - first.getTransactionFee()) *  sellFirstExchange.getUSDollarQuote();
		return new DealImpl(buyFirstExchange, sellSecondExchange, buySecondExchange, sellFirstExchange, first, second, priceEarned - priceSpent);
	}
}
