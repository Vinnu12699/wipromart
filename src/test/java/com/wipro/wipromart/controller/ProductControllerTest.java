package com.wipro.wipromart.controller;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
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
import com.wipro.wipromart.entity.Product;
import com.wipro.wipromart.service.ProductService;

@WebMvcTest(ProductController.class)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductService productService;

    @Test
    public void testAddProduct() throws Exception {
    	
    	Product product = new Product();
		product.setProductId(200);
		product.setProductName("Dell");
		product.setProductPrice(1000);
		product.setMfd(LocalDate.of(2024, 10, 10));
		product.setCategory("Laptop");

        when(productService.saveProduct(any(Product.class))).thenReturn(product);

        mockMvc.perform(post("/product/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.productName").value("Dell"))
                .andExpect(jsonPath("$.productPrice").value(1000.0));

        ArgumentCaptor<Product> productCaptor = ArgumentCaptor.forClass(Product.class);
        verify(productService).saveProduct(productCaptor.capture());
        Product capturedProduct = productCaptor.getValue();

        assertEquals("Dell", capturedProduct.getProductName());
        assertEquals(1000.0, capturedProduct.getProductPrice(), 0.01);
        assertEquals(LocalDate.of(2024, 10, 10), capturedProduct.getMfd());
        assertEquals("Laptop", capturedProduct.getCategory());
    }

    @Test
    public void testFetchProductById() throws Exception {
        
    	Product product = new Product();
		product.setProductId(200);
		product.setProductName("Dell");
		product.setProductPrice(1000);
		product.setMfd(LocalDate.of(2024, 10, 10));
		product.setCategory("Laptop");

        when(productService.getProductById(1L)).thenReturn(product);

        mockMvc.perform(get("/product/get/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productName").value("Dell"))
                .andExpect(jsonPath("$.productPrice").value(1000.0));

        verify(productService).getProductById(1L);
    }

    @Test
    public void testFetchAllProducts() throws Exception {
        
        Product product1 = new Product();
        product1.setProductId(1L);
        product1.setProductName("Dell");
        product1.setProductPrice(1000.0);
        product1.setMfd(LocalDate.of(2024, 10, 10));
		product1.setCategory("Laptop");

        Product product2 = new Product();
        product2.setProductId(2L);
        product2.setProductName("Iphone");
        product2.setProductPrice(500.0);
        product2.setMfd(LocalDate.of(2024, 10, 10));
		product2.setCategory("Mobile");

        List<Product> products = Arrays.asList(product1, product2);

        when(productService.getAllProducts()).thenReturn(products);

        mockMvc.perform(get("/product/get/all")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].productName").value("Dell"))
                .andExpect(jsonPath("$[1].productName").value("Iphone"));

        verify(productService).getAllProducts();
    }
}
