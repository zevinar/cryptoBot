package com.zevinar.crypto.exchange.realexchange;

import com.zevinar.crypto.exchange.AbstractMarketDataExchangeHandler;
import com.zevinar.crypto.utils.enums.ExchangeEnum;
import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.bittrex.dto.marketdata.BittrexSymbol;
import org.knowm.xchange.bittrex.service.BittrexMarketDataServiceRaw;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.Trade;
import org.knowm.xchange.hitbtc.v2.dto.HitbtcSymbol;
import org.knowm.xchange.hitbtc.v2.service.HitbtcMarketDataServiceRaw;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static com.zevinar.crypto.utils.FunctionalCodeUtils.methodRunner;

public class BittrexExchangeHandler extends AbstractMarketDataExchangeHandler {
	private static final Logger LOG = LoggerFactory.getLogger(BittrexExchangeHandler.class);

	public BittrexExchangeHandler() {

		INSTANCE = ExchangeFactory.INSTANCE.createExchange(ExchangeEnum.BITTREX.getExchangeName());
		init();
	}


	// WEX does not get dates, only limit
	@Override
	public List<Trade> getTrades(CurrencyPair coinType, Long fromId, Long fromTime, Long toTime, Long limit) {
		return methodRunner(() -> marketDataService.getTrades(coinType, null).getTrades());
	}

	@Override
	protected List<CurrencyPair> getSymbols() {
		List<CurrencyPair> cp=new ArrayList<CurrencyPair>();
		java.util.List<BittrexSymbol> syms=
				methodRunner(() -> ((BittrexMarketDataServiceRaw) marketDataService).getBittrexSymbols());
		for (BittrexSymbol symbol : syms) {
			CurrencyPair pair = adaptSymbol(symbol);
			cp.add(pair);
		}
		return cp;
	}

	protected static CurrencyPair adaptSymbol(BittrexSymbol bittrexSymbol) {

		return new CurrencyPair(bittrexSymbol.getMarketCurrency(),bittrexSymbol.getBaseCurrency() );
	}

}
