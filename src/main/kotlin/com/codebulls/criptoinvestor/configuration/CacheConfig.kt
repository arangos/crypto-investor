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
        val cache = CaffeineCache(
            "summaryCache",
                  Caffeine.newBuilder()
                    .maximumSize(100)
                    .expireAfterWrite(8, java.util.concurrent.TimeUnit.HOURS)
                    .build()
        )

        val manager = SimpleCacheManager()
        manager.setCaches(listOf(cache))
        return manager
    }

}