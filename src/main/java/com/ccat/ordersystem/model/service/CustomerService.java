package com.ccat.ordersystem.model.service;

import com.ccat.ordersystem.exception.InvalidIdException;
import com.ccat.ordersystem.model.CustomerCreateRequest;
import com.ccat.ordersystem.model.CustomerResponse;
import com.ccat.ordersystem.model.entity.Customer;
import com.ccat.ordersystem.model.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public CustomerResponse createCustomer(CustomerCreateRequest request) {
        Customer response = customerRepository.save(new Customer(
                UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE,
                request.registrationCode(),
                request.fullName(),
                request.email(),
                request.telephone()
        ));
        return mapToCustomerResponse(response);
    }

    public Customer getCustomerById(Long customerId) {
        return customerRepository
                .findById(customerId)
                .orElseThrow(() ->
                        new InvalidIdException("Customer with ID:%d was not found.", customerId)
                );
    }

    private static CustomerResponse mapToCustomerResponse(Customer customer) {
        return new CustomerResponse(
                customer.getId(),
                customer.getRegistrationCode(),
                customer.getFullName(),
                customer.getEmail(),
                customer.getTelephone()
        );
    }
}
