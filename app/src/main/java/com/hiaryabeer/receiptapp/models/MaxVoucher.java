package com.hiaryabeer.receiptapp.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "MaxVoucher_TABLE")
public class MaxVoucher {
   @ColumnInfo(name = "order_requst")

   String order_requst;
   @ColumnInfo(name = "vocher")
   String vocher;
   @PrimaryKey(autoGenerate = true)
   int SERIAL;

   public int getSERIAL() {
      return SERIAL;
   }

   public void setSERIAL(int SERIAL) {
      this.SERIAL = SERIAL;
   }

   public MaxVoucher() {
   }

   public MaxVoucher(String order_requst, String vocher) {
      this.order_requst = order_requst;
      this.vocher = vocher;

   }

   public String getOrder_requst() {
      return order_requst;
   }

   public void setOrder_requst(String order_requst) {
      this.order_requst = order_requst;
   }

   public String getVocher() {
      return vocher;
   }

   public void setVocher(String vocher) {
      this.vocher = vocher;
   }
}
