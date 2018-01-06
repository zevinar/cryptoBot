package com.zevinar.crypto.exchange.impl;

import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.Trade;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public enum CacheHandler {
	INSTANCE;
	public void addTransactionToCache(Trade data ){
		//TODO crypto implement
	}

	public Optional< List<Trade>> getRecords(CurrencyPair coinType, long fromTime, long toTime) {
		//TODO crypto implement
		return Optional.empty();
	}

	public Supplier< List<Trade>> fillCache( List<Trade> recordsFromExchange) {
		//TODO crypto implement
		return () -> recordsFromExchange;
	}
}
