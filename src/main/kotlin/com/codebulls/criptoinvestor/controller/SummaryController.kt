package com.codebulls.criptoinvestor.controller

import com.codebulls.criptoinvestor.model.dto.CurrencyMarketChart
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import com.codebulls.criptoinvestor.service.SummaryService

@RestController
@RequestMapping("/summary")
class SummaryController(private val summaryService: SummaryService) {

    @GetMapping("/averages")
    fun getNumbers(): CurrencyMarketChart? {
        return summaryService.predictCurrencies()
    }

}