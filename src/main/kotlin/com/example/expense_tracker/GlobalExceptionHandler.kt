package com.example.expense_tracker

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.http.converter.HttpMessageNotReadableException

@RestControllerAdvice
class GlobalExceptionHandler {

    // handler for receiving payloads to POST
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationException(
        exception: MethodArgumentNotValidException
    ): ResponseEntity<Map<String, String>> {

        val errors = exception.bindingResult.fieldErrors
            .associate { error ->
                error.field to (error.defaultMessage ?: "Invalid value")
            }

        return ResponseEntity
            .badRequest()
            .body(errors)
    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleMalformedRequest(
        exception: HttpMessageNotReadableException
    ): ResponseEntity<Map<String, String>> {

        val message = exception.message ?: ""
        println(message)

        // TODO: check different behavior for finding no amount value
        // TODO: add more request handling base don different error messages
        val regex = Regex("""JSON property (\w+) due to""")

        val match = regex.find(message)

        val missingField = match?.groupValues?.get(1)

        val errorMessage = if (missingField != null) {
            "Required field '$missingField' is missing"
        } else {
            "Invalid request body"
        }

        return ResponseEntity
            .badRequest()
            .body(
                mapOf("error" to errorMessage)
            )
    }
}