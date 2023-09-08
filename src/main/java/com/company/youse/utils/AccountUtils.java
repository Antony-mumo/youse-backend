package com.company.youse.utils;

public class AccountUtils {
    public static String getBasePayBillAccount(){
        return "2010";
    }

    public static String getBaseCashAccount(){
        return "2011";
    }

    public static String getBaseDebtorsAccount(){
        return "2020";
    }

    public static String getBaseIncomeAccount(){
        return "3010";
    }

    public static String getBaseServiceChargesAccount(){
        return "3020";
    }

    public static String getBaseBadDebtAccount(){
        return "4010";
    }

    public static String getBasePaymentProviderChargesAccount(){
        return "4020";
    }

    public static String getBaseDeferredIncomeAccount(){
        return "5010";
    }

    public static String getBaseUnpaidInterestAccount(){
        return "5140";
    }

    public static String getBaseSuspenseAccount(){
        return "5140";
    }

    public static String generateMerchantIncomeAccount(int merchantId){
        return getBaseIncomeAccount().concat(String.format("%03d", merchantId));
    }

    public static String generateMerchantDeferredIncomeAccount(int merchantId){
        return getBaseDeferredIncomeAccount().concat(String.format("%03d", merchantId));
    }

    public static String generateMerchantDebtAccount(int merchantId){
        return getBaseBadDebtAccount().concat(String.format("%03d", merchantId));
    }

    public static String generateMerchantUnpaidIncomeAccount(int merchantId){
        return getBaseUnpaidInterestAccount().concat(String.format("%03d", merchantId));
    }

    public static String generateCustomerDebtAccount(int customerId){
        return getBaseBadDebtAccount().concat(String.format("%03d", customerId));
    }

    public static String generateCustomerCashAccount(int customerId){
        return getBaseCashAccount().concat(String.format("%03d", customerId));
    }
}
