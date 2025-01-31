package com.wipro.wipromart.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wipro.wipromart.entity.Customer;
import com.wipro.wipromart.service.CustomerService;

@WebMvcTest(CustomerController.class)
public class CustomerControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private CustomerService customerService;

	@Test
	public void testAddCustomer() throws Exception {

		Customer customer = new Customer();
		customer.setCustomerId(20);
		customer.setFirstName("ram");
		customer.setLastName("charan");
		customer.setEmail("ram@gmail.com");
		customer.setMobile("9182359795");
		customer.setCity("hyd");

		when(customerService.saveCustomer(any(Customer.class))).thenReturn(customer);

		mockMvc.perform(post("/customer/save").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(customer))).andExpect(status().isCreated())
				.andExpect(jsonPath("$.firstName").value("ram")).andExpect(jsonPath("$.mobile").value("9182359795"));

		ArgumentCaptor<Customer> customerCaptor = ArgumentCaptor.forClass(Customer.class);
		verify(customerService).saveCustomer(customerCaptor.capture());
		Customer capturedCustomer = customerCaptor.getValue();

		assertEquals("ram", capturedCustomer.getFirstName());
		assertEquals("ram@gmail.com", capturedCustomer.getEmail());
		assertEquals("9182359795", capturedCustomer.getMobile());
		assertEquals("hyd", capturedCustomer.getCity());

	}

	@Test
	public void testFetchCustomerById() throws Exception {

		Customer customer = new Customer();
		customer.setCustomerId(1);
		customer.setFirstName("ram");
		customer.setLastName("charan");
		customer.setEmail("ram@gmail.com");
		customer.setMobile("9182359795");
		customer.setCity("hyd");

		when(customerService.getCustomerById(1L)).thenReturn(customer);

		mockMvc.perform(get("/customer/get/1").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.firstName").value("ram")).andExpect(jsonPath("$.mobile").value("9182359795"));

		verify(customerService).getCustomerById(1L);
	}

	@Test
	public void testFetchAllCustomers() throws Exception {

		Customer customer1 = new Customer();
		customer1.setCustomerId(1);
		customer1.setFirstName("ram");
		customer1.setLastName("charan");
		customer1.setEmail("ram@gmail.com");
		customer1.setMobile("9182359795");
		customer1.setCity("hyd");

		Customer customer2 = new Customer();
		customer2.setCustomerId(2);
		customer2.setFirstName("pawan");
		customer2.setLastName("kalyan");
		customer2.setEmail("pawan@gmail.com");
		customer2.setMobile("8782346753");
		customer2.setCity("goa");

		List<Customer> customers = Arrays.asList(customer1, customer2);

		when(customerService.getAllCustomers()).thenReturn(customers);

		mockMvc.perform(get("/customer/get/all").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.size()").value(2)).andExpect(jsonPath("$[0].firstName").value("ram"))
				.andExpect(jsonPath("$[1].firstName").value("pawan"));

		verify(customerService).getAllCustomers();
	}

}
