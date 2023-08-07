package com.hiaryabeer.receiptapp.models;

import android.util.Log;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.json.JSONException;
import org.json.JSONObject;

@Entity(tableName = "ReceiptMaster_Table")
public class ReceiptMaster {
    @PrimaryKey
    @ColumnInfo(name = "VHFNO")
    private long vhfNo;
    @ColumnInfo(name = "VOUCHERTYPE")
    private int  VOUCHERTYPE;
    @ColumnInfo(name = "Date")
    private String date;

    @ColumnInfo(name = "Time")
    private String time;

    @ColumnInfo(name = "TransNo")
    private String TransNo;


    @ColumnInfo(name = "Tax")
    private double tax;

    @ColumnInfo(name = "TotalQty")
    private double TotalQty;



    @ColumnInfo(name = "Customer_ID")
    private String customerId;

    @ColumnInfo(name = "IS_Posted", defaultValue = "0")
    private int isPosted;

    @ColumnInfo(name = "NewVochNum", defaultValue = "1")
    private long   NewVochNum;

    @ColumnInfo(name = "subTotal")
    private double subTotal;

    @ColumnInfo(name = "UserNo")
    private String UserNo;

    @ColumnInfo(name = "NetTotal",defaultValue = "0")
    private double NetTotal;

    @ColumnInfo(name = "voucherDiscountPerc")
    private double voucherDiscountPerc;
    @ColumnInfo(name = "voucherDiscountvalu")
    private double voucherDiscountvalue;
    @ColumnInfo(name = "totalVoucherDiscount")
    private double totalVoucherDiscount;
    @ColumnInfo(name = "ConfirmState")
    private int ConfirmState;
    @ColumnInfo(name = "Paymethod")
    private int Paymethod;


    @ColumnInfo(name = "Cust_Name")
    private String Cust_Name;

    public long getNewVochNum() {
        return NewVochNum;
    }

    public void setNewVochNum(long newVochNum) {
        NewVochNum = newVochNum;
    }

    public String getCust_Name() {
        return Cust_Name;
    }

    public void setCust_Name(String cust_Name) {
        Cust_Name = cust_Name;
    }

    public int getPaymethod() {
        return Paymethod;
    }

    public void setPaymethod(int paymethod) {
        Paymethod = paymethod;
    }

    public double getVoucherDiscountvalue() {
        return voucherDiscountvalue;
    }



    public void setVoucherDiscountvalue(double voucherDiscountvalue) {
        this.voucherDiscountvalue = voucherDiscountvalue;
    }

    public String getTransNo() {
        return TransNo;
    }

    public void setTransNo(String transNo) {
        TransNo = transNo;
    }

    @ColumnInfo(name = "Discounttype")//1 for perc and 0 for valu
    private int Discounttype;

    public int getDiscounttype() {
        return Discounttype;
    }

    public void setDiscounttype(int discounttype) {
        Discounttype = discounttype;
    }

    public int getConfirmState() {
        return ConfirmState;
    }

    public void setConfirmState(int confirmState) {
        ConfirmState = confirmState;
    }

    public long getVhfNo() {
        return vhfNo;
    }

    public void setVhfNo(long vhfNo) {
        this.vhfNo = vhfNo;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }



    public double getTax() {
        return tax;
    }

    public void setTax(double tax) {
        this.tax = tax;
    }

    public double getTotalQty() {
        return TotalQty;
    }

    public void setTotalQty(double totalQty) {
        TotalQty = totalQty;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public int getIsPosted() {
        return isPosted;
    }

    public void setIsPosted(int isPosted) {
        this.isPosted = isPosted;
    }

    public double getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(double subTotal) {
        this.subTotal = subTotal;
    }

    public String getUserNo() {
        return UserNo;
    }

    public void setUserNo(String userNo) {
        UserNo = userNo;
    }

    public double getNetTotal() {
        return NetTotal;
    }

    public void setNetTotal(double netTotal) {
        NetTotal = netTotal;
    }

    public double getVoucherDiscountPerc() {
        return voucherDiscountPerc;
    }

    public void setVoucherDiscountPerc(double voucherDiscountPerc) {
        this.voucherDiscountPerc = voucherDiscountPerc;
    }

    public double getTotalVoucherDiscount() {
        return totalVoucherDiscount;
    }

    public void setTotalVoucherDiscount(double totalVoucherDiscount) {
        this.totalVoucherDiscount = totalVoucherDiscount;
    }

    public int getVOUCHERTYPE() {
        return VOUCHERTYPE;
    }

    public void setVOUCHERTYPE(int VOUCHERTYPE) {
        this.VOUCHERTYPE = VOUCHERTYPE;
    }

    public JSONObject getJSONObjectDelphi() {
        JSONObject obj = new JSONObject();
        String voucherDateFormet="";
        //"JSN":[{"COMAPNYNO":290,"VOUCHERYEAR":"2021","VOUCHERNO":"1212","VOUCHERTYPE":"3","VOUCHERDATE":"24/03/2020",
        //      "SALESMANNO":"5","CUSTOMERNO":"123456","VOUCHERDISCOUNT":"50",
        //    "VOUCHERDISCOUNTPERCENT":"10","NOTES":"AAAAAA","CACR":"1","ISPOSTED":"0","PAYMETHOD":"1","NETSALES":"150.720"}]}
        try {
            obj.put("COMAPNYNO", ExportData.CONO);
            obj.put("VOUCHERNO", NewVochNum);
            obj.put("VOUCHERTYPE", VOUCHERTYPE);

            obj.put("VOUCHERDATE", date);
            obj.put("SALESMANNO", "1");
            obj.put("VOUCHERDISCOUNT", voucherDiscountvalue);
            obj.put("VOUCHERDISCOUNTPERCENT", voucherDiscountPerc);

            obj.put("NOTES", UserNo);


            obj.put("CACR", Paymethod);
            obj.put("ISPOSTED", "0");
            obj.put("NETSALES", NetTotal);
            obj.put("CUSTOMERNO", customerId);
            obj.put("VOUCHERYEAR", 2023);

            obj.put("PAYMETHOD", Paymethod);
            obj.put("EXCINC", 0);

        } catch (JSONException e) {
            Log.e("Tag" , "JSONException");
        }
        return obj;
    }
    public JSONObject getJSONObjectDelphi2() {
        JSONObject obj = new JSONObject();
        String voucherDateFormet="";
            try {
            obj.put("COMAPNYNO", ExportData.CONO);
            obj.put("VOUCHERNO", TransNo);
            obj.put("VOUCHERTYPE", VOUCHERTYPE);

            obj.put("VOUCHERDATE", date);
            obj.put("SALESMANNO", "1");
            obj.put("VOUCHERDISCOUNT", voucherDiscountvalue);
            obj.put("VOUCHERDISCOUNTPERCENT", voucherDiscountPerc);

            obj.put("NOTES", UserNo);


            obj.put("CACR", Paymethod);
            obj.put("ISPOSTED", "0");
            obj.put("NETSALES", NetTotal);
            obj.put("CUSTOMERNO", customerId);
            obj.put("VOUCHERYEAR", 2023);

            obj.put("PAYMETHOD", Paymethod);
            obj.put("EXCINC", 0);

        } catch (JSONException e) {
            Log.e("Tag" , "JSONException");
        }
        return obj;
    }
}
