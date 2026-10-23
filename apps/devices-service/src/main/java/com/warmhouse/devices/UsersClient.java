package com.warmhouse.devices;

import java.util.UUID;

// «Клиент пользователей и подписок»: asks the users service whose house it is and whether the subscription is active
public interface UsersClient {

    // Throws ApiException with 403 when the house belongs to another user or the subscription is not active
    void checkHouse(UUID userId, UUID houseId);
}
