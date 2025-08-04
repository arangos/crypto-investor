package com.codebulls.criptoinvestor.configuration

import com.github.benmanes.caffeine.cache.Caffeine
import org.springframework.cache.CacheManager
import org.springframework.cache.caffeine.CaffeineCache
import org.springframework.cache.support.SimpleCacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class CacheConfig {

    @Bean
    fun cacheManager(): CacheManager{
        val marketCache = CaffeineCache(
            "marketCache",
                  Caffeine.newBuilder()
                    .maximumSize(100)
                    .expireAfterWrite(8, java.util.concurrent.TimeUnit.HOURS)
                    .build()
        )

        val candleStickCache = CaffeineCache(
            "candleStickCache",
            Caffeine.newBuilder()
                .maximumSize(200)
                .expireAfterWrite(4, java.util.concurrent.TimeUnit.HOURS)
                .build()
        )

        val binanceCache = CaffeineCache(
            "binanceCache",
            Caffeine.newBuilder()
                .maximumSize(100)
                .expireAfterWrite(4, java.util.concurrent.TimeUnit.HOURS)
                .build()
        )

        val manager = SimpleCacheManager()
        manager.setCaches(listOf(marketCache, candleStickCache, binanceCache))
        return manager
    }

}