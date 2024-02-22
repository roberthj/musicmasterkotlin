package com.roberthj.musicmasterkotlin.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.ExchangeStrategies
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class MusicMasterConfig {

    @Bean
    fun webClient(): WebClient {
        val size = 16 * 1024 * 1024;
        val strategies = ExchangeStrategies.builder()
            .codecs{codecs -> codecs.defaultCodecs().maxInMemorySize(size)}
        .build();
        return WebClient
            .builder()
            .exchangeStrategies(strategies)
            .build();
    }

}