package com.zevinar.crypto.exchange.impl;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.zevinar.crypto.exchange.interfcaes.ICoinQuote;
import com.zevinar.crypto.exchange.interfcaes.ICoinTransaction;
import com.zevinar.crypto.exchange.interfcaes.IExchangeInfoHandler;
import com.zevinar.crypto.http.BinanceResponseElement;
import com.zevinar.crypto.utils.HttpClient;
import com.zevinar.crypto.utils.HttpClient.HttpResponse;
import com.zevinar.crypto.utils.enums.CoinTypeEnum;
import com.zevinar.crypto.utils.enums.ExchangeDetailsEnum;

public class BinanceExchangeHandler implements IExchangeInfoHandler {
	private static final Logger LOG = LoggerFactory.getLogger(BinanceExchangeHandler.class);
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
		return getSingleCoinTransactions(coinType, fromTime, System.currentTimeMillis());

	}
	
	@Override
	public List<ICoinTransaction> getSingleCoinTransactions(CoinTypeEnum coinType, long fromTime, long toTime) {
		 
		
		String urlTemplate = "https://api.binance.com/api/v1/aggTrades?symbol=%s&startTime=%s&endTime=%s";
		String queryUrl = String.format(urlTemplate, coinType.getHttpQuerySymbol(), fromTime, toTime);
		HttpResponse doGet = client.doGet(queryUrl);
		Type listType = new TypeToken<ArrayList<BinanceResponseElement>>(){}.getType();
		List<ICoinTransaction> coinTransactionList = gson.fromJson(doGet.getBody(), listType);
		coinTransactionList.forEach(element -> element.setCoinType(coinType));
		return coinTransactionList;

	}

}
