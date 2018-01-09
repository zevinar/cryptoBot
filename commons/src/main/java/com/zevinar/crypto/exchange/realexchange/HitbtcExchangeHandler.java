package com.zevinar.crypto.exchange.realexchange;

import com.zevinar.crypto.exchange.AbstractMarketDataExchangeHandler;
import com.zevinar.crypto.utils.enums.ExchangeEnum;
import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.Trade;
import org.knowm.xchange.hitbtc.v2.HitbtcAdapters;
import org.knowm.xchange.hitbtc.v2.dto.HitbtcSymbol;
import org.knowm.xchange.hitbtc.v2.service.HitbtcMarketDataServiceRaw;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static com.zevinar.crypto.utils.FunctionalCodeUtils.methodRunner;

public class HitbtcExchangeHandler extends AbstractMarketDataExchangeHandler {
	private static final Logger LOG = LoggerFactory.getLogger(HitbtcExchangeHandler.class);

	public HitbtcExchangeHandler() {

		INSTANCE = ExchangeFactory.INSTANCE.createExchange(ExchangeEnum.HITBTC.getExchangeName());
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
		java.util.List<org.knowm.xchange.hitbtc.v2.dto.HitbtcSymbol> syms=
				methodRunner(() -> ((HitbtcMarketDataServiceRaw) marketDataService).getHitbtcSymbols());
		for (HitbtcSymbol symbol : syms) {
			CurrencyPair pair = adaptSymbol(symbol);
			cp.add(pair);
		}
		return cp;
	}

	public static CurrencyPair adaptSymbol(HitbtcSymbol hitbtcSymbol) {

		return new CurrencyPair(hitbtcSymbol.getBaseCurrency(), hitbtcSymbol.getQuoteCurrency());
	}

}
