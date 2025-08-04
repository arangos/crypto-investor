package com.codebulls.criptoinvestor.controller

import com.codebulls.criptoinvestor.model.dto.MaSignal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import com.codebulls.criptoinvestor.service.BinanceSummaryService
import com.codebulls.criptoinvestor.service.CoinGekoSummaryService

@RestController
@RequestMapping("/summary")
class SummaryController(private val binanceSummaryService: BinanceSummaryService,
                        private val coinGekoSummaryService: CoinGekoSummaryService
) {

    @GetMapping("/binance")
    suspend fun getBinancePredictions(): List<MaSignal> {
        return binanceSummaryService.predictBinanceCurrencies()
    }

    @GetMapping("/coingecko")
    suspend fun getCoinGekoPredictions(): List<MaSignal>  {
        return coinGekoSummaryService.predictCoinGekoCurrencies()
    }

}