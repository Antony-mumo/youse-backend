package com.company.youse.enums;

public enum RoleEnum {
    ADMIN("admin"),
    AGENT("agent"),
    SERVICEPROVIDER("serviceProvider"),
    CUSTOMER("customer"),
    BOTH_CUSTOMER_AND_SERVICEPROVIDER("both")
    ;

    private String value;

    RoleEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
