package com.hiaryabeer.receiptapp.Interfaces;

import androidx.room.Dao;
import androidx.room.Query;

import com.hiaryabeer.receiptapp.models.Items;

import java.util.List;

@Dao
public  interface ItemMaster_ItemBalanceDao {
    //SELECT * FROM Items_Table A RIGHT JOIN ItemsBalance_TABLE B where A.Item_Num =b.ItemOCode
    @Query("SELECT  ItemsBalance_TABLE.QTY AS AviQty ,Items_Table.id ,ItemsBalance_TABLE.QTY AS BalanceQty,Item_Name ,Item_BARCODE ,Item_Num ,Item_ISAPIPIC ,Item_kind ,Item_F_D ,Item_CATEOGRYID ,TAXPERC ,Items_Table.qty ,amount ,Free ,Item_Discount ,ItemNCode" +
            " FROM Items_Table JOIN ItemsBalance_TABLE " +
            "on Items_Table.Item_Num=ItemsBalance_TABLE.ItemOCode")
    public List<Items> loadListLiveData();


    @Query("SELECT * FROM Items_Table WHERE Item_Num NOT IN (SELECT ItemOCode FROM ItemsBalance_TABLE)")
    public List<Items> loadListLiveData2();


    // You can also define this class in a separate file, as long as you add the
    // "public" access modifier.

 static   class ItemMaster_ItemBalance{

     public  int id;
     public  String NAME;

     public  String      BARCODE;

     public  String ITEMNO;

     public  String          ISAPIPIC;

     public  String ItemK;

     public  double     Item_F_D;

     public  String CATEOGRYID;



     public  double     qty;

     public  double amount;

     public  double Free;
     public     double Item_Discount;

     public     double AviQty;

     public     double BalanceQty;



     public    String   ItemOCode;




    }
}
