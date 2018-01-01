package vn.quannk.bittextrade;

import com.github.lyhnx.bittrexapiwrapper.api.AccountApi;
import com.github.lyhnx.bittrexapiwrapper.api.ApiKey;
import com.github.lyhnx.bittrexapiwrapper.api.PublicApi;
import com.github.lyhnx.bittrexapiwrapper.api.models.Ticker;
import com.github.lyhnx.bittrexapiwrapper.market.BittrexMarket;
import com.github.lyhnx.bittrexapiwrapper.market.Currency;
import com.github.lyhnx.bittrexapiwrapper.market.Market;

public class Utils {
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
        PublicApi api = new PublicApi();
        // TODO check validation of market1, market2, coin
        Ticker t12 = api.getTicker(getMarket(market1, market2));
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