package com.example.branchreportservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class ProductRequest {

    @NotBlank(message = "Product name is required")
    private String name;

    private String description;

    @NotNull(message = "Active status is required")
    private Boolean active;

    private List<ProductTypeRequest> productTypes;

    public ProductRequest() {
    }

    public ProductRequest(String name, String description, Boolean active, List<ProductTypeRequest> productTypes) {
        this.name = name;
        this.description = description;
        this.active = active;
        this.productTypes = productTypes;
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

    public List<ProductTypeRequest> getProductTypes() {
        return productTypes;
    }

    public void setProductTypes(List<ProductTypeRequest> productTypes) {
        this.productTypes = productTypes;
    }
}
