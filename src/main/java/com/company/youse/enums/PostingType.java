package com.company.youse.enums;

public enum PostingType {
	DR, CR;
    public static PostingType parse(String s) {
        return s.equalsIgnoreCase("CREDIT") || s.equals("CR")? CR:
                s.equalsIgnoreCase("DEBIT") || s.equals("DR")? DR:
                        null;
    }
}
