package com.hiaryabeer.receiptapp.Interfaces;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.hiaryabeer.receiptapp.models.ItemsBalance;

import java.util.List;

@Dao
public interface ItemsBalanceDao {
    @Insert
    void addAll(List<ItemsBalance> items);

    @Query("DELETE FROM ItemsBalance_TABLE")
    void deleteAll();
    @Query("SELECT QTY FROM ItemsBalance_TABLE WHERE ItemOCode= :itemocode")
    double getBalance(String itemocode);
    @Query("update ItemsBalance_TABLE set QTY= :qty WHERE ItemOCode= :itemocode")
  int   updateqty(String itemocode,double qty);
}
