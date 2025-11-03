package com.myportfolio.product_api.controller;

import com.myportfolio.product_api.dto.ProductRequestDTO;
import com.myportfolio.product_api.dto.ProductResponseDTO;
import com.myportfolio.product_api.entity.Product;
import com.myportfolio.product_api.mapper.ProductMapper;
import com.myportfolio.product_api.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;
    private final ProductMapper productMapper;

    public ProductController(ProductService productService, ProductMapper productMapper) {
        this.productService = productService;
        this.productMapper = productMapper;
    }

    @PostMapping
    public ResponseEntity<ProductResponseDTO> createProduct(@Valid @RequestBody ProductRequestDTO productRequestDTO) {
        Product productToSave = productMapper.convertToEntity(productRequestDTO);
        Product savedProduct = productService.createProduct(productToSave);
        ProductResponseDTO responseDTO = productMapper.convertToResponseDT0(savedProduct);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> getProductById(@PathVariable Long id) {
        Product product = productService.getProductById(id);
        ProductResponseDTO responseDTO = productMapper.convertToResponseDT0(product);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping
    public ResponseEntity<List<ProductResponseDTO>> getAllProduct() {
        List<Product> products = productService.getAllProducts();
        List<ProductResponseDTO> responseDTOs = products.stream()
                .map(productMapper::convertToResponseDT0)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseDTOs);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductRequestDTO productRequestDTO) {
        Product product = productMapper.convertToEntity(productRequestDTO);
        Product updateProduct = productService.updateProduct(id, product);
        ProductResponseDTO responseDTO = productMapper.convertToResponseDT0(updateProduct);
        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
