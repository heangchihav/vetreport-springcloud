package com.example.branchreportservice.service.shared;

import com.example.branchreportservice.entity.role.Role;
import com.example.branchreportservice.entity.role.UserRole;
import com.example.branchreportservice.repository.role.RoleRepository;
import com.example.branchreportservice.repository.role.UserRoleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleAssignmentService {

    private static final Logger logger = LoggerFactory.getLogger(RoleAssignmentService.class);

    private final UserRoleRepository userRoleRepository;
    private final RoleRepository roleRepository;

    public RoleAssignmentService(UserRoleRepository userRoleRepository, RoleRepository roleRepository) {
        this.userRoleRepository = userRoleRepository;
        this.roleRepository = roleRepository;
    }

    @Transactional
    public void assignUsersToRole(Long roleId, List<Long> userIds, String assignedBy) {
        logger.info("assignUsersToRole called with roleId: {}, userIds: {}, assignedBy: {}", roleId, userIds,
                assignedBy);

        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new IllegalArgumentException("Role not found with id: " + roleId));

        for (Long userId : userIds) {
            logger.info("Processing userId: {}", userId);
            // deactivate all other active role assignments for this user
            List<UserRole> activeAssignments = userRoleRepository.findByUserIdAndActiveTrue(userId);
            logger.info("Found {} active assignments for user {}", activeAssignments.size(), userId);

            for (UserRole assignment : activeAssignments) {
                try {
                    // Use string comparison to avoid type casting issues
                    String assignmentRoleIdStr = assignment.getRole().getId().toString();
                    String currentRoleIdStr = roleId.toString();

                    if (!assignmentRoleIdStr.equals(currentRoleIdStr)) {
                        assignment.setActive(false);
                        userRoleRepository.save(assignment);
                    }
                } catch (Exception e) {
                    // Handle any type conversion issues
                    logger.warn("Type conversion issue in role assignment, skipping: {}", e.getMessage());
                }
            }

            // ensure the target role assignment exists and is active
            UserRole userRole = userRoleRepository.findByRole_IdAndUserId(roleId, userId)
                    .orElseGet(() -> new UserRole(userId, role, assignedBy));
            userRole.setActive(true);
            userRole.setAssignedBy(assignedBy);
            userRoleRepository.save(userRole);
        }
    }

    @Transactional
    public void removeUsersFromRole(Long roleId, List<Long> userIds) {
        List<UserRole> assignments = userRoleRepository.findByRole_IdAndUserIdIn(roleId, userIds);
        assignments.forEach(assignment -> {
            assignment.setActive(false);
            userRoleRepository.save(assignment);
        });
    }

    public List<Long> getUsersInRole(Long roleId) {
        return userRoleRepository.findByRole_IdAndActiveTrue(roleId).stream()
                .map(UserRole::getUserId)
                .collect(Collectors.toList());
    }

    public List<Long> getRolesForUser(Long userId) {
        return userRoleRepository.findByUserIdAndActiveTrue(userId).stream()
                .map(userRole -> userRole.getRole().getId())
                .collect(Collectors.toList());
    }

    public List<String> getPermissionCodesForUserThroughRoles(Long userId, List<Role> allRoles) {
        List<Long> userRoleIds = getRolesForUser(userId);

        return allRoles.stream()
                .filter(role -> userRoleIds.contains(role.getId()) && role.getActive())
                .flatMap(role -> role.getPermissions().stream())
                .filter(permission -> permission.getActive())
                .map(permission -> permission.getCode())
                .collect(Collectors.toList());
    }
}
