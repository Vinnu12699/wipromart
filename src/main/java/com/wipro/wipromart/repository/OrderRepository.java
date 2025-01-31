package com.wipro.wipromart.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wipro.wipromart.entity.Customer;
import com.wipro.wipromart.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Integer>{
	
}
