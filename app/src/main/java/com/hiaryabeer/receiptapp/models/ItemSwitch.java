package com.hiaryabeer.receiptapp.models;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "ItemSwitch_TABLE")
public class ItemSwitch {
    @ColumnInfo(name = "item_Switch")
    private String  item_Switch  ;
    @ColumnInfo(name = "item_NAMEA")
    private String ItemNameA;
    @ColumnInfo(name = "item_OCODE")
    private String ItemOCode;
    @ColumnInfo(name = "item_NCODE")
    private String ItemNCode;
    @PrimaryKey(autoGenerate = true)
    int SERIAL;
    public ItemSwitch() {
    }

    public int getSERIAL() {
        return SERIAL;
    }

    public void setSERIAL(int SERIAL) {
        this.SERIAL = SERIAL;
    }

    public String getItem_Switch() {
        return item_Switch;
    }

    public void setItem_Switch(String item_Switch) {
        this.item_Switch = item_Switch;
    }

    public String getItemNameA() {
        return ItemNameA;
    }

    public void setItemNameA(String itemNameA) {
        this.ItemNameA = itemNameA;
    }

    public String getItemOCode() {
        return ItemOCode;
    }

    public void setItemOCode(String itemOCode) {
        this.ItemOCode = itemOCode;
    }

    public String getItemNCode() {
        return ItemNCode;
    }

    public void setItemNCode(String itemNCode) {
        this.ItemNCode = itemNCode;
    }
}