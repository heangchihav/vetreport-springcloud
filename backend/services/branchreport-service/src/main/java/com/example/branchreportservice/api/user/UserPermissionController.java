package com.example.branchreportservice.api.user;

import com.example.branchreportservice.entity.permission.Permission;
import com.example.branchreportservice.service.permission.PermissionService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/branchreport")
public class UserPermissionController {

    private static final Logger logger = LoggerFactory.getLogger(UserPermissionController.class);

    private final PermissionService permissionService;

    public UserPermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
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
        // In a real implementation, this should check against a user service or configuration
        return userId != null && userId == 1L;
    }
}
