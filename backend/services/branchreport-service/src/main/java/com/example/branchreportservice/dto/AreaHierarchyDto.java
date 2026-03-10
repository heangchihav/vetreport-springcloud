package com.example.branchreportservice.dto;

import java.util.List;

public class AreaHierarchyDto {
    private String id;
    private String name;
    private String description;
    private String created_at;
    private String updated_at;
    private List<SubAreaHierarchyDto> sub_areas;

    // Constructors
    public AreaHierarchyDto() {}

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

    public List<SubAreaHierarchyDto> getSub_areas() {
        return sub_areas;
    }

    public void setSub_areas(List<SubAreaHierarchyDto> sub_areas) {
        this.sub_areas = sub_areas;
    }
}
