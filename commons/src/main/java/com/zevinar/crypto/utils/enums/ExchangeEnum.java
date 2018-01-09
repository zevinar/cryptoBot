package com.zevinar.crypto.utils.enums;

import org.knowm.xchange.anx.v2.ANXExchange;
import org.knowm.xchange.binance.BinanceExchange;
import org.knowm.xchange.bitbay.BitbayExchange;
import org.knowm.xchange.bitfinex.v1.BitfinexExchange;
import org.knowm.xchange.bitflyer.BitflyerExchange;
import org.knowm.xchange.bitmarket.BitMarketExchange;
import org.knowm.xchange.bitstamp.BitstampExchange;
import org.knowm.xchange.bittrex.BittrexExchange;
import org.knowm.xchange.bleutrade.BleutradeExchange;
import org.knowm.xchange.btcc.BTCCExchange;
import org.knowm.xchange.btctrade.BTCTradeExchange;
import org.knowm.xchange.btcturk.BTCTurkExchange;
import org.knowm.xchange.campbx.CampBXExchange;
import org.knowm.xchange.cexio.CexIOExchange;
import org.knowm.xchange.coinbase.v2.CoinbaseExchange;
import org.knowm.xchange.coinfloor.CoinfloorExchange;
import org.knowm.xchange.coinmate.CoinmateExchange;
import org.knowm.xchange.cryptofacilities.CryptoFacilitiesExchange;
import org.knowm.xchange.cryptopia.CryptopiaExchange;
import org.knowm.xchange.dsx.DSXExchange;
import org.knowm.xchange.gatecoin.GatecoinExchange;
import org.knowm.xchange.gateio.GateioExchange;
import org.knowm.xchange.gdax.GDAXExchange;
import org.knowm.xchange.hitbtc.v2.HitbtcExchange;
import org.knowm.xchange.independentreserve.IndependentReserveExchange;
import org.knowm.xchange.itbit.v1.ItBitExchange;
import org.knowm.xchange.koineks.KoineksExchange;
import org.knowm.xchange.koinim.KoinimExchange;
import org.knowm.xchange.kraken.KrakenExchange;
import org.knowm.xchange.liqui.LiquiExchange;
import org.knowm.xchange.livecoin.LivecoinExchange;
import org.knowm.xchange.mercadobitcoin.MercadoBitcoinExchange;
import org.knowm.xchange.okcoin.OkCoinExchange;
import org.knowm.xchange.paribu.ParibuExchange;
import org.knowm.xchange.poloniex.PoloniexExchange;
import org.knowm.xchange.quoine.QuoineExchange;
import org.knowm.xchange.ripple.RippleExchange;
import org.knowm.xchange.therock.TheRockExchange;
import org.knowm.xchange.vircurex.VircurexExchange;
import org.knowm.xchange.wex.v3.WexExchange;
import org.knowm.xchange.yobit.YoBitExchange;
import org.known.xchange.acx.AcxExchange;

public enum ExchangeEnum {
	WEX(WexExchange.class.getName()),
	BINANCE( BinanceExchange.class.getName()),
	CRYPTOPIA(CryptopiaExchange.class.getName()),
	GATEIO(GateioExchange.class.getName()),
	BITFINEX(BitfinexExchange.class.getName()),
	BITTREX(BittrexExchange.class.getName()),
	CEXIO(CexIOExchange.class.getName()),
	GDAX(GDAXExchange.class.getName()),
	HITBTC(HitbtcExchange.class.getName()),
	KRAKEN(KrakenExchange.class.getName()),
	LIVECOIN(LivecoinExchange.class.getName()),
	POLONIEX(PoloniexExchange.class.getName()),
	BITSTAMP(BitstampExchange.class.getName())
	;


	//YOBIT(YoBitExchange.class.getName()),
	//DSX(DSXExchange.class.getName()),
	//BTCTURK(BTCTurkExchange.class.getName()),
	//PARIBU(ParibuExchange.class.getName()),
	//LIQUI(LiquiExchange.class.getName()),
	//KOINIM(KoinimExchange.class.getName()),
	//ACX(AcxExchange.class.getName()),
	//BITFLYER(BitflyerExchange.class.getName()),
	//ANX(ANXExchange.class.getName()),
	//BITBAY(BitbayExchange.class.getName()),
	//BITMARKET(BitMarketExchange.class.getName()),
	//BITSTAMP(BitstampExchange.class.getName()),
	//BLEUTRADE(BleutradeExchange.class.getName()),
	//BTCC(BTCCExchange.class.getName()),
	//BTCTRADE(BTCTradeExchange.class.getName()),
	//CAMPBX(CampBXExchange.class.getName()),
	//COINBASE(CoinbaseExchange.class.getName()),
	//COINFLOOR(CoinfloorExchange.class.getName()),
	//COINMATE(CoinmateExchange.class.getName()),
	//CRYPTOFACILITIES(CryptoFacilitiesExchange.class.getName()),
	//GATECOIN(GatecoinExchange.class.getName()),
	//INDEPENDENTRESERVE(IndependentReserveExchange.class.getName()),
//	ITBIT(ItBitExchange.class.getName()),
//	KOINEKS(KoineksExchange.class.getName()),
	//MERCADOBITCOIN(MercadoBitcoinExchange.class.getName()),
	//OKCOIN(OkCoinExchange.class.getName()),
	//QUOINE(QuoineExchange.class.getName()),
//	RIPPLE(RippleExchange.class.getName()),
	//THEROCK(TheRockExchange.class.getName()),
	//VIRCUREX(VircurexExchange.class.getName())



	private String exchangeName;

	private ExchangeEnum(String exchangeName) {
		this.exchangeName = exchangeName;
	}

	public String getExchangeName() {
		return exchangeName;
	}


	public static ExchangeEnum fromString(String text) {
		for (ExchangeEnum b : ExchangeEnum.values()) {
			if (b.exchangeName.equalsIgnoreCase(text)) {
				return b;
			}
		}
		return null;
	}
}

