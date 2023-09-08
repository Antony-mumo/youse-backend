package com.company.youse.enums;

public enum JobApplicationStatusEnum {
    APPLICATION("application"),
    INTERVIEWING("interviewing"),
    HIRED("hired"),
    EXPIRED("expired"),
    PENDING("pending"),
    JOB_CLOSED("jobClosed");

    private String value;

    JobApplicationStatusEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
