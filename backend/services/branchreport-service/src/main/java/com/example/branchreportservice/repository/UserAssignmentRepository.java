package com.example.branchreportservice.repository;

import com.example.branchreportservice.entity.UserAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserAssignmentRepository extends JpaRepository<UserAssignment, Long> {

    @Query("SELECT ua FROM UserAssignment ua WHERE ua.userId = :userId ORDER BY ua.assignedAt DESC")
    List<UserAssignment> findByUserIdOrderByAssignedAtDesc(String userId);

    @Query("SELECT ua FROM UserAssignment ua WHERE ua.userId = :userId AND ua.active = true ORDER BY ua.assignedAt DESC")
    List<UserAssignment> findByUserIdAndActiveOrderByAssignedAtDesc(String userId);

    @Query("SELECT ua FROM UserAssignment ua WHERE ua.userId = :userId AND ua.assignmentType = :assignmentType ORDER BY ua.assignedAt DESC")
    List<UserAssignment> findByUserIdAndAssignmentTypeOrderByAssignedAtDesc(String userId, String assignmentType);
}
