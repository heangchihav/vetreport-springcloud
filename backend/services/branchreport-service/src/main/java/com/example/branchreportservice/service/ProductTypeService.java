package com.example.branchreportservice.service;

import com.example.branchreportservice.dto.ProductTypeRequest;
import com.example.branchreportservice.dto.ProductTypeResponse;
import com.example.branchreportservice.entity.Product;
import com.example.branchreportservice.entity.ProductType;
import com.example.branchreportservice.repository.ProductRepository;
import com.example.branchreportservice.repository.ProductTypeRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductTypeService {

    private final ProductTypeRepository productTypeRepository;
    private final ProductRepository productRepository;

    public ProductTypeService(ProductTypeRepository productTypeRepository, ProductRepository productRepository) {
        this.productTypeRepository = productTypeRepository;
        this.productRepository = productRepository;
    }

    public ProductTypeResponse createProductType(ProductTypeRequest request, Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + productId));

        if (productTypeRepository.existsByNameAndProductId(request.getName(), productId)) {
            throw new IllegalArgumentException(
                    "Product type with name '" + request.getName() + "' already exists for this product");
        }

        if (request.getSizeCode() != null
                && productTypeRepository.existsBySizeCodeAndProductId(request.getSizeCode(), productId)) {
            throw new IllegalArgumentException(
                    "Product type with size code '" + request.getSizeCode() + "' already exists for this product");
        }

        ProductType productType = new ProductType();
        productType.setName(request.getName());
        productType.setSizeCode(request.getSizeCode());
        productType.setDimensions(request.getDimensions());
        productType.setPrice(request.getPrice());
        productType.setCostPrice(request.getCostPrice());
        productType.setWeight(request.getWeight());
        productType.setStockQuantity(request.getStockQuantity() != null ? request.getStockQuantity() : 0);
        productType.setMinStockLevel(request.getMinStockLevel() != null ? request.getMinStockLevel() : 0);
        productType.setActive(request.getActive() != null ? request.getActive() : true);
        productType.setProduct(product);

        ProductType savedProductType = productTypeRepository.save(productType);
        return convertToResponse(savedProductType);
    }

    public ProductTypeResponse updateProductType(Long id, ProductTypeRequest request) {
        ProductType productType = productTypeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product type not found with id: " + id));

        if (!request.getName().equals(productType.getName())
                && productTypeRepository.existsByNameAndProductId(request.getName(),
                        productType.getProduct().getId())) {
            throw new IllegalArgumentException(
                    "Product type with name '" + request.getName() + "' already exists for this product");
        }

        if (request.getSizeCode() != null && !request.getSizeCode().equals(productType.getSizeCode())
                && productTypeRepository.existsBySizeCodeAndProductId(request.getSizeCode(),
                        productType.getProduct().getId())) {
            throw new IllegalArgumentException(
                    "Product type with size code '" + request.getSizeCode() + "' already exists for this product");
        }

        productType.setName(request.getName());
        productType.setSizeCode(request.getSizeCode());
        productType.setDimensions(request.getDimensions());
        productType.setPrice(request.getPrice());
        productType.setCostPrice(request.getCostPrice());
        productType.setWeight(request.getWeight());
        productType.setStockQuantity(request.getStockQuantity());
        productType.setMinStockLevel(request.getMinStockLevel());
        productType.setActive(request.getActive());

        ProductType savedProductType = productTypeRepository.save(productType);
        return convertToResponse(savedProductType);
    }

    public ProductTypeResponse getProductTypeById(Long id) {
        ProductType productType = productTypeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product type not found with id: " + id));
        return convertToResponse(productType);
    }

    public List<ProductTypeResponse> getAllProductTypes() {
        return productTypeRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public List<ProductTypeResponse> getProductTypesByProductId(Long productId) {
        return productTypeRepository.findByProductId(productId).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public List<ProductTypeResponse> getActiveProductTypesByProductId(Long productId) {
        return productTypeRepository.findByProductIdAndActive(productId, true).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public List<ProductTypeResponse> searchProductTypes(Long productId, String search, Boolean active) {
        if (search != null && !search.trim().isEmpty()) {
            return productTypeRepository.findByProductId(productId).stream()
                    .filter(pt -> pt.getName().toLowerCase().contains(search.toLowerCase()))
                    .filter(pt -> active == null || pt.getActive().equals(active))
                    .map(this::convertToResponse)
                    .collect(Collectors.toList());
        } else {
            return productTypeRepository.findByProductIdAndActive(productId, active != null ? active : true).stream()
                    .map(this::convertToResponse)
                    .collect(Collectors.toList());
        }
    }

    public List<ProductTypeResponse> getProductTypesByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        return productTypeRepository.findByPriceRange(minPrice, maxPrice, true).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public List<ProductTypeResponse> getLowStockItems() {
        return productTypeRepository.findLowStockItems().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public void deleteProductType(Long id) {
        ProductType productType = productTypeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product type not found with id: " + id));

        productTypeRepository.delete(productType);
    }

    public ProductTypeResponse deactivateProductType(Long id) {
        ProductType productType = productTypeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product type not found with id: " + id));

        productType.setActive(false);
        ProductType savedProductType = productTypeRepository.save(productType);
        return convertToResponse(savedProductType);
    }

    public ProductTypeResponse updateStock(Long id, Integer quantity) {
        ProductType productType = productTypeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product type not found with id: " + id));

        if (quantity < 0) {
            throw new IllegalArgumentException("Stock quantity cannot be negative");
        }

        productType.setStockQuantity(quantity);
        ProductType savedProductType = productTypeRepository.save(productType);
        return convertToResponse(savedProductType);
    }

    public ProductTypeResponse adjustStock(Long id, Integer adjustment) {
        ProductType productType = productTypeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product type not found with id: " + id));

        Integer newQuantity = productType.getStockQuantity() + adjustment;
        if (newQuantity < 0) {
            throw new IllegalArgumentException(
                    "Insufficient stock. Current: " + productType.getStockQuantity() + ", Adjustment: " + adjustment);
        }

        productType.setStockQuantity(newQuantity);
        ProductType savedProductType = productTypeRepository.save(productType);
        return convertToResponse(savedProductType);
    }

    private ProductTypeResponse convertToResponse(ProductType productType) {
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
