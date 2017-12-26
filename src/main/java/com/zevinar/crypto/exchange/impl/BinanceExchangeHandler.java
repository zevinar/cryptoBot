package com.zevinar.crypto.exchange.impl;

import java.util.List;

import com.zevinar.crypto.exchange.interfcaes.ICoinQuote;
import com.zevinar.crypto.exchange.interfcaes.IExchangeHandler;
import com.zevinar.crypto.utils.HttpClient;
import com.zevinar.crypto.utils.HttpClient.HttpResponse;
import com.zevinar.crypto.utils.enums.CoinTypeEnum;
import com.zevinar.crypto.utils.enums.ExchangeDetailsEnum;

public class BinanceExchangeHandler implements IExchangeHandler{
	private HttpClient client = HttpClient.CLIENT;

	 
	@Override
	public ExchangeDetailsEnum getExchangeDetails() {
		return ExchangeDetailsEnum.BNC;
	}

	@Override
	public List<ICoinQuote> getALlCoinsQuotes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Double getTransactionFee() {
		return 0.02;
	}

	@Override
	public Double getMoveCoinFee() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ICoinQuote> getSingleCoinQuotes(CoinTypeEnum coinType, long fromTime) {
		//LTCUSDT
		//TODO mshitrit take symbol from coinType
		String urlTemplate = "https://api.binance.com/api/v1/aggTrades?symbol=LTCUSDT&startTime=%s&endTime=%s";
		String queryUrl = String.format(urlTemplate, fromTime, System.currentTimeMillis() );
		HttpResponse doGet = client.doGet(queryUrl);
		System.out.println(doGet.getStatusCode());
		System.out.println(doGet.getBody());
		return null;
		
	}
	

}
