package com.hiaryabeer.receiptapp.Interfaces;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.hiaryabeer.receiptapp.models.Items;

import java.util.List;

@Dao
public interface Items_Dao {
    @Insert
    void addAll(List<Items> items);

    @Query("DELETE FROM Items_Table")
    void deleteAll();

    @Delete
    void deleteItem(Items item);

    @Query("SELECT * FROM Items_Table WHERE Item_Num= :itemNo OR Item_BARCODE= :itemNo")
   Items getItembyCode(String itemNo);
    @Query("SELECT * FROM Items_Table")
    List<Items>  getAllItems();
    @Query("SELECT * FROM ItemsBalance_TABLE i INNER JOIN Items_Table ii ON i.ItemOCode  =ii.Item_Num AND ((i.QTY)>=1)")
    List<Items>  getAllItemsgraterthanzero();

    @Query("SELECT  ItemsBalance_TABLE.QTY AS AviQty ,Items_Table.id ,ItemsBalance_TABLE.QTY AS BalanceQty,Item_Name ,Item_BARCODE ,Item_Num ,Item_ISAPIPIC ,Item_kind ,Item_F_D ,Item_CATEOGRYID ,TAXPERC ,Items_Table.qty ,amount ,Free ,Item_Discount ,ItemNCode" +
            " FROM Items_Table JOIN ItemsBalance_TABLE " +
            "on Items_Table.Item_Num=ItemsBalance_TABLE.ItemOCode")
    public List<Items> loadListLiveData();


    @Query("SELECT * FROM Items_Table WHERE Item_Num NOT IN (SELECT ItemOCode FROM ItemsBalance_TABLE)")
    public List<Items> loadListLiveData2();
}
