package com.fullstackbd.ping_backend

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching

@SpringBootApplication
@EnableCaching
class PingBackendApplication

fun main(args: Array<String>) {
	runApplication<PingBackendApplication>(*args)
}
