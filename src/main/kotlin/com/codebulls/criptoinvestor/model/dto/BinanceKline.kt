package com.codebulls.criptoinvestor.model.dto

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import java.math.BigDecimal
import java.time.Instant

@JsonFormat(shape = JsonFormat.Shape.ARRAY)
@JsonPropertyOrder(
    "openTime", "open", "high", "low", "close", "volume",
    "closeTime", "quoteAssetVolume", "numberOfTrades",
    "takerBuyBaseAssetVolume", "takerBuyQuoteAssetVolume", "ignore"
)
data class BinanceKline(
    @JsonProperty(index = 0) val openTime: Instant,
    @JsonProperty(index = 1) val open: BigDecimal,
    @JsonProperty(index = 2) val high: BigDecimal,
    @JsonProperty(index = 3) val low: BigDecimal,
    @JsonProperty(index = 4) val close: BigDecimal,
    @JsonProperty(index = 5) val volume: BigDecimal,
    @JsonProperty(index = 6) val closeTime: Instant,
    @JsonProperty(index = 7) val quoteAssetVolume: BigDecimal,
    @JsonProperty(index = 8) val numberOfTrades: Long,
    @JsonProperty(index = 9) val takerBuyBaseAssetVolume: BigDecimal,
    @JsonProperty(index = 10) val takerBuyQuoteAssetVolume: BigDecimal,
    @JsonProperty(index = 11) val ignore: String
)