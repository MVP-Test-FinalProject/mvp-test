package com.team1.mvp_test.domain.mvptest.service.apply

import org.redisson.Redisson
import org.redisson.api.RedissonClient
import org.redisson.config.Config
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.core.io.ClassPathResource

@TestConfiguration
class RedissonTestConfig {

    @Bean
    fun redissonTestClient(): RedissonClient {
        val config = Config.fromYAML(ClassPathResource("redisson.yml").inputStream)
        return Redisson.create(config)
    }
}