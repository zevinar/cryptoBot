package com.zevinar.crypto.exchange;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.Trade;

import com.zevinar.crypto.utils.DateUtils;
import com.zevinar.crypto.utils.enums.ExchangeEnum;

public enum CacheHandler {
	INSTANCE;
	public void addTransactionToCache(Trade data) {
		// TODO crypto implement
	}

	public Optional<List<Trade>> getRecords(String cacheKey) {
		// TODO crypto implement
		return Optional.empty();
	}

	public Supplier<List<Trade>> fillCache(String cacheKey, List<Trade> recordsFromExchange) {
		// TODO crypto implement
		return () -> recordsFromExchange;
	}

	public String buildCacheKey(long fromTime, ExchangeEnum exchangeType, CurrencyPair currencyPair) {
		String keyTemplate = "%s#%s#%s";
		String dateKey = DateUtils.buildDateHourlyKey(fromTime);
		return String.format(keyTemplate, exchangeType.name(), currencyPair.toString(), dateKey);
	}
}
