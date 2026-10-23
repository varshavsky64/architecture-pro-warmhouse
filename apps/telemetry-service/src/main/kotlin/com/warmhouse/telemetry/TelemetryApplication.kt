package com.warmhouse.telemetry

import org.apache.kafka.clients.admin.NewTopic
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.kafka.config.TopicBuilder

@SpringBootApplication
class TelemetryApplication {

    @Bean
    fun newReadingsTopic(): NewTopic = TopicBuilder.name(NEW_READINGS_TOPIC).build()
}

fun main(args: Array<String>) {
    runApplication<TelemetryApplication>(*args)
}
