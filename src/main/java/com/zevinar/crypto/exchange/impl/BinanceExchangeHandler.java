package com.zevinar.crypto.exchange.impl;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.zevinar.crypto.exchange.interfcaes.ICoinQuote;
import com.zevinar.crypto.exchange.interfcaes.ICoinTransaction;
import com.zevinar.crypto.exchange.interfcaes.IExchangeHandler;
import com.zevinar.crypto.http.BinanceResponseElement;
import com.zevinar.crypto.utils.HttpClient;
import com.zevinar.crypto.utils.HttpClient.HttpResponse;
import com.zevinar.crypto.utils.enums.CoinTypeEnum;
import com.zevinar.crypto.utils.enums.ExchangeDetailsEnum;

public class BinanceExchangeHandler implements IExchangeHandler {
	private HttpClient client = HttpClient.CLIENT;
	private Gson gson = new GsonBuilder().setPrettyPrinting().create();

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
	public List<ICoinTransaction> getSingleCoinTransactions(CoinTypeEnum coinType, long fromTime) {
		 
		// TODO mshitrit take symbol from coinType
		String symbol = "LTCUSDT";
		
		String urlTemplate = "https://api.binance.com/api/v1/aggTrades?symbol=%s&startTime=%s&endTime=%s";
		String queryUrl = String.format(urlTemplate, symbol, fromTime, System.currentTimeMillis());
		HttpResponse doGet = client.doGet(queryUrl);
		Type listType = new TypeToken<ArrayList<BinanceResponseElement>>(){}.getType();
		List<ICoinTransaction> coinTransactionList = gson.fromJson(doGet.getBody(), listType);
		coinTransactionList.forEach(element -> element.setCoinType(coinType));
		
		return coinTransactionList;

	}

}
