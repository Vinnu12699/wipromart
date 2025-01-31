package com.wipro.wipromart.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wipro.wipromart.entity.Customer;
import com.wipro.wipromart.entity.Order;
import com.wipro.wipromart.entity.OrderItem;
import com.wipro.wipromart.entity.Product;
import com.wipro.wipromart.exception.ResourceNotFoundException;
import com.wipro.wipromart.repository.CustomerRepository;
import com.wipro.wipromart.repository.OrderItemRepository;
import com.wipro.wipromart.repository.OrderRepository;

@Service
public class OrderServiceImpl implements OrderService{

	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private OrderItemRepository orderItemRepository;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private CustomerService customerService;
	
	@Override
	public Order saveOrder(Order order) {
		long customerId = order.getCustomer().getCustomerId();
		
		Customer customer = customerService.getCustomerById(customerId);
		
		List<OrderItem> orderItemList = order.getOrderItems();
		
		double orderTotal = 0;
		for(OrderItem oi : orderItemList) {
			int qty = oi.getQty();
			//long productId = oi.getProduct().getProductId();
			long productId = oi.getProductId();
			
			Product product = productService.getProductById(productId);
			double productPrice = product.getProductPrice();
			
			double itemTotal = productPrice * qty;
			
			oi.setItemTotal(itemTotal);
			//oi.setProduct(product);
			
			orderTotal = orderTotal + itemTotal;
			
			oi.setOrder(order);
			
			//orderItemRepository.save(oi);(avoid this when you are using cascading
		}
		
		order.setOrderAmount(orderTotal);
		order.setOrderDate(LocalDate.now());
		order.setOrderStatus("Success");
		order.setCustomer(customer);
		order.setOrderItems(orderItemList);
		
		orderRepository.save(order);
		
		return order;
	}

	@Override
	public Order getOrderDetails(int orderId) {
		Optional<Order> optionalOrder = orderRepository.findById(orderId);
		if(optionalOrder.isEmpty()) {
			throw new ResourceNotFoundException("order not found");
		}
		Order order = optionalOrder.get();
		
		return order;
	}

	@Override
	public List<Order> getAllOrders() {
		return orderRepository.findAll();
	}
	

}
