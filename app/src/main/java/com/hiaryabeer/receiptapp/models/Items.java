package com.hiaryabeer.receiptapp.models;

import android.util.Log;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

@Entity(tableName = "Items_Table")
public class Items{

  @PrimaryKey(autoGenerate = true)
  private int id;

  @ColumnInfo(name = "Item_Name")
  private String NAME;
  @ColumnInfo(name = "Item_BARCODE")
    private String      BARCODE;
  @ColumnInfo(name = "Item_Num")
    private String ITEMNO;
  @ColumnInfo(name = "Item_ISAPIPIC")
    private String          ISAPIPIC;
  @ColumnInfo(name = "Item_kind")
    private String ItemK;
  @ColumnInfo(name = "Item_F_D")
    private double     F_D;
  @ColumnInfo(name = "Item_CATEOGRYID")
    private String CATEOGRYID;
  @ColumnInfo(name = "TAXPERC")
    private double     TAXPERC;
  @ColumnInfo(name = "qty")
  private double     Qty;
  @ColumnInfo(name = "amount")
  private double amount;
  @ColumnInfo(name = "Free")
  private double Free;
  @ColumnInfo(name = "Item_Discount",defaultValue = "0")
  double Discount;
  @ColumnInfo(name = "AviQty",defaultValue = "0")
  double AviQty;
  @ColumnInfo(name = "BalanceQty",defaultValue = "0")
  double BalanceQty;
  @ColumnInfo(name = "ItemNCode")
  String ItemNCode;

  @ColumnInfo(name = "ConvRate")
  String ConvRate;
  @ColumnInfo(name = "WhichUNITSTR")
  public String WhichUNITSTR;


  @ColumnInfo(name = "CALCQTY")
  public String CALCQTY;

  public String getCALCQTY() {
    return CALCQTY;
  }

  public void setCALCQTY(String CALCQTY) {
    this.CALCQTY = CALCQTY;
  }

  public String getWhichUNITSTR() {
    return WhichUNITSTR;
  }
  @ColumnInfo(name = "UNITBARCODE")
  public String  UNITBARCODE;

  public String getUNITBARCODE() {
    return UNITBARCODE;
  }

  public void setUNITBARCODE(String UNITBARCODE) {
    this.UNITBARCODE = UNITBARCODE;
  }

  public void setWhichUNITSTR(String whichUNITSTR) {
    WhichUNITSTR = whichUNITSTR;
  }

  public String getConvRate() {
    return ConvRate;
  }

  public void setConvRate(String convRate) {
    ConvRate = convRate;
  }

  public String getItemNCode() {
    return ItemNCode;
  }

  public void setItemNCode(String itemNCode) {
    ItemNCode = itemNCode;
  }

  public double getBalanceQty() {
    return BalanceQty;
  }

  public void setBalanceQty(double balanceQty) {
    BalanceQty = balanceQty;
  }

  public double getAviQty() {
    return AviQty;
  }

  public void setAviQty(double aviQty) {
    AviQty = aviQty;
  }

  public double getDiscount() {
    return Discount;
  }

  public void setDiscount(double discount) {
    Discount = discount;
  }

  public double getFree() {
    return Free;
  }

  public void setFree(double free) {
    Free = free;
  }

  public double getAmount() {
    return amount;
  }

  public void setAmount(double amount) {
    this.amount = amount;
  }

  public double getQty() {
    return Qty;
  }

  public void setQty(double qty) {
    Qty = qty;
  }

  public String getNAME() {
    return NAME;
  }

  public void setNAME(String NAME) {
    this.NAME = NAME;
  }

  public String getBARCODE() {
    return BARCODE;
  }

  public void setBARCODE(String BARCODE) {
    this.BARCODE = BARCODE;
  }

  public String getITEMNO() {
    return ITEMNO;
  }

  public void setITEMNO(String ITEMNO) {
    this.ITEMNO = ITEMNO;
  }

  public String getISAPIPIC() {
    return ISAPIPIC;
  }

  public void setISAPIPIC(String ISAPIPIC) {
    this.ISAPIPIC = ISAPIPIC;
  }

  public String getItemK() {
    return ItemK;
  }

  public void setItemK(String itemK) {
    ItemK = itemK;
  }

  public double getF_D() {
    return F_D;
  }

  public void setF_D(double f_D) {
    F_D = f_D;
  }

  public String getCATEOGRYID() {
    return CATEOGRYID;
  }

  public void setCATEOGRYID(String CATEOGRYID) {
    this.CATEOGRYID = CATEOGRYID;
  }

  public double getTAXPERC() {
    return TAXPERC;
  }

  public void setTAXPERC(double TAXPERC) {
    this.TAXPERC = TAXPERC;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }
  public static class DataStateDeserializer implements JsonDeserializer<Items> {

    @Override
    public Items deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

      Log.e("deserialize","deserialize");
      Items item = new Gson().fromJson(json, Items.class);
      JsonObject jsonObject = json.getAsJsonObject();

      if (jsonObject.has("Items_Master")) {
        JsonElement elem = jsonObject.get("Items_Master");
        if (elem != null && !elem.isJsonNull()) {
          if(elem.isJsonPrimitive()){
            Log.e("isJsonPrimitive","isJsonPrimitive");
          //  item.setMessage(elem.getAsString());
          }else{
            item.setNAME(elem.getAsJsonObject().get("NAME").getAsString());
            item.setBARCODE(elem.getAsJsonObject().get("BARCODE").getAsString());
            item.setITEMNO(elem.getAsJsonObject().get("ITEMNO").getAsString());

            item.setItemK(elem.getAsJsonObject().get("ItemK").getAsString());
            item.setF_D(Double.parseDouble(elem.getAsJsonObject().get("F_D").getAsString()));
            item.setCATEOGRYID(elem.getAsJsonObject().get("CATEOGRYID").getAsString());

            item.setTAXPERC(Double.parseDouble(elem.getAsJsonObject().get("TAXPERC").getAsString()) / 100);

            Log.e("item",item.getNAME()+"");
            //

          }
        }
      }
      return item ;
    }
  }
}
