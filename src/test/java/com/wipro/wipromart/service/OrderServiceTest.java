package com.wipro.wipromart.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.wipro.wipromart.entity.Customer;
import com.wipro.wipromart.entity.Order;
import com.wipro.wipromart.entity.OrderItem;
import com.wipro.wipromart.entity.Product;
import com.wipro.wipromart.exception.ResourceNotFoundException;
import com.wipro.wipromart.repository.OrderRepository;

@SpringBootTest
public class OrderServiceTest {
	
	@InjectMocks
	private OrderService orderService = new OrderServiceImpl();
	
	@Mock
	private OrderRepository orderRepository;
	
	@Mock
    private CustomerService customerService;

    @Mock
    private ProductService productService;

    @Test
	void testSaveOrder() {
		
		long customerId = 10L;
		long productId = 5L;
		
		Customer customer = new Customer();
        customer.setCustomerId(customerId);
        
        Product product = new Product();
        product.setProductId(productId);
        product.setProductPrice(100.0);
        
        OrderItem orderItem = new OrderItem();
        orderItem.setQty(2);
        orderItem.setProductId(productId);
        List<OrderItem> orderItemList = List.of(orderItem);
		
		Order order = new Order();
        order.setCustomer(customer);
        order.setOrderItems(orderItemList);
        
		order.setOrderId(10);
		order.setOrderAmount(200.0);
		order.setOrderStatus("Success");
		
		when(customerService.getCustomerById(customerId)).thenReturn(customer);
		when(productService.getProductById(productId)).thenReturn(product);
		
		Order saveOrder = orderService.saveOrder(order);
		
		assertNotNull(saveOrder);
        assertEquals(200.0, saveOrder.getOrderAmount());
        assertEquals("Success", saveOrder.getOrderStatus());
        assertNotNull(saveOrder.getOrderDate());
        assertEquals(customerId, saveOrder.getCustomer().getCustomerId());
        assertEquals(1, saveOrder.getOrderItems().size());
        assertEquals(200.0, saveOrder.getOrderItems().get(0).getItemTotal());
	}
    
    @Test
    public void testGetOrderDetails() {
        int orderId = 1;

        Order order = new Order();
        order.setOrderId(orderId);
        order.setOrderStatus("Success");

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        Order retrievedOrder = orderService.getOrderDetails(orderId);

        assertNotNull(retrievedOrder);
        assertEquals(orderId, retrievedOrder.getOrderId());
        assertEquals("Success", retrievedOrder.getOrderStatus());
    }
    
    @Test
	void testGetOrdeerDetailsWithException() {
		
		when(orderRepository.findById(20)).thenThrow(ResourceNotFoundException.class);
				
		assertThrows(ResourceNotFoundException.class, ()-> orderService.getOrderDetails(20));		
	}
    
    @Test
    public void testGetAllOrders() {
        
        List<Order> orders = new ArrayList<>();

        Order order1 = new Order();
        order1.setOrderId(1);
        order1.setOrderStatus("Success");
        order1.setOrderAmount(100.0);

        Order order2 = new Order();
        order2.setOrderId(2);
        order2.setOrderStatus("Pending");
        order2.setOrderAmount(200.0);

        orders.add(order1);
        orders.add(order2);

        when(orderRepository.findAll()).thenReturn(orders);

        List<Order> orderList = orderService.getAllOrders();

        assertNotNull(orderList);
        assertEquals(2, orderList.size());
        assertEquals(1, orderList.get(0).getOrderId());
        assertEquals("Success", orderList.get(0).getOrderStatus());
        assertEquals(2, orderList.get(1).getOrderId());
        assertEquals("Pending", orderList.get(1).getOrderStatus());
        assertEquals(orders.size(),orderList.size());

    }
}
