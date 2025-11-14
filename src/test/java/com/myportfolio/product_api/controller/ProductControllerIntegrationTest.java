package com.myportfolio.product_api.controller;

import com.myportfolio.product_api.dto.ProductRequestDTO;
import com.myportfolio.product_api.dto.ProductResponseDTO;
import com.myportfolio.product_api.entity.Product;
import com.myportfolio.product_api.repository.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ProductControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private ProductRequestDTO productRequest;

    @BeforeEach
    void setUp() {
        productRepository.deleteAll();

        productRequest = new ProductRequestDTO();
        productRequest.setName("Test Laptop");
        productRequest.setDescription("A laptop for testing.");
        productRequest.setPrice(new BigDecimal("999.99"));
        productRequest.setQuantityOfStock(10);
    }

    @Test
    public void createProduct_WhenValidInput_ShouldReturn201Created() throws Exception {

        String productRequestJson = objectMapper.writeValueAsString(productRequest);

        ResultActions resultActions = mockMvc.perform(
                post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productRequestJson)
        );

        resultActions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("Test Laptop"));

        assertThat(productRepository.findAll().size()).isEqualTo(1);
    }

    @Test
    public void createProduct_WhenInvalidInput_ShouldReturn400BadRequest() throws Exception {

        productRequest.setPrice(new BigDecimal("-100.00"));
        String badRequestJson = objectMapper.writeValueAsString(productRequest);

        ResultActions resultActions = mockMvc.perform(
                post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(badRequestJson)
        );

        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.price").value("Price must be positive"));
    }

    @Test
    public void getProductById_WhenProductExists_ShouldReturn200Ok() throws Exception {

        Product product = new Product(null, "Test Product", "Test", new BigDecimal("10.00"), 5);
        Product savedProduct = productRepository.save(product);
        Long savedId = savedProduct.getId();

        ResultActions resultActions = mockMvc.perform(
                get("/api/products/" + savedId)
        );

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(savedId))
                .andExpect(jsonPath("$.name").value("Test Product"));
    }

    @Test
    public void getProductById_WhenProductNotFound_ShouldReturn404NotFound() throws Exception {

        Long nonExistentId = 99L;

        ResultActions resultActions = mockMvc.perform(
                get("/api/products/" + nonExistentId)
        );

        resultActions
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Product not found with id: 99"));
    }

    @Test
    public void updateProduct_WhenProductExists_ShouldReturn200Ok() throws Exception {

        Product product = new Product(null, "Original Name", "Original desc", new BigDecimal("100.00"), 10);
        Product savedProduct = productRepository.save(product);
        Long savedId = savedProduct.getId();

        ProductResponseDTO updateRequest = new ProductResponseDTO();
        updateRequest.setName("Updated Laptop");
        updateRequest.setDescription("An updated description.");
        updateRequest.setPrice(new BigDecimal("1299.99"));
        updateRequest.setQuantityOfStock(50);

        String updatedRequestJson = objectMapper.writeValueAsString(updateRequest);

        ResultActions resultActions = mockMvc.perform(
                put("/api/products/" + savedId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedRequestJson)
        );

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(savedId))
                .andExpect(jsonPath("$.name").value("Updated Laptop"))
                .andExpect(jsonPath("$.price").value(1299.99));

        Product updatedProductFromDb = productRepository.findById(savedId).get();
        assertThat(updatedProductFromDb.getName()).isEqualTo("Updated Laptop");
        assertThat(updatedProductFromDb.getQuantityOfStock()).isEqualTo(50);
    }

    @Test
    public void deleteProduct_WhenProductExists_ShouldReturn204NoContent() throws Exception {

        Product product = new Product(null, "To Be Deleted", "Test", new BigDecimal("10.00"), 5);
        Product savedProduct = productRepository.save(product);
        Long savedId = savedProduct.getId();

        assertThat(productRepository.count()).isEqualTo(1);

        ResultActions resultActions = mockMvc.perform(
                delete("/api/products/" + savedId)
        );

        resultActions
                .andExpect(status().isNoContent());

        assertThat(productRepository.count()).isEqualTo(0);
        assertThat(productRepository.findById(savedId)).isEmpty();
    }
}