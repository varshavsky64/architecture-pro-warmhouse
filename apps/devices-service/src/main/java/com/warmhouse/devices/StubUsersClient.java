package com.warmhouse.devices;

import java.util.UUID;

import org.springframework.stereotype.Component;

// MVP: there is no users service yet, so every house belongs to the caller and every subscription is active
@Component
public class StubUsersClient implements UsersClient {

    @Override
    public void checkHouse(UUID userId, UUID houseId) {
    }
}
