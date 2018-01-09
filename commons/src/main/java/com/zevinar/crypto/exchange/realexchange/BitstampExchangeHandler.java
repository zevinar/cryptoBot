package com.zevinar.crypto.exchange.realexchange;

import com.zevinar.crypto.exchange.AbstractMarketDataExchangeHandler;
import com.zevinar.crypto.utils.enums.ExchangeEnum;
import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.Trade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static com.zevinar.crypto.utils.FunctionalCodeUtils.methodRunner;

public class BitstampExchangeHandler extends AbstractMarketDataExchangeHandler {
	private static final Logger LOG = LoggerFactory.getLogger(BitstampExchangeHandler.class);



	public BitstampExchangeHandler()
	{
		INSTANCE= ExchangeFactory.INSTANCE.createExchange(ExchangeEnum.BITSTAMP.getExchangeName());
		init();
	}

	//Cex has only from tradeid or max
	@Override
	public  List<Trade> getTrades(CurrencyPair coinType, Long fromId, Long fromTime, Long toTime, Long limit)  {
		if (fromTime==null)
			return methodRunner(() -> marketDataService.getTrades(coinType).getTrades());
		else return methodRunner(() -> marketDataService.getTrades(coinType,fromTime).getTrades());
	}



}
