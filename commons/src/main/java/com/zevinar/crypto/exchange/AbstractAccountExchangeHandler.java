package com.zevinar.crypto.exchange;

import static com.zevinar.crypto.utils.FunctionalCodeUtils.methodRunner;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.dto.account.AccountInfo;
import org.knowm.xchange.dto.account.FundingRecord;
import org.knowm.xchange.dto.account.Wallet;
import org.knowm.xchange.service.account.AccountService;
import org.knowm.xchange.service.trade.params.TradeHistoryParams;
import org.knowm.xchange.service.trade.params.WithdrawFundsParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zevinar.crypto.exchange.interfcaes.IAccountExchangeHandler;

//can get  market data and account info
public abstract class AbstractAccountExchangeHandler extends AbstractMarketDataExchangeHandler implements IAccountExchangeHandler{
	private static final Logger LOG = LoggerFactory.getLogger(AbstractAccountExchangeHandler.class);


	//TODO crypto init as spring DI
	protected static AccountService accountService=null;

	//TODO crypto init as spring DI

	public  void init() {

		accountService=getExchange().getAccountService();
	}

	@Override
	public AccountInfo getAccountInfo() throws IOException {
		return accountService.getAccountInfo();
	}

	@Override
	public String withdrawFunds(Currency currency, BigDecimal amount, String address) throws IOException {
		return  accountService.withdrawFunds( currency,  amount,  address);
	}

	@Override
	public String withdrawFunds(WithdrawFundsParams params) throws IOException {
		return accountService.withdrawFunds(params);
	}

	@Override
	public String requestDepositAddress(Currency currency, String... args) throws IOException {
		return accountService.requestDepositAddress(currency,  args);
	}

	@Override
	public TradeHistoryParams createFundingHistoryParams() {
		return accountService.createFundingHistoryParams();
	}

	@Override
	public List<FundingRecord> getFundingHistory(TradeHistoryParams params) throws IOException {
		return accountService.getFundingHistory(params);
	}

	//custom functions
	@Override
	public Double getTradingFee() {
		BigDecimal ret=null;
		try {
			 ret= accountService.getAccountInfo().getTradingFee();
		} catch (IOException e) {
			e.printStackTrace();//todo, cache result to decrease requests
		}
		if (ret==null){
			return super.getTradingFee();
		}
		return ret.doubleValue();
	}

	@Override
	public Double getCoinBalance(Currency coinType) {
			Wallet ret = methodRunner(() -> accountService.getAccountInfo().getWallet());//TODO crypto assumes one wallet
			return ret.getBalance(coinType).getAvailable().doubleValue();

	}
}
