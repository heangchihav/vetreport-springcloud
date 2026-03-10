package com.platform.regionservice.repository;

import com.platform.regionservice.model.Area;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AreaRepository extends JpaRepository<Area, UUID> {
    
    List<Area> findByNameContainingIgnoreCase(String name);
    
    Optional<Area> findByNameIgnoreCase(String name);
    
    boolean existsByNameIgnoreCase(String name);
}
