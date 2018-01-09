package com.zevinar.crypto.exchange.realexchange;

import com.zevinar.crypto.exchange.AbstractMarketDataExchangeHandler;
import com.zevinar.crypto.utils.enums.ExchangeEnum;
import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.bittrex.dto.marketdata.BittrexSymbol;
import org.knowm.xchange.bittrex.service.BittrexMarketDataServiceRaw;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.Trade;
import org.knowm.xchange.gateio.service.GateioMarketDataServiceRaw;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.zevinar.crypto.utils.FunctionalCodeUtils.methodRunner;

public class GateioExchangeHandler extends AbstractMarketDataExchangeHandler {
	private static final Logger LOG = LoggerFactory.getLogger(GateioExchangeHandler.class);



	public GateioExchangeHandler()
	{
		INSTANCE= ExchangeFactory.INSTANCE.createExchange(ExchangeEnum.GATEIO.getExchangeName());
		init();
	}

	//Cex has only from tradeid or max
	@Override
	public  List<Trade> getTrades(CurrencyPair coinType, Long fromId, Long fromTime, Long toTime, Long limit)  {
		return methodRunner( () -> marketDataService.getTrades(coinType, null  ).getTrades());
	}



		@Override
		protected List<CurrencyPair> getSymbols() {
			return methodRunner( () ->((GateioMarketDataServiceRaw) marketDataService).getExchangeSymbols());
			//return new ArrayList<>(bter.getPairs().getPairs());
		}

}
