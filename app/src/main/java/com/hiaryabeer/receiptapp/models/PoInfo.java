package com.hiaryabeer.receiptapp.models;

public class PoInfo {
   String TransNo;
    String      VHFNo;
    String    AccName;
    String    AccCode;

    public String getAccCode() {
        return AccCode;
    }

    public void setAccCode(String accCode) {
        AccCode = accCode;
    }

    public String getAccName() {
        return AccName;
    }

    public void setAccName(String accName) {
        AccName = accName;
    }

    public String getTransNo() {
        return TransNo;
    }

    public void setTransNo(String transNo) {
        TransNo = transNo;
    }

    public String getVHFNo() {
        return VHFNo;
    }

    public void setVHFNo(String VHFNo) {
        this.VHFNo = VHFNo;
    }
}
