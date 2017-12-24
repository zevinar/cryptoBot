package com.zevinar.crypto.impl.bl;

import com.zevinar.crypto.interfcaes.bl.IDeal;
import com.zevinar.crypto.interfcaes.exchange.ICoinQuote;
import com.zevinar.crypto.interfcaes.exchange.IExchangeHandler;

public class DealImpl implements IDeal{

	private ICoinQuote buyFirstExchange;
	private ICoinQuote sellSecondExchange;
	private ICoinQuote buySecondExchange;
	private ICoinQuote sellFirstExchange;
	private IExchangeHandler firstExchange;
	private IExchangeHandler secondExchange;
	private double profit;

	public DealImpl(ICoinQuote buyFirstExchange, ICoinQuote sellSecondExchange, ICoinQuote buySecondExchange,
			ICoinQuote sellFirstExchange, IExchangeHandler firstExchange, IExchangeHandler secondExchange, double profit) {
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
	public IExchangeHandler getFirstExchange() {
		return firstExchange;
	}

	@Override
	public IExchangeHandler getSecondExchange() {
		return secondExchange;
	}

}
