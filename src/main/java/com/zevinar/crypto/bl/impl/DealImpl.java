package com.zevinar.crypto.bl.impl;

import com.zevinar.crypto.bl.interfcaes.IDeal;
import com.zevinar.crypto.exchange.interfcaes.ICoinQuote;
import com.zevinar.crypto.exchange.interfcaes.IExchangeInfoHandler;

public class DealImpl implements IDeal {

	private ICoinQuote buyFirstExchange;
	private ICoinQuote sellSecondExchange;
	private ICoinQuote buySecondExchange;
	private ICoinQuote sellFirstExchange;
	private IExchangeInfoHandler firstExchange;
	private IExchangeInfoHandler secondExchange;
	private double profit;

	public DealImpl(ICoinQuote buyFirstExchange, ICoinQuote sellSecondExchange, ICoinQuote buySecondExchange,
			ICoinQuote sellFirstExchange, IExchangeInfoHandler firstExchange, IExchangeInfoHandler secondExchange,
			double profit) {
		this.buyFirstExchange = buyFirstExchange;
		this.sellSecondExchange = sellSecondExchange;
		this.buySecondExchange = buySecondExchange;
		this.sellFirstExchange = sellFirstExchange;
		this.firstExchange = firstExchange;
		this.secondExchange = secondExchange;
		this.profit = profit;
	}

	@Override
	public Double getExpectedProfit() {
		return profit;
	}

	@Override
	public ICoinQuote getCoinBoughtAtFirstExchange() {
		return buyFirstExchange;
	}

	@Override
	public ICoinQuote getCoinSoldAtFirstExchange() {
		return sellFirstExchange;
	}

	@Override
	public ICoinQuote getCoinBoughtAtSecondExchange() {
		return buySecondExchange;
	}

	@Override
	public ICoinQuote getCoinSoldAtSecondExchange() {
		return sellSecondExchange;
	}

	@Override
	public IExchangeInfoHandler getFirstExchange() {
		return firstExchange;
	}

	@Override
	public IExchangeInfoHandler getSecondExchange() {
		return secondExchange;
	}

	public String toString() {

		String template = "Buy:  %s at %s for price of %s US$ \n" + "Sell: %s at %s for price of %s US$ \n \n"
				+ "Buy:  %s at %s for price of %s US$ \n" + "Sell: %s at %s for price of %s US$ \n\n"
				+ "Expected Profit After Commissions is: %s";
		return String.format(template, 
				buyFirstExchange.getCoinType().getCoinName(), firstExchange.getExchangeDetails().getExchangeName(), buyFirstExchange.getUSDollarBuy(),
				sellSecondExchange.getCoinType().getCoinName(), secondExchange.getExchangeDetails().getExchangeName(),
				sellSecondExchange.getUSDollarSell(), buySecondExchange.getCoinType().getCoinName(),
				secondExchange.getExchangeDetails().getExchangeName(), buySecondExchange.getUSDollarBuy(),
				sellFirstExchange.getCoinType().getCoinName(), firstExchange.getExchangeDetails().getExchangeName(),
				sellFirstExchange.getUSDollarSell(), profit);
	}
}
