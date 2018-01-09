package com.zevinar.crypto.exchange;

import com.zevinar.crypto.exchange.dto.IOpenTransaction;
import com.zevinar.crypto.exchange.interfcaes.ITradeExchangeHandler;
import org.knowm.xchange.dto.Order;
import org.knowm.xchange.dto.trade.LimitOrder;
import org.knowm.xchange.dto.trade.MarketOrder;
import org.knowm.xchange.dto.trade.OpenOrders;
import org.knowm.xchange.dto.trade.UserTrades;
import org.knowm.xchange.service.trade.TradeService;
import org.knowm.xchange.service.trade.params.CancelOrderParams;
import org.knowm.xchange.service.trade.params.TradeHistoryParams;
import org.knowm.xchange.service.trade.params.orders.OpenOrdersParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Collection;

//can get  market data , account info and perform trades
public abstract class AbstractTradeExchangeHandler extends AbstractAccountExchangeHandler implements ITradeExchangeHandler {
	private static final Logger LOG = LoggerFactory.getLogger(AbstractTradeExchangeHandler.class);


	//TODO crypto init as spring DI
	protected static TradeService tradeService=null;


	public   void init() {

		tradeService=getExchange().getTradeService();
	}

	@Override
	public OpenOrders getOpenOrders() throws IOException {
		return tradeService.getOpenOrders();
	}

	@Override
	public OpenOrders getOpenOrders(OpenOrdersParams params) throws IOException {
		return  tradeService.getOpenOrders(params);
	}

	@Override
	public String placeMarketOrder(MarketOrder marketOrder) throws IOException {
		return  tradeService.placeMarketOrder(marketOrder);
	}

	@Override
	public String placeLimitOrder(LimitOrder limitOrder) throws IOException {
		return  tradeService.placeLimitOrder(limitOrder);
	}

	@Override
	public boolean cancelOrder(String orderId) throws IOException {
		return  tradeService.cancelOrder(orderId);
	}

	@Override
	public boolean cancelOrder(CancelOrderParams orderParams) throws IOException {
		return  tradeService.cancelOrder(orderParams);
	}

	@Override
	public UserTrades getTradeHistory(TradeHistoryParams params) throws IOException {
		return  tradeService.getTradeHistory(params);
	}

	@Override
	public TradeHistoryParams createTradeHistoryParams() {
		return  tradeService.createTradeHistoryParams();
	}

	@Override
	public OpenOrdersParams createOpenOrdersParams() {
		return  tradeService.createOpenOrdersParams();
	}

	@Override
	public void verifyOrder(LimitOrder limitOrder) {
		tradeService.verifyOrder(limitOrder);
	}

	@Override
	public void verifyOrder(MarketOrder marketOrder) {
		tradeService.verifyOrder(marketOrder);
	}

	@Override
	public Collection<Order> getOrder(String... orderIds) throws IOException {
		return  tradeService.getOrder(orderIds);
	}

	//custom

	public void postTransactionRequest(IOpenTransaction request) {

	}
}
