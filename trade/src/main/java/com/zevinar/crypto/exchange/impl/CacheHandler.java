package com.zevinar.crypto.exchange.impl;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import com.zevinar.crypto.exchange.interfcaes.ICoinTransaction;
import com.zevinar.crypto.utils.enums.CoinTypeEnum;

public enum CacheHandler {
	INSTANCE;
	public void addTransactionToCache(ICoinTransaction data ){
		//TODO mshitrit implement
	}

	public Optional<List<ICoinTransaction>> getRecords(CoinTypeEnum coinType, long fromTime, long toTime) {
		//TODO mshitrit implement
		return Optional.empty();
	}

	public Supplier<List<ICoinTransaction>> fillCache(List<ICoinTransaction> recordsFromExchange) {
		//TODO mshitrit implement
		return () -> recordsFromExchange;
	}
}
