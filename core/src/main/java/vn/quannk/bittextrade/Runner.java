package vn.quannk.bittextrade;

import com.github.lyhnx.bittrexapiwrapper.api.AccountApi;
import com.github.lyhnx.bittrexapiwrapper.api.ApiKey;
import com.github.lyhnx.bittrexapiwrapper.api.MarketApi;
import com.github.lyhnx.bittrexapiwrapper.api.PublicApi;
import com.github.lyhnx.bittrexapiwrapper.api.models.Market;

import java.util.ArrayList;
import java.util.List;

public class Runner {

    // read only keys
    private final static String PUBLIC_KEY = "349092a2b990483099c9b875cf2971fd";
    private final static String SECRET_KEY = "7ac488bee07b468f8c927ca18f05df3a";

    private static final PublicApi publicApi = new PublicApi();
    private static final ApiKey apiKey = new ApiKey(PUBLIC_KEY, SECRET_KEY);
    private static final MarketApi marketApi = new MarketApi(apiKey);
    private static final AccountApi accountApi = new AccountApi(apiKey);

    public static void main(String args[]) throws InterruptedException {
        List<String> coins = new ArrayList<>();
        Market[] markets = publicApi.getMarkets();
        for (Market market : markets) {
            if (market.getBaseCurrency().equalsIgnoreCase("ETH") && !market.getMarketCurrency().equalsIgnoreCase("BTC")) {
                coins.add(market.getMarketCurrency());
            }
        }
        while (true) {
            for (String coin : coins) {
                Utils.checkMarket("BTC", "ETH", coin);
            }
            Thread.sleep(1000);
            System.out.println("Rerun");
        }
    }
}