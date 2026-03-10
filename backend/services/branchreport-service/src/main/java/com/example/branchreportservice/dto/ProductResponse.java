package com.example.branchreportservice.dto;

import java.time.LocalDateTime;
import java.util.List;

public class ProductResponse {

    private Long id;
    private String name;
    private String description;
    private Boolean active;
    private List<ProductTypeResponse> productTypes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ProductResponse() {
    }

    public ProductResponse(Long id, String name, String description, Boolean active,
            List<ProductTypeResponse> productTypes, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.active = active;
        this.productTypes = productTypes;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public List<ProductTypeResponse> getProductTypes() {
        return productTypes;
    }

    public void setProductTypes(List<ProductTypeResponse> productTypes) {
        this.productTypes = productTypes;
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
}
