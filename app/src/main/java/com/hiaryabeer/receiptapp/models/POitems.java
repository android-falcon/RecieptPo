package com.hiaryabeer.receiptapp.models;

public class POitems {
  String VHFNo;
   String       ItemOCode;
   String ItemNCode;
   String      Qty;
   String Bonus;
   String     ENTERPRICE;
    String   TransNo;
    String TTAXPERC;
    String  ITEMNAME;
    String  WHICHUQTY;
    String   WHICHUNIT;
    String      WHICHUNITSTR;
    String  ENTERQTY;
    String     UNITBARCODE;
    String  CALCQTY;
    String PriceL;
    String SHOW_WHICHQTY;

    public String getSHOW_WHICHQTY() {
        return SHOW_WHICHQTY;
    }

    public void setSHOW_WHICHQTY(String SHOW_WHICHQTY) {
        this.SHOW_WHICHQTY = SHOW_WHICHQTY;
    }

    public String getPriceL() {
        return PriceL;
    }

    public void setPriceL(String priceL) {
        PriceL = priceL;
    }

    public String getWHICHUNIT() {
        return WHICHUNIT;
    }

    public void setWHICHUNIT(String WHICHUNIT) {
        this.WHICHUNIT = WHICHUNIT;
    }

    public String getWHICHUNITSTR() {
        return WHICHUNITSTR;
    }

    public void setWHICHUNITSTR(String WHICHUNITSTR) {
        this.WHICHUNITSTR = WHICHUNITSTR;
    }

    public String getENTERQTY() {
        return ENTERQTY;
    }

    public void setENTERQTY(String ENTERQTY) {
        this.ENTERQTY = ENTERQTY;
    }

    public String getUNITBARCODE() {
        return UNITBARCODE;
    }

    public void setUNITBARCODE(String UNITBARCODE) {
        this.UNITBARCODE = UNITBARCODE;
    }

    public String getCALCQTY() {
        return CALCQTY;
    }

    public void setCALCQTY(String CALCQTY) {
        this.CALCQTY = CALCQTY;
    }

    public String getWHICHUQTY() {
        return WHICHUQTY;
    }

    public void setWHICHUQTY(String WHICHUQTY) {
        this.WHICHUQTY = WHICHUQTY;
    }

    public String getITEMNAME() {
        return ITEMNAME;
    }

    public void setITEMNAME(String ITEMNAME) {
        this.ITEMNAME = ITEMNAME;
    }

    public String getTTAXPERC() {
        return TTAXPERC;
    }

    public void setTTAXPERC(String TTAXPERC) {
        this.TTAXPERC = TTAXPERC;
    }

    public String getTransNo() {
        return TransNo;
    }

    public void setTransNo(String transNo) {
        TransNo = transNo;
    }

    public String getVHFNo() {
      return VHFNo;
   }

   public void setVHFNo(String VHFNo) {
      this.VHFNo = VHFNo;
   }

   public String getItemOCode() {
      return ItemOCode;
   }

   public void setItemOCode(String itemOCode) {
      ItemOCode = itemOCode;
   }

   public String getItemNCode() {
      return ItemNCode;
   }

   public void setItemNCode(String itemNCode) {
      ItemNCode = itemNCode;
   }

   public String getQty() {
      return Qty;
   }

   public void setQty(String qty) {
      Qty = qty;
   }

   public String getBonus() {
      return Bonus;
   }

   public void setBonus(String bonus) {
      Bonus = bonus;
   }

   public String getENTERPRICE() {
      return ENTERPRICE;
   }

   public void setENTERPRICE(String ENTERPRICE) {
      this.ENTERPRICE = ENTERPRICE;
   }
}
