package com.example.branchreportservice.repository.role;

import com.example.branchreportservice.entity.role.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {

    List<UserRole> findByUserIdAndActiveTrue(Long userId);

    List<UserRole> findByRole_IdAndActiveTrue(Long roleId);

    Optional<UserRole> findByRole_IdAndUserId(Long roleId, Long userId);

    List<UserRole> findByRole_IdAndUserIdIn(Long roleId, List<Long> userIds);

    @Modifying
    @Query("DELETE FROM UserRole ur WHERE ur.role.id = :roleId")
    void deleteByRoleId(@Param("roleId") Long roleId);

    @Query("SELECT ur FROM UserRole ur WHERE ur.role.id = :roleId AND ur.active = true")
    List<UserRole> findByRoleIdAndActiveTrue(@Param("roleId") Long roleId);
}
