package com.codebulls.criptoinvestor.constants

enum class Currencies(val currencyName: String) {
    BITCOIN("bitcoin"),
    ETHEREUM("ethereum");

    companion object {
        fun fromName(currencyName: String): Currencies? {
            return entries.find { it.currencyName == currencyName }
        }
    }
}