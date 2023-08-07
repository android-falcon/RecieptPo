package com.hiaryabeer.receiptapp.models;

import android.util.Log;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.json.JSONException;
import org.json.JSONObject;

@Entity(tableName = "ReceiptDetails_Table")
public class ReceiptDetails {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "Serial")
    private int  Serial;

    @ColumnInfo(name = "vhfNo")
    private long vhfNo;

    @ColumnInfo(name = "Date")
    private String date;

    @ColumnInfo(name = "Time")
    private String time;

    @ColumnInfo(name = "Item_No")
    private String itemNo;

    @ColumnInfo(name = "Item_Name")
    private String itemName;

    @ColumnInfo(name = "Qty")
    private double qty;

    @ColumnInfo(name = "Discount")
    private double discount;

    @ColumnInfo(name = "Tax")
    private double tax;


    @ColumnInfo(name = "Total")
    private double total;

    @ColumnInfo(name = "Subtotal")
    private double Subtotal;

    @ColumnInfo(name = "Price")
    private double price;

    @ColumnInfo(name = "Customer_ID")
    private String customerId;
    @ColumnInfo(name = "Unit")
    private String Unit;
    @ColumnInfo(name = "IS_Posted", defaultValue = "0")
    private int isPosted;


    @ColumnInfo(name = "taxPercent")
    private double taxPercent;

    @ColumnInfo(name = "amount")
    private double amount;

    @ColumnInfo(name = "taxValue")
    private double taxValue;

    @ColumnInfo(name = "totalDiscVal")
    private double totalDiscVal;

    @ColumnInfo(name = "Item_Discount",defaultValue = "0")
    double Discount;
    @ColumnInfo(name = "NetTotal",defaultValue = "0")
    private double NetTotal;

    @ColumnInfo(name = "area")
    public String area;


    @ColumnInfo(name = "WhichUNIT")
    public String WhichUNIT;
    @ColumnInfo(name = "WhichUNITSTR")
    public String WhichUNITSTR;
    @ColumnInfo(name = "WHICHUQTY")
    public String WHICHUQTY;

    @ColumnInfo(name = "Paymethod")
    private int Paymethod;

    public int getPaymethod() {
        return Paymethod;
    }

    public void setPaymethod(int paymethod) {
        Paymethod = paymethod;
    }
    @ColumnInfo(name = "VOUCHERTYPE")
    private int  VOUCHERTYPE;

    @ColumnInfo(name = "Free")
    private double  Free;

    @ColumnInfo(name = "TransNo")
    private String TransNo;
    @ColumnInfo(name = "ConvRate")
    String ConvRate;
    @ColumnInfo(name = "CALCQTY")
    public String CALCQTY;
    @ColumnInfo(name = "UNITBARCODE")
    public String  UNITBARCODE;
    @ColumnInfo(name = "NewVochNum", defaultValue = "1")
    private long   NewVochNum;

    public long getNewVochNum() {
        return NewVochNum;
    }

    public void setNewVochNum(long newVochNum) {
        NewVochNum = newVochNum;
    }

    public String getCALCQTY() {
        return CALCQTY;
    }

    public void setCALCQTY(String CALCQTY) {
        this.CALCQTY = CALCQTY;
    }

    public String getUNITBARCODE() {
        return UNITBARCODE;
    }

    public void setUNITBARCODE(String UNITBARCODE) {
        this.UNITBARCODE = UNITBARCODE;
    }

    public String getConvRate() {
        return ConvRate;
    }

    public void setConvRate(String convRate) {
        ConvRate = convRate;
    }

    public String getTransNo() {
        return TransNo;
    }

    public void setTransNo(String transNo) {
        TransNo = transNo;
    }

    public double getFree() {
        return Free;
    }

    public void setFree(double free) {
        Free = free;
    }

    public int getVOUCHERTYPE() {
        return VOUCHERTYPE;
    }

    public void setVOUCHERTYPE(int VOUCHERTYPE) {
        this.VOUCHERTYPE = VOUCHERTYPE;
    }

    public int getSerial() {
        return Serial;
    }

    public void setSerial(int serial) {
        Serial = serial;
    }

    public long getVhfNo() {
        return vhfNo;
    }

    public void setVhfNo(long vhfNo) {
        this.vhfNo = vhfNo;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getItemNo() {
        return itemNo;
    }

    public void setItemNo(String itemNo) {
        this.itemNo = itemNo;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public double getQty() {
        return qty;
    }

    public void setQty(double qty) {
        this.qty = qty;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public double getNetTotal() {
        return NetTotal;
    }

    public void setNetTotal(double netTotal) {
        NetTotal = netTotal;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getWhichUNIT() {
        return WhichUNIT;
    }

    public void setWhichUNIT(String whichUNIT) {
        WhichUNIT = whichUNIT;
    }

    public String getWhichUNITSTR() {
        return WhichUNITSTR;
    }

    public void setWhichUNITSTR(String whichUNITSTR) {
        WhichUNITSTR = whichUNITSTR;
    }

    public String getWHICHUQTY() {
        return WHICHUQTY;
    }

    public void setWHICHUQTY(String WHICHUQTY) {
        this.WHICHUQTY = WHICHUQTY;
    }

    public double getTax() {
        return tax;
    }

    public void setTax(double tax) {
        this.tax = tax;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double getSubtotal() {
        return Subtotal;
    }

    public void setSubtotal(double subtotal) {
        Subtotal = subtotal;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getUnit() {
        return Unit;
    }

    public void setUnit(String unit) {
        Unit = unit;
    }

    public int getIsPosted() {
        return isPosted;
    }

    public void setIsPosted(int isPosted) {
        this.isPosted = isPosted;
    }

    public double getTaxPercent() {
        return taxPercent;
    }

    public void setTaxPercent(double taxPercent) {
        this.taxPercent = taxPercent;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getTaxValue() {
        return taxValue;
    }

    public void setTaxValue(double taxValue) {
        this.taxValue = taxValue;
    }

    public double getTotalDiscVal() {
        return totalDiscVal;
    }

    public void setTotalDiscVal(double totalDiscVal) {
        this.totalDiscVal = totalDiscVal;
    }
    public JSONObject getJSONObjectDelphi() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("VOUCHERNO", NewVochNum+"");
            obj.put("VOUCHERTYPE", VOUCHERTYPE);
            obj.put("ITEMNO", itemNo);
            obj.put("UNIT", Unit);
            try {
                double finalqty=qty*Double.parseDouble(ConvRate);


                obj.put("QTY", finalqty);
                obj.put("ENTERQTY", qty);


            }catch (Exception exception){
                Log.e("catch","exception"+exception.getMessage());
                obj.put("QTY", qty);
                obj.put("ENTERQTY", qty);
            }

            obj.put("UNITPRICE", price);
            obj.put("BONUS", Free);
            obj.put("ITEMDISCOUNTVALUE", totalDiscVal);
            obj.put("ITEMDISCOUNTPRC", discount);
            obj.put("VOUCHERDISCOUNT", discount);
            obj.put("TAXVALUE", taxValue);
            obj.put("TAXPERCENT", taxPercent);
            obj.put("COMAPNYNO", ExportData.CONO);
            obj.put("ISPOSTED", "0");
            obj.put("VOUCHERYEAR", "2023");
            obj.put("ITEM_DESCRITION", "");
            obj.put("SERIAL_CODE", "");
            obj.put("ITEM_SERIAL_CODE", "");

            obj.put("WHICHUNIT", WhichUNIT);
if(WhichUNITSTR!=null)
            obj.put("WHICHUNITSTR", WhichUNITSTR);
else    obj.put("WHICHUNITSTR", "");

            obj.put("WHICHUQTY", ConvRate);
try {

     if(!WhichUNIT.equals("0"))   obj.put("CALCQTY", qty);
    obj.put("CALCQTY", "1");

}catch (Exception exception){

}

            try {
                //      double finalprice=price/Double.parseDouble(ConvRate);
                //   obj.put("ENTERPRICE", finalprice);
                Log.e("ENTERPRICE22==","  "+price);

                obj.put("ENTERPRICE",GeneralMethod.convertToEnglish(String.valueOf(String.format("%.3f",price))));
            }catch (Exception exception){
                //obj.put("ENTERPRICE", price);
            }
          //  obj.put("ENTERPRICE", price);



            if(UNITBARCODE!=null)
         obj.put("UNITBARCODE", UNITBARCODE);
            else
                obj.put("UNITBARCODE", "");
//            obj.put("CALCQTY", "");
            obj.put("ORGVHFNO", "");






        } catch (JSONException e) {
            Log.e("Tag" , "JSONException");
        }
        return obj;
    }
//    public JSONObject getJSONObjectDelphi() {
//        JSONObject obj = new JSONObject();
//        try {
//            obj.put("VOUCHERNO", NewVochNum+"");
//            obj.put("VOUCHERTYPE", VOUCHERTYPE);
//            obj.put("ITEMNO", itemNo);
//            obj.put("UNIT", Unit);
//            try {
//                double finalqty=qty*Double.parseDouble(ConvRate);
//
//
//                obj.put("QTY", finalqty);
//                obj.put("ENTERQTY", finalqty);
//
//
//            }catch (Exception exception){
//                Log.e("catch","exception"+exception.getMessage());
//                obj.put("QTY", qty);
//                obj.put("ENTERQTY", qty);
//            }
//
//            obj.put("UNITPRICE", price);
//            obj.put("BONUS", Free);
//            obj.put("ITEMDISCOUNTVALUE", totalDiscVal);
//            obj.put("ITEMDISCOUNTPRC", discount);
//            obj.put("VOUCHERDISCOUNT", discount);
//            obj.put("TAXVALUE", taxValue);
//            obj.put("TAXPERCENT", taxPercent);
//            obj.put("COMAPNYNO", ExportData.CONO);
//            obj.put("ISPOSTED", "0");
//            obj.put("VOUCHERYEAR", "2023");
//            obj.put("ITEM_DESCRITION", "");
//            obj.put("SERIAL_CODE", "");
//            obj.put("ITEM_SERIAL_CODE", "");
//
//            obj.put("WHICHUNIT", WhichUNIT);
//            if(WhichUNITSTR!=null)
//                obj.put("WHICHUNITSTR", WhichUNITSTR);
//            else    obj.put("WHICHUNITSTR", "");
//
//            obj.put("WHICHUQTY", qty);
//            try {
//                if(!ConvRate.equals(""))
//                    obj.put("CALCQTY", ConvRate);
//                else   obj.put("CALCQTY", "1");
//
//            }catch (Exception exception){
//
//            }
//
//            try {
//                //      double finalprice=price/Double.parseDouble(ConvRate);
//                //   obj.put("ENTERPRICE", finalprice);
//                Log.e("ENTERPRICE22==","  "+price);
//
//                obj.put("ENTERPRICE",GeneralMethod.convertToEnglish(String.valueOf(String.format("%.3f",price))));
//            }catch (Exception exception){
//                //obj.put("ENTERPRICE", price);
//            }
//            //  obj.put("ENTERPRICE", price);
//
//
//
//            if(UNITBARCODE!=null)
//                obj.put("UNITBARCODE", UNITBARCODE);
//            else
//                obj.put("UNITBARCODE", "");
////            obj.put("CALCQTY", "");
//            obj.put("ORGVHFNO", "");
//
//
//
//
//
//
//        } catch (JSONException e) {
//            Log.e("Tag" , "JSONException");
//        }
//        return obj;
//    }

}
