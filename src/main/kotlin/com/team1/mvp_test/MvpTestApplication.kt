package com.team1.mvp_test

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableAsync

@SpringBootApplication
@EnableAsync
class MvpTestApplication

fun main(args: Array<String>) {
    runApplication<MvpTestApplication>(*args)
}
