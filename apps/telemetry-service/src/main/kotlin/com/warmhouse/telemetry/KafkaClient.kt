package com.warmhouse.telemetry

import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component
import tools.jackson.databind.json.JsonMapper

// «Kafka-клиент»: reads events and readings of the devices service, publishes «новое показание»

const val DEVICE_EVENTS_TOPIC = "devices.events"
const val DEVICE_READINGS_TOPIC = "devices.readings"
const val NEW_READINGS_TOPIC = "telemetry.readings"

@Component
class DevicesListener(
    private val jsonMapper: JsonMapper,
    private val registry: DeviceRegistry,
    private val processor: ReadingProcessor,
) {

    @KafkaListener(topics = [DEVICE_EVENTS_TOPIC])
    fun onDeviceEvent(message: String) {
        registry.onDeviceEvent(jsonMapper.readValue(message, DeviceEvent::class.java))
    }

    @KafkaListener(topics = [DEVICE_READINGS_TOPIC])
    fun onReading(message: String) {
        processor.process(jsonMapper.readValue(message, DeviceReading::class.java))
    }
}

@Component
class NewReadingPublisher(
    private val kafkaTemplate: KafkaTemplate<String, String>,
    private val jsonMapper: JsonMapper,
) {

    // The device ID is the message key, so the readings of one device stay in order
    fun publish(data: TelemetryData) {
        kafkaTemplate.send(NEW_READINGS_TOPIC, data.deviceId.toString(), jsonMapper.writeValueAsString(data))
    }
}
