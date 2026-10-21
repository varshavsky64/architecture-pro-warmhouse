package com.warmhouse.telemetry

import java.util.UUID
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

// «Обработка показаний»: stores a reading and reports «новое показание»
@Service
class ReadingProcessor(
    private val registry: DeviceRegistry,
    private val repository: TelemetryRepository,
    private val publisher: NewReadingPublisher,
) {

    private val log = LoggerFactory.getLogger(ReadingProcessor::class.java)

    fun process(reading: DeviceReading) {
        if (!registry.isKnown(reading.deviceId)) {
            log.warn("Reading from unknown device {} skipped", reading.deviceId)
            return
        }

        val data = TelemetryData(UUID.randomUUID(), reading.deviceId, reading.value, reading.recordedAt)
        repository.save(data)
        publisher.publish(data)
    }
}
