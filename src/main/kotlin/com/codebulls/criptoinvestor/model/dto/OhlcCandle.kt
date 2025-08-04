package com.codebulls.criptoinvestor.model.dto

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import java.time.Instant

@JsonFormat(shape = JsonFormat.Shape.ARRAY)
@JsonPropertyOrder("timestamp", "open", "high", "low", "close")
data class OhlcCandle (
    @JsonProperty(index = 0) val timestamp: Instant,
    @JsonProperty(index = 1) val open:      Double,
    @JsonProperty(index = 2) val high:      Double,
    @JsonProperty(index = 3) val low:       Double,
    @JsonProperty(index = 4) val close:     Double
)