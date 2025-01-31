package com.wipro.wipromart.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wipro.wipromart.entity.Customer;
import com.wipro.wipromart.entity.Order;
import com.wipro.wipromart.entity.OrderItem;
import com.wipro.wipromart.service.OrderService;

@WebMvcTest(OrderController.class)
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    private OrderController orderController;

    @MockBean
    private OrderService orderService; 

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    void testAddOrder() throws Exception {
        Customer customer = new Customer();
        customer.setCustomerId(1L);

        OrderItem orderItem = new OrderItem();
        orderItem.setQty(2);
        orderItem.setProductId(101L);
        orderItem.setItemTotal(200.0);

        Order order = new Order();
        order.setOrderId(1);
        order.setCustomer(customer);
        order.setOrderItems(Arrays.asList(orderItem));
        order.setOrderAmount(200.0);
        order.setOrderStatus("Success");

        when(orderService.saveOrder(any(Order.class))).thenReturn(order);

        mockMvc.perform(post("/order/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(order)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.orderId").value(order.getOrderId()))
                .andExpect(jsonPath("$.orderAmount").value(order.getOrderAmount()))
                .andExpect(jsonPath("$.orderStatus").value(order.getOrderStatus()));

        verify(orderService, times(1)).saveOrder(any(Order.class));
    }

    @Test
    void testFetchOrder() throws Exception {
        Customer customer = new Customer();
        customer.setCustomerId(1L);

        OrderItem orderItem = new OrderItem();
        orderItem.setQty(2);
        orderItem.setProductId(101L);
        orderItem.setItemTotal(200.0);

        Order order = new Order();
        order.setOrderId(1);
        order.setCustomer(customer);
        order.setOrderItems(Arrays.asList(orderItem));
        order.setOrderAmount(200.0);
        order.setOrderStatus("Success");

        when(orderService.getOrderDetails(1)).thenReturn(order);

        mockMvc.perform(get("/order/get/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId").value(order.getOrderId()))
                .andExpect(jsonPath("$.orderAmount").value(order.getOrderAmount()))
                .andExpect(jsonPath("$.orderStatus").value(order.getOrderStatus()));

        verify(orderService, times(1)).getOrderDetails(1);
    }

    @Test
    void testFetchAllOrders() throws Exception {
        Customer customer1 = new Customer();
        customer1.setCustomerId(1L);

        OrderItem orderItem1 = new OrderItem();
        orderItem1.setQty(2);
        orderItem1.setProductId(101L);
        orderItem1.setItemTotal(200.0);

        Order order1 = new Order();
        order1.setOrderId(1);
        order1.setCustomer(customer1);
        order1.setOrderItems(Arrays.asList(orderItem1));
        order1.setOrderAmount(200.0);
        order1.setOrderStatus("Success");

        Customer customer2 = new Customer();
        customer2.setCustomerId(2L);

        OrderItem orderItem2 = new OrderItem();
        orderItem2.setQty(3);
        orderItem2.setProductId(102L);
        orderItem2.setItemTotal(300.0);

        Order order2 = new Order();
        order2.setOrderId(2);
        order2.setCustomer(customer2);
        order2.setOrderItems(Arrays.asList(orderItem2));
        order2.setOrderAmount(300.0);
        order2.setOrderStatus("Success");

        when(orderService.getAllOrders()).thenReturn(Arrays.asList(order1, order2));

        mockMvc.perform(get("/order/get/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].orderId").value(order1.getOrderId()))
                .andExpect(jsonPath("$[0].orderAmount").value(order1.getOrderAmount()))
                .andExpect(jsonPath("$[0].orderStatus").value(order1.getOrderStatus()))
                .andExpect(jsonPath("$[1].orderId").value(order2.getOrderId()))
                .andExpect(jsonPath("$[1].orderAmount").value(order2.getOrderAmount()))
                .andExpect(jsonPath("$[1].orderStatus").value(order2.getOrderStatus()));

        verify(orderService, times(1)).getAllOrders();
    }
}
