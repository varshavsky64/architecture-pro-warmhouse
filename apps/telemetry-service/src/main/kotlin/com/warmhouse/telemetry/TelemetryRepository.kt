package com.warmhouse.telemetry

import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.util.UUID
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.simple.JdbcClient
import org.springframework.stereotype.Repository

// «Репозиторий показаний»: readings by time and the copy of the device list
@Repository
class TelemetryRepository(private val jdbc: JdbcClient) {

    fun save(data: TelemetryData) {
        jdbc.sql("INSERT INTO telemetry_data (id, device_id, value, recorded_at) VALUES (?, ?, ?, ?)")
            .params(data.id, data.deviceId, data.value, data.recordedAt.atOffset(ZoneOffset.UTC))
            .update()
    }

    fun findByDevice(deviceId: UUID, from: Instant, to: Instant): List<TelemetryData> =
        jdbc.sql(
            """
            SELECT id, device_id, value, recorded_at FROM telemetry_data
            WHERE device_id = ? AND recorded_at BETWEEN ? AND ?
            ORDER BY recorded_at
            """
        )
            .params(deviceId, from.atOffset(ZoneOffset.UTC), to.atOffset(ZoneOffset.UTC))
            .query(RowMapper { rs, _ ->
                TelemetryData(
                    id = rs.getObject("id", UUID::class.java),
                    deviceId = rs.getObject("device_id", UUID::class.java),
                    value = rs.getDouble("value"),
                    recordedAt = rs.getObject("recorded_at", OffsetDateTime::class.java).toInstant(),
                )
            })
            .list()

    fun saveDevice(event: DeviceEvent) {
        jdbc.sql("INSERT INTO known_devices (device_id, type_id, house_id) VALUES (?, ?, ?) ON CONFLICT (device_id) DO NOTHING")
            .params(event.deviceId, event.typeId, event.houseId)
            .update()
    }

    fun deviceExists(deviceId: UUID): Boolean =
        jdbc.sql("SELECT EXISTS (SELECT 1 FROM known_devices WHERE device_id = ?)")
            .param(deviceId)
            .query(Boolean::class.javaObjectType)
            .single()
}
