package com.laboratory.bank.service;

import com.laboratory.bank.dto.TransactionDTO;
import com.laboratory.bank.entity.Customer;
import com.laboratory.bank.entity.Transaction;
import com.laboratory.bank.repository.CustomerRepository;
import com.laboratory.bank.repository.TransactionRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    private static final String SENDER_ACCOUNT = "1001";
    private static final String RECEIVER_ACCOUNT = "2002";
    private static final LocalDateTime NOW = LocalDateTime.of(2025, 1, 15, 10, 30);

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private TransactionService transactionService;

    private Customer sender(double balance) {
        return new Customer(1L, SENDER_ACCOUNT, "Ana", "Gomez", balance);
    }

    private Customer receiver(double balance) {
        return new Customer(2L, RECEIVER_ACCOUNT, "Luis", "Perez", balance);
    }

    private TransactionDTO request(String from, String to, Double amount) {
        TransactionDTO dto = new TransactionDTO();
        dto.setSenderAccountNumber(from);
        dto.setReceiverAccountNumber(to);
        dto.setAmount(amount);
        dto.setTimestamp(NOW);
        return dto;
    }

    @Test
    @DisplayName("transferMoney mueve el saldo, guarda ambas cuentas y la transaccion")
    void transferMoney_success() {
        Customer sender = sender(500.0);
        Customer receiver = receiver(100.0);
        when(customerRepository.findByAccountNumber(SENDER_ACCOUNT)).thenReturn(Optional.of(sender));
        when(customerRepository.findByAccountNumber(RECEIVER_ACCOUNT)).thenReturn(Optional.of(receiver));
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(inv -> {
            Transaction t = inv.getArgument(0);
            t.setId(77L);
            return t;
        });

        TransactionDTO result = transactionService.transferMoney(request(SENDER_ACCOUNT, RECEIVER_ACCOUNT, 200.0));

        assertEquals(300.0, sender.getBalance());
        assertEquals(300.0, receiver.getBalance());
        verify(customerRepository).save(sender);
        verify(customerRepository).save(receiver);

        ArgumentCaptor<Transaction> captor = ArgumentCaptor.forClass(Transaction.class);
        verify(transactionRepository).save(captor.capture());
        Transaction persisted = captor.getValue();
        assertEquals(SENDER_ACCOUNT, persisted.getSenderAccountNumber());
        assertEquals(RECEIVER_ACCOUNT, persisted.getReceiverAccountNumber());
        assertEquals(200.0, persisted.getAmount());
        assertEquals(NOW, persisted.getTimestamp());

        assertEquals(77L, result.getId());
        assertEquals(SENDER_ACCOUNT, result.getSenderAccountNumber());
        assertEquals(RECEIVER_ACCOUNT, result.getReceiverAccountNumber());
        assertEquals(200.0, result.getAmount());
        assertEquals(NOW, result.getTimestamp());
    }

    @Test
    @DisplayName("transferMoney permite transferir exactamente todo el saldo")
    void transferMoney_fullBalance_isAllowed() {
        Customer sender = sender(200.0);
        Customer receiver = receiver(0.0);
        when(customerRepository.findByAccountNumber(SENDER_ACCOUNT)).thenReturn(Optional.of(sender));
        when(customerRepository.findByAccountNumber(RECEIVER_ACCOUNT)).thenReturn(Optional.of(receiver));
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(inv -> inv.getArgument(0));

        transactionService.transferMoney(request(SENDER_ACCOUNT, RECEIVER_ACCOUNT, 200.0));

        assertEquals(0.0, sender.getBalance());
        assertEquals(200.0, receiver.getBalance());
    }

    @Test
    @DisplayName("transferMoney rechaza una cuenta de remitente nula")
    void transferMoney_nullSender_throws() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> transactionService.transferMoney(request(null, RECEIVER_ACCOUNT, 10.0)));

        assertEquals("Los números de cuenta del remitente y receptor son obligatorios.", ex.getMessage());
        verifyNoInteractions(customerRepository, transactionRepository);
    }

    @Test
    @DisplayName("transferMoney rechaza una cuenta de receptor nula")
    void transferMoney_nullReceiver_throws() {
        assertThrows(IllegalArgumentException.class,
                () -> transactionService.transferMoney(request(SENDER_ACCOUNT, null, 10.0)));

        verifyNoInteractions(customerRepository, transactionRepository);
    }

    @ParameterizedTest(name = "monto = {0}")
    @NullSource
    @ValueSource(doubles = {0.0, -0.01, -100.0})
    @DisplayName("transferMoney rechaza montos nulos, cero o negativos sin tocar los saldos")
    void transferMoney_invalidAmount_throws(Double amount) {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> transactionService.transferMoney(request(SENDER_ACCOUNT, RECEIVER_ACCOUNT, amount)));

        assertEquals("El monto debe ser mayor que cero.", ex.getMessage());
        verifyNoInteractions(customerRepository, transactionRepository);
    }

    @Test
    @DisplayName("transferMoney falla si la cuenta del remitente no existe")
    void transferMoney_senderNotFound_throws() {
        when(customerRepository.findByAccountNumber(SENDER_ACCOUNT)).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> transactionService.transferMoney(request(SENDER_ACCOUNT, RECEIVER_ACCOUNT, 10.0)));

        assertEquals("La cuenta del remitente no existe.", ex.getMessage());
        verify(customerRepository, never()).save(any());
        verifyNoInteractions(transactionRepository);
    }

    @Test
    @DisplayName("transferMoney falla si la cuenta del receptor no existe")
    void transferMoney_receiverNotFound_throws() {
        when(customerRepository.findByAccountNumber(SENDER_ACCOUNT)).thenReturn(Optional.of(sender(100.0)));
        when(customerRepository.findByAccountNumber(RECEIVER_ACCOUNT)).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> transactionService.transferMoney(request(SENDER_ACCOUNT, RECEIVER_ACCOUNT, 10.0)));

        assertEquals("La cuenta del receptor no existe.", ex.getMessage());
        verify(customerRepository, never()).save(any());
        verifyNoInteractions(transactionRepository);
    }

    @Test
    @DisplayName("transferMoney falla con saldo insuficiente y no modifica nada")
    void transferMoney_insufficientBalance_throws() {
        Customer sender = sender(50.0);
        Customer receiver = receiver(100.0);
        when(customerRepository.findByAccountNumber(SENDER_ACCOUNT)).thenReturn(Optional.of(sender));
        when(customerRepository.findByAccountNumber(RECEIVER_ACCOUNT)).thenReturn(Optional.of(receiver));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> transactionService.transferMoney(request(SENDER_ACCOUNT, RECEIVER_ACCOUNT, 50.01)));

        assertEquals("Saldo insuficiente en la cuenta del remitente.", ex.getMessage());
        assertEquals(50.0, sender.getBalance());
        assertEquals(100.0, receiver.getBalance());
        verify(customerRepository, never()).save(any());
        verifyNoInteractions(transactionRepository);
    }

    @Test
    @DisplayName("getTransactionsForAccount mapea las transacciones de la cuenta a DTOs")
    void getTransactionsForAccount_returnsMappedDtos() {
        Transaction t1 = new Transaction(1L, SENDER_ACCOUNT, RECEIVER_ACCOUNT, 10.0, NOW);
        Transaction t2 = new Transaction(2L, RECEIVER_ACCOUNT, SENDER_ACCOUNT, 25.5, NOW.plusHours(1));
        when(transactionRepository.findBySenderAccountNumberOrReceiverAccountNumber(SENDER_ACCOUNT, SENDER_ACCOUNT))
                .thenReturn(List.of(t1, t2));

        List<TransactionDTO> result = transactionService.getTransactionsForAccount(SENDER_ACCOUNT);

        assertEquals(2, result.size());
        TransactionDTO first = result.get(0);
        assertEquals(1L, first.getId());
        assertEquals(SENDER_ACCOUNT, first.getSenderAccountNumber());
        assertEquals(RECEIVER_ACCOUNT, first.getReceiverAccountNumber());
        assertEquals(10.0, first.getAmount());
        assertEquals(NOW, first.getTimestamp());
        TransactionDTO second = result.get(1);
        assertEquals(2L, second.getId());
        assertEquals(25.5, second.getAmount());
        assertEquals(NOW.plusHours(1), second.getTimestamp());
    }

    @Test
    @DisplayName("getTransactionsForAccount devuelve lista vacia si no hay movimientos")
    void getTransactionsForAccount_whenNone_returnsEmptyList() {
        when(transactionRepository.findBySenderAccountNumberOrReceiverAccountNumber("9", "9")).thenReturn(List.of());

        assertTrue(transactionService.getTransactionsForAccount("9").isEmpty());
    }
}
