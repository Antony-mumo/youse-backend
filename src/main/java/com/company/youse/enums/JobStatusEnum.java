package com.company.youse.enums;

public enum JobStatusEnum {
    POSTED("1_posted"),
    PENDING("2_pending"),
    HIRED("3_hired"),
    CLOSED("4_closed"),
    EXPIRED("5_expired");

    private String value;

    JobStatusEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
