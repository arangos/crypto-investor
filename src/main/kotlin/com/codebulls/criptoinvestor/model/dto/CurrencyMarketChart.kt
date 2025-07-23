package com.codebulls.criptoinvestor.model.dto

data class CurrencyMarketChart(
    val prices: List<List<Double>>,
    val market_caps: List<List<Double>>,
    val total_volumes: List<List<Double>>
)