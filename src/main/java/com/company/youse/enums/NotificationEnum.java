package com.company.youse.enums;

public enum NotificationEnum {
    CONTRACT("contract"),
    JOB("job"),
    PROMOTIONAL("promotional"),
    PAYMENT("payment"),
    ALERT("alert"),
    YOUSE("youse"),
    INFORMATION("information")
    ;

    private String value;

    NotificationEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
