package com.codebulls.criptoinvestor.Controller


import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import com.codebulls.criptoinvestor.Service.SummaryService

@RestController
@RequestMapping("/summary")
class SummaryController(private val summaryService: SummaryService) {

    @GetMapping("/averages")
    fun getNumbers(): Map<String, Any>? {
        return summaryService.getNumbers()
    }

}