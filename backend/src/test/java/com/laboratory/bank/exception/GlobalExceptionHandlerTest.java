package com.laboratory.bank.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    @DisplayName("CustomerNotFoundException se traduce a 404 con el mensaje")
    void handleCustomerNotFound_returns404() {
        ResponseEntity<String> response = handler.handleCustomerNotFound(new CustomerNotFoundException("no existe"));

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("no existe", response.getBody());
    }

    @Test
    @DisplayName("IllegalArgumentException se traduce a 400 con el mensaje")
    void handleIllegalArgument_returns400() {
        ResponseEntity<String> response = handler.handleIllegalArgument(new IllegalArgumentException("dato invalido"));

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("dato invalido", response.getBody());
    }
}
