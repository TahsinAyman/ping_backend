package com.fullstackbd.ping_backend

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class PingBackendApplication

fun main(args: Array<String>) {
	runApplication<PingBackendApplication>(*args)
}
