package com.company.youse.enums;

/**
 * enum object to hold the status of a contract
 * a contract starts with ACTIVE, here the worker is working on the contract or has just received an offer to work on the contract
 * if a worker submits work for payment, then contract updates to pending
 * if contract is approved by client, then it updates to closed status
 * if contract has some issues and need follow up, then the contract updates to paused
 */
public enum ContractStatusEnum {

    ACTIVE("active"),
    PENDING("pending"),
    PAUSED("paused"),
    CLOSED("closed");

    private String value;

    ContractStatusEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
