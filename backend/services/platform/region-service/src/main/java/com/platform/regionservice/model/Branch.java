package com.platform.regionservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.annotations.UuidGenerator;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "branches")
public class Branch {

    @Id
    @UuidGenerator
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "area_id", nullable = false)
    private UUID areaId;

    @Column(name = "sub_area_id", nullable = false)
    private UUID subAreaId;

    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "area_id", referencedColumnName = "id", insertable = false, updatable = false)
    @JsonIgnore
    private Area area;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sub_area_id", referencedColumnName = "id", insertable = false, updatable = false)
    @JsonIgnore
    private SubArea subArea;

    // Constructors
    public Branch() {
    }

    public Branch(String name, String description, UUID areaId, UUID subAreaId) {
        this.name = name;
        this.description = description;
        this.areaId = areaId;
        this.subAreaId = subAreaId;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
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

    public UUID getAreaId() {
        return areaId;
    }

    public void setAreaId(UUID areaId) {
        this.areaId = areaId;
    }

    public UUID getSubAreaId() {
        return subAreaId;
    }

    public void setSubAreaId(UUID subAreaId) {
        this.subAreaId = subAreaId;
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

    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    public SubArea getSubArea() {
        return subArea;
    }

    public void setSubArea(SubArea subArea) {
        this.subArea = subArea;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "Branch{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", areaId=" + areaId +
                ", subAreaId=" + subAreaId +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
