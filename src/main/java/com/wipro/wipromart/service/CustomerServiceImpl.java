package com.wipro.wipromart.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wipro.wipromart.entity.Customer;
import com.wipro.wipromart.entity.Product;
import com.wipro.wipromart.exception.ResourceNotFoundException;
import com.wipro.wipromart.repository.CustomerRepository;

@Service
public class CustomerServiceImpl implements CustomerService{

	@Autowired
	private CustomerRepository customerRepository;
	
	@Override
	public Customer saveCustomer(Customer customer) {
		customerRepository.save(customer);
		return customer;
	}

	@Override
	public Customer getCustomerById(long customerId) {
		Optional<Customer> optionalCustomer = customerRepository.findById(customerId);
		
		if(optionalCustomer.isEmpty()) {
			throw new ResourceNotFoundException("Customer not found with id : "+customerId);
		}
		
		Customer customer = optionalCustomer.get();
		return customer;
	}

	@Override
	public List<Customer> getAllCustomers() {
		return customerRepository.findAll();
	}

	@Override
	public void deleteCustomer(long customerId) {
		Optional<Customer> optionalCustomer = customerRepository.findById(customerId);
		
		if(optionalCustomer.isEmpty()) {
			throw new ResourceNotFoundException("Customer not found with id : "+customerId);
		}
		
		Customer customer = optionalCustomer.get();
		
		customerRepository.delete(customer);
	}

	@Override
	public Customer updateCustomer(Customer customer) {
		Optional<Customer> optionalCustomer = customerRepository.findById(customer.getCustomerId());
		
		if(optionalCustomer.isEmpty()) {
			throw new ResourceNotFoundException("Customer not found with id : "+customer.getCustomerId());
		}
		
		customer = customerRepository.save(customer);
		return customer;
	}

}
