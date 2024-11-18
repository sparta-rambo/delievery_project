package com.delivery_project.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserRoleEnum {
    ANONYMOUS(Authority.ANONYMOUS),
    CUSTOMER(Authority.CUSTOMER),
    OWNER(Authority.OWNER),
    MANAGER(Authority.MANAGER),
    MASTER(Authority.MASTER);

    private final String authority;

    public String getAuthority() {
        return this.authority;
    }

    public static class Authority {
        public static final String ANONYMOUS = "ROLE_ANONYMOUS";
        public static final String CUSTOMER = "ROLE_CUSTOMER";
        public static final String OWNER = "ROLE_OWNER";
        public static final String MANAGER = "ROLE_MANAGER";
        public static final String MASTER = "ROLE_MASTER";
    }
}
