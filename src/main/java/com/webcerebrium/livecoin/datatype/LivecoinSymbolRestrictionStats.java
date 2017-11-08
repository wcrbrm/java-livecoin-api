package com.webcerebrium.livecoin.datatype;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.webcerebrium.livecoin.api.LivecoinApiException;
import lombok.Data;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@Data
public class LivecoinSymbolRestrictionStats {
    List<LivecoinSymbolRestriction> restrictions = new LinkedList<>();

    public LivecoinSymbolRestrictionStats() {
    }

    public LivecoinSymbolRestrictionStats(JsonObject response) throws LivecoinApiException {
        if (!response.has("restrictions")) {
            throw new LivecoinApiException("Missing restrictions in response object while trying to get restrictions");
        }
        JsonArray pairs = response.get("restrictions").getAsJsonArray();
        restrictions.clear();
        for (JsonElement entry: pairs) {
            LivecoinSymbolRestriction symbol = new LivecoinSymbolRestriction(entry.getAsJsonObject());
            restrictions.add(symbol);
        }
    }

    public List<LivecoinSymbolRestriction> getMarketsOf(String coin) {
        List<LivecoinSymbolRestriction> result = new LinkedList<>();
        for (int i = 0; i < restrictions.size(); i++ ) {
            LivecoinSymbolRestriction tradingSymbol = restrictions.get(i);
            if (tradingSymbol.getSymbol().contains(coin)) {
                result.add(tradingSymbol);
            }
        }
        return result;
    }

    public Set<LivecoinSymbol> getSymbolsOf(String coin) throws LivecoinApiException {
        List<LivecoinSymbolRestriction> coins = getMarketsOf(coin);
        Set<LivecoinSymbol> result = new TreeSet<>();
        for (LivecoinSymbolRestriction sym: coins) {
            result.add(sym.getSymbol());
        }
        return result;
    }

    public Set<String> getCoinsOf(String coin) throws LivecoinApiException {
        List<LivecoinSymbolRestriction> coins = getMarketsOf(coin);
        Set<String> result = new TreeSet<>();
        for (LivecoinSymbolRestriction sym: coins) {
            result.add(sym.getSymbol().getOpposite(coin));
        }
        return result;
    }


}
