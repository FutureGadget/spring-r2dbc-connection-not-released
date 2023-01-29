package com.example.springr2dbcpool

import io.r2dbc.pool.PoolingConnectionFactoryProvider
import io.r2dbc.spi.ConnectionFactoryOptions
import io.r2dbc.spi.ConnectionFactoryOptions.STATEMENT_TIMEOUT
import org.springframework.boot.autoconfigure.r2dbc.ConnectionFactoryOptionsBuilderCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.Duration

@Configuration
class PoolConfig {
    @Bean
    fun connectionFactoryOptionsBuilderCustomizer(): ConnectionFactoryOptionsBuilderCustomizer {
        return ConnectionFactoryOptionsBuilderCustomizer { builder: ConnectionFactoryOptions.Builder ->
            builder
                .option(ConnectionFactoryOptions.CONNECT_TIMEOUT, Duration.ofSeconds(2))
                .option(
                    STATEMENT_TIMEOUT,
                    Duration.ofSeconds(3)
                ).option(
                    PoolingConnectionFactoryProvider.MIN_IDLE, 2
                )
        }
    }
}
