package com.laboratory.bank.service;

import com.laboratory.bank.dto.CustomerDTO;
import com.laboratory.bank.entity.Customer;
import com.laboratory.bank.exception.CustomerNotFoundException;
import com.laboratory.bank.mapper.CustomerMapper;
import com.laboratory.bank.repository.CustomerRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CustomerMapper customerMapper;

    @InjectMocks
    private CustomerService customerService;

    private Customer customer(Long id, String account) {
        return new Customer(id, account, "Ana", "Gomez", 100.0);
    }

    private CustomerDTO dto(Long id, String account) {
        CustomerDTO dto = new CustomerDTO();
        dto.setId(id);
        dto.setAccountNumber(account);
        dto.setFirstName("Ana");
        dto.setLastName("Gomez");
        dto.setBalance(100.0);
        return dto;
    }

    @Test
    @DisplayName("getAllCustomers mapea cada entidad a su DTO")
    void getAllCustomers_returnsMappedDtos() {
        Customer c1 = customer(1L, "111");
        Customer c2 = customer(2L, "222");
        CustomerDTO d1 = dto(1L, "111");
        CustomerDTO d2 = dto(2L, "222");
        when(customerRepository.findAll()).thenReturn(List.of(c1, c2));
        when(customerMapper.toDTO(c1)).thenReturn(d1);
        when(customerMapper.toDTO(c2)).thenReturn(d2);

        List<CustomerDTO> result = customerService.getAllCustomers();

        assertEquals(List.of(d1, d2), result);
    }

    @Test
    @DisplayName("getAllCustomers devuelve lista vacia si no hay clientes")
    void getAllCustomers_whenNone_returnsEmptyList() {
        when(customerRepository.findAll()).thenReturn(List.of());

        List<CustomerDTO> result = customerService.getAllCustomers();

        assertTrue(result.isEmpty());
        verifyNoInteractions(customerMapper);
    }

    @Test
    @DisplayName("getCustomerById devuelve el DTO cuando el cliente existe")
    void getCustomerById_whenFound_returnsDto() {
        Customer entity = customer(1L, "111");
        CustomerDTO expected = dto(1L, "111");
        when(customerRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(customerMapper.toDTO(entity)).thenReturn(expected);

        CustomerDTO result = customerService.getCustomerById(1L);

        assertSame(expected, result);
    }

    @Test
    @DisplayName("getCustomerById lanza excepcion cuando el cliente no existe")
    void getCustomerById_whenMissing_throws() {
        when(customerRepository.findById(99L)).thenReturn(Optional.empty());

        CustomerNotFoundException ex = assertThrows(CustomerNotFoundException.class, () -> customerService.getCustomerById(99L));

        assertEquals("Cliente no encontrado", ex.getMessage());
        verifyNoInteractions(customerMapper);
    }

    @Test
    @DisplayName("createCustomer convierte, guarda y devuelve el DTO persistido")
    void createCustomer_savesAndReturnsDto() {
        CustomerDTO input = dto(null, "111");
        Customer toSave = customer(null, "111");
        Customer saved = customer(1L, "111");
        CustomerDTO expected = dto(1L, "111");
        when(customerMapper.toEntity(input)).thenReturn(toSave);
        when(customerRepository.save(toSave)).thenReturn(saved);
        when(customerMapper.toDTO(saved)).thenReturn(expected);

        CustomerDTO result = customerService.createCustomer(input);

        assertSame(expected, result);
        verify(customerRepository).save(toSave);
    }
}
