package vn.quannk.bittextrade;

import com.github.lyhnx.bittrexapiwrapper.api.AccountApi;
import com.github.lyhnx.bittrexapiwrapper.api.ApiKey;
import com.github.lyhnx.bittrexapiwrapper.api.PublicApi;
import com.github.lyhnx.bittrexapiwrapper.api.models.Ticker;
import com.github.lyhnx.bittrexapiwrapper.market.BittrexMarket;
import com.github.lyhnx.bittrexapiwrapper.market.Currency;
import com.github.lyhnx.bittrexapiwrapper.market.Market;

public class Utils {
    private static final double FEE = 0.0025;
    private static final PublicApi api = new PublicApi();

    public static boolean testReadInfoKey(ApiKey apiKey) {
        try {
            return new AccountApi(apiKey).getBalances() != null;
        } catch (Throwable e) {
            return false;
        }
    }

    /**
     * market = ada for example
     * the idea we try 2 cases
     * - buy btc from usdt, buy ada, then sell back to usdt
     * - buy ada from usdt, sell ada to btc, then sell btc back to usdt
     */
    public static void checkMarket(String market) {
        checkMarket("USDT", "BTC", market);
    }

    /**
     * coin must be able to trade in market1 and market2
     * market2 must be able to trade in market1
     */
    public static void checkMarket(String market1, String market2, String coin) {
        Ticker t12 = api.getTicker(getMarket(market1, market2));
        Ticker t23 = api.getTicker(getMarket(market2, coin));
        Ticker t13 = api.getTicker(getMarket(market1, coin));

        double p = check0(t12, t23, t13);
        if (p > 1) {
            System.out.println(coin + " " + p + " very good in check0");
        } else {
            // System.out.println(coin + " " + p);
        }

        p = check1(t12, t23, t13);
        if (p > 1) {
            System.out.println(coin + " " + p + " very good in check1 ");
        } else {
            // System.out.println(coin + " " + p);
        }
    }

    private static double check0(Ticker t12, Ticker t23, Ticker t13) {
        double nCoin1 = 1000;
        // buy coin2 with ticker t12.ask
        double nCoin2 = nCoin1 / t12.getAsk() * (1 - FEE);
        // buy coin3 with ticker t23.ask
        double nCoin3 = nCoin2 / t23.getAsk() * (1 - FEE);
        // sell coin3 with ticker t13.bid
        double nCoin1_ = nCoin3 * t13.getBid() * (1 - FEE);
        return nCoin1_ / nCoin1;
    }

    private static double check1(Ticker t12, Ticker t23, Ticker t13) {
        double nCoin1 = 1000;
        // buy coin3 with ticker t13.ask
        double nCoin3 = nCoin1 / t13.getAsk() * (1 - FEE);
        // sell coin 3 to get coin 2
        double nCoin2 = nCoin3 * t23.getBid() * (1 - FEE);
        // sell coin 2 to get coin 1
        double nCoin1_ = nCoin2 * t12.getBid() * (1 - FEE);
        return nCoin1_ / nCoin1;
    }

    private static Market getMarket(String base, String coin) {
        return new Market(getBittrexMarket(base), new Currency(coin));
    }

    private static BittrexMarket getBittrexMarket(String symbol) {
        switch (symbol.toUpperCase()) {
            case "BTC":
                return BittrexMarket.BITCOIN;
            case "ETH":
                return BittrexMarket.ETHEREUM;
            case "USDT":
                return BittrexMarket.USDT;
            default:
                throw new RuntimeException("Uknown market : " + symbol);
        }
    }
}