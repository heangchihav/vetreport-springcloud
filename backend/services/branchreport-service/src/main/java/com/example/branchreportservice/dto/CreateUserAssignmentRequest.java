package com.example.branchreportservice.dto;

import jakarta.validation.constraints.NotBlank;

public class CreateUserAssignmentRequest {

    @NotBlank(message = "User ID is required")
    private String userId;

    private String areaId;
    private String subAreaId;
    private String branchId;

    // Constructors
    public CreateUserAssignmentRequest() {
    }

    public CreateUserAssignmentRequest(String userId, String areaId, String subAreaId, String branchId) {
        this.userId = userId;
        this.areaId = areaId;
        this.subAreaId = subAreaId;
        this.branchId = branchId;
    }

    // Getters and Setters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getSubAreaId() {
        return subAreaId;
    }

    public void setSubAreaId(String subAreaId) {
        this.subAreaId = subAreaId;
    }

    public String getBranchId() {
        return branchId;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }
}
