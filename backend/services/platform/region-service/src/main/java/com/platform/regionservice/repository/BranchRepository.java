package com.platform.regionservice.repository;

import com.platform.regionservice.model.Branch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BranchRepository extends JpaRepository<Branch, UUID> {
    
    List<Branch> findByAreaId(UUID areaId);
    
    List<Branch> findBySubAreaId(UUID subAreaId);
    
    List<Branch> findByAreaIdAndSubAreaId(UUID areaId, UUID subAreaId);
    
    List<Branch> findByNameContainingIgnoreCase(String name);
    
    Optional<Branch> findByNameIgnoreCaseAndAreaIdAndSubAreaId(String name, UUID areaId, UUID subAreaId);
    
    boolean existsByNameIgnoreCaseAndAreaIdAndSubAreaId(String name, UUID areaId, UUID subAreaId);
    
    @Query("SELECT b FROM Branch b WHERE b.areaId = :areaId AND b.name LIKE %:name%")
    List<Branch> findByAreaIdAndNameContainingIgnoreCase(@Param("areaId") UUID areaId, @Param("name") String name);
    
    @Query("SELECT b FROM Branch b WHERE b.subAreaId = :subAreaId AND b.name LIKE %:name%")
    List<Branch> findBySubAreaIdAndNameContainingIgnoreCase(@Param("subAreaId") UUID subAreaId, @Param("name") String name);
}
