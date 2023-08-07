package com.hiaryabeer.receiptapp.Interfaces;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.hiaryabeer.receiptapp.models.CustomerInfo;

import java.util.List;

@Dao
public interface Customers_Dao {

    @Query("SELECT * FROM Customers_Info Where isVendor=0")
    List<CustomerInfo> getAllCustms();
    @Query("SELECT * FROM Customers_Info Where isVendor=1")
    List<CustomerInfo> getAllVendor();
    @Insert
    void insertAllCustms(CustomerInfo... customerInfos);
    @Insert
    void insertCustm(CustomerInfo customerInfos);

    @Insert
    void addAll(List<CustomerInfo> customers);

    @Query("DELETE FROM Customers_Info")
    void deleteAll();

    @Query("UPDATE Customers_Info SET Is_Posted = '1' WHERE Is_Posted = '0'")
    void setPosted();

    @Query("SELECT * FROM customers_info WHERE Is_Posted = '0'")
    List<CustomerInfo> getUnpostedCust();

    @Delete
    void deleteCustomer(CustomerInfo customerInfo);

    @Query("SELECT Customer_Name FROM Customers_Info where Customer_ID=:id")
    String getCustmByNumber(String id);
    @Query("SELECT Customer_Name FROM Customers_Info")
    List< String> getCustmName();
        @Query("SELECT Customer_ID FROM Customers_Info where Customer_Name=:name")
    int   getCustmByName(String name);

    @Query("SELECT Customer_ID FROM Customers_Info where Customer_Name=:name")
    String   getCustmIdByName(String name);
}
