package com.team1.mvp_test.domain.mvptest.service.apply

import com.team1.mvp_test.domain.member.service.MemberService
import com.team1.mvp_test.infra.redisson.RedissonService
import com.team1.mvp_test.infra.s3.s3service.S3Service
import jakarta.annotation.PostConstruct
import jakarta.annotation.PreDestroy
import org.mockito.Mockito
import org.redisson.Redisson
import org.redisson.api.RedissonClient
import org.redisson.config.Config
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.core.io.ClassPathResource
import redis.embedded.RedisServer

@TestConfiguration
class RedissonTestConfig {
    private val redisServer: RedisServer = RedisServer(6379)

    @PostConstruct
    fun startRedis() {
        redisServer.start()
    }
    @PreDestroy
    fun stopRedis() {
        redisServer.stop()
    }

    @Bean
    fun redissonTestClient(): RedissonClient {
        val config = Config.fromYAML(ClassPathResource("redisson.yml").inputStream)
        return Redisson.create(config)
    }

    @Bean
    fun redissonService(redissonTestClient: RedissonClient): RedissonService {
        return RedissonService(redissonTestClient)
    }

    @Bean
    fun s3Service(): S3Service {
        return Mockito.mock(S3Service::class.java)
    }

    @Bean
    fun memberService(): MemberService {
        return Mockito.mock(MemberService::class.java)
    }
}