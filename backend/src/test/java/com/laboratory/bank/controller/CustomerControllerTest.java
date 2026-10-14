package com.laboratory.bank.controller;

import com.laboratory.bank.dto.CustomerDTO;
import com.laboratory.bank.exception.CustomerNotFoundException;
import com.laboratory.bank.exception.GlobalExceptionHandler;
import com.laboratory.bank.service.CustomerService;
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

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class CustomerControllerTest {

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private CustomerController customerController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(customerController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    private CustomerDTO dto(Long id, String account, Double balance) {
        CustomerDTO dto = new CustomerDTO();
        dto.setId(id);
        dto.setAccountNumber(account);
        dto.setFirstName("Ana");
        dto.setLastName("Gomez");
        dto.setBalance(balance);
        return dto;
    }

    @Test
    @DisplayName("GET /api/customers devuelve 200 con la lista de clientes")
    void getAllCustomers_returnsOkWithList() throws Exception {
        when(customerService.getAllCustomers()).thenReturn(List.of(dto(1L, "111", 100.0), dto(2L, "222", 50.0)));

        mockMvc.perform(get("/api/customers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].accountNumber").value("111"))
                .andExpect(jsonPath("$[1].balance").value(50.0));
    }

    @Test
    @DisplayName("GET /api/customers/{id} devuelve 200 con el cliente")
    void getCustomerById_returnsOk() throws Exception {
        when(customerService.getCustomerById(1L)).thenReturn(dto(1L, "111", 100.0));

        mockMvc.perform(get("/api/customers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("Ana"))
                .andExpect(jsonPath("$.lastName").value("Gomez"));
    }

    @Test
    @DisplayName("GET /api/customers/{id} devuelve 404 si el cliente no existe")
    void getCustomerById_whenMissing_returnsNotFound() throws Exception {
        when(customerService.getCustomerById(99L)).thenThrow(new CustomerNotFoundException("Cliente no encontrado"));

        mockMvc.perform(get("/api/customers/99"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Cliente no encontrado"));
    }

    @Test
    @DisplayName("POST /api/customers crea el cliente y devuelve 200")
    void createCustomer_returnsOk() throws Exception {
        when(customerService.createCustomer(any(CustomerDTO.class))).thenReturn(dto(1L, "111", 100.0));

        mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"firstName":"Ana","lastName":"Gomez","accountNumber":"111","balance":100.0}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.accountNumber").value("111"));

        verify(customerService).createCustomer(any(CustomerDTO.class));
    }

    @Test
    @DisplayName("POST /api/customers sin balance devuelve 400 y no llama al servicio")
    void createCustomer_withNullBalance_returnsBadRequest() throws Exception {
        mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\":\"Ana\",\"lastName\":\"Gomez\",\"accountNumber\":\"111\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Balance cannot be null"));

        verifyNoInteractions(customerService);
    }
}
