package com.laboratory.bank.mapper;

import com.laboratory.bank.dto.CustomerDTO;
import com.laboratory.bank.entity.Customer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class CustomerMapperTest {

    private final CustomerMapper mapper = CustomerMapper.INSTANCE;

    @Test
    @DisplayName("toDTO copia todos los campos de la entidad")
    void toDTO_mapsAllFields() {
        Customer customer = new Customer(1L, "111", "Ana", "Gomez", 150.5);

        CustomerDTO dto = mapper.toDTO(customer);

        assertEquals(1L, dto.getId());
        assertEquals("111", dto.getAccountNumber());
        assertEquals("Ana", dto.getFirstName());
        assertEquals("Gomez", dto.getLastName());
        assertEquals(150.5, dto.getBalance());
    }

    @Test
    @DisplayName("toEntity copia todos los campos del DTO")
    void toEntity_mapsAllFields() {
        CustomerDTO dto = new CustomerDTO();
        dto.setId(2L);
        dto.setAccountNumber("222");
        dto.setFirstName("Luis");
        dto.setLastName("Perez");
        dto.setBalance(75.0);

        Customer customer = mapper.toEntity(dto);

        assertEquals(2L, customer.getId());
        assertEquals("222", customer.getAccountNumber());
        assertEquals("Luis", customer.getFirstName());
        assertEquals("Perez", customer.getLastName());
        assertEquals(75.0, customer.getBalance());
    }

    @Test
    @DisplayName("los mapeos devuelven null cuando la entrada es null")
    void nullInput_returnsNull() {
        assertNull(mapper.toDTO(null));
        assertNull(mapper.toEntity(null));
    }
}
