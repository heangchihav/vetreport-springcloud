package com.platform.regionservice.controller;

import com.platform.regionservice.model.Area;
import com.platform.regionservice.model.SubArea;
import com.platform.regionservice.model.Branch;
import com.platform.regionservice.service.AreaService;
import com.platform.regionservice.service.SubAreaService;
import com.platform.regionservice.service.BranchService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/region")
public class RegionController {

    private final AreaService areaService;
    private final SubAreaService subAreaService;
    private final BranchService branchService;

    public RegionController(AreaService areaService, SubAreaService subAreaService, BranchService branchService) {
        this.areaService = areaService;
        this.subAreaService = subAreaService;
        this.branchService = branchService;
    }

    // Health endpoints
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Region Service is healthy");
    }

    @GetMapping("/actuator/health")
    public ResponseEntity<String> actuatorHealth() {
        return ResponseEntity.ok("{\"status\":\"UP\"}");
    }

    // Area endpoints
    @GetMapping("/areas")
    public ResponseEntity<List<Area>> listAreas() {
        List<Area> areas = areaService.getAllAreas();
        return ResponseEntity.ok(areas);
    }

    @PostMapping("/areas")
    public ResponseEntity<Area> createArea(@Valid @RequestBody CreateAreaRequest request) {
        Area area = areaService.createArea(request.getName(), request.getDescription());
        return ResponseEntity.status(HttpStatus.CREATED).body(area);
    }

    @GetMapping("/areas/{id}")
    public ResponseEntity<Area> getArea(@PathVariable UUID id) {
        Area area = areaService.getAreaById(id);
        return ResponseEntity.ok(area);
    }

    @PutMapping("/areas/{id}")
    public ResponseEntity<Area> updateArea(@PathVariable UUID id, @Valid @RequestBody UpdateAreaRequest request) {
        Area area = areaService.updateArea(id, request.getName(), request.getDescription());
        return ResponseEntity.ok(area);
    }

    @DeleteMapping("/areas/{id}")
    public ResponseEntity<Void> deleteArea(@PathVariable UUID id) {
        areaService.deleteArea(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/areas/hierarchy")
    public ResponseEntity<List<AreaWithHierarchy>> getAreasWithHierarchy() {
        List<Area> areas = areaService.getAllAreas();
        List<AreaWithHierarchy> areasWithHierarchy = areas.stream()
                .map(this::convertToAreaWithHierarchy)
                .toList();
        return ResponseEntity.ok(areasWithHierarchy);
    }

    // Sub-area endpoints
    @GetMapping("/sub-areas")
    public ResponseEntity<List<SubArea>> listSubAreas() {
        List<SubArea> subAreas = subAreaService.getAllSubAreas();
        return ResponseEntity.ok(subAreas);
    }

    @PostMapping("/sub-areas")
    public ResponseEntity<SubArea> createSubArea(@Valid @RequestBody CreateSubAreaRequest request) {
        SubArea subArea = subAreaService.createSubArea(request.getName(), request.getDescription(),
                request.getAreaId());
        return ResponseEntity.status(HttpStatus.CREATED).body(subArea);
    }

    @GetMapping("/sub-areas/{id}")
    public ResponseEntity<SubArea> getSubArea(@PathVariable UUID id) {
        SubArea subArea = subAreaService.getSubAreaById(id);
        return ResponseEntity.ok(subArea);
    }

    @PutMapping("/sub-areas/{id}")
    public ResponseEntity<SubArea> updateSubArea(@PathVariable UUID id,
            @Valid @RequestBody UpdateSubAreaRequest request) {
        SubArea subArea = subAreaService.updateSubArea(id, request.getName(), request.getDescription(),
                request.getAreaId());
        return ResponseEntity.ok(subArea);
    }

    @DeleteMapping("/sub-areas/{id}")
    public ResponseEntity<Void> deleteSubArea(@PathVariable UUID id) {
        subAreaService.deleteSubArea(id);
        return ResponseEntity.noContent().build();
    }

    // Branch endpoints
    @GetMapping("/branches")
    public ResponseEntity<List<Branch>> listBranches() {
        List<Branch> branches = branchService.getAllBranches();
        return ResponseEntity.ok(branches);
    }

    @PostMapping("/branches")
    public ResponseEntity<Branch> createBranch(@Valid @RequestBody CreateBranchRequest request) {
        Branch branch = branchService.createBranch(request.getName(), request.getDescription(), request.getAreaId(),
                request.getSubAreaId());
        return ResponseEntity.status(HttpStatus.CREATED).body(branch);
    }

    @GetMapping("/branches/{id}")
    public ResponseEntity<Branch> getBranch(@PathVariable UUID id) {
        Branch branch = branchService.getBranchById(id);
        return ResponseEntity.ok(branch);
    }

    @PutMapping("/branches/{id}")
    public ResponseEntity<Branch> updateBranch(@PathVariable UUID id, @Valid @RequestBody UpdateBranchRequest request) {
        Branch branch = branchService.updateBranch(id, request.getName(), request.getDescription(), request.getAreaId(),
                request.getSubAreaId());
        return ResponseEntity.ok(branch);
    }

    @DeleteMapping("/branches/{id}")
    public ResponseEntity<Void> deleteBranch(@PathVariable UUID id) {
        branchService.deleteBranch(id);
        return ResponseEntity.noContent().build();
    }

    // Helper methods
    private AreaWithHierarchy convertToAreaWithHierarchy(Area area) {
        List<SubArea> subAreas = subAreaService.getSubAreasByAreaId(area.getId());
        List<SubAreaWithHierarchy> subAreasWithHierarchy = subAreas.stream()
                .map(subArea -> {
                    List<Branch> branches = branchService.getBranchesByAreaAndSubArea(area.getId(), subArea.getId());
                    List<BranchWithHierarchy> branchesWithHierarchy = branches.stream()
                            .map(branch -> new BranchWithHierarchy(
                                    branch.getId(),
                                    branch.getName(),
                                    branch.getDescription(),
                                    branch.getAreaId(),
                                    branch.getSubAreaId(),
                                    branch.getCreatedAt(),
                                    branch.getUpdatedAt(),
                                    null,
                                    null))
                            .toList();
                    return new SubAreaWithHierarchy(
                            subArea.getId(),
                            subArea.getName(),
                            subArea.getDescription(),
                            subArea.getAreaId(),
                            subArea.getCreatedAt(),
                            subArea.getUpdatedAt(),
                            null,
                            branchesWithHierarchy);
                })
                .toList();

        return new AreaWithHierarchy(
                area.getId(),
                area.getName(),
                area.getDescription(),
                area.getCreatedAt(),
                area.getUpdatedAt(),
                subAreasWithHierarchy);
    }

    // DTO classes
    public static class CreateAreaRequest {
        private String name;
        private String description;

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
    }

    public static class UpdateAreaRequest {
        private String name;
        private String description;

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
    }

    public static class CreateSubAreaRequest {
        private String name;
        private String description;
        private UUID areaId;

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
    }

    public static class UpdateSubAreaRequest {
        private String name;
        private String description;
        private UUID areaId;

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
    }

    public static class CreateBranchRequest {
        private String name;
        private String description;
        private UUID areaId;
        private UUID subAreaId;

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
    }

    public static class UpdateBranchRequest {
        private String name;
        private String description;
        private UUID areaId;
        private UUID subAreaId;

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
    }

    // Hierarchy DTO classes
    public static class AreaWithHierarchy {
        private UUID id;
        private String name;
        private String description;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private List<SubAreaWithHierarchy> subAreas;

        public AreaWithHierarchy(UUID id, String name, String description, LocalDateTime createdAt,
                LocalDateTime updatedAt, List<SubAreaWithHierarchy> subAreas) {
            this.id = id;
            this.name = name;
            this.description = description;
            this.createdAt = createdAt;
            this.updatedAt = updatedAt;
            this.subAreas = subAreas;
        }

        // Getters
        public UUID getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }

        public LocalDateTime getCreatedAt() {
            return createdAt;
        }

        public LocalDateTime getUpdatedAt() {
            return updatedAt;
        }

        public List<SubAreaWithHierarchy> getSubAreas() {
            return subAreas;
        }
    }

    public static class SubAreaWithHierarchy {
        private UUID id;
        private String name;
        private String description;
        private UUID areaId;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private Area area;
        private List<BranchWithHierarchy> branches;

        public SubAreaWithHierarchy(UUID id, String name, String description, UUID areaId, LocalDateTime createdAt,
                LocalDateTime updatedAt, Area area, List<BranchWithHierarchy> branches) {
            this.id = id;
            this.name = name;
            this.description = description;
            this.areaId = areaId;
            this.createdAt = createdAt;
            this.updatedAt = updatedAt;
            this.area = area;
            this.branches = branches;
        }

        // Getters
        public UUID getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }

        public UUID getAreaId() {
            return areaId;
        }

        public LocalDateTime getCreatedAt() {
            return createdAt;
        }

        public LocalDateTime getUpdatedAt() {
            return updatedAt;
        }

        public Area getArea() {
            return area;
        }

        public List<BranchWithHierarchy> getBranches() {
            return branches;
        }
    }

    public static class BranchWithHierarchy {
        private UUID id;
        private String name;
        private String description;
        private UUID areaId;
        private UUID subAreaId;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private Area area;
        private SubArea subArea;

        public BranchWithHierarchy(UUID id, String name, String description, UUID areaId, UUID subAreaId,
                LocalDateTime createdAt, LocalDateTime updatedAt, Area area, SubArea subArea) {
            this.id = id;
            this.name = name;
            this.description = description;
            this.areaId = areaId;
            this.subAreaId = subAreaId;
            this.createdAt = createdAt;
            this.updatedAt = updatedAt;
            this.area = area;
            this.subArea = subArea;
        }

        // Getters
        public UUID getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }

        public UUID getAreaId() {
            return areaId;
        }

        public UUID getSubAreaId() {
            return subAreaId;
        }

        public LocalDateTime getCreatedAt() {
            return createdAt;
        }

        public LocalDateTime getUpdatedAt() {
            return updatedAt;
        }

        public Area getArea() {
            return area;
        }

        public SubArea getSubArea() {
            return subArea;
        }
    }
}
