package com.hiaryabeer.receiptapp.Acitvits;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.hiaryabeer.receiptapp.R;
import com.hiaryabeer.receiptapp.models.AppDatabase;
import com.hiaryabeer.receiptapp.models.CustomerInfo;
import com.hiaryabeer.receiptapp.models.GeneralMethod;
import com.hiaryabeer.receiptapp.models.ImportData;
import com.hiaryabeer.receiptapp.models.Item_Unit_Details;
import com.hiaryabeer.receiptapp.models.Items;
import com.hiaryabeer.receiptapp.models.ItemsBalance;
import com.hiaryabeer.receiptapp.models.RetrofitInstance;
import com.hiaryabeer.receiptapp.models.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Login extends AppCompatActivity {
 AppCompatButton LoginButton,OpenSetting;
    ImageView editVochNo,saveVochNo;
    public final static String SETTINGS_PREFERENCES = "SETTINGS_PREFERENCES";
    public final static String IP_PREF = "IP_Address";
    public final static String PORT_PREF = "IP_Port";
    public final static String CONO_PREF = "Company_No";
    public final static String maxVoch_PREF = "maxVoch";
    public final static String max_Order_PREF = "Ordermaxserial";
   public static String  ipAddress, ipPort, coNo,Max_Voch,Max_Order;
    private TextInputEditText ipEdt, portEdt, coNoEdt,maxVochEdt,MaxOrderEdt;
    ImportData importData;
    public static List<Item_Unit_Details> allUnitDetails=new ArrayList<>();
    public static List<Item_Unit_Details> allUnitDetails2=new ArrayList<>();
    public static List<CustomerInfo> allCustomers=new ArrayList<>();
    public static List<CustomerInfo> listAllVendor=new ArrayList<>();
    public static List<ItemsBalance> listAlItemsBalances=new ArrayList<>();
    long MaxVo;
    long Maxorder;
    AppDatabase mydatabase;
    EditText unameEdt, passEdt;
    EditText editPassword;
    public static String salmanNumber="";
    public static List<User> allUsers= new ArrayList<>();;
    public static String userNum="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        init();

        if (!checkIpSettings())
            showSettingsDialog();
        importData=new ImportData(Login.this);
    }
    public void init(){
        mydatabase=AppDatabase.getInstanceDatabase(Login.this);
        unameEdt = findViewById(R.id.unameEdt);
        passEdt = findViewById(R.id.passEdt);
        LoginButton=findViewById(R.id.login);
        OpenSetting=findViewById(R.id.OpenSetting);
        OpenSetting.setOnClickListener(onClickListener);
        LoginButton.setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener =new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
             case R.id.login:

                 if (!checkIpSettings()) {
                     showSettingsDialog();

                 } else
                     checkUnameAndPass();
               // startActivity(new Intent(Login.this, MainActivity.class));
                break;

                case R.id.OpenSetting:
                   showSettingsDialog();
                    break;
            }

    }
   };
    private boolean checkIpSettings() {

        SharedPreferences sharedPref = getSharedPreferences(SETTINGS_PREFERENCES, MODE_PRIVATE);
        ipAddress = sharedPref.getString(IP_PREF, "");
        ipPort = sharedPref.getString(PORT_PREF, "");
        coNo = sharedPref.getString(CONO_PREF, "");
        Max_Voch = sharedPref.getString(maxVoch_PREF, "");
        Max_Order  = sharedPref.getString(max_Order_PREF, "");
        Log.e("IP_PREF", ipAddress + "");
        Log.e("PORT_PREF", ipPort);
        Log.e("CONO_PREF", coNo);
        Log.e("Max_Voch",  Max_Voch);
        return !(ipAddress + "").trim().equals("")
                &&
                !(coNo + "").trim().equals("");


    }
    void showSettingsDialog() {

        final Dialog ip_settings_dialog = new Dialog(Login.this);
        ip_settings_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        ip_settings_dialog.setCancelable(false);
        ip_settings_dialog.setContentView(R.layout.ip_settings_dialog);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(ip_settings_dialog.getWindow().getAttributes());
        lp.width = (int) (getResources().getDisplayMetrics().widthPixels / 1.19);
        ip_settings_dialog.getWindow().setAttributes(lp);

        ip_settings_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        ip_settings_dialog.show();
        editVochNo= ip_settings_dialog.findViewById(R.id.editVochNo);

    MaxVo= mydatabase.receiptMaster_dao(). getLastVoherNo();
    Maxorder= mydatabase.receiptMaster_dao(). getLastorderNo();

        Log.e("MaxVo==",MaxVo+""+"Maxorder=="+Maxorder);

        editVochNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                final Dialog dialog = new Dialog(Login.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(true);
                dialog.setContentView(R.layout.passwordsettings);
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(dialog.getWindow().getAttributes());

                lp.gravity = Gravity.CENTER;
                dialog.getWindow().setAttributes(lp);
                Button saveButton = (Button) dialog.findViewById(R.id.saveButton);
//                    TextView cancelButton = (TextView) dialog.findViewById(R.id.cancel);

                editPassword = (EditText) dialog.findViewById(R.id.passowrdEdit);


                saveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!editPassword.getText().toString().equals("")) {
                            if(editPassword.getText().toString().trim().equals("1234"))
                            {


                                maxVochEdt.setEnabled(true);
                                maxVochEdt.requestFocus();
                                dialog.dismiss();



                            }else
                            {
                                editPassword.setError("password not correct");
                            }
                        } else {
                            editPassword.setError("Required");
                        }


                    }
                });
//
//                    cancelButton.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            dialog.dismiss();
//                        }
//                    });
                dialog.show();



            }
        });
        ipEdt = ip_settings_dialog.findViewById(R.id.ipEdt);
        portEdt = ip_settings_dialog.findViewById(R.id.portEdt);
        coNoEdt = ip_settings_dialog.findViewById(R.id.coNoEdt);
        maxVochEdt= ip_settings_dialog.findViewById(R.id.MaxVoEdt);
        MaxOrderEdt= ip_settings_dialog.findViewById(R.id.OrderMaxEdt);
        MaxOrderEdt.setEnabled(true);
        maxVochEdt.setEnabled(true);
        TextInputLayout textInputIpAddress, textInputPort, textInputCoNo,textInputMaxVoch,textInputOrderMaxNu;
        textInputIpAddress = ip_settings_dialog.findViewById(R.id.textInputIpAddress);
        textInputPort = ip_settings_dialog.findViewById(R.id.portInputIpAddress);
        textInputCoNo = ip_settings_dialog.findViewById(R.id.textInputCoNo);
        textInputMaxVoch = ip_settings_dialog.findViewById(R.id.textInputMaxNu);
        textInputOrderMaxNu= ip_settings_dialog.findViewById(R.id.textInputOrderMaxNu);
        Button okBtn, cancelBtn;
        okBtn = ip_settings_dialog.findViewById(R.id.submitBtn);
        cancelBtn = ip_settings_dialog.findViewById(R.id.cancel_btn);

        textInputMaxVoch.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!maxVochEdt.getText().toString().trim().equals(""))
                {
                    Log.e("MaxVo==",MaxVo+"");
                    if(!MaxOrderEdt.getText().toString().equals("")) {
                        if (Long.parseLong(maxVochEdt.getText().toString().trim()) != Long.parseLong(MaxOrderEdt.getText().toString().trim()))
                            if (Long.parseLong(maxVochEdt.getText().toString().trim()) > MaxVo) {
                                maxVochEdt.setEnabled(false);
                                SharedPreferences.Editor editor = getSharedPreferences(SETTINGS_PREFERENCES, MODE_PRIVATE).edit();
                                editor.putString(maxVoch_PREF, maxVochEdt.getText().toString().trim());

                                editor.apply();
                            } else {
                                //  maxVochEdt.setText("");
                                maxVochEdt.setText(String.valueOf(MaxVo + 1));

                                if (MaxVo != 0)
                                    GeneralMethod.showSweetDialog(Login.this, 0, "", getResources().getString(R.string.MaxVoMsg1) + " " + MaxVo + getResources().getString(R.string.MaxVoMsg2) + (MaxVo + 1) + "  " + getResources().getString(R.string.MaxVoMsg3));
                                else
                                    GeneralMethod.showSweetDialog(Login.this, 0, "", getResources().getString(R.string.MaxVoMsg2) + (MaxVo + 1) + "  " + getResources().getString(R.string.MaxVoMsg3));

                                maxVochEdt.setError("");
                            }
                        else {
                            maxVochEdt.setText(String.valueOf(MaxVo + 1));
                            maxVochEdt.setError("");
                            GeneralMethod.showSweetDialog(Login.this, 0, "", getResources().getString(R.string.MaxVoMsg6));

                        }
                    }else
                    {
                        if (Long.parseLong(maxVochEdt.getText().toString().trim()) > MaxVo) {
                            maxVochEdt.setEnabled(false);
                            SharedPreferences.Editor editor = getSharedPreferences(SETTINGS_PREFERENCES, MODE_PRIVATE).edit();
                            editor.putString(maxVoch_PREF, maxVochEdt.getText().toString().trim());

                            editor.apply();
                        } else {
                            //  maxVochEdt.setText("");
                            maxVochEdt.setText(String.valueOf(MaxVo + 1));

                            if (MaxVo != 0)
                                GeneralMethod.showSweetDialog(Login.this, 0, "", getResources().getString(R.string.MaxVoMsg1) + " " + MaxVo + getResources().getString(R.string.MaxVoMsg2) + (MaxVo + 1) + "  " + getResources().getString(R.string.MaxVoMsg3));
                            else
                                GeneralMethod.showSweetDialog(Login.this, 0, "", getResources().getString(R.string.MaxVoMsg2) + (MaxVo + 1) + "  " + getResources().getString(R.string.MaxVoMsg3));

                            maxVochEdt.setError("");
                        }
                    }
                }
            }
        });
        textInputOrderMaxNu.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!MaxOrderEdt.getText().toString().trim().equals(""))
                {
                    Log.e("MaxVo==",MaxVo+"");
                    if(!maxVochEdt.getText().toString().equals(""))
                    {  if(    Long.parseLong(MaxOrderEdt.getText().toString().trim())!=Long.parseLong(maxVochEdt.getText().toString().trim()))
                    if(Long.parseLong(MaxOrderEdt.getText().toString().trim())>Maxorder)
                    {
                        MaxOrderEdt.setEnabled(false);
                        SharedPreferences.Editor editor = getSharedPreferences(SETTINGS_PREFERENCES, MODE_PRIVATE).edit();

                        editor.putString(max_Order_PREF,  MaxOrderEdt.getText().toString().trim());
                        editor.apply();
                    }
                    else
                    {
                        //  maxVochEdt.setText("");
                        MaxOrderEdt.setText(String.valueOf(Maxorder+1));

                        if(Maxorder!=0)
                            GeneralMethod.showSweetDialog(Login.this,0,"",getResources().getString(R.string.MaxVoMsg1)+" "+Maxorder+getResources().getString(R.string.MaxVoMsg2)+(Maxorder+1)+"  " +getResources().getString(R.string.MaxVoMsg3));
                        else
                            GeneralMethod.showSweetDialog(Login.this,0,"",getResources().getString(R.string.MaxVoMsg2)+(Maxorder+1)+"  " +getResources().getString(R.string.MaxVoMsg3));

                        MaxOrderEdt.setError("");
                    }else {
                    maxVochEdt.setText(String.valueOf(MaxVo + 1));
                    maxVochEdt.setError("");
                    GeneralMethod.showSweetDialog(Login.this, 0, "", getResources().getString(R.string.MaxVoMsg5) );

                }
                    }else
                    {
                        if(Long.parseLong(MaxOrderEdt.getText().toString().trim())>Maxorder)
                        {
                            MaxOrderEdt.setEnabled(false);
                            SharedPreferences.Editor editor = getSharedPreferences(SETTINGS_PREFERENCES, MODE_PRIVATE).edit();

                            editor.putString(max_Order_PREF,  MaxOrderEdt.getText().toString().trim());
                            editor.apply();
                        }
                        else
                        {
                            //  maxVochEdt.setText("");
                            MaxOrderEdt.setText(String.valueOf(Maxorder+1));

                            if(Maxorder!=0)
                                GeneralMethod.showSweetDialog(Login.this,0,"",getResources().getString(R.string.MaxVoMsg1)+" "+Maxorder+getResources().getString(R.string.MaxVoMsg2)+(Maxorder+1)+"  " +getResources().getString(R.string.MaxVoMsg3));
                            else
                                GeneralMethod.showSweetDialog(Login.this,0,"",getResources().getString(R.string.MaxVoMsg2)+(Maxorder+1)+"  " +getResources().getString(R.string.MaxVoMsg3));

                            MaxOrderEdt.setError("");
                        }
                    }

                }
            }
        });
        SharedPreferences sharedPref = getSharedPreferences(SETTINGS_PREFERENCES, MODE_PRIVATE);

        ipEdt.setText(sharedPref.getString(IP_PREF, ""));
        portEdt.setText(sharedPref.getString(PORT_PREF, ""));
        coNoEdt.setText(sharedPref.getString(CONO_PREF, ""));
        maxVochEdt.setText(sharedPref.getString(maxVoch_PREF, ""));
        MaxOrderEdt.setText(sharedPref.getString(max_Order_PREF, ""));
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ip_settings_dialog.dismiss();

            }
        });

        ipEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                textInputIpAddress.setError(null);

            }
        });

//        portEdt.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//                textInputPort.setError(null);
//
//            }
//        });

        coNoEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                textInputCoNo.setError(null);

            }
        });
        maxVochEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                textInputMaxVoch.setError(null);

            }
        });
//        MaxOrderEdt.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//                textInputMaxVoch.setError(null);
//
//            }
//        });

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String ipAddress1 = ipEdt.getText().toString().trim();
                String port1 = portEdt.getText().toString().trim();
                String coNo1 = coNoEdt.getText().toString().trim();
                String maxvo1 =maxVochEdt.getText().toString().trim();
                String maxOrder =MaxOrderEdt.getText().toString().trim();
                if (maxvo1.equals(""))maxvo1="1";
                if (!ipAddress1.equals("")) {



                    if (!coNo1.equals(""))
                    {
                        if (!maxvo1.equals(""))
                        {
                            RetrofitInstance.Instance=null;
                                SharedPreferences.Editor editor = getSharedPreferences(SETTINGS_PREFERENCES, MODE_PRIVATE).edit();
                                editor.putString(IP_PREF, ipAddress1);
                                editor.putString(PORT_PREF, port1);
                                editor.putString(CONO_PREF, coNo1);

                                if(!maxOrder.equals(""))
                                    editor.putString(max_Order_PREF, maxOrder);
                                else
                                    editor.putString(max_Order_PREF, "1");

                                if(maxvo1.trim().length()!=0)
                                    editor.putString(maxVoch_PREF, maxvo1);
                                else
                                    editor.putString(maxVoch_PREF, "1");
                                editor.apply();

                                ipAddress = sharedPref.getString(IP_PREF, "");
                                ipPort = sharedPref.getString(PORT_PREF, "");
                                coNo = sharedPref.getString(CONO_PREF, "");
                                Max_Voch = sharedPref.getString(maxVoch_PREF, "");
                                Max_Order = sharedPref.getString(max_Order_PREF, "");

                                ip_settings_dialog.dismiss();
                                mydatabase.itemsDao().deleteAll();
                                mydatabase.itemUnitsDao().deleteAll();
                                mydatabase.customers_dao().deleteAll();
                                mydatabase.itemsBalanceDao().deleteAll();
                            mydatabase.itemSwitchDao().dELETEAll();
                            mydatabase.usersDao().deleteAll();
                                listAlItemsBalances.clear();
//                            appDatabase.usersDao().deleteAll();
                                mydatabase.itemUnitsDao().deleteAll();
                                ImportData.AllImportItemlist.clear();
                                allUnitDetails.clear();
                            allUnitDetails2.clear();
                                allCustomers.clear();

                                listAllVendor.clear();
                                allUsers.clear();
                                importData=new ImportData(Login.this);
                                //          importData.getAllItems3();


                                importData.getAllItems(new ImportData.GetItemsCallBack() {
                                    @Override
                                    public void onResponse(JSONObject response) {

                                        try {
                                           // response.toString().getBytes(StandardCharsets.UTF_8);
                                            JSONArray itemsArray = response.getJSONArray("Items_Master");

                                            for (int i = 0; i < itemsArray.length(); i++) {

                                                Items item = new Items();
                                                item.setNAME(itemsArray.getJSONObject(i).getString("NAME"));
                                                item.setBARCODE(itemsArray.getJSONObject(i).getString("BARCODE"));
                                                item.setITEMNO(itemsArray.getJSONObject(i).getString("ITEMNO"));

                                                item.setItemK(itemsArray.getJSONObject(i).getString("ItemK"));
                                                item.setF_D(Double.parseDouble(itemsArray.getJSONObject(i).getString("F_D")));
                                                item.setCATEOGRYID(itemsArray.getJSONObject(i).getString("CATEOGRYID"));

                                                item.setTAXPERC(Double.parseDouble(itemsArray.getJSONObject(i).getString("TAXPERC")) / 100);
                                                item.setQty(1);

                                                ImportData.AllImportItemlist.add(item);

                                            }

                                            mydatabase.itemsDao().addAll(ImportData.AllImportItemlist);

                                            JSONArray unitsArray = response.getJSONArray("Item_Unit_Details2");

                                            for (int i = 0; i < unitsArray.length(); i++) {
//[{"ITEMU":"كرتونة","ITEMOCODE":"6294003583712","ITEMBARCODE":"6294003583729","CALCQTY":"12","SALEPRICE":"39","PCLASS1":"0","PCLASS2":"0","PCLASS3":"0"},
                                                Item_Unit_Details itemUnitDetails = new Item_Unit_Details();
                                            //    itemUnitDetails.setCompanyNo(unitsArray.getJSONObject(i).getString("COMAPNYNO"));
                                                itemUnitDetails.setItemNo(unitsArray.getJSONObject(i).getString("ITEMOCODE"));
                                                itemUnitDetails.setUnitId(unitsArray.getJSONObject(i).getString("ITEMU"));
                                                itemUnitDetails.setConvRate(Double.parseDouble(unitsArray.getJSONObject(i).getString("CALCQTY")));
                                                itemUnitDetails.setSALEPRICE(Double.parseDouble(unitsArray.getJSONObject(i).getString("SALEPRICE")));
                                                itemUnitDetails.setITEMBARCODE(unitsArray.getJSONObject(i).getString("ITEMBARCODE"));

                                                Log.e("ITEMBARCODE==",unitsArray.getJSONObject(i).getString("ITEMBARCODE"));
                                                allUnitDetails.add(itemUnitDetails);

                                            }

                                            mydatabase.itemUnitsDao().addAll(allUnitDetails);
                                            //
                                            JSONArray unitsArray2 = response.getJSONArray("Item_Unit_Details");

                                            for (int i = 0; i < unitsArray2.length(); i++) {
                                                //[{"COMAPNYNO":"290","ITEMNO":"0006","UNITID":"كرتونة","CONVRATE":"1"}
                                                Item_Unit_Details itemUnitDetails = new Item_Unit_Details();
                                                //    itemUnitDetails.setCompanyNo(unitsArray.getJSONObject(i).getString("COMAPNYNO"));
                                                itemUnitDetails.setItemNo(unitsArray2.getJSONObject(i).getString("ITEMNO"));
                                                itemUnitDetails.setUnitId(unitsArray2.getJSONObject(i).getString("UNITID"));
                                                itemUnitDetails.setConvRate(Double.parseDouble(unitsArray2.getJSONObject(i).getString("CONVRATE")));
                                                itemUnitDetails.setSALEPRICE(1);
                                                itemUnitDetails.setITEMBARCODE("");

                                                allUnitDetails2.add(itemUnitDetails);
                                            }
                                            mydatabase.itemUnitsDao().addAll(allUnitDetails2);

                                            try {
                                                JSONArray BalanceitemsArray = response.getJSONArray("SalesMan_Items_Balance");

                                                for (int i = 0; i < BalanceitemsArray.length(); i++) {
                                                    ItemsBalance itemsBalance = new ItemsBalance();

                                                    itemsBalance.setCOMAPNYNO(BalanceitemsArray.getJSONObject(i).getString("COMAPNYNO"));
                                                    itemsBalance.setQTY(BalanceitemsArray.getJSONObject(i).getString("QTY"));
                                                    itemsBalance.setItemOCode(BalanceitemsArray.getJSONObject(i).getString("ItemOCode"));
                                                    itemsBalance.setSTOCK_CODE(BalanceitemsArray.getJSONObject(i).getString("STOCK_CODE"));

                                                    listAlItemsBalances.add(itemsBalance);
                                                }
                                                Log.e("listAlItemsBalances===",listAlItemsBalances.size()+"");
                                                mydatabase.itemsBalanceDao().addAll(listAlItemsBalances);


                                            }catch (Exception e)
                                            {
                                                Log.e("Exception===",e.getMessage()+"");
                                            }

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }


                                        importData.getAllCustomers(new ImportData.GetCustomersCallBack() {
                                            @Override
                                            public void onResponse(JSONArray response) {

                                                for (int i = 0; i < response.length(); i++) {

                                                    try {

                                                        allCustomers.add(new CustomerInfo(
                                                                response.getJSONObject(i).getString("CUSTID"),
                                                                response.getJSONObject(i).getString("CUSTNAME"),
                                                                response.getJSONObject(i).getString("MOBILE"),
                                                                1,0));

                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }

                                                }

                                                mydatabase.customers_dao().addAll(allCustomers);

                                                importData.getAllUsers(new ImportData.GetUsersCallBack() {
                                                    @Override
                                                    public void onResponse(JSONArray response) {


                                                        for (int i = 0; i < response.length(); i++) {

                                                            try {
                                                                         if(!response.getJSONObject(i).getString("USERTYPE").equals(""))
                                                                allUsers.add(new User(
                                                                        response.getJSONObject(i).getString("SALESNO"),
                                                                        response.getJSONObject(i).getString("ACCNAME").toLowerCase(Locale.ROOT),
                                                                        response.getJSONObject(i).getString("USER_PASSWORD"),
                                                                        Integer.parseInt(response.getJSONObject(i).getString("USERTYPE")),
                                                                        Integer.parseInt("1"),
                                                                        1));
                                                                         else
                                                                             allUsers.add(new User(
                                                                                     response.getJSONObject(i).getString("SALESNO"),
                                                                                     response.getJSONObject(i).getString("ACCNAME").toLowerCase(Locale.ROOT),
                                                                                     response.getJSONObject(i).getString("USER_PASSWORD"),
                                                                                     Integer.parseInt("0"),
                                                                                     Integer.parseInt("1"),
                                                                                     1));

                                                            } catch (JSONException e) {
                                                                e.printStackTrace();
                                                            }

                                                        }

                                                        mydatabase.usersDao().addAll(allUsers);

                                                        importData.getAllVendor(new ImportData.GetUsersCallBack() {
                                                            @Override
                                                            public void onResponse(JSONArray response) {


                                                                for (int i = 0; i < response.length(); i++) {

                                                                    try {
                                                                        CustomerInfo vendourInfo = new CustomerInfo();
                                                                        vendourInfo.setCustomerName( response.getJSONObject(i).getString("AccNameA"));
                                                                        vendourInfo.setCustomerId( response.getJSONObject(i).getString("AccCode"));
                                                                        vendourInfo.setIsVendor(1);
                                                                        // vendourInfo.setSelect(0);
                                                                        listAllVendor.add(vendourInfo);

//

                                                                    } catch (JSONException e) {
                                                                        e.printStackTrace();
                                                                    }

                                                                }
                                                                mydatabase.customers_dao().addAll(listAllVendor);
//                                                                importData.getItemsBalance(new ImportData.GetItemsBalanceCallBack() {
//                                                                    @Override
//                                                                    public void onResponse(JSONObject response) {
//                                                                        try {
//                                                                            JSONArray itemsArray = response.getJSONArray("SalesMan_Items_Balance");
//
//                                                                            for (int i = 0; i < itemsArray.length(); i++) {
//                                                                                ItemsBalance itemsBalance = new ItemsBalance();
//
//                                                                                itemsBalance.setCOMAPNYNO(itemsArray.getJSONObject(i).getString("COMAPNYNO"));
//                                                                                itemsBalance.setQTY(itemsArray.getJSONObject(i).getString("QTY"));
//                                                                                itemsBalance.setItemOCode(itemsArray.getJSONObject(i).getString("ItemOCode"));
//                                                                                itemsBalance.setSTOCK_CODE(itemsArray.getJSONObject(i).getString("STOCK_CODE"));
//
//                                                                                listAlItemsBalances.add(itemsBalance);
//                                                                            }
//                                                                            Log.e("listAlItemsBalances===",listAlItemsBalances.size()+"");
//                                                                            mydatabase.itemsBalanceDao().addAll(listAlItemsBalances);
//                                                                          //  ImportData importData=new ImportData(Login.this);
//                                                                       importData. fetchItemSwitchData("01/01/2020","01/12/2040");
//
//                                                                        }catch (Exception e)
//                                                                        {
//                                                                            Log.e("Exception===",e.getMessage()+"");
//                                                                        }
//
////
//                                                                    }
//
//                                                                    @Override
//                                                                    public void onError(String error) {
//
//                                                                    }
//                                                                });
                                                                ImportData importData=new ImportData(Login.this);
                                                                importData. fetchItemSwitchData("01/01/2020","01/12/2040");

                                                            }

                                                            @Override
                                                            public void onError(String error) {


                                                            }
                                                        }, ipAddress, ipPort, coNo);
                                                    }

                                                    @Override
                                                    public void onError(String error) {


                                                    }
                                                }, ipAddress, ipPort, coNo);




                                            }

                                            @Override
                                            public void onError(String error) {

                                                if (!((error + "").contains("SSLHandshakeException") || (error + "").equals("null") ||
                                                        (error + "").contains("ConnectException") || (error + "").contains("NoRouteToHostException"))) {

                                                }


                                            }
                                        });
                                    }

                                    @Override
                                    public void onError(String error) {
                                                    Log.e("onError",error+"");
                                        if (!((error + "").contains("SSLHandshakeException") || (error + "").equals("null") ||
                                                (error + "").contains("ConnectException") || (error + "").contains("NoRouteToHostException"))) {


                                        }

                                    }
                                });



                                ip_settings_dialog.dismiss();

                        } else {

                            textInputMaxVoch.setError(getString(R.string.required));


                        }
                    } else {

                        textInputCoNo.setError(getString(R.string.required));


                    }



                } else {

                    textInputIpAddress.setError(getString(R.string.required));


                }

            }
        });
//        okBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                String ipAddress1 = ipEdt.getText().toString().trim();
//                String port1 = portEdt.getText().toString().trim();
//                String coNo1 = coNoEdt.getText().toString().trim();
//              String maxvo1 =maxVochEdt.getText().toString().trim();
//                String maxOrder =MaxOrderEdt.getText().toString().trim();
//                if (!ipAddress1.equals("")) {
//
//
//
//                        if (!coNo1.equals(""))
//                        {
//                            if (!maxvo1.equals(""))
//                            {
//                                if (!maxOrder.equals(""))
//                                {
//                            SharedPreferences.Editor editor = getSharedPreferences(SETTINGS_PREFERENCES, MODE_PRIVATE).edit();
//                            editor.putString(IP_PREF, ipAddress1);
//                            editor.putString(PORT_PREF, port1);
//                            editor.putString(CONO_PREF, coNo1);
//
//                            if(!maxOrder.equals(""))
//                                editor.putString(max_Order_PREF, maxOrder);
//                            else
//                                editor.putString(max_Order_PREF, "1");
//
//                           if(maxvo1.trim().length()!=0)
//                               editor.putString(maxVoch_PREF, maxvo1);
//                           else
//                               editor.putString(maxVoch_PREF, "1");
//                            editor.apply();
//
//                            ipAddress = sharedPref.getString(IP_PREF, "");
//                            ipPort = sharedPref.getString(PORT_PREF, "");
//                            coNo = sharedPref.getString(CONO_PREF, "");
//                            Max_Voch = sharedPref.getString(maxVoch_PREF, "");
//                            Max_Order = sharedPref.getString(max_Order_PREF, "");
//                            ip_settings_dialog.dismiss();
//                          mydatabase.itemsDao().deleteAll();
//                            mydatabase.itemUnitsDao().deleteAll();
//                        mydatabase.customers_dao().deleteAll();
//                                    mydatabase.itemsBalanceDao().deleteAll();
//                                    listAlItemsBalances.clear();
////                            appDatabase.usersDao().deleteAll();
//                            mydatabase.itemUnitsDao().deleteAll();
//                            ImportData.AllImportItemlist.clear();
//                            allUnitDetails.clear();
//                            allCustomers.clear();
//
//                                    listAllVendor.clear();
//                           allUsers.clear();
//                            importData=new ImportData(Login.this);
//                //          importData.getAllItems3();
//
//
//                            importData.getAllItems(new ImportData.GetItemsCallBack() {
//                                @Override
//                                public void onResponse(JSONObject response) {
//
//                                    try {
//
//                                        JSONArray itemsArray = response.getJSONArray("Items_Master");
//
//                                        for (int i = 0; i < itemsArray.length(); i++) {
//
//                                            Items item = new Items();
//                                            item.setNAME(itemsArray.getJSONObject(i).getString("NAME"));
//                                            item.setBARCODE(itemsArray.getJSONObject(i).getString("BARCODE"));
//                                            item.setITEMNO(itemsArray.getJSONObject(i).getString("ITEMNO"));
//
//                                            item.setItemK(itemsArray.getJSONObject(i).getString("ItemK"));
//                                            item.setF_D(Double.parseDouble(itemsArray.getJSONObject(i).getString("F_D")));
//                                            item.setCATEOGRYID(itemsArray.getJSONObject(i).getString("CATEOGRYID"));
//
//                                            item.setTAXPERC(Double.parseDouble(itemsArray.getJSONObject(i).getString("TAXPERC")) / 100);
//                                            item.setQty(1);
//
//                                            ImportData.AllImportItemlist.add(item);
//
//                                        }
//
//                                        mydatabase.itemsDao().addAll(ImportData.AllImportItemlist);
//
//                                        JSONArray unitsArray = response.getJSONArray("Item_Unit_Details");
//
//                                        for (int i = 0; i < unitsArray.length(); i++) {
//
//                                            Item_Unit_Details itemUnitDetails = new Item_Unit_Details();
//                                            itemUnitDetails.setCompanyNo(unitsArray.getJSONObject(i).getString("COMAPNYNO"));
//                                            itemUnitDetails.setItemNo(unitsArray.getJSONObject(i).getString("ITEMNO"));
//                                            itemUnitDetails.setUnitId(unitsArray.getJSONObject(i).getString("UNITID"));
//                                            itemUnitDetails.setConvRate(Double.parseDouble(unitsArray.getJSONObject(i).getString("CONVRATE")));
//
//                                            allUnitDetails.add(itemUnitDetails);
//
//                                        }
//
//                                        mydatabase.itemUnitsDao().addAll(allUnitDetails);
//
//                                    } catch (JSONException e) {
//                                        e.printStackTrace();
//                                    }
//
//
//                                    importData.getAllCustomers(new ImportData.GetCustomersCallBack() {
//                                        @Override
//                                        public void onResponse(JSONArray response) {
//
//                                            for (int i = 0; i < response.length(); i++) {
//
//                                                try {
//
//                                                    allCustomers.add(new CustomerInfo(
//                                                            response.getJSONObject(i).getString("CUSTID"),
//                                                            response.getJSONObject(i).getString("CUSTNAME"),
//                                                            response.getJSONObject(i).getString("MOBILE"),
//                                                            1,0));
//
//                                                } catch (JSONException e) {
//                                                    e.printStackTrace();
//                                                }
//
//                                            }
//
//                                            mydatabase.customers_dao().addAll(allCustomers);
//
//                                            importData.getAllUsers(new ImportData.GetUsersCallBack() {
//                                                @Override
//                                                public void onResponse(JSONArray response) {
//
//
//                                                    for (int i = 0; i < response.length(); i++) {
//
//                                                        try {
//
//                                                            allUsers.add(new User(
//                                                                    response.getJSONObject(i).getString("SALESNO"),
//                                                                    response.getJSONObject(i).getString("ACCNAME").toLowerCase(Locale.ROOT),
//                                                                    response.getJSONObject(i).getString("USER_PASSWORD"),
//                                                                    Integer.parseInt(response.getJSONObject(i).getString("USERTYPE")),
//                                                                    Integer.parseInt("1"),
//                                                                    1));
//
//                                                        } catch (JSONException e) {
//                                                            e.printStackTrace();
//                                                        }
//
//                                                    }
//
//                                                    mydatabase.usersDao().addAll(allUsers);
//
//                                                    importData.getAllVendor(new ImportData.GetUsersCallBack() {
//                                                        @Override
//                                                        public void onResponse(JSONArray response) {
//
//
//                                                            for (int i = 0; i < response.length(); i++) {
//
//                                                                try {
//                                                                    CustomerInfo vendourInfo = new CustomerInfo();
//                                                                    vendourInfo.setCustomerName( response.getJSONObject(i).getString("AccNameA"));
//                                                                    vendourInfo.setCustomerId( response.getJSONObject(i).getString("AccCode"));
//                                                                    vendourInfo.setIsVendor(1);
//                                                                    // vendourInfo.setSelect(0);
//                                                                    listAllVendor.add(vendourInfo);
//
////                                                            allUsers.add(new User(
////                                                                    response.getJSONObject(i).getString(""),
////                                                                    response.getJSONObject(i).getString("ACCNAME").toLowerCase(Locale.ROOT),
////                                                                    response.getJSONObject(i).getString("USER_PASSWORD"),
////                                                                    Integer.parseInt(response.getJSONObject(i).getString("USERTYPE")),
////                                                                    Integer.parseInt("1"),
////                                                                    1));
//
//                                                                } catch (JSONException e) {
//                                                                    e.printStackTrace();
//                                                                }
//
//                                                            }
//                                                            mydatabase.customers_dao().addAll(listAllVendor);
//                                                            importData.getItemsBalance(new ImportData.GetItemsBalanceCallBack() {
//                                                                @Override
//                                                                public void onResponse(JSONObject response) {
//                                                                    try {
//                                                                        JSONArray itemsArray = response.getJSONArray("SalesMan_Items_Balance");
//
//                                                                        for (int i = 0; i < itemsArray.length(); i++) {
//                                                                            ItemsBalance itemsBalance = new ItemsBalance();
//
//                                                                                itemsBalance.setCOMAPNYNO(itemsArray.getJSONObject(i).getString("COMAPNYNO"));
//                                                                                itemsBalance.setQTY(itemsArray.getJSONObject(i).getString("QTY"));
//                                                                                itemsBalance.setItemOCode(itemsArray.getJSONObject(i).getString("ItemOCode"));
//                                                                                itemsBalance.setSTOCK_CODE(itemsArray.getJSONObject(i).getString("STOCK_CODE"));
//
//                                                                                listAlItemsBalances.add(itemsBalance);
//                                                                        }
//                                                                        Log.e("listAlItemsBalances===",listAlItemsBalances.size()+"");
//                                                                        mydatabase.itemsBalanceDao().addAll(listAlItemsBalances);
//                                                                       importData. fetchItemSwitchData("01/01/2021","31/12/2022");
//                                                                    }catch (Exception e)
//                                                                    {
//                                                                        Log.e("Exception===",e.getMessage()+"");
//                                                                    }
//
////
//                                                                }
//
//                                                                @Override
//                                                                public void onError(String error) {
//
//                                                                }
//                                                            });
////                                                            importData.getItemsBalance(new ImportData.GetItemsBalanceCallBack() {
////                                                                @Override
////                                                                public void onResponse(JSONObject response) {
////                                                                    try {
////                                                                        JSONArray itemsArray = response.getJSONArray("SalesMan_Items_Balance");
////                                                                        for (int i = 0; i < response.length(); i++) {
////
////                                                                            try {
////
////                                                                                ItemsBalance itemsBalance = new ItemsBalance();
////                                                                                Log.e("itemsBalance",itemsBalance.getCOMAPNYNO()+"");
////                                                                                itemsBalance.setCOMAPNYNO(itemsArray.getJSONObject(i).getString("COMAPNYNO"));
////                                                                                itemsBalance.setQTY(itemsArray.getJSONObject(i).getString("QTY"));
////                                                                                itemsBalance.setItemOCode(itemsArray.getJSONObject(i).getString("ItemOCode"));
////                                                                                itemsBalance.setSTOCK_CODE(itemsArray.getJSONObject(i).getString("STOCK_CODE"));
////
////                                                                                listAlItemsBalances.add(itemsBalance);
////
////
////                                                                            } catch (JSONException e) {
////                                                                                e.printStackTrace();
////                                                                            }
////                                                                        }
////                                                                    }catch (Exception e ){
////
////                                                                    }
////                                                                    mydatabase.itemsBalanceDao().addAll(listAlItemsBalances);
////                                                                }
////
////                                                                @Override
////                                                                public void onError(String error) {
////
////                                                                }
////                                                            });
//
//                                                        }
//
//                                                        @Override
//                                                        public void onError(String error) {
//
//
//                                                        }
//                                                    }, ipAddress, ipPort, coNo);
//                                                }
//
//                                                @Override
//                                                public void onError(String error) {
//
//
//                                                }
//                                            }, ipAddress, ipPort, coNo);
//
//
//
//
//                                        }
//
//                                        @Override
//                                        public void onError(String error) {
//
//                                            if (!((error + "").contains("SSLHandshakeException") || (error + "").equals("null") ||
//                                                    (error + "").contains("ConnectException") || (error + "").contains("NoRouteToHostException"))) {
//
//                                            }
//
//
//                                        }
//                                    });
//                                }
//
//                                @Override
//                                public void onError(String error) {
//
//                                    if (!((error + "").contains("SSLHandshakeException") || (error + "").equals("null") ||
//                                            (error + "").contains("ConnectException") || (error + "").contains("NoRouteToHostException"))) {
//
//
//                                    }
//
//                                }
//                            });
//
//
//
//                            ip_settings_dialog.dismiss();
//                                } else {
//
//                                    textInputOrderMaxNu.setError(getString(R.string.required));
//
//
//                                }
//                            } else {
//
//                                textInputMaxVoch.setError(getString(R.string.required));
//
//
//                            }
//                        } else {
//
//                            textInputCoNo.setError(getString(R.string.required));
//
//
//                        }
//
//
//
//                } else {
//
//                    textInputIpAddress.setError(getString(R.string.required));
//
//
//                }
//
//            }
//        });

    }
    void checkUnameAndPass() {

        String uname = unameEdt.getText().toString().trim().toLowerCase(Locale.ROOT) + "";
        String pass = passEdt.getText().toString().trim() + "";

        allUsers = mydatabase.usersDao().getAllUsers();
        boolean valid = false;
        int i;

        if (allUsers.size() != 0) {

            if (!uname.equals("")) {

                if (!pass.equals("")) {

                    for (i = 0; i < allUsers.size(); i++) {

                        if ((uname.equals(allUsers.get(i).getUserId())||Integer.parseInt(uname)==Integer.parseInt(allUsers.get(i).getUserId())) &&
                                pass.equals(allUsers.get(i).getUserPassword()))  {
                              salmanNumber=uname;
                            valid = true;
                            break;

                        }
                    }

                    if (valid) {


                        Toast.makeText(Login.this, "SUCCESS LOGIN!", Toast.LENGTH_SHORT).show();

                   //     addUserLogs(i);

                        Intent intent = new Intent(Login.this, HomeActivity.class);
                        startActivity(intent);

                    } else {

                        Toast.makeText(Login.this, "username or password not correct", Toast.LENGTH_SHORT).show();

                    }



                } else {


                    passEdt.setError(getString(R.string.required));
//                passEdt.setError("");

                }

            } else {


                unameEdt.setError(getString(R.string.required));

            }

        } else {

            Toast.makeText(this, "No Saved Users Found !", Toast.LENGTH_SHORT).show();


        }


    }

//    private void getData_Vendor() {
////        final String url = "http://10.0.0.22:8081/GetVendorInfo?VENDOR=" + suplierNo;
//        final String url = "http://"+ipAddres+"/GetVendorAll";
//        listAllVendor.clear();
//        listAreaName.clear();
//        pdValidation = new SweetAlertDialog(Recive_Direct.this, SweetAlertDialog.PROGRESS_TYPE);
//        pdValidation.setTitleText(Recive_Direct.this.getResources().getString(R.string.load));
//        pdValidation.setCancelable(false);
//        pdValidation.show();
//
//        JsonArrayRequest stringRequest = new JsonArrayRequest(Request.Method.GET, url, null,
//                new Response.Listener<JSONArray>() {
//
//                    @Override
//                    public void onResponse(JSONArray jsonArray) {
//                        try {
//
//                            for(int i=0;i<jsonArray.length();i++){
//                                JSONObject transactioRecive = jsonArray.getJSONObject(i);
//                                Vendor vendourInfo = new Vendor();
//                                vendourInfo.setVendorname(transactioRecive.getString("AccNameA"));
//                                vendourInfo.setVendornum(transactioRecive.getString("AccCode"));
//                               // vendourInfo.setSelect(0);
//                                listAllVendor.add(vendourInfo);
//                              //  listAreaName.add(vendourInfo.getAccName().trim());
//
//                            }
//                            Log.e("listAllVendor",""+listAllVendor.size());
//
//                            pdValidation.dismissWithAnimation();
//                        } catch (Exception e) {
//                            Log.e("Exception", "" + e.getMessage());
//                            pdValidation.dismissWithAnimation();
//                        }
//                    }
//
//
//                }
//
//                , new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                pdValidation.dismissWithAnimation();
//                if(error.getCause() instanceof JSONException)
//                {
//                    displayErrorDialog("رقم المورد  خاطئ");
//                }
//                else
//                if ((error instanceof TimeoutError) || (error instanceof NoConnectionError)) {
//                    Toast.makeText(context,
//                            "تأكد من اتصال الانترنت",
//                            Toast.LENGTH_SHORT).show();
//                } else if (error instanceof AuthFailureError) {
//                    //TODO
//                } else if (error instanceof ServerError) {
//                    Toast.makeText(context,
//                            "تأكد من اتصال الانترنت",
//                            Toast.LENGTH_SHORT).show();
//                    //TODO
//                } else if (error instanceof NetworkError) {
//                    Toast.makeText(context,
//                            "تأكد من اتصال الانترنت",
//                            Toast.LENGTH_SHORT).show();
//                    //TODO
//                } else if (error instanceof ParseError) {
//                    displayErrorDialog("رقم المورد  خاطئ");
//                    //TODO
//                }
//                Log.e("onErrorResponse: ", "" + error);
//
//            }
//
//        });
//        MySingeltone.getmInstance(Recive_Direct.this).addToRequestQueue(stringRequest);
//    }

    @Override
    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);

    }

}