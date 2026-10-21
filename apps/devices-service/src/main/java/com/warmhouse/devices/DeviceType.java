package com.warmhouse.devices;

import java.util.Set;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "device_types")
public class DeviceType {

    @Id
    private UUID id;
    private String name;
    @JdbcTypeCode(SqlTypes.ARRAY)
    private Set<String> supportedCommands;

    protected DeviceType() {
    }

    // A new device type is a new row with its own command list, not new code
    public boolean supports(String action) {
        return supportedCommands.contains(action);
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
