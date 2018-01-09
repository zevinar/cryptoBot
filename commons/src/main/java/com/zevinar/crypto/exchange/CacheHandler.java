package com.zevinar.crypto.exchange;

import static com.zevinar.crypto.utils.FunctionalCodeUtils.methodRunner;
import static org.apache.commons.io.FileUtils.readFileToString;
import static org.apache.commons.io.FileUtils.writeStringToFile;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.Trade;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.zevinar.crypto.utils.DateUtils;
import com.zevinar.crypto.utils.FunctionalCodeUtils.RunnableThrows;
import com.zevinar.crypto.utils.enums.ExchangeEnum;

public enum CacheHandler {
	INSTANCE;
	private CacheHandler(){
		File dbDataPath = new File(".." + File.separator + "dbData");
		if( !dbDataPath.exists() ){
			dbDataPath.mkdir();
		}
	}
	Gson gson = new GsonBuilder().create();
	public Optional<List<Trade>> getRecords(String cacheKey) {
		 Optional<List<Trade>> ret;
		File cachedFile = new File(cacheKey);
		if( cachedFile.exists() ){
			String tradeListString = methodRunner(() -> readFileToString(cachedFile, StandardCharsets.UTF_8.name()));
			Type listType = new TypeToken<ArrayList<Trade>>(){}.getType();
			List<Trade> tradeListObject = gson.fromJson(tradeListString, listType);
			ret = Optional.of(tradeListObject);
		}
		else{
			ret = Optional.empty();
		}
		return ret;
	}

	public Supplier<List<Trade>> fillCache(String cacheKey, List<Trade> recordsFromExchange) {
		String tradesData = gson.toJson(recordsFromExchange);
		
		methodRunner((RunnableThrows<IOException>) () ->
		writeStringToFile(new File(cacheKey), tradesData, StandardCharsets.UTF_8.name(), false));
		return () -> recordsFromExchange;
	}

	public String buildCacheKey(long fromTime, ExchangeEnum exchangeType, CurrencyPair currencyPair) {
//		String keyTemplate = ".." + File.separator + "db"+File.separator+"src"+File.separator+"main"+File.separator+"resources"+File.separator+"%s#%s#%s.db";
		
		String keyTemplate = ".." + File.separator + "dbData" + File.separator+"%s#%s#%s.db";

		String dateKey = DateUtils.buildDateHourlyKey(fromTime);
		String currencyTemplate = "%s%s";
		return String.format(keyTemplate, exchangeType.name(), String.format(currencyTemplate, currencyPair.base.getSymbol(), currencyPair.counter.getSymbol()), dateKey);
	}
}
