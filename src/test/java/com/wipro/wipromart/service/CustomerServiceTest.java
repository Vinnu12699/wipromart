package com.wipro.wipromart.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.wipro.wipromart.entity.Customer;
import com.wipro.wipromart.exception.ResourceNotFoundException;
import com.wipro.wipromart.repository.CustomerRepository;

@SpringBootTest
public class CustomerServiceTest {

	@InjectMocks
	private CustomerService customerService = new CustomerServiceImpl();
	
	@Mock
	private CustomerRepository customerRepository;
	
	@Test
	void testGetCustomerById () {
		Customer customer = new Customer();
		customer.setCustomerId(20);
		customer.setFirstName("ram");
		customer.setLastName("charan");
		customer.setEmail("ram@gmail.com");
		customer.setMobile("9182359795");
		customer.setCity("hyd");
		
		Optional<Customer> optionalCustomer = Optional.of(customer);
		
		when(customerRepository.findById(20L)).thenReturn(optionalCustomer);
		
		Customer actualCustomer = customerService.getCustomerById(20);
		
		assertEquals("ram",actualCustomer.getFirstName());		
		assertEquals("9182359795",actualCustomer.getMobile());	
		
	}
	
	@Test
	void testGetCustomerByIdWithException() {
		
		when(customerRepository.findById(20L)).thenThrow(ResourceNotFoundException.class);
				
		assertThrows(ResourceNotFoundException.class, ()-> customerService.getCustomerById(20));		
	}
	
	@Test
	void testSaveCustomer() {
		
		Customer customer = new Customer();
		customer.setCustomerId(20);
		customer.setFirstName("ram");
		customer.setLastName("charan");
		customer.setEmail("ram@gmail.com");
		customer.setMobile("9182359795");
		customer.setCity("hyd");
		
		when(customerRepository.save(customer)).thenReturn(customer);
		
		Customer newCustomer = customerService.saveCustomer(customer);
		
		assertEquals(20,newCustomer.getCustomerId());
		assertEquals("ram",newCustomer.getFirstName());		
		assertEquals("charan",newCustomer.getLastName());
		assertEquals("hyd",newCustomer.getCity());
	}
	
	@Test
	void testGetAllCustomers() {
		
		Customer customer1 = new Customer();
		customer1.setCustomerId(20);
		customer1.setFirstName("ram");
		customer1.setLastName("charan");
		customer1.setEmail("ram@gmail.com");
		customer1.setMobile("9182359795");
		customer1.setCity("hyd");
		
		Customer customer2 = new Customer();
		customer2.setCustomerId(30);
		customer2.setFirstName("pawan");
		customer2.setLastName("kalyan");
		customer2.setEmail("pawan@gmail.com");
		customer2.setMobile("8734675632");
		customer2.setCity("goa");
		
		Customer customer3 = new Customer();
		customer3.setCustomerId(40);
		customer3.setFirstName("mahesh");
		customer3.setLastName("babu");
		customer3.setEmail("mahesh@gmail.com");
		customer3.setMobile("6732847633");
		customer3.setCity("pune");
		
		List<Customer> customers = new ArrayList<>();
		customers.add(customer1);
		customers.add(customer2);
		customers.add(customer3);
		
		when(customerRepository.findAll()).thenReturn(customers);
		
		List<Customer> customerList = customerService.getAllCustomers();
		
		assertEquals(customers.size(),customerList.size());		
		
	}
	
	@Test
	void testDeleteCustomer() {
		
		Customer customer1 = new Customer();
		customer1.setCustomerId(20);
		customer1.setFirstName("ram");
		customer1.setLastName("charan");
		customer1.setEmail("ram@gmail.com");
		customer1.setMobile("9182359795");
		customer1.setCity("hyd");
		
		Optional<Customer> optionalCustomer = Optional.of(customer1);
		
		when(customerRepository.findById(20L)).thenReturn(optionalCustomer);
		
		doNothing().when(customerRepository).delete(customer1);
		
		customerService.deleteCustomer(20);
		
		verify(customerRepository,times(1)).delete(customer1);
	}
}
