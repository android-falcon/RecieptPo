package com.hiaryabeer.receiptapp.Acitvits;

import android.annotation.SuppressLint;
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
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.hiaryabeer.receiptapp.Adapters.ItemsAdapterNew;
import com.hiaryabeer.receiptapp.Adapters.SearchAdapter;
import com.hiaryabeer.receiptapp.Adapters.SearchAdapterNew;
import com.hiaryabeer.receiptapp.R;
import com.hiaryabeer.receiptapp.databinding.ActivityReceivePoBinding;
import com.hiaryabeer.receiptapp.models.AppDatabase;
import com.hiaryabeer.receiptapp.models.CustomerInfo;
import com.hiaryabeer.receiptapp.models.ExportData;
import com.hiaryabeer.receiptapp.models.GeneralMethod;
import com.hiaryabeer.receiptapp.models.ImportData;
import com.hiaryabeer.receiptapp.models.Item_Unit_Details;
import com.hiaryabeer.receiptapp.models.Items;
import com.hiaryabeer.receiptapp.models.ItemsBalance;
import com.hiaryabeer.receiptapp.models.POitems;
import com.hiaryabeer.receiptapp.models.PoInfo;
import com.hiaryabeer.receiptapp.models.ReceiptDetails;
import com.hiaryabeer.receiptapp.models.ReceiptMaster;
import com.hiaryabeer.receiptapp.models.User;
import com.hiaryabeer.receiptapp.viewmodel.ReceivePoViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
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

public class ReceivePO extends AppCompatActivity {
    public static Dialog dialog1;
    public static int paymethod = 0, VochType = 504;
    ReceivePoViewModel receivePoViewModel;
    public static ActivityReceivePoBinding binding;
    public static Items item;
    public static ArrayList<Items> vocher_Items = new ArrayList<>();
    int pos=-1,num_items = 1;
    int   POitemspos=-1;
    double AllVocherDiscount = 0;
    TextView Total;
    double itemTax = 0, itemTotal = 0, subTotal = 0, itemTotalAfterTax = 0, netTotal = 0;
    int distype = 0;
    public long orderno, vohno,orderno1, vohno1,newvohno;;
    public long ServerVochno,LocalVochno;
    public static List<POitems> POitems=new ArrayList<>();
    String Cus_selection;
    int Cus_pos = 0;
   ExportData exportData;
   GeneralMethod generalMethod;
    public static double  TotalofRec=0;
   AppDatabase mydatabase;
    public ArrayList<ReceiptDetails> ordersDetailslist = new ArrayList<>();
    RadioGroup payradioGroup;
    TextView menu;
    private List<CustomerInfo> customerInfoList = new ArrayList<>();
    ImportData importData;
    public static TextView desRespon;
    String custNAME="",custNumber="";
    public static List<POitems> AllItemstest = new ArrayList<>();
    TextView scanner,barCodTextTemp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive_po);
        receivePoViewModel=  ViewModelProviders.of(this).get(ReceivePoViewModel.class);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_receive_po);


        Init();
        vocher_Items.clear();
        POitems.clear();
        receivePoViewModel  .  fetchmaxNo("504",ReceivePO.this);



     //   binding.PONO.setOnKeyListener(onKeyListener);


    //   binding.itemCode.setOnKeyListener(onKeyListener);



  //  binding.qty.setOnKeyListener(onKeyListener);

//
        menu = findViewById(R.id.menuBtn);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Initializing the popup menu and giving the reference as current context
                PopupMenu popupMenu = new PopupMenu(ReceivePO.this, menu);

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
                                    generalMethod.showSweetDialog(ReceivePO.this, 3, getResources().getString(R.string.exportdatamsg), "");

                                break;
                            case R.id.exportdata:
                          exportData.exportSalesVoucherM(""+VochType); //يجب ان يتم حذف الكومينت
                                Log.e("export V",""+VochType);
                                break;
                            case R.id.Report:

                                if(vocher_Items.size()==0)
                                {     Intent i = new Intent(ReceivePO.this, MasterVochers_report.class);
                                startActivity(i);
                                finish();}
                                else{

                                                        new SweetAlertDialog(ReceivePO.this, SweetAlertDialog.WARNING_TYPE)
                                                                .setTitleText(getResources().getString(R.string.confirm_title))
                                                                .setContentText(getResources().getString(R.string.messageExit5))
                                                                .setConfirmButton(getResources().getString(R.string.yes), new SweetAlertDialog.OnSweetClickListener() {
                                                                    @Override
                                                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                                        sweetAlertDialog.dismissWithAnimation();

                                                                        vocher_Items.clear();
                                                                        fillAdapter();
                                                                        clearText();

                                                                        Intent i = new Intent(ReceivePO.this, MasterVochers_report.class);
                                                                        startActivity(i);
                                                                        finish();
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
                                break;

                            case R.id.addTotaldis:

                                if (vocher_Items.size() == 0)
                                    generalMethod.showSweetDialog(ReceivePO.this, 3, getResources().getString(R.string.fillbasket), "");
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

    public void readBarCode(EditText itemCodeText) {

        barCodTextTemp = itemCodeText;
        Log.e("barcode_099", "in");
        IntentIntegrator intentIntegrator = new IntentIntegrator(ReceivePO.this);
        intentIntegrator.setDesiredBarcodeFormats(intentIntegrator.ALL_CODE_TYPES);
        intentIntegrator.setBeepEnabled(false);
        intentIntegrator.setCameraId(0);
        intentIntegrator.setOrientationLocked(true);

        intentIntegrator.setPrompt("SCAN");
        intentIntegrator.setBarcodeImageEnabled(false);
        intentIntegrator.initiateScan();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        IntentResult Result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (Result != null) {
            if (Result.getContents() == null) {
                Log.d("MainActivity", "cancelled scan");
//                Toast.makeText(this, "cancelled", Toast.LENGTH_SHORT).show();
                Toast.makeText(this, "Cancel", Toast.LENGTH_SHORT).show();
            } else {
                Log.d("MainActivity", "Scanned");
                Log.e("MainActivity", "Result.getContents()"+Result.getContents().toString());
//
                barCodTextTemp.setText(Result.getContents() + "");
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    public void Init(){
       binding.qty.setEnabled(false);
       Total= findViewById(R.id.Total);

        //itemCode=findViewById(R.id.item_code);
       scanner=findViewById(R.id.scanner);
       scanner.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

               readBarCode( binding.itemCode);

           }
       });
       binding.PONO.setOnEditorActionListener(new TextView.OnEditorActionListener() {
           @Override
           public boolean onEditorAction(TextView textView, int actionId, KeyEvent event) {



               if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT
                       || actionId == EditorInfo.IME_ACTION_SEARCH
                       || actionId == EditorInfo.IME_NULL) {
//                    Log.e("setOnEditorActio", "afterTextChangedNOT" +"errorData\t"+actionId);
                   if(vocher_Items.size()==0)
                   {
                       if(!binding.PONO.getText().toString().equals(""))
                       {
                           binding.PONO.setError(null);
                           binding.Poqty.setText("");
                           binding. covrateShow.setText("");
                           binding.customerName.setText("");
                           clearText();
                           POitems.clear();
                           receivePoViewModel.fetchPOData( binding.PONO.getText().toString(),ReceivePO.this);

                       }
                       else binding.PONO.setError("");

                   }
                   else
                   {

                       new SweetAlertDialog(ReceivePO.this, SweetAlertDialog.WARNING_TYPE)
                               .setTitleText(getResources().getString(R.string.confirm_title))
                               .setContentText(getResources().getString(R.string.messageExit4))
                               .setConfirmButton(getResources().getString(R.string.yes), new SweetAlertDialog.OnSweetClickListener() {
                                   @Override
                                   public void onClick(SweetAlertDialog sweetAlertDialog) {
                                       sweetAlertDialog.dismissWithAnimation();
                                       vocher_Items.clear();
                                       fillAdapter();
                                       clearText();

                                  //     startActivity(new Intent(ReceivePO.this, Login.class));

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

               }
               return false;
           }
       });

       binding. edit.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
             Dialog  dialog1 = new Dialog(ReceivePO.this);
               dialog1.setCancelable(true);
               dialog1.setContentView(R.layout.password_dialog);
               WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
               lp.copyFrom(dialog1.getWindow().getAttributes());
               lp.width = 500;
               lp.height = 700;
               lp.gravity = Gravity.CENTER;
               dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
               dialog1.show();
           EditText    editText1=  dialog1.findViewById(R.id.editText1);
               AppCompatButton   done_button=    dialog1.findViewById(R.id.done_button);
               done_button   .setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       if(!editText1.getText().toString().equals("")){
                           {
                               dialog1.dismiss();
                               CheckIsExists(POitems,editText1.getText().toString());
                               if(CheckIsExistsINLocalList(editText1.getText().toString())) GeneralMethod.showSweetDialog(ReceivePO.this,3, "",getResources().getString(R.string.itemexists6));

                               binding.itemCode.setText(editText1.getText().toString());
                               if(POitems.size()!=0&&POitemspos!=-1) {
                                   final Handler handler = new Handler();
                                   handler.postDelayed(new Runnable() {
                                       @Override
                                       public void run() {

                                           binding.tableData.clearFocus();
                                           binding.itemName.setText(POitems.get(POitemspos).getITEMNAME());
                                           binding.Poqty.setText(POitems.get(POitemspos).getQty());
                                           binding.  covrateShow.setText( GetUnitName(editText1.getText().toString(),POitems.get(POitemspos).getSHOW_WHICHQTY())+"");
                                           if(!POitems.get(POitemspos).getCALCQTY().equals(""))
                                           binding.price.setText(POitems.get(POitemspos).getPriceL());

                                           binding.qty.setEnabled(true);
                                           binding.qty.requestFocus();
                                           item = new Items();
                                           item.setITEMNO(POitems.get(POitemspos).getItemOCode());
                                           item.setF_D(Double.parseDouble(binding.price.getText().toString()));
                                           item.setNAME(POitems.get(POitemspos).getITEMNAME());
                                           item.setTAXPERC(Double.parseDouble(POitems.get(POitemspos).getTTAXPERC() )/ 100);
                                           item.setItemNCode(POitems.get(POitemspos).getItemNCode());
                                           item.setWhichUNITSTR(POitems.get(POitemspos).getWHICHUNITSTR());
                                           item.setUNITBARCODE(POitems.get(POitemspos).getUNITBARCODE());
                                           item.setCALCQTY(POitems.get(POitemspos).getCALCQTY());
                                           item.setConvRate(POitems.get(POitemspos).getWHICHUQTY());

                                           binding.qty.requestFocus();
                                       }

                                   }, 100);

                               }  else   if(POitemspos==-1)
                               {

                                   GeneralMethod.showSweetDialog(ReceivePO.this, 0, "",binding.itemCode. getText().toString()+"  " +getResources().getString(R.string.itemexists));
                                   binding.itemCode.setText("");
                                   binding.itemCode.requestFocus();

                               }else   if(POitems.size()==0){
                                   binding.itemCode.setText("");
                                   GeneralMethod.showSweetDialog(ReceivePO.this, 0, "", getResources().getString(R.string.itemexists2));

                               }
                           }
                       }
                   }
               });
           }
       });

       binding.itemCode.setOnEditorActionListener(new TextView.OnEditorActionListener() {
           @Override
           public boolean onEditorAction(TextView textView, int actionId, KeyEvent event) {

               //  Log.e("actionId==", actionId+"KeyEvent=="+event.getAction());

               if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT
                       || actionId == EditorInfo.IME_ACTION_SEARCH
                       || actionId == EditorInfo.IME_NULL) {

//                    Log.e("setOnEditorActio", "afterTextChangedNOT" +"errorData\t"+actionId);


                   if(!binding.itemCode.getText().toString().equals(""))

                   {

                       CheckIsExists(POitems,binding.itemCode.getText().toString());
                       if(CheckIsExistsINLocalList(binding.itemCode.getText().toString())) GeneralMethod.showSweetDialog(ReceivePO.this,3, "",getResources().getString(R.string.itemexists6));

                       if(POitems.size()!=0&&POitemspos!=-1) {
                           final Handler handler = new Handler();
                           handler.postDelayed(new Runnable() {
                               @Override
                               public void run() {

                                   binding.tableData.clearFocus();
                                   binding.itemName.setText(POitems.get(POitemspos).getITEMNAME());
                                   binding.Poqty.setText(POitems.get(POitemspos).getQty());
                                   //" الوحدة: "+POitems.get(POitemspos).getWHICHUNITSTR()+" الحبات:  "+POitems.get(POitemspos).getWHICHUQTY()+""
                                   binding.  covrateShow.setText( GetUnitName(binding.itemCode.getText().toString(),POitems.get(POitemspos).getSHOW_WHICHQTY()));
                                   if(!POitems.get(POitemspos).getCALCQTY().equals(""))
                                       if(Double.parseDouble(POitems.get(POitemspos).getCALCQTY())>1)

                                           binding.price.setText(POitems.get(POitemspos).getPriceL());
                                       else
                                           binding.price.setText(POitems.get(POitemspos).getPriceL());
                                   else
                                   binding.price.setText(POitems.get(POitemspos).getPriceL());

                                   binding.qty.setEnabled(true);
                                   binding.qty.requestFocus();
                                   item = new Items();
                                   item.setITEMNO(POitems.get(POitemspos).getItemOCode());
                                   item.setF_D(Double.parseDouble(binding.price.getText().toString()));
                                   item.setNAME(POitems.get(POitemspos).getITEMNAME());
                                   item.setTAXPERC(Double.parseDouble(POitems.get(POitemspos).getTTAXPERC() )/ 100);
                                   item.setItemNCode(POitems.get(POitemspos).getItemNCode());
                                   item.setWhichUNITSTR(POitems.get(POitemspos).getWHICHUNITSTR());
                                   item.setUNITBARCODE(POitems.get(POitemspos).getUNITBARCODE());
                                   item.setCALCQTY(POitems.get(POitemspos).getCALCQTY());
                                   item.setConvRate(POitems.get(POitemspos).getWHICHUQTY());
                                   binding.qty.requestFocus();
                               }

                           }, 100);

                       }  else   if(POitems.size()==0){
                           binding.itemCode.setText("");
                           GeneralMethod.showSweetDialog(ReceivePO.this, 0, "", getResources().getString(R.string.itemexists2));

                       }else   if(POitemspos==-1)
                       {

                           GeneralMethod.showSweetDialog(ReceivePO.this, 0, "",binding.itemCode. getText().toString()+"  " +getResources().getString(R.string.itemexists));
                           binding.itemCode.setText("");
                           binding.itemCode.requestFocus();

                       }
                   }  else binding.itemCode.setError("");






               }
               return false;
           }
       });
       binding.qty.setOnEditorActionListener(new TextView.OnEditorActionListener() {
           @Override
           public boolean onEditorAction(TextView textView, int actionId, KeyEvent event) {

               //  Log.e("actionId==", actionId+"KeyEvent=="+event.getAction());

               if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT
                       || actionId == EditorInfo.IME_ACTION_SEARCH
                       || actionId == EditorInfo.IME_NULL) {
//                    Log.e("setOnEditorActio", "afterTextChangedNOT" +"errorData\t"+actionId);

                   if ((!binding.itemCode.getText().equals(""))) {
                       if (binding.qty.getText().toString().length()!=0) {

//                                        if (VochType == 504) {

                           final Handler handler = new Handler();
                           handler.postDelayed(new Runnable() {
                               @Override
                               public void run() {
                                   String    itemno=binding.itemCode.getText().toString().trim();
                                   if (!CheckIsExistsINLocalList(itemno)) {
                                       // item.setBalanceQty(getBalanceforitem(itemno));
                                       Log.e("case1==", "case1");
                                       Log.e("case1==", ""+binding.Poqty.getText().toString()+" "+binding.qty.getText().toString());
                                   if(!binding.qty.getText().equals(""))
                                   {   if(
                                           Double.parseDouble(binding.qty.getText().toString().trim())>0
                                           &&Double.parseDouble(binding.Poqty.getText().toString().trim())>=Double.parseDouble(binding.qty.getText().toString().trim()))
                                       {    Log.e("case1.1==", "case1.1");

                                           Log.e("getTAXPERC==", ""+item.getTAXPERC());
                                           item.setQty(Double.parseDouble(binding.qty.getText().toString().trim()));
                                           item.setAmount(item.getF_D() * item.getQty() * num_items);
                                           item.setAviQty( Double.parseDouble(binding.Poqty.getText().toString().trim())-   item.getQty());

                                           vocher_Items.add(item);
                                           fillAdapter();
                                           Log.e("vocher_Items4==", vocher_Items.size() + "");
                                       }
                                       else{
                                           GeneralMethod.showSweetDialog(ReceivePO.this, 0, "", getResources().getString(R.string.qtyerror));
                                           binding.qty.setText("");
                                           binding.qty.requestFocus();
                                           Log.e("case1.2==", "case1.2");

                                       }}else{
                                       GeneralMethod.showSweetDialog(ReceivePO.this, 0, "", getResources().getString(R.string.qtyerror));
                                       binding.qty.setText("");
                                       binding.qty.requestFocus();
                                       Log.e("case1.2==", "case1.2");
                                   }

                                   } else {
                                       Log.e("case2==", "case2  ,"+vocher_Items.get(pos).getAviQty()+"");
                                       if(Double.parseDouble(binding.Poqty.getText().toString().trim())>=Double.parseDouble(binding.qty.getText().toString().trim())+  vocher_Items.get(pos).getQty()) {
                                           Log.e("case2.1==", "case2.1");

                                           vocher_Items.get(pos).setQty(vocher_Items.get(pos).getQty() + Double.parseDouble(binding.qty.getText().toString().trim()));

                                           vocher_Items.get(pos).setAmount(vocher_Items.get(pos).getF_D() * vocher_Items.get(pos).getQty() * num_items);
                                           Log.e("aviqty11==",vocher_Items.get(pos).getAviQty()+"");
                                           vocher_Items.get(pos).setAviQty(vocher_Items.get(pos).getBalanceQty() -  vocher_Items.get(pos).getQty());
                                           //   updateAviQtyInDBlist( vocher_Items.get(pos).getITEMNO(), vocher_Items.get(pos).getAviQty());
                                           Log.e("aviqty22==",vocher_Items.get(pos).getAviQty()+"   , "+vocher_Items.get(pos).getQty()+" , " +"");
                                           Log.e("vocher_Items3==", vocher_Items.size() + "");
                                           fillAdapter();
                                       }else{
                                           GeneralMethod.showSweetDialog(ReceivePO.this, 0, "", getResources().getString(R.string.notenoughQTY));
                                              binding.qty.setText("");
                                           binding.qty.requestFocus();
                                           Log.e("case2.2==", "case2.2");

                                       }

                                   }
                               }

                           }, 100);

//                                        }
//                                        else {
//                                            final Handler handler = new Handler();
//                                            handler.postDelayed(new Runnable() {
//                                                @Override
//                                                public void run() {
//
//                                                    addItem(item.getITEMNO());
//                                                    itemcode.requestFocus();
//
//
//                                                }
//
//                                            }, 100);
//                                        }
//
                       } else {
                           Log.e("else==", "else");
                           binding    .qty.setError("");
                           binding .qty.requestFocus();
                       }
                   } else {
                       Log.e("else==", "else");
                       binding.   itemCode.setError("");
                       binding.itemCode.requestFocus();
                   }




               }
               return false;
           }
       });


       mydatabase=AppDatabase.getInstanceDatabase(ReceivePO.this);
       importData=new ImportData(ReceivePO.this);
       binding.save.setOnClickListener(onClickListener);
       binding. cancelBtn.setOnClickListener(onClickListener);
       binding.  tableData.setLayoutManager(new LinearLayoutManager(ReceivePO.this));
       exportData =new ExportData(ReceivePO.this);
       generalMethod=new GeneralMethod(ReceivePO.this);
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
       receivePoViewModel.PoInfoMutableLiveData.observe(this, new Observer<List<PoInfo>>() {
           @Override
           public void onChanged(List<PoInfo> poInfos) {
               if(poInfos!=null)
               {  binding.customerName.setText(poInfos.get(0).getAccName());
                   custNumber    =poInfos.get(0).getAccCode();
                   custNAME=poInfos.get(0).getAccName();
               }
               else
               {
                   binding.PONO.setError("");
                   binding.PONO.requestFocus();
               }
           }
       });

       receivePoViewModel.PoItemsMutableLiveData.observe(this, new Observer<List<POitems>>() {
           @Override
           public void onChanged(List<POitems> pOitems) {
               if(pOitems!=null)
               {
                   POitems.clear();
                   POitems.addAll(pOitems);

                   for(int i=0;i<POitems.size();i++)
                   {
                       POitems.get(i).  setSHOW_WHICHQTY(POitems.get(i).getWHICHUQTY());
                       if( POitems.get(i).getCALCQTY().equals("")|| POitems.get(i).getCALCQTY().equals("0"))
                       {
                           Log.e("here","here");
                           POitems.get(i).setCALCQTY("1");
                           POitems.get(i). setWHICHUQTY("1");
                       }
                       if(POitems.get(i).getWHICHUNIT().equals("0")){
                           POitems.get(i).setCALCQTY("1");
                           POitems.get(i). setWHICHUQTY("1");

                       }

                       Log.e("here","getCALCQTY="+ POitems.get(i).getCALCQTY());
                   }
//                   com.hiaryabeer.receiptapp.models.POitems pOitems1=new POitems();
//                   pOitems1.setTransNo("21");
//                   pOitems1.setQty("33");
//                   pOitems1.setBonus("0");
//                   pOitems1.setTTAXPERC("16");
//                   pOitems1.setENTERPRICE("33");
//                   pOitems1.setItemOCode("62710280808771");
//                   pOitems1.setITEMNAME("item2");
//                   pOitems1.setItemNCode("62710280805555");
//
//                   com.hiaryabeer.receiptapp.models.POitems pOitems2=new POitems();
//                   pOitems2.setItemOCode("62710280808772");
//                   pOitems2.setTransNo("21");
//                   pOitems2.setQty("30");
//                   pOitems2.setBonus("0");
//                   pOitems2.setTTAXPERC("16");
//                   pOitems2.setENTERPRICE("30");
//                   pOitems2.setItemNCode("62710280808772");
//                   pOitems2.setITEMNAME("item2");
//
//
//                   com.hiaryabeer.receiptapp.models.POitems pOitems3=new POitems();
//                   pOitems3.setItemOCode("62710280808773");
//                   pOitems3.setTransNo("21");
//                   pOitems3.setQty("35");
//                   pOitems3.setBonus("0");
//                   pOitems3.setTTAXPERC("16");
//                   pOitems3.setENTERPRICE("35");
//                   pOitems3.setItemNCode("62710280808773");
//                   pOitems3.setITEMNAME("item2");
//
//
//                   POitems.add(pOitems1);
//                   POitems.add(pOitems2);
//                   POitems.add(pOitems3);
//                   POitems.get(0).setQty("35");
                //   POitems.get(1).setItemOCode("62710280808774");
               }


           }   });
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
       binding.search.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Log.e("POitems==",POitems.size()+"");
               opensearchDailog(POitems,ReceivePO.this);
           }
       });
   }
    void addTotalDisDailog() {
        final Dialog dialog = new Dialog(ReceivePO.this);
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
    void clearText() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

             binding.   itemName.setText("");
                binding.  qty.setText("");
                binding. price.setText("");
                binding.   free.setText("");
                binding.   itemCode.setText("");
                binding.  itemCode.requestFocus();
                binding. customerTextInput.setError(null);
                binding.  customerTv.setText("");
                AllVocherDiscount = 0;
                Log.e("clearText", "clearText");
                binding.  qty.setEnabled(false);
            }

        }, 100);


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
                            case R.id.PONO:
                            if(vocher_Items.size()==0)
                            {
                                if(!binding.PONO.getText().toString().equals(""))
                                {
                                    binding.qty.setEnabled(false);
                            binding.PONO.setError(null);
                              receivePoViewModel.fetchPOData( binding.PONO.getText().toString(),ReceivePO.this);

                                }
                                else binding.PONO.setError("");

                            }
                            else
                            {

                                new SweetAlertDialog(ReceivePO.this, SweetAlertDialog.WARNING_TYPE)
                                        .setTitleText(getResources().getString(R.string.confirm_title))
                                        .setContentText(getResources().getString(R.string.messageExit4))
                                        .setConfirmButton(getResources().getString(R.string.yes), new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                sweetAlertDialog.dismissWithAnimation();
                                                vocher_Items.clear();
                                                fillAdapter();
                                                clearText();

                                          //      startActivity(new Intent(ReceivePO.this, Login.class));

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

                            break;
                            case R.id.item_code:




                                break;

                            case R.id.  qty:
                                if ((!binding.itemCode.getText().equals(""))) {
                                    if ((!binding.qty.getText().equals(""))) {

//                                        if (VochType == 504) {

                                            final Handler handler = new Handler();
                                            handler.postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    String    itemno=binding.itemCode.getText().toString().trim();
                                                    if (!CheckIsExistsINLocalList(itemno)) {
                                                       // item.setBalanceQty(getBalanceforitem(itemno));
                                                        Log.e("case1==", "case1");
                                                        if(Double.parseDouble(binding.Poqty.getText().toString().trim())>=Double.parseDouble(binding.qty.getText().toString().trim()))
                                                        {    Log.e("case1.1==", "case1.1");
                                                            item.setTAXPERC(Double.parseDouble(POitems.get(POitemspos).getTTAXPERC() )/ 100);
                                                            Log.e("getTAXPERC==", ""+item.getTAXPERC());
                                                            item.setQty(Double.parseDouble(binding.qty.getText().toString().trim()));
                                                            item.setAmount(item.getF_D() * item.getQty() * num_items);
                                                            item.setAviQty( Double.parseDouble(binding.Poqty.getText().toString().trim())-   item.getQty());

                                                            vocher_Items.add(item);
                                                            fillAdapter();
                                                            Log.e("vocher_Items4==", vocher_Items.size() + "");
                                                        }
                                                        else{
                                                            GeneralMethod.showSweetDialog(ReceivePO.this, 0, "", getResources().getString(R.string.qtyerror));

                                                            Log.e("case1.2==", "case1.2");

                                                        }

                                                    } else {
                                                        Log.e("case2==", "case2  ,"+vocher_Items.get(pos).getAviQty()+"");
                                                        if(Double.parseDouble(binding.Poqty.getText().toString().trim())>=Double.parseDouble(binding.qty.getText().toString().trim())+  vocher_Items.get(pos).getQty()) {
                                                            Log.e("case2.1==", "case2.1");

                                                            vocher_Items.get(pos).setQty(vocher_Items.get(pos).getQty() + Double.parseDouble(binding.qty.getText().toString().trim()));

                                                            vocher_Items.get(pos).setAmount(vocher_Items.get(pos).getF_D() * vocher_Items.get(pos).getQty() * num_items);
                                                            Log.e("aviqty11==",vocher_Items.get(pos).getAviQty()+"");
                                                            vocher_Items.get(pos).setAviQty(vocher_Items.get(pos).getBalanceQty() -  vocher_Items.get(pos).getQty());
                                                            //   updateAviQtyInDBlist( vocher_Items.get(pos).getITEMNO(), vocher_Items.get(pos).getAviQty());
                                                            Log.e("aviqty22==",vocher_Items.get(pos).getAviQty()+"   , "+vocher_Items.get(pos).getQty()+" , " +"");
                                                            Log.e("vocher_Items3==", vocher_Items.size() + "");
                                                            fillAdapter();
                                                       }else{
                                                            GeneralMethod.showSweetDialog(ReceivePO.this, 0, "", getResources().getString(R.string.notenoughQTY));

                                                            Log.e("case2.2==", "case2.2");

                                                        }

                                                    }
                                                }

                                            }, 100);

//                                        }
//                                        else {
//                                            final Handler handler = new Handler();
//                                            handler.postDelayed(new Runnable() {
//                                                @Override
//                                                public void run() {
//
//                                                    addItem(item.getITEMNO());
//                                                    itemcode.requestFocus();
//
//
//                                                }
//
//                                            }, 100);
//                                        }
//
                                    } else {
                                        Log.e("else==", "else");
                                        binding    .qty.setError("");
                                        binding .qty.requestFocus();
                                    }
                                } else {
                                    Log.e("else==", "else");
                                    binding.   itemCode.setError("");
                                    binding.itemCode.requestFocus();
                                }


                                break;

                        }
                }

                return true;
            }
            return false;
        }
    };
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.save:
                    removeZeroQtyFromList();
                    if (vocher_Items.size() != 0) {
                        Log.e("vocher_Items==", vocher_Items.size() + "");

                    //    if (Cus_selection != null && !Cus_selection.equals("")) {
                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                   binding. itemCode.setError(null);

                                    SaveDetialsVocher();
                                    SaveMasterVocher();
                                    POitems.clear();
                                    //    generalMethod.showSweetDialog(MainActivity.this, 1, getResources().getString(R.string.savedSuccsesfule), "");
                                    binding.        customerTextInput.setError(null);
                                    binding.   cridt.setChecked(true);
                                    binding.   vocher.setChecked(true);
                               //     clearText();
                                    binding.PONO.setText("");
                                    binding.PONO.requestFocus();
                                    binding.customerName.setText("");
                                 exportData.exportSalesVoucherM(""+VochType);//يجب ان ترجع
                                    Log.e("exportq V",""+VochType);

                                    //    AllItemDBlist.clear();


//                                    AllItemDBlist=    mydatabase.itemsDao().loadListLiveData();
//                                    AllItemDBlist.addAll( mydatabase.itemsDao().loadListLiveData2()) ;
                            //        Log.e("AllItemDBlist.size===",AllItemDBlist.size()+"");

                                }

                            }, 100);


//                        } else
//
//                        {
//                            binding.customerTextInput.setError(getResources().getString(R.string.required));
//                            generalMethod.showSweetDialog(ReceivePO.this, 3, getResources().getString(R.string.selectcustoumer), "");
//                        }
                    } else
                        generalMethod.showSweetDialog(ReceivePO.this, 3, getResources().getString(R.string.fillbasket), "");
                    break;

                case R.id.cancel_btn:

                    try {
                        Handler h = new Handler(Looper.getMainLooper());
                        h.post(new Runnable() {
                            public void run() {


                                new SweetAlertDialog(ReceivePO.this, SweetAlertDialog.WARNING_TYPE)
                                        .setTitleText(getResources().getString(R.string.confirm_title))
                                        .setContentText(getResources().getString(R.string.messageExit))
                                        .setConfirmButton(getResources().getString(R.string.yes), new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sweetAlertDialog) {


                                                if (vocher_Items.size() != 0) {
                                                    new SweetAlertDialog(ReceivePO.this, SweetAlertDialog.WARNING_TYPE)
                                                            .setTitleText(getResources().getString(R.string.confirm_title))
                                                            .setContentText(getResources().getString(R.string.messageExit2))
                                                            .setConfirmButton(getResources().getString(R.string.yes), new SweetAlertDialog.OnSweetClickListener() {
                                                                @Override
                                                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                                    sweetAlertDialog.dismissWithAnimation();

                                                                    vocher_Items.clear();
                                                                    fillAdapter();
                                                                    clearText();

                                                                    startActivity(new Intent(ReceivePO.this, HomeActivity.class));

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
                                                    Intent intent = new Intent(ReceivePO.this, HomeActivity.class);
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
    private boolean CheckIsExistsINLocalList( String barcode) {


        boolean flag = false;
        if (vocher_Items.size() != 0)
            for (int i = 0; i < vocher_Items.size(); i++) {

                if (
                        GeneralMethod.convertToEnglish(vocher_Items.get(i).getITEMNO()).equals(GeneralMethod.convertToEnglish(barcode))
                            ||
                           GeneralMethod. convertToEnglish(vocher_Items.get(i).getITEMNO()).equals(GeneralMethod.convertToEnglish(mydatabase.itemSwitchDao().getitemocode(barcode)))

//                                )
                                ||
                                GeneralMethod. convertToEnglish(vocher_Items.get(i).getItemNCode()).equals(GeneralMethod.convertToEnglish(barcode))

                ) {
                    pos = i;
                    flag = true;
                    break;

                } else {
                    flag = false;
                    continue;
                }
            }

        return flag;


    }

    private boolean CheckIsExists(List<POitems> vocher_Items,String barcode) {
        Log.e("vocher_Items==",""+vocher_Items.size());
        POitemspos=-1;
        boolean flag = false;
        if (vocher_Items.size() != 0)
            for (int i = 0; i < vocher_Items.size(); i++) {
                Log.e("barcode1==",vocher_Items.get(i).getItemOCode());
                if (
                        GeneralMethod.convertToEnglish(vocher_Items.get(i).getItemOCode()).equals(GeneralMethod.convertToEnglish(barcode))
                              ||
                                GeneralMethod. convertToEnglish(vocher_Items.get(i).getItemNCode()).equals(GeneralMethod.convertToEnglish(barcode))
||                            GeneralMethod. convertToEnglish(vocher_Items.get(i).getItemOCode()).equals(GeneralMethod.convertToEnglish(mydatabase.itemSwitchDao().getitemocode(barcode)))
                        ||         GeneralMethod. convertToEnglish(vocher_Items.get(i).getItemOCode()).equals(GeneralMethod.convertToEnglish(mydatabase.itemUnitsDao().getUnitbyBarcode(barcode)))
//
//                                )


                ) {
                    Log.e("barcode==",barcode+"  "+vocher_Items.get(i).getItemOCode());
                    POitemspos = i;
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
                Log.e("vocher_Items===",vocher_Items.size()+"");
              //  vocher_Items=     creatlistGraterZero(vocher_Items);
                Log.e("vocher_Items===",vocher_Items.size()+"");
              binding.  tableData.setAdapter(new ItemsAdapterNew(vocher_Items, ReceivePO.this));
                FillLastRowCalculations();

                binding. itemName.setText("");
                binding.    qty.setText("");
                binding.    price.setText("");
                binding.     free.setText("");
                binding.    Poqty.setText("");
                binding.  covrateShow.setText("");
                binding.       customerTextInput.setError(null);

                Log.e("clearText", "clearText");
                binding.      qty.setEnabled(false);
                binding.    itemCode.setText("");
                binding.   itemCode.requestFocus();
                binding.      qty.setError(null);
            }

        }, 100);
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


                totalnetsal += netsal;

            }

            Log.e("totdisvalu22==", (totalnetsal * AllVocherDiscount) + "");


            if (distype == 0) {

                binding.    vlauoftotaldis.setText(GeneralMethod.convertToEnglish(String.valueOf(String.format("%.3f",AllVocherDiscount))));
                binding.  netsales.setText(GeneralMethod.convertToEnglish(String.format("%.3f", Math.abs(totalnetsal - AllVocherDiscount))));
            } else {
                binding.     netsales.setText(GeneralMethod.convertToEnglish(String.format("%.3f", Math.abs(totalnetsal - (totalnetsal * AllVocherDiscount)))));
                binding.   vlauoftotaldis.setText(GeneralMethod.convertToEnglish(String.valueOf(String.format("%.3f",totalnetsal * AllVocherDiscount))));
            }


            ///////

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
    @SuppressLint("LongLogTag")
    void removeZeroQtyFromList() {
        Log.e("vocher_ItemssizeBeforremove==", vocher_Items.size() + "");

        if (vocher_Items.size() != 0)
            for (int i = 0; i < vocher_Items.size(); i++) {

                if (vocher_Items.get(i).getQty() == 0) {
                    vocher_Items.remove(i);
                    i--;
                }
            }
        Log.e("C==", vocher_Items.size() + "");

    }

    @SuppressLint("LongLogTag")
    void SaveDetialsVocher() {
        Log.e("vocher_ItemsSize==", vocher_Items.size() + "");
        for (int i = 0; i < vocher_Items.size(); i++) {
            ReceiptDetails ordersDetails = new ReceiptDetails();

            ordersDetails.setVOUCHERTYPE(VochType);
//         cash.
            if(binding.cash.isChecked()) paymethod=1;
            else
                paymethod=0;
            ordersDetails.setPaymethod(paymethod);


            ordersDetails.setDiscount(vocher_Items.get(i).getDiscount());
            ordersDetails.setItemNo(vocher_Items.get(i).getITEMNO());
            ordersDetails.setFree(vocher_Items.get(i).getFree());
            ordersDetails.setItemName(vocher_Items.get(i).getNAME());
            ordersDetails.setQty(vocher_Items.get(i).getQty());
            ordersDetails.setPrice(vocher_Items.get(i).getF_D());
            Log.e("getF_D==",  vocher_Items.get(i).getF_D()+ ",");

            ordersDetails.setDate(generalMethod.getCurentTimeDate(1));
            ordersDetails.setTime(generalMethod.getCurentTimeDate(2));
            ordersDetails.setAmount(vocher_Items.get(i).getF_D()*vocher_Items.get(i).getQty()*Double.parseDouble(vocher_Items.get(i).getConvRate()));
            Log.e("getF_D33==",  vocher_Items.get(i).getF_D()+ ","+vocher_Items.get(i).getQty());
            ordersDetails.setTaxPercent(vocher_Items.get(i).getTAXPERC());
           ordersDetails.setUnit(vocher_Items.get(i).getConvRate());
            ordersDetails.setConvRate(vocher_Items.get(i).getConvRate());
            ordersDetails.setCALCQTY(vocher_Items.get(i).getCALCQTY());
            ordersDetails.setUNITBARCODE(vocher_Items.get(i).getUNITBARCODE());
            if (ordersDetails.getUnit().equals("1"))
                ordersDetails.setWhichUNIT("0");
            else
                ordersDetails.setWhichUNIT("1");

            ordersDetails.setWhichUNITSTR(vocher_Items.get(i).getWhichUNITSTR());

      //    double num_items = mydatabase.itemUnitsDao().getConvRatebyitemnum(ordersDetails.getItemNo());
            ordersDetails.setWHICHUQTY(vocher_Items.get(i).getConvRate()+ "");

            // ordersDetails.setCustomerId(mydatabase.customers_dao().getCustmByName(Cus_selection));
            ordersDetails.setCustomerId("");
//            if (ordersDetails.getVOUCHERTYPE() == 504)
//                ordersDetails.setCustomerId(customerInfoList.get(Cus_pos).getCustomerId());
//            else ordersDetails.setCustomerId(VendorInfoList.get(Cus_pos).getCustomerId());

            ordersDetails.setIsPosted(0);
            ordersDetails.setArea("");

//            ordersDetails.setUnit(vocher_Items.get(i).getUnit().equals("One Unit") ? 0 : 1);

            //Discount calcualtios
            Log.e("getAmount22==",   ordersDetails.getAmount()+"");
                    ordersDetails.setTotalDiscVal(ordersDetails.getAmount() * ordersDetails.getDiscount());
            Log.e("getAmount==",   ordersDetails.getAmount()+ ","+ordersDetails.getDiscount());
            Log.e("setTotalDiscVal==",   ordersDetails.getTotalDiscVal() + "");
            ordersDetails.setTotal(vocher_Items.get(i).getAmount() - ordersDetails.getTotalDiscVal());

            //Tax calcualtios
            Log.e("fd=", vocher_Items.get(i).getF_D() + "ttax" + vocher_Items.get(i).getTAXPERC());
            ordersDetails.setTax(vocher_Items.get(i).getF_D() * vocher_Items.get(i).getTAXPERC());
            Log.e("tax=", ordersDetails.getTax() + "");
            ordersDetails.setTaxValue(ordersDetails.getTax() * vocher_Items.get(i).getQty());

            // ضريبة شاملة
            double subtotal = 0;
            subtotal = ordersDetails.getAmount() - ordersDetails.getTaxValue() - ordersDetails.getTotalDiscVal();

            ordersDetails.setSubtotal(subtotal);
            Log.e("ordersDetails.getSubtotal()=", ordersDetails.getSubtotal() + "");

            double nettotal = 0;
            nettotal = ordersDetails.getSubtotal() + ordersDetails.getTaxValue();
            ordersDetails.setNetTotal(nettotal);
            Log.e("ordersDetails.getNetTotal()=", ordersDetails.getNetTotal() + "");


            // ضريبة خاضعة
      /*    double subtotal=ordersDetails.getAmount()-ordersDetails.getDiscount();
            ordersDetails.setSubtotal(subtotal);*/


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
        if(binding.cash.isChecked()) paymethod=1;
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
        orderMaster.setCustomerId(custNumber);
        orderMaster.setCust_Name(custNAME);
        orderMaster.setNOTE(binding.note.getText().toString()+"");
//        if (orderMaster.getVOUCHERTYPE() == 504) {
//            orderMaster.setCustomerId(customerInfoList.get(Cus_pos).getCustomerId());
//            Log.e("aaaaa(==", customerInfoList.get(Cus_pos).getCustomerId() + "");
//        } else {
//            Log.e("aaaaaaa44(==", VendorInfoList.get(Cus_pos).getCustomerId() + "");
//            orderMaster.setCustomerId(VendorInfoList.get(Cus_pos).getCustomerId());
//        }

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
        vohno1 = Long.parseLong(sharedPref.getString(Login.maxVoch_PREF, ""));
        orderno1 = Long.parseLong(sharedPref.getString(Login.max_Order_PREF, ""));




        Log.e("vohno===", vohno + "");
        // vohno =Integer.parseInt(vohno) ;
// save Vocher Num for order and vocher
//        if (orderMaster.getVOUCHERTYPE() == 504) {
//            orderMaster.setVhfNo(vohno);
//            mydatabase.receiptMaster_dao().insertOrder(orderMaster);
//
//            for (int l = 0; l < ordersDetailslist.size(); l++) {
//                ordersDetailslist.get(l).setVhfNo(vohno);
//                mydatabase.receiptDetails_dao().insertOrder(ordersDetailslist.get(l));
//            }
//
//          //  UpdateMaxVo(1);
//        } else {
//            orderMaster.setVhfNo(orderno);
//            mydatabase.receiptMaster_dao().insertOrder(orderMaster);
//
//            for (int l = 0; l < ordersDetailslist.size(); l++) {
//                ordersDetailslist.get(l).setVhfNo(orderno);
//                mydatabase.receiptDetails_dao().insertOrder(ordersDetailslist.get(l));
//            }
//
//            //UpdateMaxVo(2);
//        }
        mydatabase=AppDatabase.getInstanceDatabase(ReceivePO.this);
        try {
            ServerVochno = Long.parseLong(mydatabase.maxVoucherDao().getMaxVocherSerial());
            LocalVochno = mydatabase.receiptMaster_dao().getLastVoherNo();
            Log.e("ServerVochno===",ServerVochno+" LocalVochno= "+LocalVochno);
            if(LocalVochno>ServerVochno)newvohno=LocalVochno;
            else
                newvohno=ServerVochno;
        }catch (Exception exception){
            newvohno= mydatabase.receiptMaster_dao().getLastVoherNo();
            if(newvohno==0)newvohno=1;
        }



        Log.e("orderno===",newvohno+"");



    //    UpdateMaxVoInDB();
        orderMaster.setTransNo(POitems.get(0).getVHFNo());
        Log.e("vohno1===",vohno1+"");
        orderMaster.setVhfNo(vohno1);
        orderMaster.setNewVochNum(vohno1);
        mydatabase.receiptMaster_dao().insertOrder(orderMaster);

        for (int l = 0; l < ordersDetailslist.size(); l++) {

            ordersDetailslist.get(l).setVhfNo(vohno1);
            ordersDetailslist.get(l).setNewVochNum(vohno1);
            ordersDetailslist.get(l).setTransNo(POitems.get(0).getVHFNo());
            mydatabase.receiptDetails_dao().insertOrder(ordersDetailslist.get(l));
            Log.e("c====",  "cccccc");
        }



//        if(orderMaster.getVOUCHERTYPE()==504)  updateQtyInItemBalanceTable();
        UpdateMaxVo(1);
        ordersDetailslist.clear();
        vocher_Items.clear();

        fillAdapter();

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
            Login.allUnitDetails2 .clear();
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
                        //    itemUnitDetails.setCompanyNo(unitsArray.getJSONObject(i).getString("COMAPNYNO"));
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

                                            ImportData importData=new ImportData(ReceivePO.this);
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
//                                                        ImportData importData=new ImportData(ReceivePO.this);
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
    void UpdateMaxVo(int flage) {

        SharedPreferences.Editor editor = getSharedPreferences(SETTINGS_PREFERENCES, MODE_PRIVATE).edit();
        Log.e("vohno==", vohno + "");
        if (flage == 1) editor.putString(Login.maxVoch_PREF, String.valueOf(++vohno));
        else

            editor.putString(Login.max_Order_PREF, String.valueOf(++orderno));
        editor.apply();
    }
    private void opensearchDailog(List<POitems>AllItemInPolist, Context context) {
        AllItemstest.clear();
        dialog1 = new Dialog(ReceivePO.this);
        dialog1.setCancelable(false);
        dialog1.setContentView(R.layout.searchdailog);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog1.getWindow().getAttributes());
        lp.width = 500;
        lp.height = 700;
        lp.gravity = Gravity.CENTER;
        dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog1.show();
        Log.e("size", AllItemInPolist.size() + "");
        final RecyclerView listView = dialog1.findViewById(R.id.Rec);
        listView.setLayoutManager(new LinearLayoutManager(ReceivePO.this));

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
                       search.setEnabled(true);
                        dialog1.dismiss();
                        search.setText("");

                    }
                }, 100);

            }
        });
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

                    SearchAdapterNew adapter1 = new SearchAdapterNew(ReceivePO.this, AllItemInPolist);
                    listView.setAdapter(adapter1);

                } else {

                    searchItems(AllItemInPolist,0, "",
                           0, "",
                            editable.toString());

                    SearchAdapterNew adapter2 = new SearchAdapterNew(ReceivePO.this, AllItemstest);
                    listView.setAdapter(adapter2);

                }

            }
        });
        List<String> categories = new ArrayList<>();
        List<String> kinds = new ArrayList<>();
        kinds.clear();
        categories.clear();


//        categories.add(0, getString(R.string.category));
//        kinds.add(0, getString(R.string.kind));
//
//        for (int i = 0; i < AllItemInPolist.size(); i++) {
//
//            if (AllItemInPolist.get(i).getCATEOGRYID() != null && AllItemInPolist.get(i).getItemK() != null) {
//
//                if (!categories.contains(AllItemInPolist.get(i).getCATEOGRYID()) && !AllItemInPolist.get(i).getCATEOGRYID().equals(""))
//                    categories.add(AllItemInPolist.get(i).getCATEOGRYID());
//
//                if (!kinds.contains(AllItemInPolist.get(i).getItemK()) && !AllItemInPolist.get(i).getItemK().equals(""))
//                    kinds.add(AllItemInPolist.get(i).getItemK());
//
//            }
//
//        }

//        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(ReceivePO.this, android.R.layout.simple_dropdown_item_1line, categories);
//        categorySpinner.setAdapter(categoryAdapter);
//        categorySpinner.setSelection(0);
//
//        ArrayAdapter<String> kindAdapter = new ArrayAdapter<>(ReceivePO.this, android.R.layout.simple_dropdown_item_1line, kinds);
//        kindSpinner.setAdapter(kindAdapter);
//        kindSpinner.setSelection(0);

        SearchAdapterNew adapter1 = new SearchAdapterNew(this, AllItemInPolist);
        listView.setAdapter(adapter1);



//        search.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//                if (categorySpinner.getSelectedItemPosition() == 0 && kindSpinner.getSelectedItemPosition() == 0 && editable.toString().trim().equals("")) {
//
//                    SearchAdapter adapter1 = new SearchAdapter(ReceivePO.this, AllItemInPolist);
//                    listView.setAdapter(adapter1);
//
//                } else {
//
//                    searchItems(AllItemInPolist,categorySpinner.getSelectedItemPosition(), categorySpinner.getSelectedItem().toString(),
//                            kindSpinner.getSelectedItemPosition(), kindSpinner.getSelectedItem().toString(),
//                            editable.toString());
//
//                    SearchAdapter adapter2 = new SearchAdapter(ReceivePO.this, AllItemstest);
//                    listView.setAdapter(adapter2);
//
//                }
//
//            }
//        });

//        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//
//                if (categorySpinner.getSelectedItemPosition() == 0 && kindSpinner.getSelectedItemPosition() == 0 && search.getText().toString().trim().equals("")) {
//
//                    SearchAdapter adapter1 = new SearchAdapter(ReceivePO.this, AllItemInPolist);
//                    listView.setAdapter(adapter1);
//
//                } else {
//
//                    searchItems(AllItemInPolist,position, categorySpinner.getSelectedItem().toString(),
//                            kindSpinner.getSelectedItemPosition(), kindSpinner.getSelectedItem().toString(),
//                            search.getText().toString());
//
//                    SearchAdapter adapter1 = new SearchAdapter(ReceivePO.this, AllItemstest);
//                    listView.setAdapter(adapter1);
//
//                }
//
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//
//            }
//        });
//
//        kindSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//
//                if (categorySpinner.getSelectedItemPosition() == 0 && kindSpinner.getSelectedItemPosition() == 0 && search.getText().toString().trim().equals("")) {
//
//                    SearchAdapter adapter1 = new SearchAdapter(ReceivePO.this, AllItemInPolist);
//                    listView.setAdapter(adapter1);
//
//                } else {
//
//                    searchItems(AllItemInPolist,categorySpinner.getSelectedItemPosition(), categorySpinner.getSelectedItem().toString(),
//                            position, kindSpinner.getSelectedItem().toString(),
//                            search.getText().toString());
//
//                    SearchAdapter adapter1 = new SearchAdapter(ReceivePO.this, AllItemstest);
//                    listView.setAdapter(adapter1);
//
//                }
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });


    }

    private void searchItems(List<POitems>AllItemDBlist,int catPosition, String category, int kindPos, String kind, String search) {

        AllItemstest.clear();



            if (kindPos == 0 && search.trim().equals("")) {
                AllItemstest.clear();
                AllItemstest.addAll(AllItemDBlist) ;


            } else {

                for (int i = 0; i < AllItemDBlist.size(); i++) {

                    if (kindPos == 0 && !search.trim().equals("")) {

                        if (AllItemDBlist.get(i).getITEMNAME().toLowerCase().trim().contains(search.trim().toLowerCase()) ||
                                AllItemDBlist.get(i).getItemOCode().trim().contains(search.trim()))
                            AllItemstest.add(AllItemDBlist.get(i));

                    }else  if (kindPos == 0 && search.trim().equals("")){
                        AllItemstest.addAll(AllItemDBlist);
                    }

                }

            }



    }
    @SuppressLint("LongLogTag")
    public ArrayList<Items> creatlistGraterZero(List<Items> items){

        Log.e("creatlistGraterZero,items",items.size()+"");
       List<Items> matchingObjects = items.stream().
                filter(item -> item.getQty()>0).
                collect(Collectors.toList());
       ArrayList<Items> myList = new ArrayList<>();
        myList.addAll(matchingObjects);
        Log.e("creatlistGraterZero,myList",myList.size()+"");
        return myList;

    }
    void UpdateMaxVoInDB() {

        long  Vouchno = Long.parseLong(mydatabase.maxVoucherDao().getMaxVocherSerial());

        mydatabase.maxVoucherDao().UpdateVouch(String.valueOf(Vouchno+1));
    }

    String    GetUnitName(String itemcode,String covrate){
        String name="";
    try    {    Log.e("GetUnitName==",itemcode+" "+covrate);

        Item_Unit_Details unitDetails=mydatabase.itemUnitsDao().getunitID( itemcode, "1");

        if(unitDetails!=null)
            name=unitDetails.getUnitId();
        Log.e("name==",name+"");
    }catch (Exception e){
        Log.e("Exception==",e.getMessage()+"");
         name="";
    }
        return name;

    }
}