package com.app.model;
//Real World Entities are named model

import java.math.BigDecimal;

public class BankAccount {

    private int bankaccountid;
    //considering using BIGDECIMAL because of the double/float problem
    private int userid;
    private BigDecimal accountBalance;
    private String statuses;
    private int reviewedBy;

    public BankAccount(){

    }

    public BankAccount(int bankaccountid, int userid, BigDecimal accountBalance, String statuses, int reviewedBy) {
        this.bankaccountid = bankaccountid;
        this.userid = userid;
        this.accountBalance = accountBalance;
        this.statuses = statuses;
        this.reviewedBy = reviewedBy;
    }

    public BankAccount(int bankaccountid, BigDecimal account, int userid, String statuses) {
        this.bankaccountid = bankaccountid;
        this.accountBalance = account;
        this.userid = userid;
    }

    public BankAccount(int bankaccountid) {
        this.bankaccountid = bankaccountid;
    }


    public int getBankaccountid() {
        return bankaccountid;
    }

    public void setBankaccountid(int bankaccountid) {
        this.bankaccountid = bankaccountid;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public BigDecimal getAccountBalance() {
        return accountBalance;
    }

    public boolean setAccountBalance(BigDecimal accountbalance) {

        if (accountbalance.intValue() < 0)
                return false;

        this.accountBalance = accountbalance;
        return true;
    }

    public String getStatuses() {
        if (statuses==null)
            return "pending";
        return statuses;
    }

    public void setStatuses(String statuses) {
        this.statuses = statuses;
    }

    public int getReviewedBy() {
        return reviewedBy;
    }

    public void setReviewedBy(int reviewedBy) {
        this.reviewedBy = reviewedBy;
    }

    @Override
    public String toString() {
        return "BankAccount{" +
                "bankaccountid=" + bankaccountid +
                ", userid='" + userid + '\'' +
                ", amount=" + accountBalance +
                ", Status='" + statuses+ '\'' +
                '}';
    }
}

