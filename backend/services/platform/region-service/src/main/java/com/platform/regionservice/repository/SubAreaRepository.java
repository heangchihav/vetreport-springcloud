package com.platform.regionservice.repository;

import com.platform.regionservice.model.SubArea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SubAreaRepository extends JpaRepository<SubArea, UUID> {
    
    List<SubArea> findByAreaId(UUID areaId);
    
    List<SubArea> findByNameContainingIgnoreCase(String name);
    
    Optional<SubArea> findByNameIgnoreCaseAndAreaId(String name, UUID areaId);
    
    boolean existsByNameIgnoreCaseAndAreaId(String name, UUID areaId);
    
    List<SubArea> findByAreaIdAndNameContainingIgnoreCase(UUID areaId, String name);
}
