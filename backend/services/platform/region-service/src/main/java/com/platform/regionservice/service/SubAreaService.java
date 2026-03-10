package com.platform.regionservice.service;

import com.platform.regionservice.exception.ResourceNotFoundException;
import com.platform.regionservice.exception.ResourceAlreadyExistsException;
import com.platform.regionservice.model.SubArea;
import com.platform.regionservice.repository.SubAreaRepository;
import com.platform.regionservice.repository.AreaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class SubAreaService {
    
    private final SubAreaRepository subAreaRepository;
    private final AreaRepository areaRepository;
    
    public SubAreaService(SubAreaRepository subAreaRepository, AreaRepository areaRepository) {
        this.subAreaRepository = subAreaRepository;
        this.areaRepository = areaRepository;
    }
    
    @Transactional(readOnly = true)
    public List<SubArea> getAllSubAreas() {
        return subAreaRepository.findAll();
    }
    
    @Transactional(readOnly = true)
    public SubArea getSubAreaById(UUID id) {
        return subAreaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SubArea not found with id: " + id));
    }
    
    @Transactional(readOnly = true)
    public List<SubArea> getSubAreasByAreaId(UUID areaId) {
        if (!areaRepository.existsById(areaId)) {
            throw new ResourceNotFoundException("Area not found with id: " + areaId);
        }
        return subAreaRepository.findByAreaId(areaId);
    }
    
    @Transactional(readOnly = true)
    public List<SubArea> searchSubAreasByName(String name) {
        return subAreaRepository.findByNameContainingIgnoreCase(name);
    }
    
    @Transactional(readOnly = true)
    public List<SubArea> searchSubAreasByAreaAndName(UUID areaId, String name) {
        if (!areaRepository.existsById(areaId)) {
            throw new ResourceNotFoundException("Area not found with id: " + areaId);
        }
        return subAreaRepository.findByAreaIdAndNameContainingIgnoreCase(areaId, name);
    }
    
    public SubArea createSubArea(String name, String description, UUID areaId) {
        if (!areaRepository.existsById(areaId)) {
            throw new ResourceNotFoundException("Area not found with id: " + areaId);
        }
        
        if (subAreaRepository.existsByNameIgnoreCaseAndAreaId(name, areaId)) {
            throw new ResourceAlreadyExistsException("SubArea already exists with name: " + name + " in area: " + areaId);
        }
        
        SubArea subArea = new SubArea(name, description, areaId);
        return subAreaRepository.save(subArea);
    }
    
    public SubArea updateSubArea(UUID id, String name, String description, UUID areaId) {
        SubArea existingSubArea = getSubAreaById(id);
        
        if (!areaRepository.existsById(areaId)) {
            throw new ResourceNotFoundException("Area not found with id: " + areaId);
        }
        
        if ((!existingSubArea.getName().equalsIgnoreCase(name) || !existingSubArea.getAreaId().equals(areaId)) &&
            subAreaRepository.existsByNameIgnoreCaseAndAreaId(name, areaId)) {
            throw new ResourceAlreadyExistsException("SubArea already exists with name: " + name + " in area: " + areaId);
        }
        
        existingSubArea.setName(name);
        existingSubArea.setDescription(description);
        existingSubArea.setAreaId(areaId);
        
        return subAreaRepository.save(existingSubArea);
    }
    
    public void deleteSubArea(UUID id) {
        SubArea subArea = getSubAreaById(id);
        subAreaRepository.delete(subArea);
    }
    
    @Transactional(readOnly = true)
    public boolean subAreaExists(UUID id) {
        return subAreaRepository.existsById(id);
    }
    
    @Transactional(readOnly = true)
    public boolean subAreaExistsByNameAndArea(String name, UUID areaId) {
        return subAreaRepository.existsByNameIgnoreCaseAndAreaId(name, areaId);
    }
}
