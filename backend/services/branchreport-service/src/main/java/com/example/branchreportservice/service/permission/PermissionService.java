package com.example.branchreportservice.service.permission;

import com.example.branchreportservice.entity.permission.Permission;
import com.example.branchreportservice.entity.role.Role;
import com.example.branchreportservice.repository.permission.PermissionRepository;
import com.example.branchreportservice.service.shared.RoleAssignmentService;
import com.example.branchreportservice.service.role.RoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional
public class PermissionService {
    
    private static final Logger logger = LoggerFactory.getLogger(PermissionService.class);
    
    private final PermissionRepository permissionRepository;
    private final RoleAssignmentService roleAssignmentService;
    private final RoleService roleService;
    
    public PermissionService(PermissionRepository permissionRepository, 
                           RoleAssignmentService roleAssignmentService,
                           RoleService roleService) {
        this.permissionRepository = permissionRepository;
        this.roleAssignmentService = roleAssignmentService;
        this.roleService = roleService;
    }
    
    // Permission management
    public List<Permission> getAllPermissions() {
        return permissionRepository.findByActiveTrueOrderByName();
    }
    
    public Optional<Permission> getPermissionById(Long id) {
        return permissionRepository.findById(id);
    }
    
    public Optional<Permission> getPermissionByCode(String code) {
        return permissionRepository.findByCode(code);
    }
    
    public List<Permission> getPermissionsForUser(Long userId) {
        if (userId == null) {
            return List.of();
        }
        
        // Get permissions through user's roles
        List<Role> allRoles = roleService.getAllRoles();
        List<String> rolePermissionCodes = roleAssignmentService.getPermissionCodesForUserThroughRoles(userId, allRoles);
        List<Permission> rolePermissions = permissionRepository.findByCodeIn(rolePermissionCodes);
        
        return rolePermissions.stream()
            .collect(Collectors.toSet())
            .stream()
            .collect(Collectors.toList());
    }
    
    public Set<String> getRoleDerivedPermissionCodes(Long userId) {
        if (userId == null) {
            return Set.of();
        }
        List<Role> allRoles = roleService.getAllRoles();
        List<String> rolePermissionCodes = roleAssignmentService.getPermissionCodesForUserThroughRoles(userId, allRoles);
        return new HashSet<>(rolePermissionCodes);
    }
    
    public Permission createPermission(String code, String name, String description) {
        return createPermission(code, name, description, null, null);
    }
    
    public Permission createPermission(String code, String name, String description, String menuGroup, String menuNumber) {
        if (permissionRepository.existsByCode(code)) {
            throw new IllegalArgumentException("Permission with code " + code + " already exists");
        }
        
        Permission permission = new Permission(code, name, description);
        permission.setMenuGroup(menuGroup);
        permission.setMenuNumber(menuNumber);
        Permission saved = permissionRepository.save(permission);
        logger.info("Created permission: {}", saved.getCode());
        return saved;
    }
    
    public Permission updatePermission(Long id, String name, String description) {
        Permission permission = permissionRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Permission not found with id: " + id));
        
        permission.setName(name);
        permission.setDescription(description);
        Permission updated = permissionRepository.save(permission);
        logger.info("Updated permission: {}", updated.getCode());
        return updated;
    }
    
    public void deletePermission(Long id) {
        Permission permission = permissionRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Permission not found with id: " + id));
        
        permission.setActive(false);
        permissionRepository.save(permission);
        logger.info("Deactivated permission: {}", permission.getCode());
    }
    
    public boolean hasUserPermission(Long userId, String permissionCode) {
        // Check role-based permissions using in-memory assignments
        List<Role> allRoles = roleService.getAllRoles();
        List<String> rolePermissionCodes = roleAssignmentService.getPermissionCodesForUserThroughRoles(userId, allRoles);
        return rolePermissionCodes.contains(permissionCode);
    }

    public void trimUserPermissionsToAllowedCodes(Long userId, Set<String> allowedPermissionCodes) {
        if (userId == null) {
            return;
        }
        // For branch-report-service, we only handle role-based permissions
        // Direct user permissions are not used in this service
        logger.info("Trimming role permissions for user {} to allowed codes", userId);
    }
}
