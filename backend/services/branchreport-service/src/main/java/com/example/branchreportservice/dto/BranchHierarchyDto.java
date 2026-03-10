package com.example.branchreportservice.dto;

public class BranchHierarchyDto {
    private String id;
    private String name;
    private String description;
    private String area_id;
    private String sub_area_id;
    private String created_at;
    private String updated_at;
    private AreaDto area;
    private SubAreaDto sub_area;

    // Constructors
    public BranchHierarchyDto() {}

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public String getArea_id() {
        return area_id;
    }

    public void setArea_id(String area_id) {
        this.area_id = area_id;
    }

    public String getSub_area_id() {
        return sub_area_id;
    }

    public void setSub_area_id(String sub_area_id) {
        this.sub_area_id = sub_area_id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public AreaDto getArea() {
        return area;
    }

    public void setArea(AreaDto area) {
        this.area = area;
    }

    public SubAreaDto getSub_area() {
        return sub_area;
    }

    public void setSub_area(SubAreaDto sub_area) {
        this.sub_area = sub_area;
    }
}
