package com.laboratory.bank.controller;

import com.laboratory.bank.dto.TransactionDTO;
import com.laboratory.bank.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class TransactionControllerTest {

    private static final String BODY = """
            {"senderAccountNumber":"1001","receiverAccountNumber":"2002","amount":200.0}
            """;

    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private TransactionController transactionController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(transactionController).build();
    }

    private TransactionDTO dto(Long id, String from, String to, Double amount) {
        return new TransactionDTO(id, from, to, amount, LocalDateTime.of(2025, 1, 15, 10, 30));
    }

    @Test
    @DisplayName("POST /api/transactions devuelve 200 con la transaccion creada")
    void transferMoney_returnsOk() throws Exception {
        when(transactionService.transferMoney(any(TransactionDTO.class)))
                .thenReturn(dto(5L, "1001", "2002", 200.0));

        mockMvc.perform(post("/api/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(BODY))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.senderAccountNumber").value("1001"))
                .andExpect(jsonPath("$.receiverAccountNumber").value("2002"))
                .andExpect(jsonPath("$.amount").value(200.0));

        verify(transactionService).transferMoney(any(TransactionDTO.class));
    }

    @Test
    @DisplayName("POST /api/transactions devuelve 400 con el mensaje cuando el servicio rechaza la operacion")
    void transferMoney_whenIllegalArgument_returnsBadRequest() throws Exception {
        when(transactionService.transferMoney(any(TransactionDTO.class)))
                .thenThrow(new IllegalArgumentException("Saldo insuficiente en la cuenta del remitente."));

        mockMvc.perform(post("/api/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(BODY))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Saldo insuficiente en la cuenta del remitente."));
    }

    @Test
    @DisplayName("GET /api/transactions/{accountNumber} devuelve las transacciones de la cuenta")
    void getTransactionsByAccount_returnsList() throws Exception {
        when(transactionService.getTransactionsForAccount("1001")).thenReturn(List.of(
                dto(1L, "1001", "2002", 10.0),
                dto(2L, "2002", "1001", 20.0)));

        mockMvc.perform(get("/api/transactions/1001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].amount").value(20.0));
    }

    @Test
    @DisplayName("GET /api/transactions/{accountNumber} devuelve lista vacia si no hay movimientos")
    void getTransactionsByAccount_whenNone_returnsEmptyArray() throws Exception {
        when(transactionService.getTransactionsForAccount("9999")).thenReturn(List.of());

        mockMvc.perform(get("/api/transactions/9999"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }
}
