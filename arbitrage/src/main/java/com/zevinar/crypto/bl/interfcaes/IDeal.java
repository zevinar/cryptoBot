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
				return null;
			}

			@Override
			public ICoinQuote getCoinSoldAtFirstExchange() {
				return null;
			}

			@Override
			public ICoinQuote getCoinBoughtAtSecondExchange() {
				return null;
			}

			@Override
			public ICoinQuote getCoinSoldAtSecondExchange() {
				return null;
			}

			@Override
			public IExchangeHandlerForArbitrage getFirstExchange() {
				return null;
			}

			@Override
			public IExchangeHandlerForArbitrage getSecondExchange() {
				return null;
			}
		};
	}
}
