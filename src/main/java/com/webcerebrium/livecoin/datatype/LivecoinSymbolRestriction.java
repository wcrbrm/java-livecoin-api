package com.webcerebrium.livecoin.datatype;

import com.google.gson.JsonObject;
import com.webcerebrium.liqui.api.LiquiApiException;
import com.webcerebrium.livecoin.api.LivecoinApiException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Data
@Slf4j
public class LivecoinSymbolRestriction {
    LivecoinSymbol symbol;
    BigDecimal minLimitQuantity;
    BigDecimal priceScale;

    public LivecoinSymbolRestriction() {
    }

    private void jsonExpect(JsonObject obj, Set<String> fields) throws LiquiApiException {
        Set<String> missing = new HashSet<>();
        for (String f: fields) { if (!obj.has(f) || obj.get(f).isJsonNull()) missing.add(f); }
        if (missing.size() > 0) {
            log.warn("Missing fields {} in {}", missing.toString(), obj.toString());
            throw new LiquiApiException("Missing fields " + missing.toString());
        }
    }

    private BigDecimal safeDecimal(JsonObject obj, String field) {
        if (obj.has(field) && obj.get(field).isJsonPrimitive() && obj.get(field) != null) {
            try {
                return obj.get(field).getAsBigDecimal();
            } catch (java.lang.NumberFormatException nfe) {
                log.info("Number format exception in field={} value={} trade={}", field, obj.get(field), obj.toString());
            }
        }
        return null;
    }

    public LivecoinSymbolRestriction(JsonObject obj) throws LivecoinApiException {
        String currencyPair = obj.get("currencyPair").getAsString();

        this.symbol = LivecoinSymbol.valueOf(currencyPair);
        this.minLimitQuantity = safeDecimal(obj, "minLimitQuantity");
        this.priceScale = safeDecimal(obj, "priceScale");
    }

}
