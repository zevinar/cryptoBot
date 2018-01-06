package com.zevinar.crypto.exchange.interfcaes;

import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.dto.account.AccountInfo;
import org.knowm.xchange.dto.account.FundingRecord;
import org.knowm.xchange.service.trade.params.TradeHistoryParams;
import org.knowm.xchange.service.trade.params.WithdrawFundsParams;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

public interface IAccountExchangeHandler  extends IMarketDataExchangeHandler {


	AccountInfo getAccountInfo() throws IOException;

	String withdrawFunds(Currency currency, BigDecimal amount,
						 String address) throws IOException;

	String withdrawFunds(WithdrawFundsParams params) throws IOException;

	String requestDepositAddress(Currency currency,
								 String... args) throws IOException;

	TradeHistoryParams createFundingHistoryParams();

	List<FundingRecord> getFundingHistory(
			TradeHistoryParams params) throws IOException;

	//custom
	 Double getTradingFee() ;

	Double getCoinBalance(Currency coinType) throws IOException;

}
