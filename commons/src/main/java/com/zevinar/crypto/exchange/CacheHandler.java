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
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

import org.apache.commons.collections4.map.HashedMap;
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
	Map<String, List<Trade>> memCache = new HashedMap<>();
	Gson gson = new GsonBuilder().create();
	private CacheHandler() {
		File dbDataPath = new File(".." + File.separator + "dbData");
		if (!dbDataPath.exists()) {
			dbDataPath.mkdir();
		}
	}


	public Optional<List<Trade>> getRecords(String cacheKey) {
		Optional<List<Trade>> ret;
		if (memCache.containsKey(cacheKey)) {
			return Optional.of(memCache.get(cacheKey));
		}
		File cachedFile = new File(cacheKey);
		if (cachedFile.exists()) {
			String tradeListString = methodRunner(() -> readFileToString(cachedFile, StandardCharsets.UTF_8.name()));
			Type listType = new TypeToken<ArrayList<Trade>>() {
			}.getType();
			List<Trade> tradeListObject = gson.fromJson(tradeListString, listType);
			memCache.put(cacheKey, tradeListObject);
			ret = Optional.of(tradeListObject);
		} else {
			ret = Optional.empty();
		}
		return ret;
	}

	public Supplier<List<Trade>> fillCache(String cacheKey, List<Trade> recordsFromExchange) {
		String tradesData = gson.toJson(recordsFromExchange);
		memCache.put(cacheKey, recordsFromExchange);
		methodRunner((RunnableThrows<IOException>) () -> writeStringToFile(new File(cacheKey), tradesData,
				StandardCharsets.UTF_8.name(), false));
		return () -> recordsFromExchange;
	}

	public String buildCacheKey(long fromTime, ExchangeEnum exchangeType, CurrencyPair currencyPair) {
		String keyTemplate = ".." + File.separator + "dbData" + File.separator + "%s#%s#%s.db";

		String dateKey = DateUtils.buildDateHourlyKey(fromTime);
		String currencyTemplate = "%s%s";
		return String.format(keyTemplate, exchangeType.name(),
				String.format(currencyTemplate, currencyPair.base.getSymbol(), currencyPair.counter.getSymbol()),
				dateKey);
	}
}
