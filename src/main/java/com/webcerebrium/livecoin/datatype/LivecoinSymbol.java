package com.webcerebrium.livecoin.datatype;

import com.google.common.base.Strings;
import com.webcerebrium.livecoin.api.LivecoinApiException;

/**
 * @author viclopata
 */

public class LivecoinSymbol {
    String symbol = "";
    public static String delimiter = "/";

    public LivecoinSymbol(String symbol)  throws LivecoinApiException {
        // sanitizing symbol, preventing from common user-input errors
        if (Strings.isNullOrEmpty(symbol)) {
            throw new LivecoinApiException("Symbol cannot be empty. Example: ETH/BTC");
        }
        if (symbol.contains(" ")) {
            throw new LivecoinApiException("Symbol cannot contain spaces. Example: ETH/BTC");
        }
//        if (!symbol.endsWith("/BTC") && !symbol.endsWith("/ETH")
//                && !symbol.endsWith("/USD")
//                && !symbol.endsWith("/EUR")
//                && !symbol.endsWith("/RUR")
//                && !symbol.endsWith("/VRC")
//                ) {
//            throw new LivecoinApiException("Market Symbol should be ending with /BTC, /ETH or /USD. Example: ETH/BTC. Given: " + symbol);
//        }
        this.symbol = symbol.toUpperCase();
    }

    public String get(){ return this.symbol; }

    public String getSymbol(){ return this.symbol; }

    public String getCoin(){
        if (symbol.equals("ETH/BTC")) return "ETH";
        String[] splitted = symbol.split(delimiter);
        return splitted[0];
    }

    public String getBaseCoin(){
        if (symbol.endsWith(delimiter + "BTC") || symbol.endsWith(delimiter + "ETH")) return symbol.substring(4);
        if (symbol.equals("ETH/BTC")) return "BTC";
        String[] splitted = symbol.split(delimiter);
        return splitted.length > 0 ? splitted[1] : "";
    }

    public String getOpposite(String coin) {
        if (symbol.startsWith(coin + delimiter)) {
            return symbol.substring((coin + delimiter).length());
        }
        if (symbol.endsWith(delimiter + coin)) {
            int index = symbol.length() - (delimiter + coin).length();
            return symbol.substring(0, index);
        }
        return "";
    }

    public String toString() { return this.get(); }

    public static LivecoinSymbol valueOf(String s) throws LivecoinApiException {
        return new LivecoinSymbol(s);
    }

    public static LivecoinSymbol BTC(String pair) throws LivecoinApiException {
        return LivecoinSymbol.valueOf(pair.toUpperCase() + LivecoinSymbol.delimiter + "BTC");
    }

    public static LivecoinSymbol ETH(String pair) throws LivecoinApiException {
        return LivecoinSymbol.valueOf(pair.toUpperCase() + LivecoinSymbol.delimiter + "ETH");
    }

    public static LivecoinSymbol ETH_BTC() throws LivecoinApiException {
        return LivecoinSymbol.valueOf("ETH/BTC");
    }

    public static LivecoinSymbol USD(String pair) throws LivecoinApiException {
        return LivecoinSymbol.valueOf(pair.toUpperCase() + LivecoinSymbol.delimiter + "USD");
    }
    public static LivecoinSymbol EUR(String pair) throws LivecoinApiException {
        return LivecoinSymbol.valueOf(pair.toUpperCase() + LivecoinSymbol.delimiter + "EUR");
    }
    public static LivecoinSymbol RUR(String pair) throws LivecoinApiException {
        return LivecoinSymbol.valueOf(pair.toUpperCase() + LivecoinSymbol.delimiter + "RUR");
    }

    public boolean contains(String coin) {
        return (symbol.endsWith(delimiter + coin.toUpperCase())) || (symbol.startsWith(coin.toUpperCase() + delimiter));
    }
}
