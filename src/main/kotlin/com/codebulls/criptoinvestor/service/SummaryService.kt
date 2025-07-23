package com.codebulls.criptoinvestor.service

import com.codebulls.criptoinvestor.constants.Currencies
import com.codebulls.criptoinvestor.model.dto.CurrencyMarketChart
import org.springframework.cache.annotation.Cacheable
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder

@Service
class SummaryService( private val restTemplate: RestTemplate = RestTemplate() ) {

    fun predictCurrencies() : CurrencyMarketChart? {
        // 1.Fetch the currency market chart data
        var currencyMarketChart = fetchCurrencyMarketChart()
        // 2.Fetch OHLC Data (candlesticks)
        val candleStickData = fetchCandleStickCache()
        // 3.Binance: Kline/Candlestick Data

        return currencyMarketChart
    }

    @Cacheable("marketCache")
    fun fetchCurrencyMarketChart() : CurrencyMarketChart? {
        val vsCurrencies : Array<String> = arrayOf("usd")
        val url = UriComponentsBuilder
            .fromUriString("https://api.coingecko.com/api/v3/coins/"+ Currencies.BITCOIN.currencyName +"/market_chart")
            .queryParam("vs_currency", vsCurrencies.joinToString(","))
            .queryParam("days", 15)
            .queryParam("interval", "daily")
            .toUriString()
        val response = restTemplate.exchange(
            url,
            HttpMethod.GET,
            null,
            object : ParameterizedTypeReference<CurrencyMarketChart>() {}
        )
        return response.body
    }

    @Cacheable("candleStickCache")
    fun fetchCandleStickCache() : Any? {
        return "Not implemented yet"
    }

}