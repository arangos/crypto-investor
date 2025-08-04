package com.codebulls.criptoinvestor.service

import com.codebulls.criptoinvestor.constants.Currencies
import com.codebulls.criptoinvestor.constants.SignalType
import com.codebulls.criptoinvestor.model.dto.BinanceKline
import com.codebulls.criptoinvestor.model.dto.CurrencyMarketChart
import com.codebulls.criptoinvestor.model.dto.MaSignal
import com.codebulls.criptoinvestor.model.dto.OhlcCandle
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody
import java.time.Instant


@Service
class BinanceSummaryService( private val binanceWebClient: WebClient) {

    suspend fun predictBinanceCurrencies(): List<MaSignal> {
        // Fetch Binance daily klines
        val binanceCandleSticks = fetchBinanceCandleSticks()

        // Extract closing prices and timestamps
        val closes = binanceCandleSticks.map { it.close.toDouble() }
        val timestamps = binanceCandleSticks.map { it.openTime }

        // Define window lengths
        val shortWindow = 3
        val longWindow = 30

        // Calculate moving averages
        val shortMa = calculateEMA(closes, shortWindow)
        val longMa = calculateEMA(closes, longWindow)

        // Generate crossover signals
        return generateSignals(timestamps, shortMa, longMa, shortWindow, longWindow)
    }

    @Cacheable("binanceCache")
    suspend fun fetchBinanceCandleSticks(): List<BinanceKline> =
        binanceWebClient.get()
            .uri { uri ->
                uri.path("/klines")
                    .queryParam("symbol", "BTCUSDT")
                    .queryParam("interval", "1d")
                    .queryParam("limit", 30)
                    .build()
            }
            .retrieve()
            .awaitBody<List<BinanceKline>>()

    /**
     * Simple Moving Average: returns a list where indices < period-1 are null,
     * and from period-1 onward contain the average of the last [period] prices.
     */
    private fun calculateSMA(data: List<Double>, period: Int): List<Double?> {
        val sma = MutableList<Double?>(data.size) { null }
        for (i in (period - 1) until data.size) {
            val window = data.subList(i - period + 1, i + 1)
            sma[i] = window.average()
        }
        return sma
    }

    /**
     * Exponential Moving Average: seeds with SMA at index period-1,
     * then applies EMA formula for subsequent points.
     */
    private fun calculateEMA(data: List<Double>, period: Int): List<Double?> {
        val ema = MutableList<Double?>(data.size) { null }
        if (data.size < period) return ema
        val alpha = 2.0 / (period + 1)
        // Seed EMA with the first SMA value
        ema[period - 1] = data.subList(0, period).average()
        for (i in period until data.size) {
            val price = data[i]
            val prevEma = ema[i - 1]!!
            ema[i] = alpha * price + (1 - alpha) * prevEma
        }
        return ema
    }

    /**
     * Generates a list of crossover signals (Buy/Sell/Hold) once both
     * short and long MA are available.
     */
    private fun generateSignals(
        timestamps: List<Instant>,
        shortMa: List<Double?>,
        longMa: List<Double?>,
        shortWindow: Int,
        longWindow: Int
    ): List<MaSignal> {
        val signals = mutableListOf<MaSignal>()
        val startIndex = maxOf(shortWindow, longWindow) - 1
        for (i in startIndex until timestamps.size) {
            val s = shortMa[i]!!
            val l = longMa[i]!!
            val signal = when {
                s > l   -> SignalType.BUY
                s < l   -> SignalType.SELL
                else    -> SignalType.HOLD
            }
            signals.add(MaSignal(timestamps[i], signal))
        }
        return signals
    }

}