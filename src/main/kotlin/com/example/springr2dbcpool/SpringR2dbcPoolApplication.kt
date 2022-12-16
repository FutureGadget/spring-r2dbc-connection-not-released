package com.example.springr2dbcpool

import io.r2dbc.pool.ConnectionPool
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.r2dbc.connection.TransactionAwareConnectionFactoryProxy
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono

@SpringBootApplication
class SpringR2dbcPoolApplication

fun main(args: Array<String>) {
    runApplication<SpringR2dbcPoolApplication>(*args)
}

@RestController
class TestController(
    connectionPool: ConnectionPool
) {
    val proxy = TransactionAwareConnectionFactoryProxy(connectionPool)

    /**
     * This controller is for interactive testing purposes.
     */
    @GetMapping("/sleep")
    @Transactional(isolation = Isolation.SERIALIZABLE)
    fun sleep(): Mono<Void> {
        return proxy.create()
            .flatMap {
                it.createStatement("SELECT SLEEP(10)").execute().toMono().then()
            }
    }
}
