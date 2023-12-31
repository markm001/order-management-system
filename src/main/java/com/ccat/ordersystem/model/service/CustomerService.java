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

    /**
     * Creates a Customer Entity from a Request Object and saves it
     * @param request CusterCreateRequest Object
     * @return CustomerResponse Object
     */
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

    /**
     * Retrieves a Customer Entity by its Id
     * @param customerId Customer Primary-Key
     * @return Customer Entity
     */
    public Customer getCustomerById(Long customerId) {
        return customerRepository
                .findById(customerId)
                .orElseThrow(() ->
                        new InvalidIdException("Customer with ID:%d was not found.", customerId)
                );
    }

    /**
     * Maps a Customer Entity to a Response Object
     * @param customer Customer Entity to be mapped
     * @return Customer Response Object
     */
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
