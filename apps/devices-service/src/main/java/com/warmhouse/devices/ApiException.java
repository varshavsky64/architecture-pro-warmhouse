package com.warmhouse.devices;

import java.util.UUID;

import org.springframework.http.HttpStatus;

public class ApiException extends RuntimeException {

    private final HttpStatus status;
    private final String code;

    public ApiException(HttpStatus status, String code, String message) {
        super(message);
        this.status = status;
        this.code = code;
    }

    public static ApiException deviceNotFound(UUID id) {
        return new ApiException(HttpStatus.NOT_FOUND, "DEVICE_NOT_FOUND", "Устройство " + id + " не найдено");
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getCode() {
        return code;
    }
}
