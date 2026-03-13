package com.example.branchreportservice.config;

import com.example.branchreportservice.entity.permission.Permission;
import com.example.branchreportservice.entity.role.Role;
import com.example.branchreportservice.repository.permission.PermissionRepository;
import com.example.branchreportservice.repository.role.RoleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class PermissionDataInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(PermissionDataInitializer.class);

    private final PermissionRepository permissionRepository;
    private final RoleRepository roleRepository;

    public PermissionDataInitializer(PermissionRepository permissionRepository, RoleRepository roleRepository) {
        this.permissionRepository = permissionRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        logger.info("Initializing permissions for branch-report-service...");
        initializePermissions();
    }

    private void initializePermissions() {
        logger.info("Initializing branch-report-service permissions...");

        List<Permission> defaultPermissions = Arrays.asList(
                // Dashboard permissions
                new Permission("dashboard.view", "View Dashboard", "Can view branch report service dashboard"),
                new Permission("dashboard.overview", "View Overview", "Can view branch report service overview"),
                new Permission("dashboard.analytics", "View Analytics", "Can view branch report analytics"),

                // Product permissions (API: /api/branchreport/products)
                new Permission("product.view", "View Products", "Can view branch report products"),
                new Permission("product.create", "Create Products", "Can create new branch report products"),
                new Permission("product.edit", "Edit Products", "Can edit existing branch report products"),
                new Permission("product.delete", "Delete Products", "Can delete branch report products"),

                // Product Type permissions (API: /api/branchreport/product-types)
                new Permission("producttype.view", "View Product Types", "Can view branch report product types"),
                new Permission("producttype.create", "Create Product Types", "Can create new branch report product types"),
                new Permission("producttype.edit", "Edit Product Types", "Can edit existing branch report product types"),
                new Permission("producttype.delete", "Delete Product Types", "Can delete branch report product types"),

                // Region permissions (API: /api/branchreport/regions)
                new Permission("region.view", "View Regions", "Can view branch report regions"),
                new Permission("region.create", "Create Regions", "Can create new branch report regions"),
                new Permission("region.edit", "Edit Regions", "Can edit existing branch report regions"),
                new Permission("region.delete", "Delete Regions", "Can delete branch report regions"),

                // User permissions (API: /api/branchreport/users)
                new Permission("user.view", "View Users", "Can view branch report service users"),
                new Permission("user.create", "Create Users", "Can create new branch report service users"),
                new Permission("user.edit", "Edit Users", "Can edit existing branch report service users"),
                new Permission("user.delete", "Delete Users", "Can delete branch report service users"),

                // User assignment permissions (API: /api/branchreport/user-assignments)
                new Permission("user.assign", "Assign Users", "Can assign users to branches and regions"),
                new Permission("user.remove", "Remove Users", "Can remove users from branches and regions"),
                new Permission("user.view.assignments", "View User Assignments", "Can view user branch and region assignments"),

                // Role permissions (API: /api/branchreport/roles)
                new Permission("role.view", "View Roles", "Can view branch report service roles"),
                new Permission("role.manage", "Manage Roles", "Can create, edit, delete roles"),
                new Permission("role.assign", "Assign Roles", "Can assign users to roles"),

                // Permission permissions (API: /api/branchreport/permissions)
                new Permission("permission.view", "View Permissions", "Can view branch report service permissions"),
                new Permission("permission.manage", "Manage Permissions", "Can create, edit, delete permissions"),

                // Branch Report permissions (API: /api/branchreport/reports)
                new Permission("report.view", "View Reports", "Can view branch reports"),
                new Permission("report.create", "Create Reports", "Can create new branch reports"),
                new Permission("report.edit", "Edit Reports", "Can edit existing branch reports"),
                new Permission("report.delete", "Delete Reports", "Can delete branch reports"),
                new Permission("report.export", "Export Reports", "Can export branch reports"),

                // Branch permissions (API: /api/branchreport/branches)
                new Permission("branch.view", "View Branches", "Can view branch report service branches"),
                new Permission("branch.create", "Create Branches", "Can create new branch report service branches"),
                new Permission("branch.edit", "Edit Branches", "Can edit existing branch report service branches"),
                new Permission("branch.update", "Update Branches", "Can update branch report service branches"),
                new Permission("branch.delete", "Delete Branches", "Can delete branch report service branches"),

                // System permissions
                new Permission("system.settings", "System Settings", "Can access system settings"),
                new Permission("system.audit", "View Audit Logs", "Can view system audit logs"));

        logger.info("Processing {} default permissions", defaultPermissions.size());

        // Check if permissions already exist and add missing ones
        long existingCount = permissionRepository.count();
        logger.info("Found {} existing permissions in database", existingCount);

        int createdCount = 0;
        int skippedCount = 0;

        for (Permission permission : defaultPermissions) {
            try {
                logger.info("Checking permission: {}", permission.getCode());

                // Check if permission already exists
                if (permissionRepository.existsByCode(permission.getCode())) {
                    skippedCount++;
                    logger.info("Permission already exists, skipping: {}", permission.getCode());
                    continue;
                }

                // Create new permission
                logger.info("Creating new permission: {}", permission.getCode());
                setMenuMetadata(permission);
                permissionRepository.save(permission);
                createdCount++;
                logger.info("Created permission: {} - {}", permission.getCode(), permission.getName());
            } catch (Exception e) {
                logger.error("Failed to create permission {}: {}", permission.getCode(), e.getMessage());
            }
        }

        logger.info("Permission initialization complete: {} created, {} skipped", createdCount, skippedCount);

        long totalPermissions = permissionRepository.count();
        logger.info("Successfully initialized {} permissions for branch-report-service", totalPermissions);

        // Always update default role to include all permissions
        updateDefaultRole();
    }

    private void updateDefaultRole() {
        logger.info("Updating default role with all permissions...");

        // Find or create default role
        Role fullAccessRole = roleRepository.findByName("Full Access")
                .orElseGet(() -> {
                    logger.info("Creating new 'Full Access' role");
                    return new Role("Full Access", "Role with access to all branch-report-service permissions", 0L);
                });

        // Get all permissions and assign them to role
        List<Permission> allPermissions = permissionRepository.findAll();
        fullAccessRole.setPermissions(new java.util.HashSet<>(allPermissions));

        // Save role
        roleRepository.save(fullAccessRole);

        logger.info("Updated 'Full Access' role with {} permissions", allPermissions.size());
    }

    private void setMenuMetadata(Permission permission) {
        String code = permission.getCode();

        // Dashboard permissions
        if (code.startsWith("dashboard.")) {
            permission.setMenuGroup("Dashboard");
            if (code.equals("dashboard.view")) {
                permission.setMenuNumber("1.1");
            } else if (code.equals("dashboard.overview")) {
                permission.setMenuNumber("1.2");
            } else if (code.equals("dashboard.analytics")) {
                permission.setMenuNumber("1.3");
            }
            return;
        }

        // Product Management permissions
        if (code.startsWith("product.")) {
            permission.setMenuGroup("Products");
            if (code.equals("product.view")) {
                permission.setMenuNumber("2.1");
            } else if (code.equals("product.create")) {
                permission.setMenuNumber("2.2");
            } else if (code.equals("product.edit")) {
                permission.setMenuNumber("2.3");
            } else if (code.equals("product.delete")) {
                permission.setMenuNumber("2.4");
            }
            return;
        }

        // Product Type Management permissions
        if (code.startsWith("producttype.")) {
            permission.setMenuGroup("Product Types");
            if (code.equals("producttype.view")) {
                permission.setMenuNumber("3.1");
            } else if (code.equals("producttype.create")) {
                permission.setMenuNumber("3.2");
            } else if (code.equals("producttype.edit")) {
                permission.setMenuNumber("3.3");
            } else if (code.equals("producttype.delete")) {
                permission.setMenuNumber("3.4");
            }
            return;
        }

        // Region Management permissions
        if (code.startsWith("region.")) {
            permission.setMenuGroup("Regions");
            if (code.equals("region.view")) {
                permission.setMenuNumber("4.1");
            } else if (code.equals("region.create")) {
                permission.setMenuNumber("4.2");
            } else if (code.equals("region.edit")) {
                permission.setMenuNumber("4.3");
            } else if (code.equals("region.delete")) {
                permission.setMenuNumber("4.4");
            }
            return;
        }

        // User Management permissions
        if (code.startsWith("user.") && !code.startsWith("user.assign")) {
            permission.setMenuGroup("User Management");
            if (code.equals("user.view")) {
                permission.setMenuNumber("5.1");
            } else if (code.equals("user.create")) {
                permission.setMenuNumber("5.2");
            } else if (code.equals("user.edit")) {
                permission.setMenuNumber("5.3");
            } else if (code.equals("user.delete")) {
                permission.setMenuNumber("5.4");
            }
            return;
        }

        // User Assignment permissions
        if (code.startsWith("user.assign") || code.startsWith("user.remove") || code.startsWith("user.view.assignments")) {
            permission.setMenuGroup("User Assignments");
            if (code.equals("user.assign")) {
                permission.setMenuNumber("6.1");
            } else if (code.equals("user.remove")) {
                permission.setMenuNumber("6.2");
            } else if (code.equals("user.view.assignments")) {
                permission.setMenuNumber("6.3");
            }
            return;
        }

        // Role Management permissions
        if (code.startsWith("role.")) {
            permission.setMenuGroup("Role Management");
            if (code.equals("role.view")) {
                permission.setMenuNumber("7.1");
            } else if (code.equals("role.manage")) {
                permission.setMenuNumber("7.2");
            } else if (code.equals("role.assign")) {
                permission.setMenuNumber("7.3");
            }
            return;
        }

        // Permission Management permissions
        if (code.startsWith("permission.")) {
            permission.setMenuGroup("Permissions Management");
            if (code.equals("permission.view")) {
                permission.setMenuNumber("8.1");
            } else if (code.equals("permission.manage")) {
                permission.setMenuNumber("8.2");
            }
            return;
        }

        // Branch Report permissions
        if (code.startsWith("report.")) {
            permission.setMenuGroup("Branch Reports");
            if (code.equals("report.view")) {
                permission.setMenuNumber("9.1");
            } else if (code.equals("report.create")) {
                permission.setMenuNumber("9.2");
            } else if (code.equals("report.edit")) {
                permission.setMenuNumber("9.3");
            } else if (code.equals("report.delete")) {
                permission.setMenuNumber("9.4");
            } else if (code.equals("report.export")) {
                permission.setMenuNumber("9.5");
            }
            return;
        }

        // Branch Management permissions
        if (code.startsWith("branch.")) {
            permission.setMenuGroup("Branch Management");
            if (code.equals("branch.view")) {
                permission.setMenuNumber("10.1");
            } else if (code.equals("branch.create")) {
                permission.setMenuNumber("10.2");
            } else if (code.equals("branch.edit")) {
                permission.setMenuNumber("10.3");
            } else if (code.equals("branch.update")) {
                permission.setMenuNumber("10.4");
            } else if (code.equals("branch.delete")) {
                permission.setMenuNumber("10.5");
            }
            return;
        }

        // System permissions
        if (code.startsWith("system.")) {
            permission.setMenuGroup("System Administration");
            if (code.equals("system.settings")) {
                permission.setMenuNumber("11.1");
            } else if (code.equals("system.audit")) {
                permission.setMenuNumber("11.2");
            }
            return;
        }

        // Default grouping
        permission.setMenuGroup("Other");
    }
}
