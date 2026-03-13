package com.example.branchreportservice.api.userassignment;

import com.example.branchreportservice.dto.CreateUserAssignmentRequest;
import com.example.branchreportservice.dto.UpdateUserAssignmentRequest;
import com.example.branchreportservice.entity.UserAssignment;
import com.example.branchreportservice.service.UserAssignmentService;
import com.example.branchreportservice.utils.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/branchreport/users")
public class UserAssignmentController {

    private final UserAssignmentService userAssignmentService;

    public UserAssignmentController(UserAssignmentService userAssignmentService) {
        this.userAssignmentService = userAssignmentService;
    }

    @GetMapping("/{userId}/assignments")
    public ResponseEntity<ApiResponse<List<UserAssignment>>> getUserAssignments(@PathVariable String userId) {
        List<UserAssignment> assignments = userAssignmentService.getUserAssignments(userId);
        return ResponseEntity.ok(ApiResponse.success(assignments));
    }

    @PostMapping("/user-assignments")
    public ResponseEntity<ApiResponse<UserAssignment>> createUserAssignment(
            @RequestBody CreateUserAssignmentRequest request) {
        UserAssignment assignment = userAssignmentService.createUserAssignment(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(assignment));
    }

    @PutMapping("/user-assignments/{id}")
    public ResponseEntity<ApiResponse<UserAssignment>> updateUserAssignment(
            @PathVariable Long id,
            @RequestBody UpdateUserAssignmentRequest request) {
        UserAssignment assignment = userAssignmentService.updateUserAssignment(id, request);
        return ResponseEntity.ok(ApiResponse.success(assignment));
    }

    @DeleteMapping("/user-assignments/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUserAssignment(@PathVariable Long id) {
        userAssignmentService.deleteUserAssignment(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
