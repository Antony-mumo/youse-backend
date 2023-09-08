package com.company.youse.enums;

public enum Currency {

    KES("Ksh", "Kenya Shilling"),
    UGX("Ush", "Ugandan Shilling"),
    TZS("Tzs", "Tanzania Shilling");

    private final String abbreviation;
    private final String name;

    private Currency(String abbreviation, String name){
        this.abbreviation = abbreviation;
        this.name = name;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public String getName() {
        return name;
    }
}
