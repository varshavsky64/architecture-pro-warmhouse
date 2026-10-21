package com.warmhouse.devices;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DeviceRepository extends JpaRepository<Device, UUID> {

    boolean existsBySerialNumber(String serialNumber);

    List<Device> findByTypeIdIn(Collection<UUID> typeIds);
}
