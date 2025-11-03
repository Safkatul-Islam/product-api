package com.myportfolio.product_api.mapper;

import com.myportfolio.product_api.dto.ProductRequestDTO;
import com.myportfolio.product_api.dto.ProductResponseDTO;
import com.myportfolio.product_api.entity.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public Product convertToEntity(ProductRequestDTO requestDTO) {
        Product product = new Product();

        product.setName(requestDTO.getName());
        product.setDescription(requestDTO.getDescription());
        product.setPrice(requestDTO.getPrice());
        product.setQuantityOfStock(requestDTO.getQuantityOfStock());

        return product;
    }

    public ProductResponseDTO convertToResponseDT0(Product product) {
        ProductResponseDTO productResponseDTO = new ProductResponseDTO();

        productResponseDTO.setId(product.getId());
        productResponseDTO.setName(product.getName());
        productResponseDTO.setDescription(product.getDescription());
        productResponseDTO.setPrice(product.getPrice());
        productResponseDTO.setQuantityOfStock(product.getQuantityOfStock());

        return productResponseDTO;
    }
}
