package com.laboratory.bank.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EntityTest {

    @Test
    @DisplayName("Customer asigna cada campo en el constructor completo")
    void customer_allArgsConstructor() {
        Customer customer = new Customer(1L, "111", "Ana", "Gomez", 10.0);

        assertEquals(1L, customer.getId());
        assertEquals("111", customer.getAccountNumber());
        assertEquals("Ana", customer.getFirstName());
        assertEquals("Gomez", customer.getLastName());
        assertEquals(10.0, customer.getBalance());
    }

    @Test
    @DisplayName("Customer expone setters")
    void customer_setters() {
        Customer customer = new Customer();
        assertNull(customer.getId());

        customer.setId(2L);
        customer.setAccountNumber("222");
        customer.setFirstName("Luis");
        customer.setLastName("Perez");
        customer.setBalance(20.0);

        assertEquals(2L, customer.getId());
        assertEquals("222", customer.getAccountNumber());
        assertEquals("Luis", customer.getFirstName());
        assertEquals("Perez", customer.getLastName());
        assertEquals(20.0, customer.getBalance());
    }

    @Test
    @DisplayName("Transaction inicializa el timestamp por defecto")
    void transaction_defaultTimestampIsSet() {
        assertNotNull(new Transaction().getTimestamp());
    }

    @Test
    @DisplayName("Transaction asigna cada campo en el constructor completo")
    void transaction_allArgsConstructor() {
        LocalDateTime when = LocalDateTime.of(2025, 1, 1, 8, 0);

        Transaction transaction = new Transaction(1L, "1001", "2002", 5.0, when);

        assertEquals(1L, transaction.getId());
        assertEquals("1001", transaction.getSenderAccountNumber());
        assertEquals("2002", transaction.getReceiverAccountNumber());
        assertEquals(5.0, transaction.getAmount());
        assertEquals(when, transaction.getTimestamp());
    }

    @Test
    @DisplayName("Transaction expone setters")
    void transaction_setters() {
        LocalDateTime when = LocalDateTime.of(2025, 2, 2, 12, 0);
        Transaction transaction = new Transaction();

        transaction.setId(3L);
        transaction.setSenderAccountNumber("1001");
        transaction.setReceiverAccountNumber("2002");
        transaction.setAmount(7.5);
        transaction.setTimestamp(when);

        assertEquals(3L, transaction.getId());
        assertEquals("1001", transaction.getSenderAccountNumber());
        assertEquals("2002", transaction.getReceiverAccountNumber());
        assertEquals(7.5, transaction.getAmount());
        assertEquals(when, transaction.getTimestamp());
    }

    @Test
    @DisplayName("Transaction (@Data) implementa equals, hashCode y toString por valor")
    void transaction_valueSemantics() {
        LocalDateTime when = LocalDateTime.of(2025, 1, 1, 8, 0);
        Transaction a = new Transaction(1L, "1001", "2002", 5.0, when);
        Transaction b = new Transaction(1L, "1001", "2002", 5.0, when);
        Transaction c = new Transaction(2L, "1001", "2002", 5.0, when);

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
        assertNotEquals(a, c);
        assertTrue(a.toString().contains("1001"));
    }
}
