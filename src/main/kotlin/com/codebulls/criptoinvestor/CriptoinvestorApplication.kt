package com.codebulls.criptoinvestor

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity

@SpringBootApplication
@EnableWebSecurity
@EnableCaching
class CriptoInvestorApplication

fun main(args: Array<String>) {
	runApplication<CriptoInvestorApplication>(*args)
}
