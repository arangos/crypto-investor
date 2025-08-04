package com.codebulls.criptoinvestor.service

import com.codebulls.criptoinvestor.constants.Currencies
import com.codebulls.criptoinvestor.constants.SignalType
import com.codebulls.criptoinvestor.model.dto.CurrencyMarketChart
import com.codebulls.criptoinvestor.model.dto.MaSignal
import com.codebulls.criptoinvestor.model.dto.OhlcCandle
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody
import java.time.Instant

@Service
class CoinGekoSummaryService (private val coinGeckoWebClient: WebClient){

    /**
     * Fetches market chart and OHLC data, then computes moving-average crossover signals.
     */
    suspend fun predictCoinGekoCurrencies(): List<MaSignal> {
        // 1. Fetch OHLC Candlesticks
        val candles = fetchCandleStickCache()

        // 2. Extract closing prices and timestamps
        val closes = candles.map { it.close }
        val timestamps = candles.map { it.timestamp }

        // 3. Define MA windows (can be parameterized)
        val shortWindow = 10
        val longWindow = 50

        // 4. Calculate EMAs seeded by SMA
        val shortEma = calculateEMA(closes, shortWindow)
        val longEma = calculateEMA(closes, longWindow)

        // 5. Generate and return crossover signals
        return generateSignals(timestamps, shortEma, longEma, shortWindow, longWindow)
    }


    @Cacheable("marketCache")
    suspend fun fetchCurrencyMarketChart(): CurrencyMarketChart =
        coinGeckoWebClient.get()
            .uri { uri ->
                uri.path("/coins/${Currencies.BITCOIN.currencyName}/market_chart")
                    .queryParam("vs_currency", "usd")
                    .queryParam("days", 15)
                    .queryParam("interval", "daily")
                    .build()
            }
            .retrieve()
            .awaitBody<CurrencyMarketChart>()


    @Cacheable("candleStickCache")
    suspend fun fetchCandleStickCache() : List<OhlcCandle> =
        coinGeckoWebClient.get()
            .uri { uri ->
                uri.path("/coins/${Currencies.BITCOIN.currencyName}/ohlc")
                    .queryParam("vs_currency", "usd")
                    .queryParam("days", 30)
                    .build()
            }
            .retrieve()
            .awaitBody<List<OhlcCandle>>()

    /**
     * Exponential Moving Average (EMA): seeds with the SMA value at index [period - 1].
     */
    private fun calculateEMA(data: List<Double>, period: Int): List<Double?> {
        val ema = MutableList<Double?>(data.size) { null }
        if (data.size < period) return ema
        val alpha = 2.0 / (period + 1)
        // Seed EMA with the simple average of the first window
        ema[period - 1] = data.subList(0, period).average()
        // Calculate EMA for subsequent data points
        for (i in period until data.size) {
            val price = data[i]
            val prev = ema[i - 1]!!
            ema[i] = alpha * price + (1 - alpha) * prev
        }
        return ema
    }

    /**
     * Generates moving-average crossover signals: BUY when short EMA > long EMA,
     * SELL when short EMA < long EMA, else HOLD.
     */
    private fun generateSignals(
        timestamps: List<Instant>,
        shortMa: List<Double?>,
        longMa: List<Double?>,
        shortWindow: Int,
        longWindow: Int
    ): List<MaSignal> {
        val signals = mutableListOf<MaSignal>()
        val start = maxOf(shortWindow, longWindow) - 1
        for (i in start until timestamps.size) {
            val s = shortMa[i]!!
            val l = longMa[i]!!
            val type = when {
                s > l -> SignalType.BUY
                s < l -> SignalType.SELL
                else  -> SignalType.HOLD
            }
            signals.add(MaSignal(timestamps[i], type))
        }
        return signals
    }
}