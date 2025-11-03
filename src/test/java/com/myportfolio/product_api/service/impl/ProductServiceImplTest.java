package com.myportfolio.product_api.service.impl;

import com.myportfolio.product_api.entity.Product;
import com.myportfolio.product_api.exception.ResourceNotFoundException;
import com.myportfolio.product_api.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {
    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    @Test
    void getProductById_WhenProductExists_ShouldReturnProduct() {

        // Arrange
        Product testProduct = new Product(1L, "Test Product", "A test", new BigDecimal("10.00"), 5);

        when (productRepository.findById(1L)).thenReturn(Optional.of(testProduct));

        // Act
        Product actualResult = productService.getProductById(1L);

        //Assert
        assertThat(actualResult).isNotNull();
        assertThat(actualResult.getId()).isEqualTo(1L);
        assertThat(actualResult.getName()).isEqualTo("Test Product");
        assertThat(actualResult.getDescription()).isEqualTo("A test");
        assertThat(actualResult.getPrice()).isEqualTo(new BigDecimal("10.00"));
        assertThat(actualResult.getQuantityOfStock()).isEqualTo(5);

        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    void getProductById_WhenProductNotFound_ShouldThrowResourceNotFoundException() {

        // Arrange
        long nonExistentId = 99L;

        when(productRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        // Act
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            productService.getProductById(nonExistentId);
        });

        // Assert
        assertThat(exception.getMessage()).isEqualTo("Product not found with id: " + nonExistentId);

        verify(productRepository, times(1)).findById(nonExistentId);
    }

    @Test
    void createProduct_WhenGivenValidDTO_ShouldSaveAndReturnResponseDTO() {

        // Arrange
        Product productToSave = new Product();
        productToSave.setName("New Product");
        productToSave.setDescription("A new test product");
        productToSave.setPrice(new BigDecimal("100.00"));
        productToSave.setQuantityOfStock(10);

        Product savedProduct = new Product(1L, "New Product", "A new test product", new BigDecimal("100.00"), 10);

        when(productRepository.save(productToSave)).thenReturn(savedProduct);

        // Act
        Product actualResponse  = productService.createProduct(productToSave);

        // Assert
        assertThat(actualResponse).isNotNull();
        assertThat(actualResponse.getId()).isEqualTo(1L);
        assertThat(actualResponse.getName()).isEqualTo("New Product");
        assertThat(actualResponse.getDescription()).isEqualTo("A new test product");
        assertThat(actualResponse.getPrice()).isEqualTo(new BigDecimal("100.00"));
        assertThat(actualResponse.getQuantityOfStock()).isEqualTo(10);

        verify(productRepository, times(1)).save(productToSave);
    }

    @Test
    void getAllProducts_WhenProductExists_ShouldReturnListOfProducts() {

        // Arrange
        List<Product> productList = List.of(
                new Product(1L, "Product 1", "First test product", new BigDecimal("100.00"), 10),
                new Product(2L, "Product 2", "Second test product", new BigDecimal("200.00"), 20)
        );

        when(productRepository.findAll()).thenReturn(productList);

        // Act
        List<Product> result = productService.getAllProducts();

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(2);
        assertThat(result.get(0).getName()).isEqualTo("Product 1");
        assertThat(result.get(1).getId()).isEqualTo(2L);

        verify(productRepository, times(1)).findAll();
    }

    @Test
    void updateProduct_WhenProductExists_ShouldReturnUpdatedProduct() {

        // Arrange
        Product existingProduct = new Product(1L, "Test Product", "A test product", new BigDecimal("50.00"), 50);
        Product updatedProduct = new Product(1L, "Updated Product", "An updated test product", new BigDecimal("100.00"), 20);

        when(productRepository.findById(1L)).thenReturn(Optional.of(existingProduct));
        when(productRepository.save(any(Product.class))).thenReturn(updatedProduct);

        // Act
        Product result = productService.updateProduct(1L, updatedProduct);

        // Assert
        assertThat(result.getName()).isEqualTo("Updated Product");
        assertThat(result.getDescription()).isEqualTo("An updated test product");
        assertThat(result.getQuantityOfStock()).isEqualTo(20);

        verify(productRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    public void deleteProduct_WhenProductExists_ShouldDeleteProduct() {

        // Arrange
        Product product = new Product(1L, "Test Product", "A test product", new BigDecimal("50.00"), 50);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        doNothing().when(productRepository).delete(product);

        // Act
        productService.deleteProduct(1L);

        // Assert & Verify
        verify(productRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).delete(product);
    }
}