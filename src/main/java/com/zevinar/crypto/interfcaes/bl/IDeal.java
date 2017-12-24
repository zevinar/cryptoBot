package com.zevinar.crypto.interfcaes.bl;

import com.zevinar.crypto.interfcaes.exchange.ICoinQuote;
import com.zevinar.crypto.interfcaes.exchange.IExchangeHandler;

public interface IDeal extends Comparable<IDeal>{
	Double getExpectedProfit();
	default int compareTo(IDeal second) {
		return getExpectedProfit().compareTo(second.getExpectedProfit());
	}
	ICoinQuote getCoinBoughtAtFirstExchange();
	ICoinQuote getCoinSoldAtFirstExchange();
	ICoinQuote getCoinBoughtAtSecondExchange();
	ICoinQuote getCoinSoldAtSecondExchange();
	IExchangeHandler getFirstExchange();
	IExchangeHandler getSecondExchange();
	
	static IDeal getDummyDeal() {
		return new IDeal() {
			
			@Override
			public Double getExpectedProfit() {
				return -1D;
			}

			@Override
			public ICoinQuote getCoinBoughtAtFirstExchange() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public ICoinQuote getCoinSoldAtFirstExchange() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public ICoinQuote getCoinBoughtAtSecondExchange() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public ICoinQuote getCoinSoldAtSecondExchange() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public IExchangeHandler getFirstExchange() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public IExchangeHandler getSecondExchange() {
				// TODO Auto-generated method stub
				return null;
			}
		};
	}
}
