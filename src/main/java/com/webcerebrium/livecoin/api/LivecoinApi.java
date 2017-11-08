package com.webcerebrium.livecoin.api;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableSet;
import com.google.common.escape.Escaper;
import com.google.common.net.UrlEscapers;
import com.google.gson.JsonObject;
import com.webcerebrium.livecoin.datatype.LivecoinSymbolRestrictionStats;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;

@Data
@Slf4j
public class LivecoinApi {


    /* Actual API key and Secret Key that will be used */
    public String apiKey;
    public String secretKey;

    public LivecoinConfig config = new LivecoinConfig();

    /**
     * API Base URL
     */
    public String baseUrl = "https://api.livecoin.net/";

    /**
     * Guava Class Instance for escaping
     */
    private Escaper esc = UrlEscapers.urlFormParameterEscaper();


    /**
     * Constructor of API when you exactly know the keys
     * @param apiKey Public API Key
     * @param secretKey Secret API Key
     * @throws LivecoinApiException in case of any error
     */
    public LivecoinApi(String apiKey, String secretKey) throws LivecoinApiException {

        this.apiKey = apiKey;
        this.secretKey = secretKey;
        validateCredentials();
    }

    /**
     * Constructor of API - keys are loaded from VM options, environment variables, resource files
     * @throws LivecoinApiException in case of any error
     */
    public LivecoinApi() {
        this.apiKey = config.getVariable("LIVECOIN_API_KEY");
        this.secretKey = config.getVariable("LIVECOIN_SECRET");
    }

    /**
     * Validation we have API keys set up
     * @throws LivecoinApiException in case of any error
     */
    protected void validateCredentials() throws LivecoinApiException {
        String humanMessage = "Please check environment variables or VM options";
        if (Strings.isNullOrEmpty(this.getApiKey()))
            throw new LivecoinApiException("Missing LIVECOIN_API_KEY. " + humanMessage);
        if (Strings.isNullOrEmpty(this.getSecretKey()))
            throw new LivecoinApiException("Missing LIVECOIN_SECRET. " + humanMessage);
    }

    // MARKET INFORMATION
    public Set<String> getCoinsOf(String coin) {
        try {
            LivecoinSymbolRestrictionStats stats = this.getExchangeRestrictions();
            return stats.getCoinsOf(coin.toUpperCase());
        } catch (Exception e) {
            log.error("LIVECOIN UNCAUGHT EXCEPTION {}", e.getMessage());
        } catch (LivecoinApiException e) {
            log.warn("LIVECOIN ERROR {}", e.getMessage());
        }
        return ImmutableSet.of();
    }

    public LivecoinSymbolRestrictionStats getExchangeRestrictions() throws LivecoinApiException {
        JsonObject jsonObject = new LivecoinRequest(baseUrl + "exchange/restrictions").read().asJsonObject();
        return new LivecoinSymbolRestrictionStats(jsonObject);
    }



}
