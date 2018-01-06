package com.zevinar.crypto.exchange.interfcaes;

import com.zevinar.crypto.exchange.dto.IOpenTransaction;
import org.knowm.xchange.dto.Order;
import org.knowm.xchange.dto.trade.LimitOrder;
import org.knowm.xchange.dto.trade.MarketOrder;
import org.knowm.xchange.dto.trade.OpenOrders;
import org.knowm.xchange.dto.trade.UserTrades;
import org.knowm.xchange.service.trade.params.CancelOrderParams;
import org.knowm.xchange.service.trade.params.TradeHistoryParams;
import org.knowm.xchange.service.trade.params.orders.OpenOrdersParams;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

public interface ITradeExchangeHandler extends IAccountExchangeHandler {


	OpenOrders getOpenOrders() throws IOException;

	OpenOrders getOpenOrders(
			OpenOrdersParams params) throws IOException;

	String placeMarketOrder(
			MarketOrder marketOrder) throws IOException;

	String placeLimitOrder(
			LimitOrder limitOrder) throws IOException;

	boolean cancelOrder(String orderId) throws IOException;

	boolean cancelOrder(CancelOrderParams orderParams) throws IOException;

	UserTrades getTradeHistory(TradeHistoryParams params) throws IOException;

	TradeHistoryParams createTradeHistoryParams();

	OpenOrdersParams createOpenOrdersParams();

	void verifyOrder(LimitOrder limitOrder);

	void verifyOrder(MarketOrder marketOrder);

	Collection<Order> getOrder(
			String... orderIds) throws IOException;


	//custom
	 void postTransactionRequest(IOpenTransaction request) ;

	List<IOpenTransaction> getOpenTransactions() ;//TODO crypto remove, use getopenorders



}
