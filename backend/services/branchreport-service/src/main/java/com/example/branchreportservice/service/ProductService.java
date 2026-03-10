package com.example.branchreportservice.service;

import com.example.branchreportservice.dto.ProductRequest;
import com.example.branchreportservice.dto.ProductResponse;
import com.example.branchreportservice.dto.ProductTypeRequest;
import com.example.branchreportservice.dto.ProductTypeResponse;
import com.example.branchreportservice.entity.Product;
import com.example.branchreportservice.entity.ProductType;
import com.example.branchreportservice.repository.ProductRepository;
import com.example.branchreportservice.repository.ProductTypeRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductTypeRepository productTypeRepository;

    public ProductService(ProductRepository productRepository, ProductTypeRepository productTypeRepository) {
        this.productRepository = productRepository;
        this.productTypeRepository = productTypeRepository;
    }

    public ProductResponse createProduct(ProductRequest request) {
        Product product = new Product();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setActive(request.getActive() != null ? request.getActive() : true);

        Product savedProduct = productRepository.save(product);

        if (request.getProductTypes() != null && !request.getProductTypes().isEmpty()) {
            for (ProductTypeRequest typeRequest : request.getProductTypes()) {
                ProductType productType = new ProductType();
                productType.setName(typeRequest.getName());
                productType.setSizeCode(typeRequest.getSizeCode());
                productType.setDimensions(typeRequest.getDimensions());
                productType.setPrice(typeRequest.getPrice());
                productType.setCostPrice(typeRequest.getCostPrice());
                productType.setWeight(typeRequest.getWeight());
                productType
                        .setStockQuantity(typeRequest.getStockQuantity() != null ? typeRequest.getStockQuantity() : 0);
                productType
                        .setMinStockLevel(typeRequest.getMinStockLevel() != null ? typeRequest.getMinStockLevel() : 0);
                productType.setActive(typeRequest.getActive() != null ? typeRequest.getActive() : true);
                productType.setProduct(savedProduct);

                productTypeRepository.save(productType);
            }
        }

        return convertToResponse(savedProduct);
    }

    public ProductResponse updateProduct(Long id, ProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + id));

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setActive(request.getActive() != null ? request.getActive() : true);

        Product savedProduct = productRepository.save(product);
        return convertToResponse(savedProduct);
    }

    public ProductResponse getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + id));
        return convertToResponse(product);
    }

    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public List<ProductResponse> searchProducts(String search, Boolean active) {
        if (search != null && !search.trim().isEmpty()) {
            return productRepository.findByNameContainingIgnoreCase(search).stream()
                    .filter(product -> active == null || product.getActive().equals(active))
                    .map(this::convertToResponse)
                    .collect(Collectors.toList());
        } else {
            if (active != null) {
                return productRepository.findByActive(active).stream()
                        .map(this::convertToResponse)
                        .collect(Collectors.toList());
            } else {
                return productRepository.findAll().stream()
                        .map(this::convertToResponse)
                        .collect(Collectors.toList());
            }
        }
    }


    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + id));

        productRepository.delete(product);
    }

    public ProductResponse deactivateProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + id));

        product.setActive(false);
        Product savedProduct = productRepository.save(product);
        return convertToResponse(savedProduct);
    }

    private ProductResponse convertToResponse(Product product) {
        List<ProductTypeResponse> productTypeResponses = product.getProductTypes().stream()
                .map(this::convertToTypeResponse)
                .collect(Collectors.toList());

        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getActive(),
                productTypeResponses,
                product.getCreatedAt(),
                product.getUpdatedAt());
    }

    private ProductTypeResponse convertToTypeResponse(ProductType productType) {
        return new ProductTypeResponse(
                productType.getId(),
                productType.getName(),
                productType.getSizeCode(),
                productType.getDimensions(),
                productType.getPrice(),
                productType.getCostPrice(),
                productType.getWeight(),
                productType.getStockQuantity(),
                productType.getMinStockLevel(),
                productType.getActive(),
                productType.getProduct().getId(),
                productType.getProduct().getName(),
                productType.getCreatedAt(),
                productType.getUpdatedAt(),
                productType.isLowStock());
    }
}
