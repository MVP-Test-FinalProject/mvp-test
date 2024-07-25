package com.team1.mvp_test.infra.redisson

import org.redisson.Redisson
import org.redisson.api.RedissonClient
import org.redisson.config.Config
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource

@Configuration
class RedissonConfig {

    @Bean
    fun redissonClient(): RedissonClient {
        val config = Config.fromYAML(ClassPathResource("redisson.yml").inputStream)
        return Redisson.create(config)
    }
}