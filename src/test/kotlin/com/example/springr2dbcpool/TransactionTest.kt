package com.example.springr2dbcpool

import io.r2dbc.pool.ConnectionPool
import org.awaitility.kotlin.await
import org.hamcrest.core.IsEqual
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.r2dbc.connection.ConnectionFactoryUtils
import org.springframework.transaction.reactive.TransactionalOperator
import reactor.kotlin.core.publisher.toMono
import java.time.Duration
import java.util.concurrent.atomic.AtomicBoolean

/**
 * To run this test, please run docker-compose.yaml included in this project root directory.
 */
@SpringBootTest
class TransactionTest {
    @Autowired
    private lateinit var connectionPool: ConnectionPool

    @Autowired
    private lateinit var transactionalOperator: TransactionalOperator

    @Test
    fun `connection must be released after transaction finishes`() {
        val executed = AtomicBoolean(false)
        val mono = ConnectionFactoryUtils.getConnection(connectionPool)
            .log()
            .flatMap {
                it.createStatement("SELECT SLEEP(5)")
                    .execute()
                    .toMono()
                    .doOnSubscribe {
                        executed.set(true)
                    }
            }
        val tr = transactionalOperator.execute { mono }

        tr.subscribe()
        await.untilAtomic(executed, IsEqual(true))
        await.atMost(Duration.ofSeconds(10)).until { connectionPool.metrics.get().acquiredSize() == 0 }
    }
}
