package com.hiaryabeer.receiptapp.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "ItemsBalance_TABLE")
public class ItemsBalance {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "COMAPNYNO")
    String COMAPNYNO;
    @ColumnInfo(name = "ItemOCode")
    String   ItemOCode;
    @ColumnInfo(name = "QTY")
    String QTY;
    @ColumnInfo(name = "STOCK_CODE")
    String STOCK_CODE;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCOMAPNYNO() {
        return COMAPNYNO;
    }

    public void setCOMAPNYNO(String COMAPNYNO) {
        this.COMAPNYNO = COMAPNYNO;
    }

    public String getItemOCode() {
        return ItemOCode;
    }

    public void setItemOCode(String itemOCode) {
        ItemOCode = itemOCode;
    }

    public String getQTY() {
        return QTY;
    }

    public void setQTY(String QTY) {
        this.QTY = QTY;
    }

    public String getSTOCK_CODE() {
        return STOCK_CODE;
    }

    public void setSTOCK_CODE(String STOCK_CODE) {
        this.STOCK_CODE = STOCK_CODE;
    }
}
