package com.demo.community.account.role;

import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;

public enum AccountRole {

    USER, SPECIALIST, ADMIN, OTHER;

    public static final String ROLE_PREFIX = "ROLE_";

    public String getRoleName() {
        return ROLE_PREFIX + this.name();
    }

    public static RoleHierarchy getRoleHierarchy() {
        String hierarchy = String.join("\n",
                String.format("%s > %s", ADMIN.getRoleName(), SPECIALIST.getRoleName()),
                String.format("%s > %s", SPECIALIST.getRoleName(), USER.getRoleName())
        );
        return RoleHierarchyImpl.fromHierarchy(hierarchy);
    }

}
