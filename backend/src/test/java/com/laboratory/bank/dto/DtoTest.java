package com.laboratory.bank.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class DtoTest {

    @Test
    @DisplayName("CustomerDTO expone getters y setters")
    void customerDto_gettersAndSetters() {
        CustomerDTO dto = new CustomerDTO();
        dto.setId(1L);
        dto.setFirstName("Ana");
        dto.setLastName("Gomez");
        dto.setAccountNumber("111");
        dto.setBalance(10.0);

        assertEquals(1L, dto.getId());
        assertEquals("Ana", dto.getFirstName());
        assertEquals("Gomez", dto.getLastName());
        assertEquals("111", dto.getAccountNumber());
        assertEquals(10.0, dto.getBalance());
    }

    @Test
    @DisplayName("CustomerDTO con todos los argumentos asigna cada campo")
    void customerDto_allArgsConstructor() {
        CustomerDTO dto = new CustomerDTO(1L, "Ana", "Gomez", "111", 10.0);

        assertEquals(1L, dto.getId());
        assertEquals("Ana", dto.getFirstName());
        assertEquals("Gomez", dto.getLastName());
        assertEquals("111", dto.getAccountNumber());
        assertEquals(10.0, dto.getBalance());
    }

    @Test
    @DisplayName("TransactionDTO expone getters y setters")
    void transactionDto_gettersAndSetters() {
        LocalDateTime when = LocalDateTime.of(2025, 1, 1, 8, 0);
        TransactionDTO dto = new TransactionDTO();
        dto.setId(1L);
        dto.setSenderAccountNumber("1001");
        dto.setReceiverAccountNumber("2002");
        dto.setAmount(5.0);
        dto.setTimestamp(when);

        assertEquals(1L, dto.getId());
        assertEquals("1001", dto.getSenderAccountNumber());
        assertEquals("2002", dto.getReceiverAccountNumber());
        assertEquals(5.0, dto.getAmount());
        assertEquals(when, dto.getTimestamp());
    }

    @Test
    @DisplayName("TransactionDTO inicializa el timestamp por defecto")
    void transactionDto_defaultTimestampIsSet() {
        assertNotNull(new TransactionDTO().getTimestamp());
    }

    @Test
    @DisplayName("TransactionDTO con todos los argumentos asigna cada campo")
    void transactionDto_allArgsConstructor() {
        LocalDateTime when = LocalDateTime.of(2025, 1, 1, 8, 0);

        TransactionDTO dto = new TransactionDTO(9L, "1001", "2002", 5.0, when);

        assertEquals(9L, dto.getId());
        assertEquals("1001", dto.getSenderAccountNumber());
        assertEquals("2002", dto.getReceiverAccountNumber());
        assertEquals(5.0, dto.getAmount());
        assertEquals(when, dto.getTimestamp());
    }

    @Test
    @DisplayName("TransactionDTO omite los campos nulos al serializar (@JsonInclude NON_NULL)")
    void transactionDto_omitsNullFieldsInJson() throws Exception {
        ObjectMapper mapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        TransactionDTO dto = new TransactionDTO(null, "1001", "2002", 5.0, null);

        String json = mapper.writeValueAsString(dto);

        assertFalse(json.contains("\"id\""));
        assertFalse(json.contains("\"timestamp\""));
        assertEquals("1001", mapper.readTree(json).get("senderAccountNumber").asText());
    }

    @Test
    @DisplayName("TransferRequestDTO expone getters y setters")
    void transferRequestDto_gettersAndSetters() {
        TransferRequestDTO dto = new TransferRequestDTO();
        assertNull(dto.getAmount());

        dto.setSenderAccountNumber("1001");
        dto.setReceiverAccountNumber("2002");
        dto.setAmount(12.5);

        assertEquals("1001", dto.getSenderAccountNumber());
        assertEquals("2002", dto.getReceiverAccountNumber());
        assertEquals(12.5, dto.getAmount());
    }
}
