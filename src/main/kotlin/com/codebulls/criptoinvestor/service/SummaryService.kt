package com.codebulls.criptoinvestor.service

import com.codebulls.criptoinvestor.constants.Currencies
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder

@Service
class SummaryService( private val restTemplate: RestTemplate = RestTemplate() ) {

    @Cacheable("summaryCache")
    fun getNumbers(
        ids: Array<String> = arrayOf(Currencies.BITCOIN.toString(), Currencies.ETHEREUM.toString()),
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