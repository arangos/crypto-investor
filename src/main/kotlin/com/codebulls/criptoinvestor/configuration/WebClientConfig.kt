package com.codebulls.criptoinvestor.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class WebClientConfig {

    @Bean
    fun coinGeckoWebClient(builder: WebClient.Builder): WebClient =
        builder
            .baseUrl("https://api.coingecko.com/api/v3")
            .build()

    @Bean
    fun binanceWebClient(builder: WebClient.Builder): WebClient =
        builder
            .baseUrl("https://api.binance.com/api/v3")
            .build()
}