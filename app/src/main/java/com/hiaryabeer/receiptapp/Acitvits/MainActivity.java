package com.hiaryabeer.receiptapp.Acitvits;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputLayout;
import com.hiaryabeer.receiptapp.Adapters.ItemsAdapter;
import com.hiaryabeer.receiptapp.Adapters.ItemsAdapterNew;
import com.hiaryabeer.receiptapp.Adapters.OrderItemAdapter;
import com.hiaryabeer.receiptapp.Adapters.OrderSearchAdapter;
import com.hiaryabeer.receiptapp.Adapters.SearchAdapter;
import com.hiaryabeer.receiptapp.Interfaces.ItemMaster_ItemBalanceDao;
import com.hiaryabeer.receiptapp.R;
import com.hiaryabeer.receiptapp.models.AppDatabase;
import com.hiaryabeer.receiptapp.models.CustomerInfo;
import com.hiaryabeer.receiptapp.models.ExportData;
import com.hiaryabeer.receiptapp.models.GeneralMethod;
import com.hiaryabeer.receiptapp.models.ImportData;
import com.hiaryabeer.receiptapp.models.ItemSwitch;
import com.hiaryabeer.receiptapp.models.Item_Unit_Details;
import com.hiaryabeer.receiptapp.models.Items;
import com.hiaryabeer.receiptapp.models.ItemsBalance;
import com.hiaryabeer.receiptapp.models.ReceiptDetails;
import com.hiaryabeer.receiptapp.models.ReceiptMaster;
import com.hiaryabeer.receiptapp.models.User;
import com.hiaryabeer.receiptapp.viewmodel.ReceivePoViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.hiaryabeer.receiptapp.Acitvits.Login.SETTINGS_PREFERENCES;
import static com.hiaryabeer.receiptapp.Acitvits.Login.coNo;
import static com.hiaryabeer.receiptapp.Acitvits.Login.ipAddress;
import static com.hiaryabeer.receiptapp.Acitvits.Login.ipPort;
import static com.hiaryabeer.receiptapp.Acitvits.Login.listAlItemsBalances;
import static com.hiaryabeer.receiptapp.Acitvits.Login.listAllVendor;

public class MainActivity extends
        AppCompatActivity {
    public static double  TotalofRec=0;
    public static List<Items> AllItemstest = new ArrayList<>();
    public static List<Items> AllItemDBlist = new ArrayList<>();
    public static Dialog dialog1;
    public static EditText
            itemcode,
            itemqty,

    free;

    RecyclerView itemRec;
    OrderItemAdapter itemsAdapter;
    AppCompatButton Save,
            close;

    AppDatabase mydatabase;
    private AutoCompleteTextView customerTv;
    public static TextView desRespon, search, itemname, itemprice, vlauoftotaldis, netsales,Total;
    public static Items item;
    int pos, num_items = 1;
    public long orderno, vohno,orderno1, vohno1,neworderno;
    public long Serverorderno,Localorderno;
    public static ArrayList<Items> vocher_Items = new ArrayList<>();
    public ArrayList<ReceiptDetails> ordersDetailslist = new ArrayList<>();
   Item_Unit_Details   itemUnitDetails;
    GeneralMethod generalMethod;
    private List<CustomerInfo> customerInfoList = new ArrayList<>();
    private List<CustomerInfo> VendorInfoList = new ArrayList<>();
    private ArrayList<String> customerNames = new ArrayList<>();
    String Cus_selection;
    int Cus_pos = 0;
    private TextInputLayout customer_textInput;
    double itemTax = 0, itemTotal = 0, subTotal = 0, itemTotalAfterTax = 0, netTotal = 0;
    private double itemTotalPerc = 0, itemDiscVal = 0, totalDiscount = 0, totalTaxValue = 0;
    RadioGroup radioGroup;
    AppCompatRadioButton order, vocher, cash, cridt;
    ExportData exportData;
    TextView menu;
    ImportData importData;
    TextView cus_num;
    double AllVocherDiscount = 0;
    int distype = 0;
    public static int paymethod = 0, VochType = 505;
    List<Items> AllItemDBlistGraterZero= new ArrayList<>();;
    List<ItemMaster_ItemBalanceDao.ItemMaster_ItemBalance> loadListLiveData;
     RadioGroup payradioGroup;
    ItemSwitch itemSwitch;
    Item_Unit_Details Item_itemUnitDetails;
    public static List<ItemSwitch> DB_itemswitch = new ArrayList<>();
  ReceivePoViewModel  receivePoViewModel;
    public static  TextView covrate_show;
    String     cusno;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
     //   receivePoViewModel=  ViewModelProviders.of(this).get(MainActivity.class);
        init();
        importData  . fetchmaxNo("505",MainActivity.this);





        ///
        customerInfoList.clear();
        customerInfoList = mydatabase.customers_dao().getAllCustms();


//        fillCustomerspinner(customerInfoList);

        VendorInfoList.clear();
        VendorInfoList = mydatabase.customers_dao().getAllVendor();
        fillCustomerspinner(VendorInfoList);
        //////////




        AllItemDBlist=    mydatabase.itemsDao().loadListLiveData();
        Log.e("AllItemDBlist.size===",AllItemDBlist.size()+"");
        AllItemDBlist.addAll( mydatabase.itemsDao().loadListLiveData2()) ;
        Log.e("AllItemDBlist.size===",AllItemDBlist.size()+"");


        itemcode.requestFocus();
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Initializing the popup menu and giving the reference as current context
                PopupMenu popupMenu = new PopupMenu(MainActivity.this, menu);

                // Inflating popup menu from popup_menu.xml file
                popupMenu.getMenuInflater().inflate(R.menu.menu_example, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.importdata:
                                Log.e("importdata==", "importdata");
                                List<ReceiptMaster> vouchers = mydatabase.receiptMaster_dao().getAllOrdersConfirm();
                                if (vouchers.size() == 0)
                                    importdata();
                                else
                                    generalMethod.showSweetDialog(MainActivity.this, 3, getResources().getString(R.string.exportdatamsg), "");

                              break;
                            case R.id.exportdata:
                              exportData.exportSalesVoucherM("505");
                                break;
                            case R.id.Report:


//                                        Intent i = new Intent(MainActivity.this, MasterVochers_report.class);
//                                        startActivity(i);
//                                        finish();
                                  break;

                            case R.id.addTotaldis:

                                if (vocher_Items.size() == 0)
                                    generalMethod.showSweetDialog(MainActivity.this, 3, getResources().getString(R.string.fillbasket), "");
                                else
                                    addTotalDisDailog();

                        }
                        return true;
                    }
                });
                // Showing the popup menu
                popupMenu.show();
            }
        });

    }


    private void  updateAviQtyInDBlist(String itemno,double qty){
        for(int i=0;i<AllItemDBlist.size();i++)
        {
           if(AllItemDBlist.get(i).getITEMNO().equals(itemno))
               AllItemDBlist.get(i).setAviQty(qty);

            }

    }

    double  getBalanceforitem(String itemnum) {


        for (int i = 0; i < AllItemDBlist.size(); i++) {

            if(AllItemDBlist.get(i).getITEMNO().equals(itemnum)
            ||AllItemDBlist.get(i).getBARCODE().equals(itemnum)) {
                Log.e("BalanceQty==",AllItemDBlist.get(i).getAviQty()+"");
                return AllItemDBlist.get(i).getBalanceQty();


            }

        }
        return 0;

    }
    double  getaviqtyforitem(String itemnum) {


        for (int i = 0; i < AllItemDBlist.size(); i++) {

            if(AllItemDBlist.get(i).getITEMNO().equals(itemnum)) {
                Log.e("AviQty==",AllItemDBlist.get(i).getAviQty()+"");
                        return AllItemDBlist.get(i).getAviQty();


            }

        }
        return 0;

    }
    public void init() {
        cus_num = findViewById(R.id.cus_num);
        importData = new ImportData(MainActivity.this);
        menu = findViewById(R.id.menuBtn);
        order = findViewById(R.id.order);
        covrate_show= findViewById(R.id.covrate_show);
        vocher = findViewById(R.id.vocher);
        cash = findViewById(R.id.cash);
        cridt = findViewById(R.id.cridt);
        desRespon = findViewById(R.id.desRespon);
        desRespon.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!editable.toString().equals("")) {
                    FillLastRowCalculations();
                }
            }
        });


       payradioGroup = (RadioGroup) findViewById(R.id.payradioGroup);
        payradioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                View radioButton = payradioGroup.findViewById(checkedId);
                int index = payradioGroup.indexOfChild(radioButton);

                // Add logic here

                switch (index) {
                    case 0: // first button  ذمم

                        paymethod = 0;

                        //     Toast.makeText(getApplicationContext(), "Selected button number " + index, 500).show();
                        break;
                    case 1: // secondbutton  نقدا
                        paymethod = 1;

                        //      Toast.makeText(getApplicationContext(), "Selected button number " + index, 500).show();
                        break;
                }
            }
        });
        final RadioGroup voch_typeradio = (RadioGroup) findViewById(R.id.radioGroup);
        voch_typeradio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                View radioButton = voch_typeradio.findViewById(checkedId);
                int index = voch_typeradio.indexOfChild(radioButton);

                // Add logic here

                switch (index) {
                    case 0: // first button  طلب شراء

                        if (vocher_Items.size() > 0) {
                            new SweetAlertDialog(MainActivity.this, SweetAlertDialog.WARNING_TYPE)
                                    .setTitleText(getResources().getString(R.string.confirm_title))
                                    .setContentText(getResources().getString(R.string.changevochertype))
                                    .setConfirmButton(getResources().getString(R.string.yes), new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {


                                            if (vocher_Items.size() != 0) {
                                                vocher_Items.clear();
                                                fillAdapter();
                                                clearText();
                                                sweetAlertDialog.dismiss();
                                                VochType = 505;
                                                VendorInfoList.clear();
                                                VendorInfoList = mydatabase.customers_dao().getAllVendor();
                                                fillCustomerspinner(VendorInfoList);
                                                customerTv.setText("");
                                            }
                                        }

                                    })
                                    .setCancelButton(getResources().getString(R.string.No), new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {

                                            sweetAlertDialog.dismiss();
                                        }
                                    })
                                    .show();

                        } else {
                            VochType = 505;
                            VendorInfoList.clear();
                            VendorInfoList = mydatabase.customers_dao().getAllVendor();
                            fillCustomerspinner(VendorInfoList);
                            customerTv.setText("");
                        }
                        break;
                    case 1: // secondbutton فاتورة
                        if (vocher_Items.size() > 0) {
                            new SweetAlertDialog(MainActivity.this, SweetAlertDialog.WARNING_TYPE)
                                    .setTitleText(getResources().getString(R.string.confirm_title))
                                    .setContentText(getResources().getString(R.string.changevochertype))
                                    .setConfirmButton(getResources().getString(R.string.yes), new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {


                                            if (vocher_Items.size() != 0) {
                                                vocher_Items.clear();
                                                fillAdapter();
                                                clearText();
                                                sweetAlertDialog.dismiss();

                                                customerInfoList.clear();
                                                customerInfoList = mydatabase.customers_dao().getAllCustms();
                                                customerTv.setText("");
                                                //fillCustomerspinner(customerInfoList);
                                            }
                                        }

                                    })
                                    .setCancelButton(getResources().getString(R.string.No), new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {

                                            sweetAlertDialog.dismiss();
                                        }
                                    })
                                    .show();

                        } else {

                            customerInfoList.clear();
                            customerInfoList = mydatabase.customers_dao().getAllCustms();
                            customerTv.setText("");
                            //fillCustomerspinner(customerInfoList);
                        }
                        break;
                }

            }
        });
        radioGroup = findViewById(R.id.radioGroup);
        exportData = new ExportData(MainActivity.this);
        generalMethod = new GeneralMethod(MainActivity.this);
        vocher_Items.clear();
        itemRec = findViewById(R.id.tableData);
        itemRec.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        mydatabase = AppDatabase.getInstanceDatabase(MainActivity.this);
        itemname = findViewById(R.id.item_name);
        search = findViewById(R.id.search);
        search.setEnabled(true);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search.setEnabled(false);



                if(AllItemDBlist.size()==0)
                    AllItemDBlist=   mydatabase.itemsDao().loadListLiveData();

              AllItemDBlistGraterZero.clear();


                opensearchDailog(AllItemDBlist,MainActivity.this);
//                if (VochType == 504) {
//
////                    AllItemDBlistGraterZero = mydatabase.itemsDao().getAllItemsgraterthanzero();
////                    Log.e("AllItemDBlistGraterZero==",""+AllItemDBlistGraterZero.size());
////
////                    for (int i = 0; i < AllItemDBlist.size(); i++)
////
////                        if(AllItemDBlist.get(i).getAviQty()>0)
////                            AllItemDBlistGraterZero.add(AllItemDBlist.get(i));
//                    AllItemDBlistGraterZero.addAll(creatlistGraterZero())   ;
//                    Log.e("AllItemDBlistGraterZero",""+AllItemDBlistGraterZero.size());
//                 opensearchDailog(AllItemDBlistGraterZero,MainActivity.this);
//
//
//                } else {
//                    opensearchDailog(AllItemDBlist,MainActivity.this);
//                }
            }
        });
        itemcode = findViewById(R.id.item_code);
        itemqty = findViewById(R.id.qty);
        itemprice = findViewById(R.id.price);
        free = findViewById(R.id.free);
        vlauoftotaldis = findViewById(R.id.vlauoftotaldis);
        netsales = findViewById(R.id.netsales);
        Total= findViewById(R.id.Total);
        Save = findViewById(R.id.save);
        close = findViewById(R.id.cancel_btn);
        itemqty.setEnabled(false);
        HorizontalScrollView horizontalScroll = findViewById(R.id.HorizontalScroll);
//        horizontalScroll.setOnScrollChangeListener(new View.OnScrollChangeListener() {
//            @Override
//            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
//                itemcode.requestFocus();
//            }
//        });
 //  itemcode.setOnKeyListener(onKeyListener);

        itemcode.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent event) {

                //  Log.e("actionId==", actionId+"KeyEvent=="+event.getAction());

                if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT
                        || actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_NULL) {
                    if ((!itemcode.getText().equals(""))) {

                        covrate_show.setText(GetUnitName(itemcode.getText().toString().trim()));
                        Log.e("itemcode==", itemcode.getText().toString().trim());
                        item = mydatabase.itemsDao().getItembyCode(itemcode.getText().toString().trim());

                        Log.e("itemcode==", item + "");
                        if (item != null) {

                            if(CheckIsExistsINLocalList2(itemcode.getText().toString())) GeneralMethod.showSweetDialog(MainActivity.this,3, "",getResources().getString(R.string.itemexists6));

                            itemqty.requestFocus();
                            Log.e("itemname==", item.getNAME() + "");
                            Log.e("qty==", item.getQty() + "");
                            Log.e("itemprice==", item.getF_D() + "");
                            Log.e("free==", "");
                            Log.e("itemcode==", item.getITEMNO() + "");




                            if(item!=null)      itemUnitDetails =   mydatabase.itemUnitsDao(). getItemUnitsOfItembybarcode(item.getITEMNO(),itemcode.getText().toString().trim());

                            if (itemUnitDetails!=null)
                            {
                                item.setF_D(itemUnitDetails.getSALEPRICE());

                            }
                            double num_items =1;
                           num_items =   mydatabase.itemUnitsDao(). getConvRatebyitemnumAndBarcode(item.getITEMNO(),itemcode.getText().toString().trim());

                            if(num_items!=0)   item.setConvRate(num_items+"");
                             else   item.setConvRate("1");

                            if (itemUnitDetails!=null)
                                item.setF_D(itemUnitDetails.getSALEPRICE()/num_items);
                            else
                                item.setF_D(  item.getF_D());

                            Log.e("num_items==",item.getConvRate()+"");
                            String unitid= mydatabase.itemUnitsDao().getUnitidbyitemnumAndBarcode(item.getITEMNO(),itemcode.getText().toString().trim());
                            item.  setWhichUNITSTR(unitid);

                            itemname.setText(item.getNAME());

                            itemqty.setText(item.getQty() + "");
                            itemprice.setText(GeneralMethod.convertToEnglish(String.valueOf(String.format("%.3f",item.getF_D()))) + "");
                            free.setText("");

                            //         addItem(item.getITEMNO());
                            itemqty.requestFocus();
                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    itemname.setText(item.getNAME());
                                    itemqty.setText(item.getQty() + "");
                                    itemprice.setText(GeneralMethod.convertToEnglish(String.valueOf(String.format("%.3f",item.getF_D())))  + "");
                                    free.setText("");

                                    //         addItem(item.getITEMNO());
                                    itemqty.requestFocus();
                                    itemqty.setEnabled(true);
                                }
                            }, 100);
                        } else if (
                                Exsitsin_itemswitchlist(itemcode.getText().toString())){
                            itemqty.requestFocus();
                            item=  mydatabase.itemsDao().getItembyCode( itemSwitch.getItemOCode());
                            if(item!=null)      if(CheckIsExistsINLocalList2(item.getITEMNO())) GeneralMethod.showSweetDialog(MainActivity.this,3, "",getResources().getString(R.string.itemexists6));


                           if(item!=null)    itemUnitDetails =   mydatabase.itemUnitsDao(). getItemUnitsOfItembybarcode(item.getITEMNO(),itemcode.getText().toString().trim());

                                if (itemUnitDetails!=null)
                                    item.setF_D(itemUnitDetails.getSALEPRICE());
                            double num_items =1;
                            if(item!=null)  num_items =   mydatabase.itemUnitsDao(). getConvRatebyitemnumAndBarcode(item.getITEMNO(),itemcode.getText().toString().trim());


                          if(num_items!=0)    item.setConvRate(num_items+"");
                          else
                              item.setConvRate("1");

                            if (itemUnitDetails!=null)
                                item.setF_D(itemUnitDetails.getSALEPRICE()/num_items);
                            else
                                item.setF_D(  item.getF_D());


                            Log.e("num_items==", item.getConvRate()+"");
                            String unitid= mydatabase.itemUnitsDao().getUnitidbyitemnumAndBarcode(item.getITEMNO(),itemcode.getText().toString().trim());

                            item.  setWhichUNITSTR(unitid);

                            Log.e("itemname==", item.getNAME() + "");
                            Log.e("qty==", item.getQty() + "");
                            Log.e("itemprice==", item.getF_D() + "");
                            Log.e("free==", "");
                            Log.e("itemcode==", item.getITEMNO() + "");

                            itemname.setText(item.getNAME());
                            itemqty.setText(item.getQty() + "");
                            itemprice.setText(GeneralMethod.convertToEnglish(String.valueOf(String.format("%.3f",item.getF_D()))) + "");
                            free.setText("");

                            //         addItem(item.getITEMNO());
                            itemqty.requestFocus();
                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    itemname.setText(item.getNAME());
                                    itemqty.setText(item.getQty() + "");
                                    itemprice.setText(GeneralMethod.convertToEnglish(String.valueOf(String.format("%.3f",item.getF_D()))) + "");
                                    free.setText("");

                                    //         addItem(item.getITEMNO());
                                    itemqty.requestFocus();
                                    itemqty.setEnabled(true);
                                }
                            }, 100);
                        }


                    else    if( Exsitsin_itemsUnits(itemcode.getText().toString()))

                        {

                            itemqty.requestFocus();
                            item=  mydatabase.itemsDao().getItembyCode( Item_itemUnitDetails.getItemNo());
                          if(item!=null)  if(CheckIsExistsINLocalList2(item.getITEMNO())) GeneralMethod.showSweetDialog(MainActivity.this,3, "",getResources().getString(R.string.itemexists6));



                            if(item!=null)      itemUnitDetails =   mydatabase.itemUnitsDao(). getItemUnitsOfItembybarcode(item.getITEMNO(),itemcode.getText().toString().trim());

                            if (itemUnitDetails!=null)
                                item.setF_D(itemUnitDetails.getSALEPRICE());
                            double num_items =1;
                         num_items =   mydatabase.itemUnitsDao(). getConvRatebyitemnumAndBarcode(item.getITEMNO(),itemcode.getText().toString().trim());

String unitid= mydatabase.itemUnitsDao().getUnitidbyitemnumAndBarcode(item.getITEMNO(),itemcode.getText().toString().trim());
                            if(num_items!=0)    item.setConvRate(num_items+"");
                            else
                                item.setConvRate("1");
                            if (itemUnitDetails!=null)
                                item.setF_D(itemUnitDetails.getSALEPRICE()/num_items);
                            else
                                item.setF_D(  item.getF_D());


                            item.  setWhichUNITSTR(unitid);
                            Log.e("num_items==", item.getConvRate()+"");



                            Log.e("itemname==", item.getNAME() + "");
                            Log.e("qty==", item.getQty() + "");
                            Log.e("itemprice==", item.getF_D() + "");
                            Log.e("free==", "");
                            Log.e("itemcode==", item.getITEMNO() + "");

                            itemname.setText(item.getNAME());
                            itemqty.setText(item.getQty() + "");
                            itemprice.setText(GeneralMethod.convertToEnglish(String.valueOf(String.format("%.3f",item.getF_D())))  + "");
                            free.setText("");

                            //         addItem(item.getITEMNO());
                            itemqty.requestFocus();
                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    itemname.setText(item.getNAME());
                                    itemqty.setText(item.getQty() + "");
                                    itemprice.setText(GeneralMethod.convertToEnglish(String.valueOf(String.format("%.3f",item.getF_D()))) + "");
                                    free.setText("");

                                    //         addItem(item.getITEMNO());
                                    itemqty.requestFocus();
                                    itemqty.setEnabled(true);
                                }
                            }, 100);

                        }
                        else {

                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Log.e("else3==", "else");
                                    itemcode.setError("");
                                    itemcode.requestFocus();
                                    itemqty.setEnabled(false);
                                }
                            }, 100);
                        }
//
                    } else {
                        Log.e("else2==", "else");
                        itemcode.setError("");
                        itemcode.requestFocus();
                        itemqty.setEnabled(false);

                    }

                }
                return false;
            }
        });

//        itemqty.setOnKeyListener(onKeyListener);
//        itemcode.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View view, int i, KeyEvent keyEvent) {
//
//                String itemNo_text = itemcode.getText().toString().trim();
//
//
//
//                    if (keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
//                        switch (i) {
//
//                            case KeyEvent.KEYCODE_DPAD_CENTER:
//                            case KeyEvent.KEYCODE_ENTER:
////                                switch (view.getId()) {
////
////
////                                }
//                                return true;
//                            default:
//                                break;
//                        }
//                    }
//
//
//                return false;
//            }
//        });
        itemqty.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {

                String itemNo_text = itemqty.getText().toString().trim();

                Log.e("setOnKeyactionId==", i + "KeyEvent==" + keyEvent.getAction());


                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    switch (i) {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            if ((!itemcode.getText().equals(""))) {
                                if ((!itemqty.getText().equals(""))) {

                                    if (VochType == 504) {

                                        final Handler handler = new Handler();
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                String    itemno=item.getITEMNO();
                                                if (!CheckIsExistsINLocalList(itemno)) {
                                                    item.setBalanceQty(getBalanceforitem(itemno));
                                                    Log.e("case1==", "case1");

                                                    {    Log.e("case1.1==", "case1.1");

                                                        item.setQty(Double.parseDouble(itemqty.getText().toString().trim()));
                                                        item.setAmount(item.getF_D() * item.getQty() * num_items);
                                                        item.setAviQty( item.getBalanceQty()-   item.getQty());
                                                        Log.e("vocher_Items4==", vocher_Items.size() + "");
                                                        vocher_Items.add(item);
                                                        fillAdapter();
                                                    }


                                                } else {
                                                    Log.e("case2==", "case2  ,"+vocher_Items.get(pos).getAviQty()+"");
                                                    if(vocher_Items.get(pos).getAviQty()>=Double.parseDouble(itemqty.getText().toString().trim())) {
                                                        Log.e("case2.1==", "case2.1");
                                                        vocher_Items.get(pos).setQty(vocher_Items.get(pos).getQty() + Double.parseDouble(itemqty.getText().toString().trim()));

                                                        vocher_Items.get(pos).setAmount(vocher_Items.get(pos).getF_D() * vocher_Items.get(pos).getQty() * num_items);
                                                        Log.e("aviqty11==",vocher_Items.get(pos).getAviQty()+"");
                                                        vocher_Items.get(pos).setAviQty(vocher_Items.get(pos).getBalanceQty() -  vocher_Items.get(pos).getQty());
                                                        //   updateAviQtyInDBlist( vocher_Items.get(pos).getITEMNO(), vocher_Items.get(pos).getAviQty());
                                                        Log.e("aviqty22==",vocher_Items.get(pos).getAviQty()+"   , "+vocher_Items.get(pos).getQty()+" , " +"");
                                                        Log.e("vocher_Items3==", vocher_Items.size() + "");
                                                        fillAdapter();
                                                    }else{
                                                        GeneralMethod.showSweetDialog(MainActivity.this, 0, "", getResources().getString(R.string.notenoughQTY));

                                                        Log.e("case2.2==", "case2.2");

                                                    }

                                                }
                                            }

                                        }, 100);

                                    }
                                    else {
                                        final Handler handler = new Handler();
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {

                                                addItem(item.getITEMNO());
                                                itemcode.requestFocus();


                                            }

                                        }, 100);
                                    }
//
                                } else {
                                    Log.e("else==", "else");
                                    itemqty.setError("");
                                    itemqty.requestFocus();
                                }
                            } else {
                                Log.e("else==", "else");
                                itemcode.setError("");
                                itemcode.requestFocus();
                            }
//                            switch (view.getId()) {
//
//                                case R.id.item_Qty:

//                            }
                            return true;
                        default:
                            break;
                    }
                }


                return false;
            }
        });
        itemqty.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent event) {

                //  Log.e("actionId==", actionId+"KeyEvent=="+event.getAction());

                if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT
                        || actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_NULL) {
//                    Log.e("setOnEditorActio", "afterTextChangedNOT" +"errorData\t"+actionId);
                    if ((!itemcode.getText().equals(""))) {
                        if ((!itemqty.getText().equals(""))) {

                            if (VochType == 504) {

                                final Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                    String    itemno=item.getITEMNO();
                                        if (!CheckIsExistsINLocalList(itemno)) {
                                            item.setBalanceQty(getBalanceforitem(itemno));
                                            Log.e("case1==", "case1");

                                                {    Log.e("case1.1==", "case1.1");

                                                    item.setQty(Double.parseDouble(itemqty.getText().toString().trim()));
                                                    item.setAmount(item.getF_D() * item.getQty() * num_items);
                                                    item.setAviQty( item.getBalanceQty()-   item.getQty());
                                                    Log.e("vocher_Items4==", vocher_Items.size() + "");
                                                    vocher_Items.add(item);
                                                }

                                        } else {
                                            Log.e("case2==", "case2  ,"+vocher_Items.get(pos).getAviQty()+"");
                                            if(vocher_Items.get(pos).getAviQty()>=Double.parseDouble(itemqty.getText().toString().trim())) {
                                                Log.e("case2.1==", "case2.1");
                                                vocher_Items.get(pos).setQty(vocher_Items.get(pos).getQty() + Double.parseDouble(itemqty.getText().toString().trim()));

                                                vocher_Items.get(pos).setAmount(vocher_Items.get(pos).getF_D() * vocher_Items.get(pos).getQty() * num_items);
                                                Log.e("aviqty11==",vocher_Items.get(pos).getAviQty()+"");
                                                vocher_Items.get(pos).setAviQty(vocher_Items.get(pos).getBalanceQty() -  vocher_Items.get(pos).getQty());
                                             //   updateAviQtyInDBlist( vocher_Items.get(pos).getITEMNO(), vocher_Items.get(pos).getAviQty());
                                                Log.e("aviqty22==",vocher_Items.get(pos).getAviQty()+"   , "+vocher_Items.get(pos).getQty()+" , " +"");
                                                Log.e("vocher_Items3==", vocher_Items.size() + "");
                                            }else{
                                                GeneralMethod.showSweetDialog(MainActivity.this, 0, "", getResources().getString(R.string.notenoughQTY));

                                                Log.e("case2.2==", "case2.2");

                                            }

                                        }
                                    }

                                }, 100);
                                fillAdapter();
                            }
                            else {
                                final Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {

                                        addItem(item.getITEMNO());
                                        itemcode.requestFocus();


                                    }

                                }, 100);
                            }
//
                        } else {
                            Log.e("else==", "else");
                            itemqty.setError("");
                            itemqty.requestFocus();
                        }
                    } else {
                        Log.e("else==", "else");
                        itemcode.setError("");
                        itemcode.requestFocus();
                    }
                }
                return false;
            }
        });
        customer_textInput = findViewById(R.id.customer_textInput);
        customerTv = findViewById(R.id.customerTv);


        Save.setOnClickListener(onClickListener);
        close.setOnClickListener(onClickListener);


//        customerTv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            public void onItemClick(AdapterView<?> parent, View view, int position, long rowId) {
//                Cus_selection = (String) parent.getItemAtPosition(position);
//                Cus_pos = position;
//                Log.e("Cus_selection==", Cus_selection);
//                customer_textInput.setError(null);
//                customer_textInput.clearFocus();
//            }
//        });
        customerTv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long rowId) {
                Cus_selection = (String) parent.getItemAtPosition(position);
                Cus_pos = position;
                Log.e("Cus_pos==", Cus_pos+"");
                Log.e("Cus_selection==", Cus_selection);

                  cusno=   mydatabase.customers_dao(). getCustmIdByName(Cus_selection);
                cus_num.setText(  cusno);
//                if (VochType == 504) {
//                    Log.e("Cus_num==", customerInfoList.get(Cus_pos).getCustomerId());
//                    cus_num.setText(  customerInfoList.get(Cus_pos).getCustomerId());
//
//                } else {
//                    Log.e("Cus_num==",VendorInfoList.get(Cus_pos).getCustomerId());
//                    cus_num.setText(     VendorInfoList.get(Cus_pos).getCustomerId());
//
//                }
                customer_textInput.setError(null);
                customer_textInput.clearFocus();
            }
        });
    }

    public void fillCustomerspinner(List<CustomerInfo> customerInfoList) {
        // customerInfoList.clear();
        customerNames.clear();


        for (int i = 0; i < customerInfoList.size(); i++) {

            customerNames.add(customerInfoList.get(i).getCustomerName());

        }

        ArrayAdapter<String> customersAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_dropdown_item_1line, customerNames);

        customerTv.setAdapter(customersAdapter);
    }

    @Override
    public void onBackPressed() {
        try {
            search.setEnabled(true);
            Handler h = new Handler(Looper.getMainLooper());
            h.post(new Runnable() {
                public void run() {


                    new SweetAlertDialog(MainActivity.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText(getResources().getString(R.string.confirm_title))
                            .setContentText(getResources().getString(R.string.messageExit))
                            .setConfirmButton(getResources().getString(R.string.yes), new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {


                                    if (vocher_Items.size() != 0) {
                                        new SweetAlertDialog(MainActivity.this, SweetAlertDialog.WARNING_TYPE)
                                                .setTitleText(getResources().getString(R.string.confirm_title))
                                                .setContentText(getResources().getString(R.string.messageExit2))
                                                .setConfirmButton(getResources().getString(R.string.yes), new SweetAlertDialog.OnSweetClickListener() {
                                                    @Override
                                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                        sweetAlertDialog.dismissWithAnimation();

                                                        vocher_Items.clear();
                                                        fillAdapter();
                                                        clearText();

                                                        startActivity(new Intent(MainActivity.this, HomeActivity.class));

                                                    }
                                                })
                                                .setCancelButton(getResources().getString(R.string.No), new SweetAlertDialog.OnSweetClickListener() {
                                                    @Override
                                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                        sweetAlertDialog.dismiss();
                                                    }
                                                })
                                                .show();

                                    } else {
                                        sweetAlertDialog.dismiss();
                                        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                                        startActivity(intent);
                                        finish();

                                    }
                                }

                            })
                            .setCancelButton(getResources().getString(R.string.No), new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {

                                    sweetAlertDialog.dismiss();
                                }
                            })
                            .show();
                }
            });


        } catch (Exception e) {

        }
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.save:
                    removeZeroQtyFromList();
                    if (vocher_Items.size() != 0) {
                        Log.e("vocher_Items==", vocher_Items.size() + "");

                        if (Cus_selection != null && !Cus_selection.equals("")) {
                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    itemcode.setError(null);

                                    SaveDetialsVocher();
                                    SaveMasterVocher();
                                //    generalMethod.showSweetDialog(MainActivity.this, 1, getResources().getString(R.string.savedSuccsesfule), "");
                                    customer_textInput.setError(null);
                                    cridt.setChecked(true);
                                    vocher.setChecked(true);
                                    cus_num.setText("");


                      exportData.exportSalesVoucherM("505");
                                    AllItemDBlist.clear();


                                    AllItemDBlist=    mydatabase.itemsDao().loadListLiveData();
                                    AllItemDBlist.addAll( mydatabase.itemsDao().loadListLiveData2()) ;
                                    Log.e("AllItemDBlist.size===",AllItemDBlist.size()+"");

                                }

                            }, 100);


                        } else

                        {
                            customer_textInput.setError(getResources().getString(R.string.required));
                            generalMethod.showSweetDialog(MainActivity.this, 3, getResources().getString(R.string.selectcustoumer), "");
                        }
                    } else
                        generalMethod.showSweetDialog(MainActivity.this, 3, getResources().getString(R.string.fillbasket), "");
                    break;

                case R.id.cancel_btn:

                    try {
                        Handler h = new Handler(Looper.getMainLooper());
                        h.post(new Runnable() {
                            public void run() {


                                new SweetAlertDialog(MainActivity.this, SweetAlertDialog.WARNING_TYPE)
                                        .setTitleText(getResources().getString(R.string.confirm_title))
                                        .setContentText(getResources().getString(R.string.messageExit))
                                        .setConfirmButton(getResources().getString(R.string.yes), new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sweetAlertDialog) {


                                                if (vocher_Items.size() != 0) {
                                                    new SweetAlertDialog(MainActivity.this, SweetAlertDialog.WARNING_TYPE)
                                                            .setTitleText(getResources().getString(R.string.confirm_title))
                                                            .setContentText(getResources().getString(R.string.messageExit2))
                                                            .setConfirmButton(getResources().getString(R.string.yes), new SweetAlertDialog.OnSweetClickListener() {
                                                                @Override
                                                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                                    sweetAlertDialog.dismissWithAnimation();

                                                                    vocher_Items.clear();
                                                                    fillAdapter();
                                                                    clearText();

                                                                    startActivity(new Intent(MainActivity.this, HomeActivity.class));

                                                                }
                                                            })
                                                            .setCancelButton(getResources().getString(R.string.No), new SweetAlertDialog.OnSweetClickListener() {
                                                                @Override
                                                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                                    sweetAlertDialog.dismiss();
                                                                }
                                                            })
                                                            .show();

                                                } else {
                                                    sweetAlertDialog.dismiss();
                                                    Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                                                    startActivity(intent);
                                                    finish();

                                                }
                                            }

                                        })
                                        .setCancelButton(getResources().getString(R.string.No), new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sweetAlertDialog) {

                                                sweetAlertDialog.dismiss();
                                            }
                                        })
                                        .show();
                            }
                        });


                    } catch (Exception e) {

                    }
                    break;
            }

        }
    };

    void addItem(String itemno) {
//    Items items=new Items();
//    items.set
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!CheckIsExistsINLocalList(itemno)) {

                    item.setBalanceQty(getBalanceforitem(itemno));
                    item.setQty(Double.parseDouble(itemqty.getText().toString().trim()));
                    item.setAmount(item.getF_D() * item.getQty() * num_items);
                    item.setAviQty(item.getBalanceQty()-item.getQty());
                    Log.e("vocher_Items4==", vocher_Items.size() + "");
                    vocher_Items.add(item);


                } else {
                    vocher_Items.get(pos).setQty(vocher_Items.get(pos).getQty() + Double.parseDouble(itemqty.getText().toString().trim()));
                    vocher_Items.get(pos).setAviQty( vocher_Items.get(pos).getBalanceQty()- vocher_Items.get(pos).getQty());
                    vocher_Items.get(pos).setAmount(vocher_Items.get(pos).getF_D() * vocher_Items.get(pos).getQty() * num_items);
                    Log.e("vocher_Items3==", vocher_Items.size() + "");


                }
            }

        }, 100);
        fillAdapter();
    }

    private boolean CheckIsExistsINLocalList(String barcode) {
    Log.e("covrate==",item.getConvRate()+"");

        boolean flag = false;
        if (vocher_Items.size() != 0)
            for (int i = 0; i < vocher_Items.size(); i++) {

                if (
                        (  GeneralMethod.convertToEnglish(vocher_Items.get(i).getITEMNO()).equals(GeneralMethod.convertToEnglish(barcode))
                                ||
                                GeneralMethod. convertToEnglish(vocher_Items.get(i).getITEMNO()).equals(GeneralMethod.convertToEnglish(mydatabase.itemSwitchDao().getitemocode(barcode)))

                    ) &&  GeneralMethod. convertToEnglish(vocher_Items.get(i).getConvRate()).equals(GeneralMethod.convertToEnglish(item.getConvRate()))
                ) {
                    pos = i;
                    flag = true;
                    Log.e("vocher_Itemscovrate==",vocher_Items.get(i).getConvRate()+"");
                    break;

                } else {
                    flag = false;
                    continue;
                }
            }

        return flag;


    }
    private boolean CheckIsExistsINLocalList2(String barcode) {
        Log.e("covrate==",item.getConvRate()+"");

        boolean flag = false;
        if (vocher_Items.size() != 0)
            for (int i = 0; i < vocher_Items.size(); i++) {

                if (
                        (  GeneralMethod.convertToEnglish(vocher_Items.get(i).getITEMNO()).equals(GeneralMethod.convertToEnglish(barcode))
                                ||
                                GeneralMethod. convertToEnglish(vocher_Items.get(i).getITEMNO()).equals(GeneralMethod.convertToEnglish(mydatabase.itemSwitchDao().getitemocode(barcode)))

                        )
                ) {

                    flag = true;

                    break;

                } else {
                    flag = false;
                    continue;
                }
            }

        return flag;


    }
    void fillAdapter() {

        //  clearText();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                vocher_Items=     creatlistGraterZero(vocher_Items);
                itemRec.setAdapter(new OrderItemAdapter(vocher_Items, MainActivity.this));
                FillLastRowCalculations();

                itemname.setText("");
                covrate_show.setText("");
                itemqty.setText("");
                itemprice.setText("");
                free.setText("");

                customer_textInput.setError(null);

                Log.e("clearText", "clearText");
                itemqty.setEnabled(false);
                itemcode.setText("");
                itemcode.requestFocus();
                itemqty.setError(null);
            }

        }, 100);
    }

    void clearText() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                itemname.setText("");
                covrate_show.setText("");
                itemqty.setText("");
                itemprice.setText("");
                free.setText("");
                itemcode.setText("");
                itemcode.requestFocus();
                customer_textInput.setError(null);
                customerTv.setText("");
                AllVocherDiscount = 0;
                Log.e("clearText", "clearText");
                itemqty.setEnabled(false);
            }

        }, 100);


    }


    void SaveDetialsVocher() {
        Log.e("vocher_ItemsSize==", vocher_Items.size() + "");
        for (int i = 0; i < vocher_Items.size(); i++) {
            ReceiptDetails ordersDetails = new ReceiptDetails();

            ordersDetails.setVOUCHERTYPE(VochType);
//         cash.
           if(cash.isChecked()) paymethod=1;
           else
               paymethod=0;
            ordersDetails.setPaymethod(paymethod);


            ordersDetails.setDiscount(vocher_Items.get(i).getDiscount());
            ordersDetails.setItemNo(vocher_Items.get(i).getITEMNO());
            ordersDetails.setFree(vocher_Items.get(i).getFree());
            ordersDetails.setItemName(vocher_Items.get(i).getNAME());
            ordersDetails.setQty(vocher_Items.get(i).getQty());
            ordersDetails.setConvRate(vocher_Items.get(i).getConvRate());
            ordersDetails.setPrice(vocher_Items.get(i).getF_D());
            ordersDetails.setDate(generalMethod.getCurentTimeDate(1));
            ordersDetails.setTime(generalMethod.getCurentTimeDate(2));
            ordersDetails.setAmount(vocher_Items.get(i).getF_D()*vocher_Items.get(i).getQty()*Double.parseDouble(vocher_Items.get(i).getConvRate()));
            ordersDetails.setTaxPercent(vocher_Items.get(i).getTAXPERC());
            ordersDetails.setUnit(vocher_Items.get(i).getConvRate());
            if (ordersDetails.getUnit().equals("1"))
                ordersDetails.setWhichUNIT("0");
            else
                ordersDetails.setWhichUNIT("1");

            ordersDetails.setWhichUNITSTR(vocher_Items.get(i).getWhichUNITSTR());


//            double num_items = mydatabase.itemUnitsDao().getConvRatebyitemnum(ordersDetails.getItemNo());
            ordersDetails.setWHICHUQTY(vocher_Items.get(i).getConvRate());

            // ordersDetails.setCustomerId(mydatabase.customers_dao().getCustmByName(Cus_selection));
            if (ordersDetails.getVOUCHERTYPE() == 504)
                ordersDetails.setCustomerId(customerInfoList.get(Cus_pos).getCustomerId());
            else ordersDetails.setCustomerId(VendorInfoList.get(Cus_pos).getCustomerId());

            ordersDetails.setIsPosted(0);
            ordersDetails.setArea("");

//            ordersDetails.setUnit(vocher_Items.get(i).getUnit().equals("One Unit") ? 0 : 1);

            //Discount calcualtios
            ordersDetails.setTotalDiscVal(    ordersDetails.getAmount()* ordersDetails.getDiscount());
            ordersDetails.setTotal(ordersDetails.getAmount() - ordersDetails.getTotalDiscVal());

            //Tax calcualtios
            Log.e("fd=", vocher_Items.get(i).getF_D() + "ttax" + vocher_Items.get(i).getTAXPERC());
            ordersDetails.setTax(vocher_Items.get(i).getF_D() * vocher_Items.get(i).getTAXPERC());
            Log.e("tax=", ordersDetails.getTax() + "");
            ordersDetails.setTaxValue(ordersDetails.getTax() * vocher_Items.get(i).getQty());

            // ضريبة خاضعة
            double subtotal = 0;
            subtotal = ordersDetails.getAmount() - ordersDetails.getTotalDiscVal() -ordersDetails.getTaxValue();

            ordersDetails.setSubtotal(subtotal);
            Log.e("ordersDetails.getSubtotal()=", ordersDetails.getSubtotal() + "");

            double nettotal = 0;
            nettotal = ordersDetails.getSubtotal() + ordersDetails.getTaxValue();
            ordersDetails.setNetTotal(nettotal);
            Log.e("ordersDetails.getNetTotal()=", ordersDetails.getNetTotal() + "");






            ordersDetailslist.add(ordersDetails);
            Log.e("hereordersDetailslist==", ordersDetailslist.size() + "");


            Log.e("ordersDetails.getarea", ordersDetails.getArea() + "");
            Log.e("ordersDetails.getAmount()=", ordersDetails.getAmount() + "");
            Log.e("ordersDetails.getTaxValue()=", ordersDetails.getTaxValue() + "");
            Log.e("ordersDetails.getDiscount()=", ordersDetails.getDiscount() + "");


        }


    }

    void SaveMasterVocher() {

        ReceiptMaster orderMaster = new ReceiptMaster();
        orderMaster.setIsPosted(0);
        if(cash.isChecked()) paymethod=1;
        else
            paymethod=0;

        orderMaster.setPaymethod(paymethod);
        orderMaster.setVOUCHERTYPE(2);


        orderMaster.setUserNo(Login.salmanNumber);
        orderMaster.setDiscounttype(distype);

        orderMaster.setVOUCHERTYPE(VochType);

        orderMaster.setDate(generalMethod.getCurentTimeDate(1));
        orderMaster.setTime(generalMethod.getCurentTimeDate(2));
        // orderMaster.setCustomerId();
        Log.e("getCustmByName(==", mydatabase.customers_dao().getCustmByName(Cus_selection) + "");
        //     orderMaster.setCustomerId(mydatabase.customers_dao().getCustmByName(Cus_selection) );

//        if (orderMaster.getVOUCHERTYPE() == 504) {
//            orderMaster.setCustomerId(customerInfoList.get(Cus_pos).getCustomerId());
//
//            Log.e("aaaaa(==", customerInfoList.get(Cus_pos).getCustomerId() + "");
//        } else {
//            Log.e("aaaaaaa44(==", VendorInfoList.get(Cus_pos).getCustomerId() + "");
//            orderMaster.setCustomerId(VendorInfoList.get(Cus_pos).getCustomerId());
//        }
        orderMaster.setCustomerId(cusno);
        Log.e("netTotal444==", netTotal + "");

        orderMaster.setNetTotal(netTotal);
        Log.e("netTotal777==", orderMaster.getNetTotal() + "");


        double totalnetsales = 0;
        double totalqty = 0;
        double totaltax = 0;
        double subtotal = 0;
        double dis = 0;
        for (int x = 0; x < ordersDetailslist.size(); x++) {
            totalqty += ordersDetailslist.get(x).getQty();
            totalnetsales += ordersDetailslist.get(x).getNetTotal();
            totaltax += ordersDetailslist.get(x).getTaxValue();
            subtotal += ordersDetailslist.get(x).getSubtotal();
            dis += ordersDetailslist.get(x).getTotalDiscVal();
        }
        orderMaster.setTotalQty(totalqty);
        if (AllVocherDiscount <= 0) {
            Log.e("totdisvalu1==", (totalnetsales * AllVocherDiscount) + "");
            orderMaster.setTotalVoucherDiscount(0);
            orderMaster.setVoucherDiscountvalue(AllVocherDiscount);
            orderMaster.setNetTotal(totalnetsales);
            orderMaster.setSubTotal(subtotal);
        } else {
            if (orderMaster.getDiscounttype() == 0) { //value
                Log.e("totdisvalu2==", (totalnetsales * AllVocherDiscount) + "");
                orderMaster.setVoucherDiscountvalue(AllVocherDiscount);
                orderMaster.setNetTotal(totalnetsales - AllVocherDiscount);
                orderMaster.setSubTotal(subtotal - AllVocherDiscount);

            } else { //perc

                orderMaster.setVoucherDiscountPerc(AllVocherDiscount);
                orderMaster.setNetTotal(totalnetsales - (totalnetsales * AllVocherDiscount));
                orderMaster.setSubTotal(subtotal - (subtotal * AllVocherDiscount));
                Log.e("totdisvalu3==", (totalnetsales * AllVocherDiscount) + "");
            }

        }
        orderMaster.setTotalVoucherDiscount(AllVocherDiscount);
        orderMaster.setTax(totaltax);


        SharedPreferences sharedPref = getSharedPreferences(SETTINGS_PREFERENCES, MODE_PRIVATE);
        vohno = Long.parseLong(sharedPref.getString(Login.maxVoch_PREF, ""));
        orderno = Long.parseLong(sharedPref.getString(Login.max_Order_PREF, ""));
        Log.e("c", vohno + "");
        // vohno =Integer.parseInt(vohno) ;
// save Vocher Num for order and vocher

        vohno1 = Long.parseLong(sharedPref.getString(Login.maxVoch_PREF, ""));
        orderno1 = Long.parseLong(sharedPref.getString(Login.max_Order_PREF, ""));

        try {
            Log.e("Serverorderno===",mydatabase.maxVoucherDao().getMaxOrderSerial()+" Localorderno= "+Localorderno);
            Serverorderno = Long.parseLong(mydatabase.maxVoucherDao().getMaxOrderSerial());
            Localorderno = mydatabase.receiptMaster_dao().getLastorderNo();
            Log.e("Serverorderno===",Serverorderno+" Localorderno= "+Localorderno);
            if(Localorderno>Serverorderno)neworderno=Localorderno;
            else
                neworderno=Serverorderno;
        }catch (Exception exception){
            neworderno= mydatabase.receiptMaster_dao().getLastorderNo();
            if(neworderno==0)neworderno=1;

        }



Log.e("orderno===",neworderno+"");
       {
           Log.e("orderno1===",orderno1+"");
            orderMaster.setVhfNo(orderno1);

           orderMaster.setNewVochNum(orderno1);
            mydatabase.receiptMaster_dao().insertOrder(orderMaster);

            for (int l = 0; l < ordersDetailslist.size(); l++) {
                ordersDetailslist.get(l).setVhfNo(orderno1);
                ordersDetailslist.get(l).setNewVochNum(orderno1);

                mydatabase.receiptDetails_dao().insertOrder(ordersDetailslist.get(l));
            }

            UpdateMaxVo(2);
        }
      //  UpdateMaxVoInDB();
      if(orderMaster.getVOUCHERTYPE()==504)  updateQtyInItemBalanceTable();
        ordersDetailslist.clear();
        vocher_Items.clear();


        fillAdapter();

    }

    void UpdateMaxVo(int flage) {

        SharedPreferences.Editor editor = getSharedPreferences(SETTINGS_PREFERENCES, MODE_PRIVATE).edit();
        Log.e("vohno==", vohno + "");
        if (flage == 1) editor.putString(Login.maxVoch_PREF, String.valueOf(++vohno));
        else

            editor.putString(Login.max_Order_PREF, String.valueOf(++orderno));
        editor.apply();
    }
    void UpdateMaxVoInDB() {

      long  orderno = Long.parseLong(mydatabase.maxVoucherDao().getMaxOrderSerial());

        mydatabase.maxVoucherDao().UpdateOrder(String.valueOf(orderno+1));
    }


    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_example, popup.getMenu());
        popup.show();
    }


    void importdata() {
        {

            mydatabase.itemsDao().deleteAll();
            mydatabase.itemUnitsDao().deleteAll();
            mydatabase.customers_dao().deleteAll();
//                            appDatabase.usersDao().deleteAll();
            mydatabase.itemUnitsDao().deleteAll();
            mydatabase.itemsBalanceDao().deleteAll();
            mydatabase.usersDao().deleteAll();
            listAlItemsBalances.clear();
            ImportData.AllImportItemlist.clear();
            Login.allUnitDetails.clear();
            Login. allUnitDetails2.clear();
            Login.allCustomers.clear();
            listAllVendor.clear();
            Login.allUsers.clear();

            //          importData.getAllItems3();


            importData.getAllItems(new ImportData.GetItemsCallBack() {
                @Override
                public void onResponse(JSONObject response) {

                    try {

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

                            Item_Unit_Details itemUnitDetails = new Item_Unit_Details();
                         //   itemUnitDetails.setCompanyNo(unitsArray.getJSONObject(i).getString("COMAPNYNO"));
                            itemUnitDetails.setItemNo(unitsArray.getJSONObject(i).getString("ITEMOCODE"));
                            itemUnitDetails.setUnitId(unitsArray.getJSONObject(i).getString("ITEMU"));
                            itemUnitDetails.setConvRate(Double.parseDouble(unitsArray.getJSONObject(i).getString("CALCQTY")));
                            itemUnitDetails.setSALEPRICE(Double.parseDouble(unitsArray.getJSONObject(i).getString("SALEPRICE")));
                            itemUnitDetails.setITEMBARCODE(unitsArray.getJSONObject(i).getString("ITEMBARCODE"));

                            Login.allUnitDetails.add(itemUnitDetails);

                        }

                        mydatabase.itemUnitsDao().addAll(Login.allUnitDetails);
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

                            Login.  allUnitDetails2.add(itemUnitDetails);
                        }
                        mydatabase.itemUnitsDao().addAll(  Login.allUnitDetails2);


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


                        }catch (Exception e){}

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    importData.getAllCustomers(new ImportData.GetCustomersCallBack() {
                        @Override
                        public void onResponse(JSONArray response) {

                            for (int i = 0; i < response.length(); i++) {

                                try {

                                    Login.allCustomers.add(new CustomerInfo(
                                            response.getJSONObject(i).getString("CUSTID"),
                                            response.getJSONObject(i).getString("CUSTNAME"),
                                            response.getJSONObject(i).getString("MOBILE"),
                                            1, 0));

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }

                            mydatabase.customers_dao().addAll(customerInfoList);
                            importData.getAllUsers(new ImportData.GetUsersCallBack() {
                                @Override
                                public void onResponse(JSONArray response) {


                                    for (int i = 0; i < response.length(); i++) {

                                        try {
                                            if(!response.getJSONObject(i).getString("USERTYPE").equals(""))
                                            Login.allUsers.add(new User(
                                                    response.getJSONObject(i).getString("SALESNO"),
                                                    response.getJSONObject(i).getString("ACCNAME").toLowerCase(Locale.ROOT),
                                                    response.getJSONObject(i).getString("USER_PASSWORD"),
                                                    Integer.parseInt(response.getJSONObject(i).getString("USERTYPE")),
                                                    Integer.parseInt("1"),
                                                    1));
                                            else
                                                Login.allUsers.add(new User(
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

                                    mydatabase.usersDao().addAll(Login.allUsers);
                                    importData.getAllVendor(new ImportData.GetUsersCallBack() {
                                        @Override
                                        public void onResponse(JSONArray response) {


                                            for (int i = 0; i < response.length(); i++) {

                                                try {
                                                    CustomerInfo vendourInfo = new CustomerInfo();
                                                    vendourInfo.setCustomerName(response.getJSONObject(i).getString("AccNameA"));
                                                    vendourInfo.setCustomerId(response.getJSONObject(i).getString("AccCode"));
                                                    vendourInfo.setIsVendor(1);
                                                    // vendourInfo.setSelect(0);
                                                    listAllVendor.add(vendourInfo);

//                                                            allUsers.add(new User(
//                                                                    response.getJSONObject(i).getString(""),
//                                                                    response.getJSONObject(i).getString("ACCNAME").toLowerCase(Locale.ROOT),
//                                                                    response.getJSONObject(i).getString("USER_PASSWORD"),
//                                                                    Integer.parseInt(response.getJSONObject(i).getString("USERTYPE")),
//                                                                    Integer.parseInt("1"),
//                                                                    1));

                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }

                                            }
                                            mydatabase.customers_dao().addAll(listAllVendor);
                                            ImportData importData=new ImportData(MainActivity.this);
                                            importData. fetchItemSwitchData("01/01/2020","01/12/2040");
//                                            importData.getItemsBalance(new ImportData.GetItemsBalanceCallBack() {
//                                                @Override
//                                                public void onResponse(JSONObject response) {
//                                                    try {
//                                                        JSONArray itemsArray = response.getJSONArray("SalesMan_Items_Balance");
//
//                                                        for (int i = 0; i < itemsArray.length(); i++) {
//                                                            ItemsBalance itemsBalance = new ItemsBalance();
//
//                                                            itemsBalance.setCOMAPNYNO(itemsArray.getJSONObject(i).getString("COMAPNYNO"));
//                                                            itemsBalance.setQTY(itemsArray.getJSONObject(i).getString("QTY"));
//                                                            itemsBalance.setItemOCode(itemsArray.getJSONObject(i).getString("ItemOCode"));
//                                                            itemsBalance.setSTOCK_CODE(itemsArray.getJSONObject(i).getString("STOCK_CODE"));
//
//                                                            listAlItemsBalances.add(itemsBalance);
//                                                        }
//                                                        mydatabase.itemsBalanceDao().addAll(listAlItemsBalances);
//                                                        ImportData importData=new ImportData(MainActivity.this);
//                                                        importData. fetchItemSwitchData("01/01/2020","01/12/2040");
//                                                        Log.e("listAlItemsBalances===", listAlItemsBalances.size() + "");
//                                                    } catch (Exception e) {
//                                                        Log.e("Exception===", e.getMessage() + "");
//                                                    }
//
////
//                                                }
//
//                                                @Override
//                                                public void onError(String error) {
//
//                                                }
//                                            });
//                                                            importData.getItemsBalance(new ImportData.GetItemsBalanceCallBack() {
//                                                                @Override
//                                                                public void onResponse(JSONObject response) {
//                                                                    try {
//                                                                        JSONArray itemsArray = response.getJSONArray("SalesMan_Items_Balance");
//                                                                        for (int i = 0; i < response.length(); i++) {
//
//                                                                            try {
//
//                                                                                ItemsBalance itemsBalance = new ItemsBalance();
//                                                                                Log.e("itemsBalance",itemsBalance.getCOMAPNYNO()+"");
//                                                                                itemsBalance.setCOMAPNYNO(itemsArray.getJSONObject(i).getString("COMAPNYNO"));
//                                                                                itemsBalance.setQTY(itemsArray.getJSONObject(i).getString("QTY"));
//                                                                                itemsBalance.setItemOCode(itemsArray.getJSONObject(i).getString("ItemOCode"));
//                                                                                itemsBalance.setSTOCK_CODE(itemsArray.getJSONObject(i).getString("STOCK_CODE"));
//
//                                                                                listAlItemsBalances.add(itemsBalance);
//
//
//                                                                            } catch (JSONException e) {
//                                                                                e.printStackTrace();
//                                                                            }
//                                                                        }
//                                                                    }catch (Exception e ){
//
//                                                                    }
//                                                                    mydatabase.itemsBalanceDao().addAll(listAlItemsBalances);
//                                                                }
//
//                                                                @Override
//                                                                public void onError(String error) {
//
//                                                                }
//                                                            });

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

                    if (!((error + "").contains("SSLHandshakeException") || (error + "").equals("null") ||
                            (error + "").contains("ConnectException") || (error + "").contains("NoRouteToHostException"))) {


                    }

                }
            });


        }
    }
//    @Override
//    public boolean onMenuItemClick(MenuItem item) {
//        Toast.makeText(this, "Selected Item: " +item.getTitle(), Toast.LENGTH_SHORT).show();
//
//        switch (item.getItemId()) {
//
//            case R.id.importdata:
//                Log.e("importdata==","importdata");
//                importdata();
//                return true;
//            case R.id.exportdata:
//                exportData.exportSalesVoucherM();
//                return true;
//            default:
//                return false;
//        }
//    }


    void addTotalDisDailog() {
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.addtotaldiscount);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = (int) (getResources().getDisplayMetrics().widthPixels / 1.19);
        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ImageView cancelButton = dialog.findViewById(R.id.cancelButton);
        AppCompatButton okButton = dialog.findViewById(R.id.okButton);
        EditText discEditText = dialog.findViewById(R.id.discEditText);
        RadioButton discValueRadioButton = dialog.findViewById(R.id.discValueRadioButton);
        RadioButton discPercRadioButton = dialog.findViewById(R.id.discPercRadioButton);

        dialog.show();


        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!discEditText.getText().toString().trim().equals("")) {
                    if (discPercRadioButton.isChecked()) {
                        distype = 1;
                        AllVocherDiscount = Double.parseDouble(discEditText.getText().toString().trim()) * 0.01;
//                total_dis.setText(AllVocherDiscount+ "");


                    } else {
                        distype = 0;
                        AllVocherDiscount = Double.parseDouble(discEditText.getText().toString().trim());
                        //          total_dis.setText(AllVocherDiscount+ "");
                    }
                    FillLastRowCalculations();
                    dialog.dismiss();
                } else
                    discEditText.setError(getResources().getString(R.string.required));
            }
        });
    }

    void FillLastRowCalculations() {

        try {
            double totalnetsales = 0;
            double totaltax = 0;
            double dis = 0;
            double disvalu = 0;
            double amount = 0;
            double tax = 0;
            double taxvalu = 0;
            double subtotal = 0;
            double netsal = 0;
            double totalnetsal = 0;
            for (int i = 0; i < vocher_Items.size(); i++) {
                amount = vocher_Items.get(i).getF_D() * vocher_Items.get(i).getQty()*Double.parseDouble(vocher_Items.get(i).getConvRate());
                disvalu = vocher_Items.get(i).getDiscount() * amount;

                tax = Double.parseDouble(vocher_Items.get(i).getConvRate()) * vocher_Items.get(i).getF_D() * vocher_Items.get(i).getTAXPERC();
                taxvalu = tax * vocher_Items.get(i).getQty();
                subtotal = amount - disvalu;
                netsal = subtotal;

                Log.e("netsal100==", netsal  + "taxvalu"+taxvalu);
                totalnetsal += netsal+taxvalu;

            }

            Log.e("totdisvalu22==", (totalnetsal * AllVocherDiscount) + "");

            Log.e("totdisvalu100==", totalnetsal  + "");
            if (distype == 0) {
                vlauoftotaldis.setText(GeneralMethod.convertToEnglish(String.valueOf(String.format("%.3f",AllVocherDiscount))));
                netsales.setText(GeneralMethod.convertToEnglish(String.format("%.3f", Math.abs(totalnetsal - AllVocherDiscount))));
            } else {
                netsales.setText(GeneralMethod.convertToEnglish(String.format("%.3f", Math.abs(totalnetsal - (totalnetsal * AllVocherDiscount)))));

                vlauoftotaldis.setText(GeneralMethod.convertToEnglish(String.valueOf(String.format("%.3f",totalnetsal * AllVocherDiscount))));



            }

            ////////
            TotalofRec=0;
            for (int position = 0; position < vocher_Items.size(); position++) {
                double TotalDiscVal = Double.parseDouble(vocher_Items.get(position).getConvRate())*vocher_Items.get(position).getQty() * vocher_Items.get(position).getF_D() * vocher_Items.get(position).getDiscount();


                double Tax = Double.parseDouble(vocher_Items.get(position).getConvRate())*vocher_Items.get(position).getF_D() * vocher_Items.get(position).getTAXPERC();
                double TaxValue = Tax * vocher_Items.get(position).getQty();

                double subtotal1 = Double.parseDouble(vocher_Items.get(position).getConvRate())*vocher_Items.get(position).getQty() * vocher_Items.get(position).getF_D() - TotalDiscVal - TaxValue;
                double nettotal1 = subtotal1 + TaxValue;
                TotalofRec+=nettotal1 ;
            }
            Total.setText(GeneralMethod.convertToEnglish(String.format("%.3f", TotalofRec)));

        } catch (Exception e) {

        }

    }


    TextView.OnKeyListener onKeyListener = new View.OnKeyListener() {
        @Override
        public boolean onKey(View view, int i, KeyEvent keyEvent) {


            Log.e("keyEvent.getAction()", keyEvent.getAction() + "");


            if (keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                switch (i) {
                    case KeyEvent.KEYCODE_DPAD_CENTER:
                    case KeyEvent.KEYCODE_ENTER:


                        switch (view.getId()) {

                            case R.id.item_code: {
                                if ((!itemcode.getText().equals(""))) {


                                    Log.e("itemcode==", itemcode.getText().toString().trim());
                                    item = mydatabase.itemsDao().getItembyCode(itemcode.getText().toString().trim());
                                  List<Item_Unit_Details>   itemUnitDetails =   mydatabase.itemUnitsDao(). getItemUnitsOfItem(item.getITEMNO());
                                    Log.e("itemUnitDetailssize==", itemUnitDetails.size() + "");
                                    Log.e("itemcode==", item + "");
                                    if (item != null) {

                                        itemqty.requestFocus();
                                        Log.e("itemname==", item.getNAME() + "");
                                        Log.e("qty==", item.getQty() + "");
                                        Log.e("itemprice==", item.getF_D() + "");
                                        Log.e("free==", "");
                                        Log.e("itemcode==", item.getITEMNO() + "");

                                        itemname.setText(item.getNAME());
                                        itemqty.setText(item.getQty() + "");
                                        itemprice.setText(GeneralMethod.convertToEnglish(String.valueOf(String.format("%.3f",item.getF_D()))) + "");
                                        free.setText("");

                                        //         addItem(item.getITEMNO());
                                        itemqty.requestFocus();
                                        final Handler handler = new Handler();
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                itemname.setText(item.getNAME());
                                                itemqty.setText(item.getQty() + "");
                                                itemprice.setText(GeneralMethod.convertToEnglish(String.valueOf(String.format("%.3f",item.getF_D())))  + "");
                                                free.setText("");

                                                //         addItem(item.getITEMNO());
                                                itemqty.requestFocus();
                                                itemqty.setEnabled(true);
                                            }
                                        }, 100);
                                    } else if (
                                    Exsitsin_itemswitchlist(itemcode.getText().toString())){
                                        itemqty.requestFocus();
                                         item=  mydatabase.itemsDao().getItembyCode( itemSwitch.getItemOCode());
                                        Log.e("itemname==", item.getNAME() + "");
                                        Log.e("qty==", item.getQty() + "");
                                        Log.e("itemprice==", item.getF_D() + "");
                                        Log.e("free==", "");
                                        Log.e("itemcode==", item.getITEMNO() + "");

                                        itemname.setText(item.getNAME());
                                        itemqty.setText(item.getQty() + "");
                                        itemprice.setText(GeneralMethod.convertToEnglish(String.valueOf(String.format("%.3f",item.getF_D()))) + "");
                                        free.setText("");

                                        //         addItem(item.getITEMNO());
                                        itemqty.requestFocus();
                                        final Handler handler = new Handler();
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                itemname.setText(item.getNAME());
                                                itemqty.setText(item.getQty() + "");
                                                itemprice.setText(GeneralMethod.convertToEnglish(String.valueOf(String.format("%.3f",item.getF_D())))  + "");
                                                free.setText("");

                                                //         addItem(item.getITEMNO());
                                                itemqty.requestFocus();
                                                itemqty.setEnabled(true);
                                            }
                                        }, 100);
                                    }else {

                                        final Handler handler = new Handler();
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                Log.e("else3==", "else");
                                                itemcode.setError("");
                                                itemcode.requestFocus();
                                                itemqty.setEnabled(false);
                                            }
                                        }, 100);
                                    }
//
                                } else {
                                    Log.e("else2==", "else");
                                    itemcode.setError("");
                                    itemcode.requestFocus();
                                    itemqty.setEnabled(false);

                                }

                                break;
                            }
                            case R.id.qty:
                                if ((!itemcode.getText().equals(""))) {
                                    if ((!itemqty.getText().equals(""))) {

                                        itemcode.setError(null);
                                        Log.e("KeyListeneritemqty==", itemqty.getText().toString().trim());
                                        double itembalnce = mydatabase.itemsBalanceDao().getBalance(itemcode.getText().toString());
                                        if (itembalnce >= Double.parseDouble(itemqty.getText().toString())) {


                                            final Handler handler = new Handler();
                                            handler.postDelayed(new Runnable() {
                                                @Override
                                                public void run() {

                                                    addItem(item.getITEMNO());
                                                    itemcode.requestFocus();


                                                }

                                            }, 100);

                                        } else {
                                            GeneralMethod.showSweetDialog(MainActivity.this, 0, "", getResources().getString(R.string.aviQTY)+" "+itembalnce);

                                            itemqty.setError("");
                                            itemqty.requestFocus();
                                        }
//
                                    } else {
                                        Log.e("else==", "else");
                                        itemqty.setError("");
                                        itemqty.requestFocus();
                                    }
                                } else {
                                    Log.e("else==", "else");
                                    itemcode.setError("");
                                    itemcode.requestFocus();
                                }
                                break;


                        }
                }

                return true;
            }
            return false;
        }
    };


    private void opensearchDailog(List<Items>AllItemDBlist,Context context) {
        AllItemstest.clear();

        dialog1 = new Dialog(MainActivity.this);
        dialog1.setCancelable(false);
        dialog1.setContentView(R.layout.searchdailog);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog1.getWindow().getAttributes());
        lp.width = 500;
        lp.height = 700;
        lp.gravity = Gravity.CENTER;
        dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog1.show();
        Log.e("size", AllItemDBlist.size() + "");
        final RecyclerView listView = dialog1.findViewById(R.id.Rec);
        listView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        final EditText search = dialog1.findViewById(R.id.search);
        final Spinner categorySpinner = dialog1.findViewById(R.id.categorySpinner);
        final Spinner kindSpinner = dialog1.findViewById(R.id.kindSpinner);
        dialog1.findViewById(R.id.clear_text).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                       MainActivity. search.setEnabled(true);
                        dialog1.dismiss();
                        search.setText("");

                    }
                }, 100);

            }
        });
        List<String> categories = new ArrayList<>();
        List<String> kinds = new ArrayList<>();
        kinds.clear();
        categories.clear();


        categories.add(0, getString(R.string.category));
        kinds.add(0, getString(R.string.kind));

        for (int i = 0; i < AllItemDBlist.size(); i++) {

            if (AllItemDBlist.get(i).getCATEOGRYID() != null && AllItemDBlist.get(i).getItemK() != null) {

                if (!categories.contains(AllItemDBlist.get(i).getCATEOGRYID()) && !AllItemDBlist.get(i).getCATEOGRYID().equals(""))
                    categories.add(AllItemDBlist.get(i).getCATEOGRYID());

                if (!kinds.contains(AllItemDBlist.get(i).getItemK()) && !AllItemDBlist.get(i).getItemK().equals(""))
                    kinds.add(AllItemDBlist.get(i).getItemK());

            }

        }

        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_dropdown_item_1line, categories);
        categorySpinner.setAdapter(categoryAdapter);
        categorySpinner.setSelection(0);

        ArrayAdapter<String> kindAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_dropdown_item_1line, kinds);
        kindSpinner.setAdapter(kindAdapter);
        kindSpinner.setSelection(0);

        OrderSearchAdapter  adapter1 = new OrderSearchAdapter(this, AllItemDBlist);
        listView.setAdapter(adapter1);

//        search.setOnTouchListener(new View.OnTouchListener() {
//            @SuppressLint("ClickableViewAccessibility")
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                final int DRAWABLE_START = 0;
//                final int DRAWABLE_TOP = 1;
//                final int DRAWABLE_END = 2;
//                final int DRAWABLE_BOTTOM = 3;
//
//                if (event.getAction() == MotionEvent.ACTION_UP) {
//                    int end = v.getResources().getConfiguration().getLayoutDirection() == LAYOUT_DIRECTION_RTL ? search.getLeft() : search.getRight();
//
//                    if (event.getRawX() >= (end - search.getCompoundDrawables()[DRAWABLE_END].getBounds().width())) {
//
//                        search.setText("");
//
//                        return true;
//                    }
//                }
//                return false;
//            }
//        });

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (categorySpinner.getSelectedItemPosition() == 0 && kindSpinner.getSelectedItemPosition() == 0 && editable.toString().trim().equals("")) {

                    OrderSearchAdapter adapter1 = new OrderSearchAdapter(MainActivity.this, AllItemDBlist);
                    listView.setAdapter(adapter1);

                } else {

                    searchItems(AllItemDBlist,categorySpinner.getSelectedItemPosition(), categorySpinner.getSelectedItem().toString(),
                            kindSpinner.getSelectedItemPosition(), kindSpinner.getSelectedItem().toString(),
                            editable.toString());

                    OrderSearchAdapter adapter2 = new OrderSearchAdapter(MainActivity.this, AllItemstest);
                    listView.setAdapter(adapter2);

                }

            }
        });

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (categorySpinner.getSelectedItemPosition() == 0 && kindSpinner.getSelectedItemPosition() == 0 && search.getText().toString().trim().equals("")) {

                    OrderSearchAdapter adapter1 = new OrderSearchAdapter(MainActivity.this, AllItemDBlist);
                    listView.setAdapter(adapter1);

                } else {

                    searchItems(AllItemDBlist,position, categorySpinner.getSelectedItem().toString(),
                            kindSpinner.getSelectedItemPosition(), kindSpinner.getSelectedItem().toString(),
                            search.getText().toString());

                    OrderSearchAdapter adapter1 = new OrderSearchAdapter(MainActivity.this, AllItemstest);
                    listView.setAdapter(adapter1);

                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {


            }
        });

        kindSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (categorySpinner.getSelectedItemPosition() == 0 && kindSpinner.getSelectedItemPosition() == 0 && search.getText().toString().trim().equals("")) {

                    OrderSearchAdapter adapter1 = new OrderSearchAdapter(MainActivity.this, AllItemDBlist);
                    listView.setAdapter(adapter1);

                } else {

                    searchItems(AllItemDBlist,categorySpinner.getSelectedItemPosition(), categorySpinner.getSelectedItem().toString(),
                            position, kindSpinner.getSelectedItem().toString(),
                            search.getText().toString());

                    OrderSearchAdapter adapter1 = new OrderSearchAdapter(MainActivity.this, AllItemstest);
                    listView.setAdapter(adapter1);

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void searchItems(List<Items>AllItemDBlist,int catPosition, String category, int kindPos, String kind, String search) {

        AllItemstest.clear();

        if (catPosition != 0) {

            for (int i = 0; i < AllItemDBlist.size(); i++) {

                if (AllItemDBlist.get(i).getCATEOGRYID().equals(category)) {

                    if (kindPos == 0 && search.trim().equals("")) {

                        AllItemstest.add(AllItemDBlist.get(i));

                    } else if (kindPos != 0 && search.trim().equals("")) {

                        if (AllItemDBlist.get(i).getItemK().equals(kind))
                            AllItemstest.add(AllItemDBlist.get(i));

                    } else if (kindPos == 0 && !search.trim().equals("")) {

                        if (AllItemDBlist.get(i).getNAME().toLowerCase().trim().contains(search.trim().toLowerCase()) ||
                                AllItemDBlist.get(i).getITEMNO().trim().contains(search.trim()))
                            AllItemstest.add(AllItemDBlist.get(i));

                    } else if (kindPos != 0 && !search.trim().equals("")) {

                        if (AllItemDBlist.get(i).getItemK().equals(kind) && (
                                AllItemDBlist.get(i).getNAME().toLowerCase().trim().contains(search.trim().toLowerCase()) ||
                                        AllItemDBlist.get(i).getITEMNO().trim().contains(search.trim())))
                            AllItemstest.add(AllItemDBlist.get(i));

                    }

                }

            }

        } else {

            if (kindPos == 0 && search.trim().equals("")) {

                AllItemstest = AllItemDBlist;


            } else {

                for (int i = 0; i < AllItemDBlist.size(); i++) {

                    if (kindPos != 0 && search.trim().equals("")) {

                        if (AllItemDBlist.get(i).getItemK().equals(kind))
                            AllItemstest.add(AllItemDBlist.get(i));

                    } else if (kindPos == 0 && !search.trim().equals("")) {

                        if (AllItemDBlist.get(i).getNAME().toLowerCase().trim().contains(search.trim().toLowerCase()) ||
                                AllItemDBlist.get(i).getITEMNO().trim().contains(search.trim()))
                            AllItemstest.add(AllItemDBlist.get(i));

                    } else if (kindPos != 0 && !search.trim().equals("")) {

                        if (AllItemDBlist.get(i).getItemK().equals(kind) && (
                                AllItemDBlist.get(i).getNAME().toLowerCase().trim().contains(search.trim().toLowerCase()) ||
                                        AllItemDBlist.get(i).getITEMNO().trim().contains(search.trim())))
                            AllItemstest.add(AllItemDBlist.get(i));

                    }

                }

            }

        }

    }


    private double getqtyINLocalList(String barcode) {


        double qty = 0;
        if (vocher_Items.size() != 0)
            for (int i = 0; i < vocher_Items.size(); i++) {

                if (
                        GeneralMethod.convertToEnglish(vocher_Items.get(i).getITEMNO()).equals(GeneralMethod.convertToEnglish(barcode))

                ) {

                    qty = vocher_Items.get(i).getQty();
                    break;

                } else {
                    qty = 0;
                    continue;
                }
            }

        return qty;


    }

    void removeZeroQtyFromList() {
        Log.e("vocher_ItemssizeBeforremove==", vocher_Items.size() + "");

        if (vocher_Items.size() != 0)
            for (int i = 0; i < vocher_Items.size(); i++) {

                if (vocher_Items.get(i).getQty() == 0) {
                    vocher_Items.remove(i);
                    i--;
                }
            }
        Log.e("vocher_ItemssizeAfetrremove==", vocher_Items.size() + "");

    }

    void CheckAllQtysINlist() {


        if (vocher_Items.size() != 0)
            for (int i = 0; i < vocher_Items.size(); i++) {
                double itembalnce=mydatabase.itemsBalanceDao().getBalance( vocher_Items.get(i).getITEMNO());
                Log.e("itembalnce==",itembalnce+" qty== "+vocher_Items.get(i).getQty());
                if (vocher_Items.get(i).getQty()>itembalnce) {
                    Log.e("ddddddd","ddddd");
                ItemsAdapter. qtyinvalidrespon.setText("vv");
                }
            }


    }
    void updateQtyInItemBalanceTable(){
        for (int i=0;i<  ordersDetailslist.size();i++)
        {
      double  Balance=     mydatabase.itemsBalanceDao().getBalance( ordersDetailslist.get(i).getItemNo());
        double newBalance=Balance-ordersDetailslist.get(i).getQty();

     int x=   mydatabase.itemsBalanceDao().updateqty(ordersDetailslist.get(i).getItemNo(),newBalance);

        }
    }
   boolean checkqtyinLocal(String itemnum){
       boolean flage=false;

       for (int i = 0; i < vocher_Items.size(); i++) {

           if(vocher_Items.get(i).getITEMNO().equals(itemnum)) {

               double itembalnce = mydatabase.itemsBalanceDao().getBalance(vocher_Items.get(i).getITEMNO());

               if (vocher_Items.get(i).getQty() < itembalnce) {

                   flage = true;

               }

           }
       }
       return flage;
   }
    @Override
    protected void onResume() {
        super.onResume();
    }
    public List<Items> creatlistGraterZero(){
        ArrayList<Items> myList = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            List<Items> matchingObjects = AllItemDBlist.stream().
                    filter(item -> item.getAviQty() > 0).
                    collect(Collectors.toList());
            myList.addAll(matchingObjects);
        }
        else {
            for(int i=0;i< AllItemDBlist.size();i++)

                if( AllItemDBlist.get(i).getQty()>0)
                {   myList.add(AllItemDBlist.get(i));}

     //       myList.addAll(AllItemDBlist);
            Log.e("creatlistGraterZero,myList",myList.size()+"");
        }

        return myList;

    }
//    List<Items> covert (){
//
//
//        for(ItemMaster_ItemBalanceDao.ItemMaster_ItemBalance o: loadListLiveData){
//            AllItemDBlist.add((Items) o);
//        }
//
//        return AllItemDBlist;
//
//
//}
    public static <T> List<T> cast(Collection<?> list) {
        List<T> valueList = new ArrayList<T>(list.size());

        for(Object o : list) {
            // throws casting exception
            valueList.add((T)o);
        }

        return valueList;
    }
    private boolean Exsitsin_itemswitchlist(String itemcode) {

        Log.e("Exsitsin_itemswit", "Exsitsin_itemswitchlist="+DB_itemswitch.size());
        Log.e("Exsitsin_itemswit", "itemcode="+itemcode);




        itemSwitch=mydatabase.itemSwitchDao().getitemSwitchByNcocd(itemcode);
        if(itemSwitch==null)return false;
        return true;

    }
    private boolean Exsitsin_itemsUnits(String itemcode) {

        Log.e("Exsitsin_itemsUnits", "Exsitsin_itemsUnits="+DB_itemswitch.size());
        Log.e("Exsitsin_itemsUnits", "itemcode="+itemcode);




        Item_itemUnitDetails=mydatabase.itemUnitsDao().SelectItemUnitsBybarcode(itemcode);
        if(Item_itemUnitDetails==null)return false;
        return true;

    }
//
public ArrayList<Items> creatlistGraterZero(List<Items> items){
    ArrayList<Items> myList = new ArrayList<>();
    Log.e("creatlistGraterZero,items",items.size()+"");
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        List<Items> matchingObjects = items.stream().
                filter(item -> item.getQty() > 0).
                collect(Collectors.toList());

        myList.addAll(matchingObjects);
        Log.e("creatlistGraterZero,myList",myList.size()+"");
    }else {
        for(int i=0;i< items.size();i++)

        if( items.get(i).getQty()<=0)
        {   items.remove(i);
        i--;}

        myList.addAll(items);
        Log.e("creatlistGraterZero,myList",myList.size()+"");
    }

    return myList;

}
    String    GetUnitName(String itemcode){
        Log.e("GetUnitName==",itemcode+" ");
        String name="";
        try {


            Item_Unit_Details unitDetails3 = null;
            Item_Unit_Details unitDetails = mydatabase.itemUnitsDao().getunitID2(itemcode);
            if (unitDetails != null)
                name = unitDetails.getUnitId();

            else {
                Item_Unit_Details unitDetails1 = mydatabase.itemUnitsDao().getunitID1(itemcode);
                if (unitDetails1 != null) name = unitDetails1.getUnitId();
                else {
                    ItemSwitch itemSwitch = mydatabase.itemSwitchDao().getitemSwitchByNcocd(itemcode);
                    if (itemSwitch != null)
                        unitDetails3 = mydatabase.itemUnitsDao().getunitID1(itemSwitch.getItemOCode());

                    if (unitDetails3 != null) name = unitDetails3.getUnitId();
                    else
                        name = "";
                }

            }

        }catch (Exception e){
            Log.e("Exception==",e.getMessage()+"");
             name="";
        }
        Log.e("name==",name+"");
        return name;

    }
}