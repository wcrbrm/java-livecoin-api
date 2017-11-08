package com.webcerebrium.livecoin.api;

import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;

@Slf4j
public class LivecoinPublicApiTest {

    LivecoinApi api;

    @Before
    public void setUp() throws Exception, LivecoinApiException {
        api = new LivecoinApi();
    }

    @Test
    public void testOpenMarkets() throws Exception, LivecoinApiException {
        log.info("restrictions={}", api.getExchangeRestrictions());

    }
}
