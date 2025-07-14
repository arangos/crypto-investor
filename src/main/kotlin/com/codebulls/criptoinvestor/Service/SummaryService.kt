package com.codebulls.criptoinvestor.Service

import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder

@Service
class SummaryService( private val restTemplate: RestTemplate = RestTemplate() ) {

    fun getNumbers(
        ids: Array<String> = arrayOf("bitcoin", "ethereum"),
        vsCurrencies: Array<String> = arrayOf("usd")
    ): Map<String, Any>? {
        val url = UriComponentsBuilder
            .fromUriString("https://api.coingecko.com/api/v3/simple/price")
            .queryParam("ids", ids.joinToString(","))
            .queryParam("vs_currencies", vsCurrencies.joinToString(","))
            .toUriString()

        return restTemplate.getForObject(url, Map::class.java) as Map<String, Any>?
    }

}