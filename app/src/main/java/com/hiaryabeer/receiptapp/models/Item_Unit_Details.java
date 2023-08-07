package com.hiaryabeer.receiptapp.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Item_Unit_Details")
public class Item_Unit_Details {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "COMAPNYNO")
    private String companyNo;

    @ColumnInfo(name = "ITEMNO")
    private String itemNo;

    @ColumnInfo(name = "UNITID")
    private String unitId;

    @ColumnInfo(name = "CONVRATE")
    private double convRate;
    @ColumnInfo(name = "SALEPRICE",defaultValue = "1")
    private double SALEPRICE;

    public double getSALEPRICE() {
        return SALEPRICE;
    }

    public void setSALEPRICE(double SALEPRICE) {
        this.SALEPRICE = SALEPRICE;
    }

    @ColumnInfo(name = "ITEMBARCODE")
    private String ITEMBARCODE;

    public String getITEMBARCODE() {
        return ITEMBARCODE;
    }

    public void setITEMBARCODE(String ITEMBARCODE) {
        this.ITEMBARCODE = ITEMBARCODE;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCompanyNo() {
        return companyNo;
    }

    public void setCompanyNo(String companyNo) {
        this.companyNo = companyNo;
    }

    public String getItemNo() {
        return itemNo;
    }

    public void setItemNo(String itemNo) {
        this.itemNo = itemNo;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public double getConvRate() {
        return convRate;
    }

    public void setConvRate(double convRate) {
        this.convRate = convRate;
    }
}
