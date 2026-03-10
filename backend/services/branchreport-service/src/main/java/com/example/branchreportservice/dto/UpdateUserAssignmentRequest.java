package com.example.branchreportservice.dto;

public class UpdateUserAssignmentRequest {

    private String areaId;
    private String subAreaId;
    private String branchId;

    // Constructors
    public UpdateUserAssignmentRequest() {}

    public UpdateUserAssignmentRequest(String areaId, String subAreaId, String branchId) {
        this.areaId = areaId;
        this.subAreaId = subAreaId;
        this.branchId = branchId;
    }

    // Getters and Setters
    public String getAreaId() { return areaId; }
    public void setAreaId(String areaId) { this.areaId = areaId; }

    public String getSubAreaId() { return subAreaId; }
    public void setSubAreaId(String subAreaId) { this.subAreaId = subAreaId; }

    public String getBranchId() { return branchId; }
    public void setBranchId(String branchId) { this.branchId = branchId; }
}
