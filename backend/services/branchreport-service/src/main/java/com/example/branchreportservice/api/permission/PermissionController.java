package com.example.branchreportservice.api.permission;

import com.example.branchreportservice.dto.permission.PermissionResponse;
import com.example.branchreportservice.dto.role.RoleResponse;
import com.example.branchreportservice.dto.role.UpdateRoleRequest;
import com.example.branchreportservice.dto.user.UserResponse;
import com.example.branchreportservice.entity.permission.Permission;
import com.example.branchreportservice.entity.role.Role;
import com.example.branchreportservice.service.permission.PermissionService;
import com.example.branchreportservice.service.role.RoleService;
import com.example.branchreportservice.service.shared.RoleAssignmentService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/branchreport/permissions")
public class PermissionController {

    private static final Logger logger = LoggerFactory.getLogger(PermissionController.class);

    private final PermissionService permissionService;
    private final RoleService roleService;
    private final RoleAssignmentService roleAssignmentService;

    public PermissionController(PermissionService permissionService, RoleService roleService,
            RoleAssignmentService roleAssignmentService) {
        this.permissionService = permissionService;
        this.roleService = roleService;
        this.roleAssignmentService = roleAssignmentService;
    }

    @GetMapping
    public ResponseEntity<List<PermissionResponse>> getAllPermissions(HttpServletRequest request) {
        logger.info("GET /api/branchreport/permissions - Fetching all permissions");

        // For now, we'll allow all authenticated users to view permissions
        // In a real implementation, you might want to check for "permission.view"
        // permission
        Long currentUserId = getCurrentUserId(request);
        if (currentUserId == null) {
            logger.warn("Unauthorized access attempt - no user ID found");
            return ResponseEntity.status(401).build();
        }

        boolean isRootUser = isRootUser(currentUserId);
        List<Permission> permissions = isRootUser
                ? permissionService.getAllPermissions()
                : permissionService.getPermissionsForUser(currentUserId);

        List<PermissionResponse> response = permissions.stream()
                .map(p -> new PermissionResponse(
                        p.getId(),
                        p.getCode(),
                        p.getName(),
                        p.getDescription(),
                        p.getActive(),
                        p.getCreatedAt(),
                        p.getUpdatedAt(),
                        p.getMenuGroup(),
                        p.getMenuNumber()))
                .collect(Collectors.toList());

        logger.info("Returning {} permissions for user {} (root: {})", response.size(), currentUserId, isRootUser);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PermissionResponse> getPermissionById(@PathVariable Long id) {
        logger.info("GET /api/branchreport/permissions/{} - Fetching permission by ID", id);

        Permission permission = permissionService.getPermissionById(id)
                .orElseThrow(() -> new IllegalArgumentException("Permission not found with id: " + id));

        PermissionResponse response = new PermissionResponse(
                permission.getId(),
                permission.getCode(),
                permission.getName(),
                permission.getDescription(),
                permission.getActive(),
                permission.getCreatedAt(),
                permission.getUpdatedAt(),
                permission.getMenuGroup(),
                permission.getMenuNumber());

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<PermissionResponse> createPermission(@RequestBody Map<String, String> request) {
        logger.info("POST /api/branchreport/permissions - Creating new permission");

        String code = request.get("code");
        String name = request.get("name");
        String description = request.get("description");
        String menuGroup = request.get("menuGroup");
        String menuNumber = request.get("menuNumber");

        if (code == null || name == null || description == null) {
            return ResponseEntity.badRequest().build();
        }

        try {
            Permission createdPermission = permissionService.createPermission(code, name, description, menuGroup,
                    menuNumber);
            PermissionResponse response = new PermissionResponse(
                    createdPermission.getId(),
                    createdPermission.getCode(),
                    createdPermission.getName(),
                    createdPermission.getDescription(),
                    createdPermission.getActive(),
                    createdPermission.getCreatedAt(),
                    createdPermission.getUpdatedAt(),
                    createdPermission.getMenuGroup(),
                    createdPermission.getMenuNumber());

            logger.info("Created permission: {}", createdPermission.getCode());
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            logger.error("Failed to create permission: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<PermissionResponse> updatePermission(@PathVariable Long id,
            @RequestBody Map<String, String> request) {
        logger.info("PUT /api/branchreport/permissions/{} - Updating permission", id);

        String name = request.get("name");
        String description = request.get("description");

        if (name == null || description == null) {
            return ResponseEntity.badRequest().build();
        }

        try {
            Permission updatedPermission = permissionService.updatePermission(id, name, description);
            PermissionResponse response = new PermissionResponse(
                    updatedPermission.getId(),
                    updatedPermission.getCode(),
                    updatedPermission.getName(),
                    updatedPermission.getDescription(),
                    updatedPermission.getActive(),
                    updatedPermission.getCreatedAt(),
                    updatedPermission.getUpdatedAt(),
                    updatedPermission.getMenuGroup(),
                    updatedPermission.getMenuNumber());

            logger.info("Updated permission: {}", updatedPermission.getCode());
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            logger.error("Failed to update permission: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePermission(@PathVariable Long id) {
        logger.info("DELETE /api/branchreport/permissions/{} - Deleting permission", id);

        try {
            permissionService.deletePermission(id);
            logger.info("Deleted permission with ID: {}", id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            logger.error("Failed to delete permission: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/user-permissions")
    public ResponseEntity<List<Map<String, String>>> getUserPermissions(HttpServletRequest request) {
        logger.info("GET /api/branchreport/user-permissions - Fetching user permissions");

        Long currentUserId = getCurrentUserId(request);
        if (currentUserId == null) {
            logger.warn("Unauthorized access attempt - no user ID found");
            return ResponseEntity.status(401).build();
        }

        boolean isRootUser = isRootUser(currentUserId);
        List<Permission> permissions = isRootUser
                ? permissionService.getAllPermissions()
                : permissionService.getPermissionsForUser(currentUserId);

        List<Map<String, String>> response = permissions.stream()
                .map(permission -> {
                    Map<String, String> permissionMap = new java.util.HashMap<>();
                    permissionMap.put("permissionCode", permission.getCode());
                    return permissionMap;
                })
                .collect(Collectors.toList());

        logger.info("Returning {} permissions for user {} (root: {})", response.size(), currentUserId, isRootUser);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user-permissions-array")
    public ResponseEntity<List<PermissionResponse>> getUserPermissionsArray(HttpServletRequest request) {
        logger.info("GET /api/branchreport/user-permissions-array - Fetching user permissions");

        Long currentUserId = getCurrentUserId(request);
        if (currentUserId == null) {
            logger.warn("Unauthorized access attempt - no user ID found");
            return ResponseEntity.status(401).build();
        }

        boolean isRootUser = isRootUser(currentUserId);
        List<Permission> permissions = isRootUser
                ? permissionService.getAllPermissions()
                : permissionService.getPermissionsForUser(currentUserId);

        List<PermissionResponse> response = permissions.stream()
                .map(permission -> new PermissionResponse(
                        permission.getId(),
                        permission.getCode(),
                        permission.getName(),
                        permission.getDescription(),
                        permission.getActive(),
                        permission.getCreatedAt(),
                        permission.getUpdatedAt(),
                        permission.getMenuGroup(),
                        permission.getMenuNumber()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/user-permissions-array/{userId}")
    public ResponseEntity<List<PermissionResponse>> getUserPermissionsArray(@PathVariable Long userId,
            HttpServletRequest request) {
        logger.info("GET /api/branchreport/permissions/user-permissions-array/{} - Fetching user permissions", userId);

        Long currentUserId = getCurrentUserId(request);
        if (currentUserId == null) {
            logger.warn("Unauthorized access attempt - no user ID found");
            return ResponseEntity.status(401).build();
        }

        // Only allow users to fetch their own permissions unless they're root user
        if (!currentUserId.equals(userId) && !isRootUser(currentUserId)) {
            logger.warn("Unauthorized access attempt - user {} trying to access permissions of user {}", currentUserId,
                    userId);
            return ResponseEntity.status(403).build();
        }

        boolean isRootUser = isRootUser(currentUserId);
        List<Permission> permissions = isRootUser
                ? permissionService.getAllPermissions()
                : permissionService.getPermissionsForUser(userId);

        List<PermissionResponse> response = permissions.stream()
                .map(permission -> new PermissionResponse(
                        permission.getId(),
                        permission.getCode(),
                        permission.getName(),
                        permission.getDescription(),
                        permission.getActive(),
                        permission.getCreatedAt(),
                        permission.getUpdatedAt(),
                        permission.getMenuGroup(),
                        permission.getMenuNumber()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/roles/{roleId}/users")
    public ResponseEntity<Void> assignUsersToRole(@PathVariable Long roleId,
            @RequestBody Map<String, List<Object>> request, HttpServletRequest httpRequest) {
        logger.info("POST /api/branchreport/permissions/roles/{}/users - Assigning users to role", roleId);

        Long currentUserId = getCurrentUserId(httpRequest);
        if (currentUserId == null) {
            return ResponseEntity.status(401).build();
        }

        List<Object> userIds = request.get("userIds");
        if (userIds == null || userIds.isEmpty()) {
            logger.error("User IDs are required but received null or empty list");
            return ResponseEntity.badRequest().build();
        }

        // Convert Integer values to Long if needed
        List<Long> convertedUserIds = userIds.stream()
                .map(id -> Long.parseLong(id.toString()))
                .collect(java.util.stream.Collectors.toList());

        try {
            if (!roleService.existsById(roleId)) {
                logger.error("Role not found with id: {}", roleId);
                return ResponseEntity.badRequest().build();
            }

            String assignedBy = currentUserId.toString();
            roleAssignmentService.assignUsersToRole(roleId, convertedUserIds, assignedBy);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            logger.error("Unexpected error in assignUsersToRole: {}", e.getMessage(), e);
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/roles/{roleId}/users")
    public ResponseEntity<List<UserResponse>> getUsersInRole(@PathVariable Long roleId) {
        logger.info("GET /api/branchreport/permissions/roles/{}/users - Getting users for role", roleId);

        if (!roleService.existsById(roleId)) {
            throw new IllegalArgumentException("Role not found with id: " + roleId);
        }

        // Return the actual stored user assignments with real user data
        List<Long> assignedUserIds = roleAssignmentService.getUsersInRole(roleId);
        List<UserResponse> users = new ArrayList<>();

        // For now, create mock responses that match the real user structure
        for (Long userId : assignedUserIds) {
            UserResponse user = new UserResponse();
            user.setId(userId);
            user.setUsername("user" + userId);
            user.setEmail("user" + userId + "@example.com");
            user.setFullName("User " + userId);
            user.setActive(true);
            users.add(user);
        }
        return ResponseEntity.ok(users);
    }

    @GetMapping("/users/{userId}/permissions")
    public ResponseEntity<List<PermissionResponse>> getUserPermissions(@PathVariable Long userId,
            HttpServletRequest request) {
        logger.info("GET /api/branchreport/permissions/users/{}/permissions - Fetching user permissions", userId);

        Long currentUserId = getCurrentUserId(request);
        if (currentUserId == null) {
            return ResponseEntity.status(401).build();
        }

        // Only allow users to fetch their own permissions unless they're root user
        if (!currentUserId.equals(userId) && !isRootUser(currentUserId)) {
            return ResponseEntity.status(403).build();
        }

        boolean isRootUser = isRootUser(currentUserId);
        List<Permission> permissions = isRootUser
                ? permissionService.getAllPermissions()
                : permissionService.getPermissionsForUser(userId);

        List<PermissionResponse> response = permissions.stream()
                .map(permission -> new PermissionResponse(
                        permission.getId(),
                        permission.getCode(),
                        permission.getName(),
                        permission.getDescription(),
                        permission.getActive(),
                        permission.getCreatedAt(),
                        permission.getUpdatedAt(),
                        permission.getMenuGroup(),
                        permission.getMenuNumber()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long userId) {
        logger.info("GET /api/branchreport/permissions/users/{} - Getting user by ID", userId);

        // Mock user data for now - in a real implementation, you would fetch from user
        // service
        UserResponse user = new UserResponse(
                userId,
                "user" + userId,
                "User " + userId,
                "user" + userId + "@example.com",
                true);

        return ResponseEntity.ok(user);
    }

    @PostMapping("/roles")
    public ResponseEntity<RoleResponse> createRole(@RequestBody Map<String, Object> request,
            HttpServletRequest httpRequest) {
        logger.info("POST /api/branchreport/permissions/roles - Creating role");

        Long currentUserId = getCurrentUserId(httpRequest);
        if (currentUserId == null) {
            return ResponseEntity.status(401).build();
        }

        boolean isRootUser = isRootUser(currentUserId);

        // Extract role data from request
        String name = (String) request.get("name");
        String description = (String) request.get("description");
        @SuppressWarnings("unchecked")
        List<String> permissionCodes = (List<String>) request.get("permissionCodes");

        if (name == null || description == null) {
            return ResponseEntity.badRequest().build();
        }

        // Create CreateRoleRequest
        com.example.branchreportservice.dto.role.CreateRoleRequest createRoleRequest = new com.example.branchreportservice.dto.role.CreateRoleRequest();
        createRoleRequest.setName(name);
        createRoleRequest.setDescription(description);
        createRoleRequest.setPermissionCodes(permissionCodes);

        try {
            Role role = roleService.createRole(createRoleRequest, currentUserId, isRootUser);

            RoleResponse response = new RoleResponse(
                    role.getId(),
                    role.getName(),
                    role.getDescription(),
                    role.getPermissions().stream()
                            .map(permission -> new PermissionResponse(
                                    permission.getId(),
                                    permission.getCode(),
                                    permission.getName(),
                                    permission.getDescription(),
                                    permission.getActive(),
                                    permission.getCreatedAt(),
                                    permission.getUpdatedAt(),
                                    permission.getMenuGroup(),
                                    permission.getMenuNumber()))
                            .collect(Collectors.toList()),
                    roleService.getUserCountForRole(role.getId()),
                    role.getActive(),
                    role.getCreatedAt(),
                    role.getUpdatedAt());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Failed to create role: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/roles/{roleId}")
    public ResponseEntity<RoleResponse> getRoleById(@PathVariable Long roleId) {
        logger.info("GET /api/branchreport/permissions/roles/{} - Getting role by ID", roleId);

        try {
            Role role = roleService.getRoleById(roleId)
                    .orElseThrow(() -> new IllegalArgumentException("Role not found with id: " + roleId));

            // Convert permissions to PermissionResponse objects
            List<PermissionResponse> permissionResponses = role.getPermissions().stream()
                    .map(permission -> new PermissionResponse(
                            permission.getId(),
                            permission.getCode(),
                            permission.getName(),
                            permission.getDescription(),
                            permission.getActive(),
                            permission.getCreatedAt(),
                            permission.getUpdatedAt(),
                            permission.getMenuGroup(),
                            permission.getMenuNumber()))
                    .collect(Collectors.toList());

            RoleResponse response = new RoleResponse(
                    role.getId(),
                    role.getName(),
                    role.getDescription(),
                    permissionResponses,
                    null, // userCount - not needed for single role response
                    role.getActive(),
                    role.getCreatedAt(),
                    role.getUpdatedAt());

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            logger.error("Role not found: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("Error getting role: {}", e.getMessage());
            return ResponseEntity.status(500).build();
        }
    }

    @PutMapping("/roles/{roleId}")
    public ResponseEntity<RoleResponse> updateRole(@PathVariable Long roleId, @RequestBody Map<String, Object> request,
            HttpServletRequest httpRequest) {
        logger.info("PUT /api/branchreport/permissions/roles/{} - Updating role", roleId);

        Long currentUserId = getCurrentUserId(httpRequest);
        if (currentUserId == null) {
            return ResponseEntity.status(401).build();
        }

        try {
            // Extract role data from request
            String name = (String) request.get("name");
            String description = (String) request.get("description");
            @SuppressWarnings("unchecked")
            List<String> permissionCodes = (List<String>) request.get("permissionCodes");

            if (name == null || name.trim().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }

            // Check if role exists
            if (!roleService.existsById(roleId)) {
                return ResponseEntity.notFound().build();
            }

            // Create UpdateRoleRequest
            UpdateRoleRequest updateRequest = new UpdateRoleRequest(name, description, permissionCodes);

            // Update the role
            boolean isRootUser = isRootUser(currentUserId);
            Role updatedRole = roleService.updateRole(roleId, updateRequest, currentUserId, isRootUser);

            // Convert permissions to PermissionResponse objects
            List<PermissionResponse> permissionResponses = updatedRole.getPermissions().stream()
                    .map(permission -> new PermissionResponse(
                            permission.getId(),
                            permission.getCode(),
                            permission.getName(),
                            permission.getDescription(),
                            permission.getActive(),
                            permission.getCreatedAt(),
                            permission.getUpdatedAt(),
                            permission.getMenuGroup(),
                            permission.getMenuNumber()))
                    .collect(Collectors.toList());

            RoleResponse response = new RoleResponse(
                    updatedRole.getId(),
                    updatedRole.getName(),
                    updatedRole.getDescription(),
                    permissionResponses,
                    null, // userCount - not needed for update response
                    updatedRole.getActive(),
                    updatedRole.getCreatedAt(),
                    updatedRole.getUpdatedAt());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Failed to update role: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/roles/{roleId}")
    public ResponseEntity<Void> deleteRole(@PathVariable Long roleId, HttpServletRequest request) {
        logger.info("DELETE /api/branchreport/permissions/roles/{} - Deleting role", roleId);

        Long currentUserId = getCurrentUserId(request);
        if (currentUserId == null) {
            return ResponseEntity.status(401).build();
        }

        try {
            // Try to delete the role - if it doesn't exist, that's fine for a DELETE
            // operation
            roleService.deleteRole(roleId);
            logger.info("Deleted role with ID: {}", roleId);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            // If role not found, return 204 No Content (idempotent DELETE)
            if (e.getMessage().contains("Role not found")) {
                logger.info("Role {} not found, but returning 204 for idempotent DELETE", roleId);
                return ResponseEntity.noContent().build();
            }
            logger.error("Failed to delete role {}: {}", roleId, e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            logger.error("Failed to delete role {}: {}", roleId, e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/roles")
    public ResponseEntity<List<RoleResponse>> getRolesForPermissions(HttpServletRequest request) {
        logger.info("GET /api/branchreport/permissions/roles - Returning roles with permissions");

        // Forward to the roles service logic
        Long currentUserId = getCurrentUserId(request);
        if (currentUserId == null) {
            return ResponseEntity.status(401).build();
        }

        boolean isRootUser = isRootUser(currentUserId);
        List<Role> roles = roleService.getRolesForUser(currentUserId, isRootUser);

        List<RoleResponse> response = roles.stream()
                .map(role -> new RoleResponse(
                        role.getId(),
                        role.getName(),
                        role.getDescription(),
                        role.getPermissions().stream()
                                .map(permission -> new PermissionResponse(
                                        permission.getId(),
                                        permission.getCode(),
                                        permission.getName(),
                                        permission.getDescription(),
                                        permission.getActive(),
                                        permission.getCreatedAt(),
                                        permission.getUpdatedAt(),
                                        permission.getMenuGroup(),
                                        permission.getMenuNumber()))
                                .collect(Collectors.toList()),
                        roleService.getUserCountForRole(role.getId()),
                        role.getActive(),
                        role.getCreatedAt(),
                        role.getUpdatedAt()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    // Helper methods
    private Long getCurrentUserId(HttpServletRequest request) {
        String userIdHeader = request.getHeader("X-User-Id");
        if (userIdHeader != null) {
            try {
                return Long.parseLong(userIdHeader);
            } catch (NumberFormatException e) {
                logger.warn("Invalid X-User-Id header: {}", userIdHeader);
                return null;
            }
        }
        return null;
    }

    private boolean isRootUser(Long userId) {
        // For now, we'll assume user ID 1 is root user
        // In a real implementation, this should check against a user service or
        // configuration
        return userId != null && userId == 1L;
    }
}
