package com.example.branchreportservice.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class ProductTypeRequest {
    
    @NotBlank(message = "Type name is required")
    private String name;
    
    private String sizeCode;
    
    private String dimensions;
    
    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    private BigDecimal price;
    
    private BigDecimal costPrice;
    
    private Double weight;
    
    private Integer stockQuantity;
    
    private Integer minStockLevel;
    
    private Boolean active;
    
    public ProductTypeRequest() {}
    
    public ProductTypeRequest(String name, String sizeCode, String dimensions, BigDecimal price, 
                             BigDecimal costPrice, Double weight, Integer stockQuantity, Integer minStockLevel, Boolean active) {
        this.name = name;
        this.sizeCode = sizeCode;
        this.dimensions = dimensions;
        this.price = price;
        this.costPrice = costPrice;
        this.weight = weight;
        this.stockQuantity = stockQuantity;
        this.minStockLevel = minStockLevel;
        this.active = active;
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
}
