package com.hiaryabeer.receiptapp.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.json.JSONException;
import org.json.JSONObject;

@Entity(tableName = "Customers_Info")
public class CustomerInfo {

    @NonNull
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "Serial_No")
    private int serial;


    @ColumnInfo(name = "Customer_ID")
    private String customerId;

    @ColumnInfo(name = "Customer_Name")
    private String customerName;

    @ColumnInfo(name = "Phone_No")
    private String phoneNo;

    @ColumnInfo(name = "Is_Posted")
    private int isPosted;
    @ColumnInfo(name = "isVendor")
    private int isVendor;

    public int getIsVendor() {
        return isVendor;
    }

    public void setIsVendor(int isVendor) {
        this.isVendor = isVendor;
    }

    public CustomerInfo( String customerId, String customerName, String phoneNo, int isPosted, int isVendor) {

        this.customerId = customerId;
        this.customerName = customerName;
        this.phoneNo = phoneNo;
        this.isPosted = isPosted;
        this.isVendor = isVendor;
    }

    public CustomerInfo() {
        this.isPosted = 0;
    }

    public int getSerial() {
        return serial;
    }

    public void setSerial(int serial) {
        this.serial = serial;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public int getIsPosted() {
        return isPosted;
    }

    public void setIsPosted(int isPosted) {
        this.isPosted = isPosted;
    }

    public JSONObject getJSONObject(String userName, String userNo) {

        JSONObject jsonObject = new JSONObject();

        try {

            jsonObject.put("CUSTOMERNAME", customerName + "");

            jsonObject.put("SALESMAN", userName);
            jsonObject.put("SALESMANNO", userNo);

            jsonObject.put("ISPOSTED", isPosted);

            jsonObject.put("MOBILE", phoneNo);

            jsonObject.put("REMARK", "");
            jsonObject.put("LATITUDE", "");
            jsonObject.put("LONGITUDE", "");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }
}
