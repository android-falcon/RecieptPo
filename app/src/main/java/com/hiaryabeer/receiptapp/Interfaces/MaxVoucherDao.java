package com.hiaryabeer.receiptapp.Interfaces;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.hiaryabeer.receiptapp.models.ItemSwitch;
import com.hiaryabeer.receiptapp.models.MaxVoucher;

import java.util.List;

@Dao
public interface MaxVoucherDao {
    @Insert
    public long[] insertAll(List<MaxVoucher> maxVouchers);

    @Insert
    void insert(MaxVoucher maxVoucher);

    @Query("UPDATE MaxVoucher_TABLE SET order_requst= :maxVoucher")
    void UpdateOrder(String maxVoucher);
    @Query("UPDATE MaxVoucher_TABLE SET vocher= :maxVoucher")
    void UpdateVouch(String maxVoucher);

    @Query("Select order_requst from MaxVoucher_TABLE")
    String getMaxOrderSerial();
    @Query("Select vocher from MaxVoucher_TABLE")
    String getMaxVocherSerial();

    @Query("Select * from MaxVoucher_TABLE")
   List<MaxVoucher>  getMaxVocherSerials();

}
