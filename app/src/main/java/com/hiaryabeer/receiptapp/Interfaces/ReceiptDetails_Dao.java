package com.hiaryabeer.receiptapp.Interfaces;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.hiaryabeer.receiptapp.models.ReceiptDetails;

import java.util.List;

@Dao
public interface ReceiptDetails_Dao {

    @Query("SELECT * FROM ReceiptDetails_Table Where IS_Posted='0'")
    List<ReceiptDetails> getAllOrders();
    @Query("UPDATE ReceiptDetails_Table SET  IS_Posted='1' WHERE IS_Posted='0' AND VOUCHERTYPE= :VOUCHERTYPE")
    int  updateVoucherDetailsByType (String VOUCHERTYPE);



    @Query("SELECT * FROM ReceiptDetails_Table Where IS_Posted='0'")
    List<ReceiptDetails> getAllOrdersConfirm();



    @Query("SELECT * FROM ReceiptDetails_Table Where IS_Posted='0' AND VOUCHERTYPE= :VOUCHERTYPE")
    List<ReceiptDetails> getAllOrdersConfirmBytype(String VOUCHERTYPE );

    @Query("SELECT * FROM ReceiptDetails_Table Where NewVochNum= :VHFNO and IS_Posted='0'")
    List<ReceiptDetails> getAllOrdersByNumber(int VHFNO );

    @Query("SELECT * FROM ReceiptDetails_Table  Where Item_No= :itemCode order by Serial DESC  LIMIT 1")
    List<ReceiptDetails> getAllOrdersByCodeSerial(String itemCode );
    @Insert
    void insertAllOrders(ReceiptDetails  receiptDetails);
    @Insert
    void insertOrder(ReceiptDetails  receiptDetails);

    @Delete
    void deleteOrder(ReceiptDetails receiptDetails);

    @Query("UPDATE ReceiptDetails_Table SET  IS_Posted='1' WHERE IS_Posted='0'")
  int  updateVoucherDetails ();

    @Query("delete from ReceiptDetails_Table where NewVochNum= :vohno")
    int deleteOrderByVOHNO(int vohno);

    @Query("SELECT * FROM ReceiptDetails_Table Where NewVochNum= :VHFNO and VOUCHERTYPE= :type")
    List<ReceiptDetails> getOrdersByNumber(long VHFNO ,int type);

    @Query("select * from ReceiptDetails_Table where TransNo= :Tranno and NewVochNum= :vohno")
    List<ReceiptDetails> getOrderByTransNo(long vohno,long Tranno);

}
