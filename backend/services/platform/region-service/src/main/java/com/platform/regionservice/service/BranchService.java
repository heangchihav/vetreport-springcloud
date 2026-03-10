package com.platform.regionservice.service;

import com.platform.regionservice.exception.ResourceNotFoundException;
import com.platform.regionservice.exception.ResourceAlreadyExistsException;
import com.platform.regionservice.model.Branch;
import com.platform.regionservice.repository.BranchRepository;
import com.platform.regionservice.repository.AreaRepository;
import com.platform.regionservice.repository.SubAreaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class BranchService {
    
    private final BranchRepository branchRepository;
    private final AreaRepository areaRepository;
    private final SubAreaRepository subAreaRepository;
    
    public BranchService(BranchRepository branchRepository, AreaRepository areaRepository, SubAreaRepository subAreaRepository) {
        this.branchRepository = branchRepository;
        this.areaRepository = areaRepository;
        this.subAreaRepository = subAreaRepository;
    }
    
    @Transactional(readOnly = true)
    public List<Branch> getAllBranches() {
        return branchRepository.findAll();
    }
    
    @Transactional(readOnly = true)
    public Branch getBranchById(UUID id) {
        return branchRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Branch not found with id: " + id));
    }
    
    @Transactional(readOnly = true)
    public List<Branch> getBranchesByAreaId(UUID areaId) {
        if (!areaRepository.existsById(areaId)) {
            throw new ResourceNotFoundException("Area not found with id: " + areaId);
        }
        return branchRepository.findByAreaId(areaId);
    }
    
    @Transactional(readOnly = true)
    public List<Branch> getBranchesBySubAreaId(UUID subAreaId) {
        if (!subAreaRepository.existsById(subAreaId)) {
            throw new ResourceNotFoundException("SubArea not found with id: " + subAreaId);
        }
        return branchRepository.findBySubAreaId(subAreaId);
    }
    
    @Transactional(readOnly = true)
    public List<Branch> getBranchesByAreaAndSubArea(UUID areaId, UUID subAreaId) {
        if (!areaRepository.existsById(areaId)) {
            throw new ResourceNotFoundException("Area not found with id: " + areaId);
        }
        if (!subAreaRepository.existsById(subAreaId)) {
            throw new ResourceNotFoundException("SubArea not found with id: " + subAreaId);
        }
        return branchRepository.findByAreaIdAndSubAreaId(areaId, subAreaId);
    }
    
    @Transactional(readOnly = true)
    public List<Branch> searchBranchesByName(String name) {
        return branchRepository.findByNameContainingIgnoreCase(name);
    }
    
    @Transactional(readOnly = true)
    public List<Branch> searchBranchesByAreaAndName(UUID areaId, String name) {
        if (!areaRepository.existsById(areaId)) {
            throw new ResourceNotFoundException("Area not found with id: " + areaId);
        }
        return branchRepository.findByAreaIdAndNameContainingIgnoreCase(areaId, name);
    }
    
    @Transactional(readOnly = true)
    public List<Branch> searchBranchesBySubAreaAndName(UUID subAreaId, String name) {
        if (!subAreaRepository.existsById(subAreaId)) {
            throw new ResourceNotFoundException("SubArea not found with id: " + subAreaId);
        }
        return branchRepository.findBySubAreaIdAndNameContainingIgnoreCase(subAreaId, name);
    }
    
    public Branch createBranch(String name, String description, UUID areaId, UUID subAreaId) {
        if (!areaRepository.existsById(areaId)) {
            throw new ResourceNotFoundException("Area not found with id: " + areaId);
        }
        if (!subAreaRepository.existsById(subAreaId)) {
            throw new ResourceNotFoundException("SubArea not found with id: " + subAreaId);
        }
        
        if (branchRepository.existsByNameIgnoreCaseAndAreaIdAndSubAreaId(name, areaId, subAreaId)) {
            throw new ResourceAlreadyExistsException("Branch already exists with name: " + name + " in area: " + areaId + " and sub-area: " + subAreaId);
        }
        
        Branch branch = new Branch(name, description, areaId, subAreaId);
        return branchRepository.save(branch);
    }
    
    public Branch updateBranch(UUID id, String name, String description, UUID areaId, UUID subAreaId) {
        Branch existingBranch = getBranchById(id);
        
        if (!areaRepository.existsById(areaId)) {
            throw new ResourceNotFoundException("Area not found with id: " + areaId);
        }
        if (!subAreaRepository.existsById(subAreaId)) {
            throw new ResourceNotFoundException("SubArea not found with id: " + subAreaId);
        }
        
        if ((!existingBranch.getName().equalsIgnoreCase(name) || 
             !existingBranch.getAreaId().equals(areaId) || 
             !existingBranch.getSubAreaId().equals(subAreaId)) &&
            branchRepository.existsByNameIgnoreCaseAndAreaIdAndSubAreaId(name, areaId, subAreaId)) {
            throw new ResourceAlreadyExistsException("Branch already exists with name: " + name + " in area: " + areaId + " and sub-area: " + subAreaId);
        }
        
        existingBranch.setName(name);
        existingBranch.setDescription(description);
        existingBranch.setAreaId(areaId);
        existingBranch.setSubAreaId(subAreaId);
        
        return branchRepository.save(existingBranch);
    }
    
    public void deleteBranch(UUID id) {
        Branch branch = getBranchById(id);
        branchRepository.delete(branch);
    }
    
    @Transactional(readOnly = true)
    public boolean branchExists(UUID id) {
        return branchRepository.existsById(id);
    }
    
    @Transactional(readOnly = true)
    public boolean branchExistsByNameAndAreaAndSubArea(String name, UUID areaId, UUID subAreaId) {
        return branchRepository.existsByNameIgnoreCaseAndAreaIdAndSubAreaId(name, areaId, subAreaId);
    }
}
