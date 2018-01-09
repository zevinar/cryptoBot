package com.zevinar.crypto.exchange;

import com.zevinar.crypto.exchange.interfcaes.IMarketDataExchangeHandler;
import com.zevinar.crypto.exchange.realexchange.BinanceExchangeHandler;
import com.zevinar.crypto.exchange.realexchange.BittrexExchangeHandler;
import com.zevinar.crypto.exchange.realexchange.GateioExchangeHandler;
import com.zevinar.crypto.exchange.realexchange.WexExchangeHandler;
import com.zevinar.crypto.utils.DateUtils;
import com.zevinar.crypto.utils.FunctionalCodeUtils;
import org.junit.Test;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.OrderBook;
import org.knowm.xchange.dto.marketdata.Ticker;
import org.knowm.xchange.dto.marketdata.Trade;
import org.reflections.Reflections;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.knowm.xchange.currency.Currency.LTC;

public class AllExchangeHandlerTest {
	@Test
	public void testAllMarketDataExchanges() {
		Reflections reflections = new Reflections("com.zevinar.crypto.exchange.realexchange");
		Boolean failed=false;
		String faildExchange="";
		Set<Class<? extends AbstractMarketDataExchangeHandler>> allClasses =reflections.getSubTypesOf(AbstractMarketDataExchangeHandler.class);
		for (Class<? extends IMarketDataExchangeHandler> cls : allClasses) {
			try {

				if (Modifier.isAbstract( cls.getModifiers()) ||
						(cls.getCanonicalName().equals(GateioExchangeHandler.class.getCanonicalName()) ||
								cls.getCanonicalName().equals(WexExchangeHandler.class.getCanonicalName())	))
					continue;
				IMarketDataExchangeHandler exhandler=cls.newInstance();
				System.out.println("");
				System.out.println("===========================================");
				System.out.println("getExchangeName - "+exhandler.getExchangeType().getExchangeName());
				System.out.println("===========================================");
				List<CurrencyPair> syms=exhandler.getExchangeSymbols();

				CurrencyPair pair=new CurrencyPair(syms.get(1).toString());
				System.out.println("getExchangeSymbols #"+syms.size() +" - "+syms);
				System.out.println("------------------------------------------");
				System.out.println("getExchangeMetaData - "+exhandler.getExchangeMetaData());//.toJSONString()
				System.out.println("------------------------------------------");
				System.out.println("getExchangeSpecification - ");
				printFields(exhandler.getExchange().getExchangeSpecification());
				System.out.println("------------------------------------------");
				List<Trade> trades=exhandler.getTrades(pair,null,null,null,null);
				System.out.println("getTrades - "+trades);
				System.out.println("------------------------------------------");
				Ticker ticker=exhandler.getTicker(pair);
				System.out.println("getTicker - "+ticker);
				System.out.println("------------------------------------------");
				OrderBook ob=exhandler.getOrderBook(pair);
				System.out.println("getOrderBook - "+ob);
				System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
				System.out.println("");
				System.out.println("");

				if (trades==null || ticker==null||ob==null) {
					faildExchange+=exhandler.getExchangeType().getExchangeName()+" , ";
					failed = true;
				}
			} catch (InstantiationException e) {
				System.out.println("InstantiationException"+cls.getCanonicalName());
				failed=true;

			} catch (IllegalAccessException e) {
				System.out.println("IllegalAccessException"+cls.getCanonicalName());
				failed=true;
			}
		}

		if (faildExchange.length()>0)
			System.out.println("faildExchange = "+faildExchange);
		assertThat(failed, is(Boolean.FALSE));


	}

	private void printFields(Object c) {
		for (Field field : c.getClass().getDeclaredFields()) {
			field.setAccessible(true);
			String name = field.getName();
			Object value = null;
			try {
				value = field.get(c);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			if (value!=null && !value.equals("0")) {
				System.out.printf("%s: %s , ", name, value);
				System.out.println("");
			}

		}

	}

}
