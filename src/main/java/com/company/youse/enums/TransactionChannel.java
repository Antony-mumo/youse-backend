package com.company.youse.enums;

/**
 * How was this money received or sent,
 * MOBILE_MONEY_TOPHONE (Mpesa, Airtel),
 * MOBILE_MONEY_TOBANK (Mpesa, Airtel),
 * API_INTER_BANK(Interswitch, EQUITY, PESALINK),
 */

public enum TransactionChannel {
    MOBILE_MONEY,
    API_INTER_BANK,
    OVERPAYMENT_CREDIT,
    DIRECT_ENTRY;
}
