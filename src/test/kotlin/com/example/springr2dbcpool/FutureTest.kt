package com.example.springr2dbcpool

import org.junit.jupiter.api.Test
import reactor.core.publisher.Flux
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executors

class FutureTest {
    @Test
    fun `completableFuture can be converted into Flux`() {
        val future = CompletableFuture<String>()
        Executors.newSingleThreadExecutor()
            .submit {
                Thread.sleep(1000L)
                future.complete("TEST")
            }
    }
}