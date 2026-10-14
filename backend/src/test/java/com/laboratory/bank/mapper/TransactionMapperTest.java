package com.laboratory.bank.mapper;

import com.laboratory.bank.dto.TransactionDTO;
import com.laboratory.bank.entity.Transaction;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class TransactionMapperTest {

    private final TransactionMapper mapper = TransactionMapper.INSTANCE;

    @Test
    @DisplayName("toDTO copia todos los campos de la transaccion")
    void toDTO_mapsAllFields() {
        LocalDateTime when = LocalDateTime.of(2025, 3, 1, 9, 0);
        Transaction transaction = new Transaction(3L, "1001", "2002", 99.9, when);

        TransactionDTO dto = mapper.toDTO(transaction);

        assertEquals(3L, dto.getId());
        assertEquals("1001", dto.getSenderAccountNumber());
        assertEquals("2002", dto.getReceiverAccountNumber());
        assertEquals(99.9, dto.getAmount());
        assertEquals(when, dto.getTimestamp());
    }

    @Test
    @DisplayName("toDTO devuelve null cuando la entrada es null")
    void toDTO_nullInput_returnsNull() {
        assertNull(mapper.toDTO(null));
    }
}
