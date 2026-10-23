package com.warmhouse.telemetry

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException

class ApiException(val status: HttpStatus, val code: String, message: String) : RuntimeException(message)

// Turns errors into the {code, message} format from api/telemetry-openapi.yaml
@RestControllerAdvice
class ApiExceptionHandler {

    @ExceptionHandler(ApiException::class)
    fun handleApiException(e: ApiException): ResponseEntity<ErrorResponse> =
        ResponseEntity.status(e.status).body(ErrorResponse(e.code, e.message.orEmpty()))

    @ExceptionHandler(MissingServletRequestParameterException::class)
    fun handleMissingParameter(e: MissingServletRequestParameterException): ResponseEntity<ErrorResponse> =
        badRequest("Параметр ${e.parameterName} обязателен")

    @ExceptionHandler(MethodArgumentTypeMismatchException::class)
    fun handleWrongParameter(e: MethodArgumentTypeMismatchException): ResponseEntity<ErrorResponse> =
        badRequest("Неверное значение параметра ${e.name}")

    private fun badRequest(message: String): ResponseEntity<ErrorResponse> =
        ResponseEntity.badRequest().body(ErrorResponse("VALIDATION_ERROR", message))
}
