package com.zevinar.crypto.bl.interfcaes;

import com.zevinar.crypto.exchange.interfcaes.ICoinQuote;
import com.zevinar.crypto.exchange.interfcaes.IExchangeHandlerForArbitrage;

public interface IDeal extends Comparable<IDeal>{
	Double getExpectedProfit();
	default int compareTo(IDeal second) {
		return getExpectedProfit().compareTo(second.getExpectedProfit());
	}
	ICoinQuote getCoinBoughtAtFirstExchange();
	ICoinQuote getCoinSoldAtFirstExchange();
	ICoinQuote getCoinBoughtAtSecondExchange();
	ICoinQuote getCoinSoldAtSecondExchange();
	IExchangeHandlerForArbitrage getFirstExchange();
	IExchangeHandlerForArbitrage getSecondExchange();
	
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
			public IExchangeHandlerForArbitrage getFirstExchange() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public IExchangeHandlerForArbitrage getSecondExchange() {
				// TODO Auto-generated method stub
				return null;
			}
		};
	}
}
