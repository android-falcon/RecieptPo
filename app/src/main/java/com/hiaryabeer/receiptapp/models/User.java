package com.hiaryabeer.receiptapp.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.json.JSONException;
import org.json.JSONObject;

@Entity(tableName = "Users_Table")
public class User {

    @NonNull
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "Serial")
    private int serial;

    @ColumnInfo(name = "User_ID")
    private String userId;

    @ColumnInfo(name = "User_Name")
    private String userName;

    @ColumnInfo(name = "User_Password")
    private String userPassword;

    @ColumnInfo(name = "User_Type")
    private int userType;

    @ColumnInfo(name = "Disc_Permission")
    private int discPermission;

    @ColumnInfo(name = "Is_Posted")
    private int isPosted;

    public User(String userId, String userName, String userPassword, int userType, int discPermission, int isPosted) {
        this.isPosted = isPosted;
        this.userId = userId;
        this.userName = userName;
        this.userPassword = userPassword;
        this.userType = userType;
        this.discPermission = discPermission;
    }

    public User() {
        this.isPosted = 0;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public int getDiscPermission() {
        return discPermission;
    }

    public void setDiscPermission(int discPermission) {
        this.discPermission = discPermission;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public int getSerial() {
        return serial;
    }

    public void setSerial(int serial) {
        this.serial = serial;
    }

    public int getIsPosted() {
        return isPosted;
    }

    public void setIsPosted(int isPosted) {
        this.isPosted = isPosted;
    }

    public JSONObject getJSONObject(boolean addUser) {

        JSONObject jsonObject = new JSONObject();

        try {
            if (addUser)
                jsonObject.put("SALESNO", "00000");
            else
                jsonObject.put("SALESNO", userId + "");

            jsonObject.put("ACCNAME", userName);
            jsonObject.put("USER_PASSWORD", userPassword);
            jsonObject.put("USERTYPE", userType);
            jsonObject.put("DISCOUNTPER", discPermission);
            jsonObject.put("ACTIVE_USER", "");

            jsonObject.put("FROM_VOUCHER_SERIAL", "");
            jsonObject.put("TO_VOUCHER_SERIAL", "");
            jsonObject.put("FROM_RETURN_SERIAL", "");
            jsonObject.put("TO_RETURN_SERIAL", "");
            jsonObject.put("FROM_STOCK_SERIAL", "");
            jsonObject.put("TO_STOCK_SERIAL", "");


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

}
