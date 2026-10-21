package com.warmhouse.devices;

import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

// Aggregate root of the «Команды» component, see schemas/code-commands.puml
@Entity
@Table(name = "devices")
public class Device {

    @Id
    private UUID id;
    private UUID typeId;
    private UUID houseId;
    private String serialNumber;
    @Embedded
    private DeviceState state;

    protected Device() {
    }

    public Device(UUID id, UUID typeId, UUID houseId, String serialNumber, DeviceState state) {
        this.id = id;
        this.typeId = typeId;
        this.houseId = houseId;
        this.serialNumber = serialNumber;
        this.state = state;
    }

    // Applies a command that the device type supports and returns the new state
    public DeviceState apply(Command command) {
        var status = switch (command.action()) {
            case "TURN_ON" -> "ON";
            case "TURN_OFF" -> "OFF";
            case "OPEN" -> "OPEN";
            case "CLOSE" -> "CLOSED";
            default -> state.status();
        };
        state = new DeviceState(status, Instant.now());
        return state;
    }

    public UUID getId() {
        return id;
    }

    public UUID getTypeId() {
        return typeId;
    }

    public UUID getHouseId() {
        return houseId;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public DeviceState getState() {
        return state;
    }
}
