package com.example.branchreportservice.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ProductTypeResponse {
    
    private Long id;
    private String name;
    private String sizeCode;
    private String dimensions;
    private BigDecimal price;
    private BigDecimal costPrice;
    private Double weight;
    private Integer stockQuantity;
    private Integer minStockLevel;
    private Boolean active;
    private Long productId;
    private String productName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean lowStock;
    
    public ProductTypeResponse() {}
    
    public ProductTypeResponse(Long id, String name, String sizeCode, String dimensions, BigDecimal price, 
                             BigDecimal costPrice, Double weight, Integer stockQuantity, Integer minStockLevel, 
                             Boolean active, Long productId, String productName, LocalDateTime createdAt, 
                             LocalDateTime updatedAt, Boolean lowStock) {
        this.id = id;
        this.name = name;
        this.sizeCode = sizeCode;
        this.dimensions = dimensions;
        this.price = price;
        this.costPrice = costPrice;
        this.weight = weight;
        this.stockQuantity = stockQuantity;
        this.minStockLevel = minStockLevel;
        this.active = active;
        this.productId = productId;
        this.productName = productName;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.lowStock = lowStock;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getSizeCode() {
        return sizeCode;
    }
    
    public void setSizeCode(String sizeCode) {
        this.sizeCode = sizeCode;
    }
    
    public String getDimensions() {
        return dimensions;
    }
    
    public void setDimensions(String dimensions) {
        this.dimensions = dimensions;
    }
    
    public BigDecimal getPrice() {
        return price;
    }
    
    public void setPrice(BigDecimal price) {
        this.price = price;
    }
    
    public BigDecimal getCostPrice() {
        return costPrice;
    }
    
    public void setCostPrice(BigDecimal costPrice) {
        this.costPrice = costPrice;
    }
    
    public Double getWeight() {
        return weight;
    }
    
    public void setWeight(Double weight) {
        this.weight = weight;
    }
    
    public Integer getStockQuantity() {
        return stockQuantity;
    }
    
    public void setStockQuantity(Integer stockQuantity) {
        this.stockQuantity = stockQuantity;
    }
    
    public Integer getMinStockLevel() {
        return minStockLevel;
    }
    
    public void setMinStockLevel(Integer minStockLevel) {
        this.minStockLevel = minStockLevel;
    }
    
    public Boolean getActive() {
        return active;
    }
    
    public void setActive(Boolean active) {
        this.active = active;
    }
    
    public Long getProductId() {
        return productId;
    }
    
    public void setProductId(Long productId) {
        this.productId = productId;
    }
    
    public String getProductName() {
        return productName;
    }
    
    public void setProductName(String productName) {
        this.productName = productName;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    public Boolean getLowStock() {
        return lowStock;
    }
    
    public void setLowStock(Boolean lowStock) {
        this.lowStock = lowStock;
    }
}
