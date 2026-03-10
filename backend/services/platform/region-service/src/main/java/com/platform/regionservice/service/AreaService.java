package com.platform.regionservice.service;

import com.platform.regionservice.exception.ResourceNotFoundException;
import com.platform.regionservice.exception.ResourceAlreadyExistsException;
import com.platform.regionservice.model.Area;
import com.platform.regionservice.repository.AreaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class AreaService {
    
    private final AreaRepository areaRepository;
    
    public AreaService(AreaRepository areaRepository) {
        this.areaRepository = areaRepository;
    }
    
    @Transactional(readOnly = true)
    public List<Area> getAllAreas() {
        return areaRepository.findAll();
    }
    
    @Transactional(readOnly = true)
    public Area getAreaById(UUID id) {
        return areaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Area not found with id: " + id));
    }
    
    @Transactional(readOnly = true)
    public List<Area> searchAreasByName(String name) {
        return areaRepository.findByNameContainingIgnoreCase(name);
    }
    
    public Area createArea(String name, String description) {
        if (areaRepository.existsByNameIgnoreCase(name)) {
            throw new ResourceAlreadyExistsException("Area already exists with name: " + name);
        }
        
        Area area = new Area(name, description);
        return areaRepository.save(area);
    }
    
    public Area updateArea(UUID id, String name, String description) {
        Area existingArea = getAreaById(id);
        
        if (!existingArea.getName().equalsIgnoreCase(name) && 
            areaRepository.existsByNameIgnoreCase(name)) {
            throw new ResourceAlreadyExistsException("Area already exists with name: " + name);
        }
        
        existingArea.setName(name);
        existingArea.setDescription(description);
        
        return areaRepository.save(existingArea);
    }
    
    public void deleteArea(UUID id) {
        Area area = getAreaById(id);
        areaRepository.delete(area);
    }
    
    @Transactional(readOnly = true)
    public boolean areaExists(UUID id) {
        return areaRepository.existsById(id);
    }
    
    @Transactional(readOnly = true)
    public boolean areaExistsByName(String name) {
        return areaRepository.existsByNameIgnoreCase(name);
    }
}
