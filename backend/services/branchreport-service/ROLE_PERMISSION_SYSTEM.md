# Branch Report Service - Role and Permission System

## Overview

This document describes the role and permission system implementation for the branch-report-service, following the same pattern used in call-service and marketing-service.

## Architecture

### Entities

#### Role Entity
- **Location**: `com.example.branchreportservice.entity.role.Role`
- **Table**: `branch_report_service_roles`
- **Fields**:
  - `id`: Primary key
  - `name`: Role name (unique, required)
  - `description`: Role description
  - `active`: Boolean flag for active status
  - `createdBy`: User ID who created the role
  - `createdAt`: Creation timestamp
  - `updatedAt`: Last update timestamp
  - `permissions`: Many-to-many relationship with permissions

#### UserRole Entity
- **Location**: `com.example.branchreportservice.entity.role.UserRole`
- **Table**: `branch_report_service_user_roles`
- **Fields**:
  - `id`: Primary key
  - `role`: Reference to Role entity
  - `userId`: User ID
  - `assignedBy`: Who assigned this role
  - `active`: Boolean flag for active status
  - `assignedAt`: Assignment timestamp
  - `updatedAt`: Last update timestamp

#### Permission Entity
- **Location**: `com.example.branchreportservice.entity.permission.Permission`
- **Table**: `branch_report_service_permissions`
- **Fields**:
  - `id`: Primary key
  - `code`: Permission code (unique, required)
  - `name`: Permission name
  - `description`: Permission description
  - `active`: Boolean flag for active status
  - `createdAt`: Creation timestamp
  - `updatedAt`: Last update timestamp
  - `menuGroup`: Menu group for UI organization
  - `menuNumber`: Menu number for ordering

### Repositories

- `RoleRepository`: Role data access
- `UserRoleRepository`: User-role assignment data access
- `PermissionRepository`: Permission data access

### Services

#### RoleService
- **Location**: `com.example.branchreportservice.service.role.RoleService`
- **Responsibilities**:
  - Role CRUD operations
  - Permission filtering based on user permissions
  - Role synchronization for creators
  - Support for root users with unrestricted permissions

#### PermissionService
- **Location**: `com.example.branchreportservice.service.permission.PermissionService`
- **Responsibilities**:
  - Permission CRUD operations
  - User permission checking through roles
  - Permission code management

#### RoleAssignmentService
- **Location**: `com.example.branchreportservice.service.shared.RoleAssignmentService`
- **Responsibilities**:
  - User-role assignment management
  - Permission aggregation through roles
  - Role deactivation when assigning new roles

### Controllers

#### RoleController
- **Location**: `com.example.branchreportservice.controller.RoleController`
- **Base Path**: `/api/branchreport/roles`
- **Endpoints**:
  - `GET /` - Get all roles for user
  - `GET /{id}` - Get role by ID
  - `POST /` - Create new role
  - `PUT /{id}` - Update existing role
  - `DELETE /{id}` - Delete role
  - `POST /{roleId}/assign-users` - Assign users to role
  - `GET /{roleId}/users` - Get users in role
  - `POST /{roleId}/remove-users` - Remove users from role

### DTOs

#### Request DTOs
- `CreateRoleRequest`: Role creation payload
- `UpdateRoleRequest`: Role update payload

#### Response DTOs
- `RoleResponse`: Role response with permissions and user count
- `PermissionResponse`: Permission response details
- `UserResponse`: User information for role assignments

## Security Features

### Permission-Based Access Control
- Users can only create roles with permissions they already possess
- Root users have unrestricted permission access
- Role permissions are automatically filtered based on creator permissions

### User Assignment Management
- Only one active role per user at a time
- Previous roles are deactivated when assigning new ones
- Audit trail for who assigned roles and when

### Permission Synchronization
- Automatic permission trimming when creator permissions change
- Cascading permission updates for role assignments
- Support for hierarchical permission management

## API Usage

### Creating a Role
```bash
POST /api/branchreport/roles
Headers:
- X-User-Id: 123
- X-Root-User: false
Body:
{
  "name": "Branch Manager",
  "description": "Can manage branch reports",
  "permissionCodes": ["branch.report.view", "branch.report.manage"]
}
```

### Assigning Users to Role
```bash
POST /api/branchreport/roles/{roleId}/assign-users
Headers:
- X-User-Id: 123
Body:
{
  "userIds": [456, 789, 101]
}
```

### Getting Roles for User
```bash
GET /api/branchreport/roles
Headers:
- X-User-Id: 123
- X-Root-User: false
```

## Database Schema

### Tables Created
1. `branch_report_service_roles`
2. `branch_report_service_user_roles`
3. `branch_report_service_role_permissions`
4. `branch_report_service_permissions`

### Indexes
- Primary keys on all tables
- Unique constraint on role names
- Unique constraint on permission codes
- Foreign key constraints for relationships

## Integration Points

### Gateway Service
- Routes: `/api/branchreport/roles/**` → branch-report-service
- Authentication headers passed through: `X-User-Id`, `X-Root-User`

### User Service Integration
- Uses user-service for user validation
- Respects user-service permission structure
- Compatible with existing user management

## Testing

The implementation includes comprehensive unit tests for:
- Role creation with permission filtering
- Root user unrestricted access
- Role validation and error handling
- User assignment operations
- Permission checking logic

## Deployment Notes

### Environment Variables
- Standard Spring Boot configuration
- Database connectivity through existing datasource
- No additional configuration required

### Migration
- New tables will be auto-created by Hibernate
- Existing data remains unaffected
- Backward compatible with current API structure

## Future Enhancements

1. **Permission Categories**: Add permission categorization for better organization
2. **Role Templates**: Predefined role templates for common use cases
3. **Audit Logging**: Enhanced audit trail for role changes
4. **Bulk Operations**: Support for bulk user assignments
5. **Role Hierarchy**: Support for nested role structures

## Compatibility

This implementation follows the exact same patterns as:
- call-service role/permission system
- marketing-service role/permission system

Ensuring consistent behavior and user experience across all services.
