# Branch Report Service - Permission Data Initializer

## Overview

The `PermissionDataInitializer` is a Spring Boot `CommandLineRunner` component that automatically initializes the default permissions and roles for the branch-report-service when the application starts up. This follows the exact same pattern as implemented in call-service and marketing-service.

## Implementation Details

### Component Structure
- **Package**: `com.example.branchreportservice.config`
- **Class**: `PermissionDataInitializer`
- **Interface**: `CommandLineRunner`
- **Execution**: Runs automatically on application startup

### Core Features

#### 1. Permission Initialization
- **Automatic Creation**: Creates default permissions if they don't exist
- **Idempotent Operation**: Skips permissions that already exist
- **Logging**: Comprehensive logging for debugging and monitoring
- **Error Handling**: Graceful handling of individual permission creation failures

#### 2. Default Role Management
- **Full Access Role**: Automatically creates/updates "Full Access" role
- **Permission Assignment**: Assigns all available permissions to the default role
- **System User**: Created with createdBy = 0L for system-level role

#### 3. Menu Organization
- **Menu Groups**: Permissions organized by functional areas
- **Menu Numbers**: Hierarchical numbering for UI organization
- **Metadata**: Menu group and number assigned for frontend display

## Permission Categories

### 1. Dashboard (1.x)
- `dashboard.view` - View main dashboard
- `dashboard.overview` - View service overview
- `dashboard.analytics` - View analytics data

### 2. Product Management (2.x)
- `product.view` - View products
- `product.create` - Create new products
- `product.edit` - Edit existing products
- `product.delete` - Delete products

### 3. Product Type Management (3.x)
- `producttype.view` - View product types
- `producttype.create` - Create product types
- `producttype.edit` - Edit product types
- `producttype.delete` - Delete product types

### 4. Region Management (4.x)
- `region.view` - View regions
- `region.create` - Create regions
- `region.edit` - Edit regions
- `region.delete` - Delete regions

### 5. User Management (5.x)
- `user.view` - View users
- `user.create` - Create users
- `user.edit` - Edit users
- `user.delete` - Delete users

### 6. User Assignments (6.x)
- `user.assign` - Assign users to branches/regions
- `user.remove` - Remove user assignments
- `user.view.assignments` - View user assignments

### 7. Role Management (7.x)
- `role.view` - View roles
- `role.manage` - Manage roles (CRUD)
- `role.assign` - Assign users to roles

### 8. Permission Management (8.x)
- `permission.view` - View permissions
- `permission.manage` - Manage permissions (CRUD)

### 9. Branch Reports (9.x)
- `report.view` - View branch reports
- `report.create` - Create new reports
- `report.edit` - Edit existing reports
- `report.delete` - Delete reports
- `report.export` - Export reports

### 10. Branch Management (10.x)
- `branch.view` - View branches
- `branch.create` - Create branches
- `branch.edit` - Edit branches
- `branch.update` - Update branches
- `branch.delete` - Delete branches

### 11. System Administration (11.x)
- `system.settings` - Access system settings
- `system.audit` - View audit logs

## Database Operations

### Permission Creation Process
1. **Check Existence**: Verifies if permission already exists by code
2. **Skip if Exists**: Logs and skips existing permissions
3. **Create New**: Creates new permission with menu metadata
4. **Assign Metadata**: Sets menu group and number for UI organization
5. **Log Success**: Logs successful creation with details

### Default Role Update Process
1. **Find Role**: Looks for existing "Full Access" role
2. **Create if Missing**: Creates new role if not found
3. **Load All Permissions**: Retrieves all permissions from database
4. **Assign Permissions**: Sets all permissions to the role
5. **Save Role**: Persists the updated role

## Menu Metadata Structure

### Menu Groups
Permissions are organized into logical groups for frontend menu display:
- Dashboard (1.x)
- Products (2.x)
- Product Types (3.x)
- Regions (4.x)
- User Management (5.x)
- User Assignments (6.x)
- Role Management (7.x)
- Permissions Management (8.x)
- Branch Reports (9.x)
- Branch Management (10.x)
- System Administration (11.x)

### Menu Numbers
- **Hierarchical**: Format "group.item" (e.g., "1.1", "2.3")
- **Sequential**: Each group has numbered items
- **UI Ordering**: Used for frontend menu organization

## Logging and Monitoring

### Log Levels
- **INFO**: General initialization progress
- **INFO**: Permission creation/skip details
- **INFO**: Role update progress
- **ERROR**: Permission creation failures

### Key Metrics Tracked
- Total permissions processed
- Permissions created vs skipped
- Existing permissions count
- Final permission count
- Default role permissions count

## Error Handling

### Graceful Degradation
- **Individual Failures**: One permission failure doesn't stop others
- **Error Logging**: Failed permissions are logged but process continues
- **Rollback Safe**: Database transactions ensure consistency

### Common Failure Scenarios
- Database connection issues
- Constraint violations
- Invalid permission data
- Repository access errors

## Configuration

### Spring Boot Integration
- **Automatic Execution**: Runs on application startup
- **Conditional**: Only runs if database is accessible
- **Order**: Executes after Spring context initialization

### Environment Considerations
- **Development**: Useful for fresh database setup
- **Production**: Safe to run (idempotent)
- **Testing**: Can be disabled in test profiles if needed

## Security Considerations

### System Role Creation
- **System User**: Default role created with createdBy = 0L
- **Full Access**: Default role has all permissions
- **Audit Trail**: Creation is logged for compliance

### Permission Scope
- **Service-Specific**: Only branch-report-service permissions
- **No Cross-Service**: Isolated from other services
- **Controlled Access**: Requires proper authentication for management

## Usage Examples

### Startup Logs
```
INFO  --- PermissionDataInitializer : Initializing permissions for branch-report-service...
INFO  --- PermissionDataInitializer : Initializing branch-report-service permissions...
INFO  --- PermissionDataInitializer : Processing 40 default permissions
INFO  --- PermissionDataInitializer : Found 0 existing permissions in database
INFO  --- PermissionDataInitializer : Creating new permission: dashboard.view
INFO  --- PermissionDataInitializer : Created permission: dashboard.view - View Dashboard
...
INFO  --- PermissionDataInitializer : Permission initialization complete: 40 created, 0 skipped
INFO  --- PermissionDataInitializer : Successfully initialized 40 permissions for branch-report-service
INFO  --- PermissionDataInitializer : Updating default role with all permissions...
INFO  --- PermissionDataInitializer : Creating new 'Full Access' role
INFO  --- PermissionDataInitializer : Updated 'Full Access' role with 40 permissions
```

### Database State After Initialization
- **Permissions Table**: 40 rows with all default permissions
- **Roles Table**: 1 row for "Full Access" role
- **Role_Permissions Table**: 40 rows linking role to permissions

## Maintenance

### Adding New Permissions
1. Add to `defaultPermissions` list in `initializePermissions()`
2. Update `setMenuMetadata()` method for menu organization
3. Restart application or manually run initialization

### Updating Existing Permissions
1. Modify permission details in the list
2. Update menu metadata if needed
3. Manual database update may be required for existing permissions

### Version Compatibility
- **Backward Compatible**: Existing permissions are preserved
- **Forward Compatible**: New permissions are added automatically
- **Migration Safe**: Can be run multiple times safely

## Integration Points

### Frontend Integration
- **Menu Rendering**: Menu groups and numbers used for UI organization
- **Permission Checking**: Permission codes used for access control
- **Role Management**: Default role provides baseline permissions

### API Integration
- **Permission Endpoints**: Permissions available via REST API
- **Role Endpoints**: Default role available for assignment
- **User Management**: Permissions used for user access control

This implementation ensures that branch-report-service has a comprehensive, organized, and maintainable permission system that follows established patterns and integrates seamlessly with the existing microservices architecture.
