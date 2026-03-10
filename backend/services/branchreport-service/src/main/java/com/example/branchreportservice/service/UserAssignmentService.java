package com.example.branchreportservice.service;

import com.example.branchreportservice.entity.UserAssignment;
import com.example.branchreportservice.dto.CreateUserAssignmentRequest;
import com.example.branchreportservice.dto.UpdateUserAssignmentRequest;
import com.example.branchreportservice.repository.UserAssignmentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserAssignmentService {

    private final UserAssignmentRepository userAssignmentRepository;

    public UserAssignmentService(UserAssignmentRepository userAssignmentRepository) {
        this.userAssignmentRepository = userAssignmentRepository;
    }

    @Transactional
    public UserAssignment createUserAssignment(CreateUserAssignmentRequest request) {
        UserAssignment assignment = new UserAssignment(
            request.getUserId(),
            request.getAreaId(),
            request.getSubAreaId(),
            request.getBranchId()
        );
        
        // Set assignment type based on what's provided
        if (request.getBranchId() != null) {
            assignment.setAssignmentType(com.example.branchreportservice.enums.AssignmentType.BRANCH);
        } else if (request.getSubAreaId() != null) {
            assignment.setAssignmentType(com.example.branchreportservice.enums.AssignmentType.SUB_AREA);
        } else if (request.getAreaId() != null) {
            assignment.setAssignmentType(com.example.branchreportservice.enums.AssignmentType.AREA);
        }
        
        return userAssignmentRepository.save(assignment);
    }

    @Transactional
    public UserAssignment updateUserAssignment(Long assignmentId, UpdateUserAssignmentRequest request) {
        UserAssignment assignment = userAssignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new RuntimeException("User assignment not found with id: " + assignmentId));
        
        // Update fields
        if (request.getAreaId() != null) {
            assignment.setAreaId(request.getAreaId());
        }
        if (request.getSubAreaId() != null) {
            assignment.setSubAreaId(request.getSubAreaId());
        }
        if (request.getBranchId() != null) {
            assignment.setBranchId(request.getBranchId());
        }
        
        // Update assignment type based on what's provided
        if (request.getBranchId() != null) {
            assignment.setAssignmentType(com.example.branchreportservice.enums.AssignmentType.BRANCH);
        } else if (request.getSubAreaId() != null) {
            assignment.setAssignmentType(com.example.branchreportservice.enums.AssignmentType.SUB_AREA);
        } else if (request.getAreaId() != null) {
            assignment.setAssignmentType(com.example.branchreportservice.enums.AssignmentType.AREA);
        }
        
        return userAssignmentRepository.save(assignment);
    }

    @Transactional
    public void deleteUserAssignment(Long assignmentId) {
        userAssignmentRepository.deleteById(assignmentId);
    }

    public List<UserAssignment> getUserAssignments(String userId) {
        return userAssignmentRepository.findByUserIdOrderByAssignedAtDesc(userId);
    }

    public List<UserAssignment> getUserAssignmentsByType(String userId, String assignmentType) {
        return userAssignmentRepository.findByUserIdAndAssignmentTypeOrderByAssignedAtDesc(userId, assignmentType);
    }
}
