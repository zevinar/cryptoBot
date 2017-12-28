package com.zevinar.crypto.bl;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.zevinar.crypto.bl.interfcaes.IDeal;
import com.zevinar.crypto.exchange.interfcaes.ICoinQuote;
import com.zevinar.crypto.exchange.interfcaes.IExchangeHandlerForArbitrage;
import com.zevinar.crypto.utils.enums.CoinTypeEnum;
import com.zevinar.crypto.utils.enums.ExchangeDetailsEnum;

public class CryptoBusinessLogicTest {

	@Test
	public void testCalculateDealNoCommissions() {
		ICoinQuote buyFirstExchange = buildMockQuote(250, CoinTypeEnum.LTC);
		ICoinQuote sellSecondExchange = buildMockQuote(300, CoinTypeEnum.LTC);
		ICoinQuote buySecondExchange = buildMockQuote(10000, CoinTypeEnum.BTC);
		ICoinQuote sellFirstExchange = buildMockQuote(9800, CoinTypeEnum.BTC);
		IExchangeHandlerForArbitrage handler = buildMockHandler(0, 0);
		IDeal deal = ArbitrageBusinessLogic.calculateDeal(buyFirstExchange, sellSecondExchange, buySecondExchange,
				sellFirstExchange, handler, handler);
		Assert.assertTrue(deal.getExpectedProfit() == 44); // (300/10000)*9800 -
															// 250
	}

	@Test
	public void testCalculateDealWithCommissions() {
		ICoinQuote buyFirstExchange = buildMockQuote(250, 249, CoinTypeEnum.LTC);
		ICoinQuote sellSecondExchange = buildMockQuote(301, 300, CoinTypeEnum.LTC);
		ICoinQuote buySecondExchange = buildMockQuote(10000, 9995, CoinTypeEnum.BTC);
		ICoinQuote sellFirstExchange = buildMockQuote(9850, 9800, CoinTypeEnum.BTC);

		IDeal deal = ArbitrageBusinessLogic.calculateDeal(buyFirstExchange, sellSecondExchange, buySecondExchange,
				sellFirstExchange, buildMockHandler(0.02, 0.01), buildMockHandler(0.04, 0.02));
		double rounded = ((int)(deal.getExpectedProfit() * 100))/100.0;
		assertTrue(rounded == 2.46); // (((((250*0.98*0.99)/250)*300*0.96)/10000)*0.96*0.98)*9800*0.98
																		// - 250
																		// =
																		// 2.466185388032
	}

	@Test
	public void testCalculateBestArbitrage() {
		ICoinQuote ltcFirstExchange = buildMockQuote(350, 346, CoinTypeEnum.LTC);
		ICoinQuote btcFirstExchange = buildMockQuote(17000, 16900, CoinTypeEnum.BTC);
		ICoinQuote ethFirstExchange = buildMockQuote(850, 833, CoinTypeEnum.ETH);
		IExchangeHandlerForArbitrage binanceExchange = buildMockHandler(0.02, 0.01, ExchangeDetailsEnum.BNC,
				Arrays.asList(ltcFirstExchange, btcFirstExchange, ethFirstExchange));
		
		
		ICoinQuote ltcSecondExchange = buildMockQuote(270, 264, CoinTypeEnum.LTC);
		ICoinQuote btcSecondExchange = buildMockQuote(15000, 14800, CoinTypeEnum.BTC);
		ICoinQuote ethSecondExchange = buildMockQuote(700, 687, CoinTypeEnum.ETH);
		ICoinQuote dashSecondExchange = buildMockQuote(900, 897, CoinTypeEnum.DSH);
		IExchangeHandlerForArbitrage wexExchange = buildMockHandler(0.02, 0.01, ExchangeDetailsEnum.WEX,
				Arrays.asList(ltcSecondExchange, btcSecondExchange, ethSecondExchange, dashSecondExchange));
		
		IDeal deal = ArbitrageBusinessLogic.calculateBestArbitrage(binanceExchange, wexExchange);
		double rounded = ((int)(deal.getExpectedProfit() * 100))/100.0;
		assertTrue(rounded == 145.44 );
		
	}

	private IExchangeHandlerForArbitrage buildMockHandler(double transactionFee, double moveCoinFee) {
		return buildMockHandler(transactionFee, moveCoinFee, null, null);
	}

	private IExchangeHandlerForArbitrage buildMockHandler(double transactionFee, double moveCoinFee, ExchangeDetailsEnum details,
			List<ICoinQuote> quotes) {
		IExchangeHandlerForArbitrage handler = Mockito.mock(IExchangeHandlerForArbitrage.class);
		Mockito.when(handler.getTransactionFee()).thenReturn(transactionFee);
		Mockito.when(handler.getMoveCoinFee()).thenReturn(moveCoinFee);
		Mockito.when(handler.getExchangeDetails()).thenReturn(details);
		Mockito.when(handler.getAllCoinsQuotes()).thenReturn(quotes);
		return handler;
	}

	private ICoinQuote buildMockQuote(double dollarQuote, CoinTypeEnum type) {
		return buildMockQuote(dollarQuote, dollarQuote, type);
	}

	private ICoinQuote buildMockQuote(double dollarQuoteBuy, double dollarQuoteSell, CoinTypeEnum type) {
		ICoinQuote quote = Mockito.mock(ICoinQuote.class);
		Mockito.when(quote.getCoinType()).thenReturn(type);
		Mockito.when(quote.getUSDollarBuy()).thenReturn(dollarQuoteBuy);
		Mockito.when(quote.getUSDollarSell()).thenReturn(dollarQuoteSell);
		return quote;
	}
}
