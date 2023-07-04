package com.ccat.ordersystem.model.service;

import com.ccat.ordersystem.model.entity.Customer;
import com.ccat.ordersystem.model.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class CustomerServiceTest {
    private CustomerRepository customerRepository;
    private CustomerService customerService;

    @BeforeEach
    void setUp() {
        this.customerRepository = mock(CustomerRepository.class);
        this.customerService = new CustomerService(customerRepository);
    }

    @Test
    void getCustomerByInvalidId_throwsException() {
        //given
        Long customerId = 1L;
        given(customerRepository.findById(customerId)).willReturn(Optional.empty());

        //then
        assertThrows(RuntimeException.class, () ->
                //when
                customerService.getCustomerById(customerId)
        );
    }

    @Test
    void getCustomerById_returnCustomerObject() {
        //given
        Customer customer = new Customer(10L, "", "", "", "");

        given(customerRepository.findById(customer.getId())).willReturn(Optional.of(customer));

        //when
        Customer customerById = customerService.getCustomerById(customer.getId());

        //then
        assertThat(customerById).isEqualTo(customer);
    }
}