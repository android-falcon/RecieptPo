package com.hiaryabeer.receiptapp.models;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hiaryabeer.receiptapp.Acitvits.Login;
import com.hiaryabeer.receiptapp.Interfaces.ApiService;
import com.hiaryabeer.receiptapp.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

import static android.content.Context.MODE_PRIVATE;
import static com.hiaryabeer.receiptapp.Acitvits.Login.SETTINGS_PREFERENCES;

public class ImportData {
   public static List<Items> AllImportItemlist = new ArrayList<>();
   public static ArrayList<ItemSwitch> listAllItemSwitch = new ArrayList<>();
   ApiService apiService;
   private static Context context;
   public String ipAddress = "", CONO = "",ipPort="", headerDll = "", link = "";
   public static SweetAlertDialog pditem,pDialog,pDialog2, pDialog3, pDialog4,pDialog5;
AppDatabase my_dataBase;
   SharedPreferences sharedPref;
   private void getIpAddress() {
 headerDll="";
    //  headerDll= "/Falcons/VAN.Dll/";
      ipAddress = Login.ipAddress;
      ipPort = Login.ipPort;
      CONO =Login.coNo;

   }
   public ImportData(Context context) {
      this.context = context;
      my_dataBase = AppDatabase.getInstanceDatabase(context);
      try {
         getIpAddress();
         link = "http://" + ipAddress.trim() + headerDll.trim();

         Retrofit retrofit = RetrofitInstance.getInstance(link);
         Log.e("link",link+"");
         apiService = retrofit.create(ApiService.class);
         sharedPref = context.getSharedPreferences(SETTINGS_PREFERENCES, MODE_PRIVATE);

      } catch (Exception e) {
         Log.e("Exception==",e.getMessage());

      }

   }
   public void getAllItems2(){
      try {


         pditem = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
         pditem.getProgressHelper().setBarColor(Color.parseColor("#FDD835"));
         pditem.setTitleText(context.getResources().getString(R.string.getdata));
         pditem.setCancelable(false);
         pditem.show();
         AllImportItemlist.clear();
         Log.e("context", context.getClass().getName().toString());



         if (!ipAddress.equals(""))
         {  Log.e("ipAddress", ipAddress);
//            AllItems_fetchCallData();

         }
         else
            Toast.makeText(context, "Fill Ip", Toast.LENGTH_SHORT).show();

      }catch (Exception e){
         Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();

      }

   }
   public void getAllItems3(){


         Gson gson = new GsonBuilder()
                 .registerTypeAdapter(Items.class, new Items.DataStateDeserializer())
                 .create();


      Call<Items>  call = apiService.gatItemInfoDetail("1","295");
      Log.e("myData", "" + call.request());
      Log.e("myData", "" + call.isExecuted());
//      call.enqueue(new Callback<Items>() {
//         @Override
//         public void onResponse(Call<Items> call, Response<Items> response) {
//            if (!response.isSuccessful()) {
//
//               Log.e("onResponse333", "not=" + response.message());
//
//
//            } else {
//               AllImportItemlist.clear();
//               AllImportItemlist.addAll(response.body());
//               my_dataBase.itemsDao().addAll(AllImportItemlist);
//
//            }
//         }
//
//         @Override
//         public void onFailure(Call<List<Items>> call, Throwable throwable) {
//            Log.e("onFailure333", "=" + throwable.getMessage());
//            //  Toast.makeText(context, "throwable333"+throwable.getMessage(), Toast.LENGTH_SHORT).show();
//
//
//
//         }
//      });
   }
//   public void AllItems_fetchCallData() {
//
//      Call <Items> myData = apiService.gatItemInfoDetail("1",CONO);
//      Log.e("myData", "" + myData.request());
//      Log.e("AllItems_fetchCallData", "AllItems_fetchCallData");
//      myData.enqueue(new Callback<List<Items>>() {
//         @Override
//         public void onResponse(Call<List<Items>> call, Response<List<Items>> response) {
//            if (!response.isSuccessful()) {
//
//               Log.e("onResponse333", "not=" + response.message());
//               pditem.dismissWithAnimation();
//
//            } else {
//               AllImportItemlist.clear();
//               AllImportItemlist.addAll(response.body());
//               my_dataBase.itemsDao().addAll(AllImportItemlist);
//          pditem.dismissWithAnimation();
//            }
//         }
//
//         @Override
//         public void onFailure(Call<List<Items>> call, Throwable throwable) {
//            Log.e("onFailure333", "=" + throwable.getMessage());
//            //  Toast.makeText(context, "throwable333"+throwable.getMessage(), Toast.LENGTH_SHORT).show();
//
//
//            pditem.dismissWithAnimation();
//         }
//      });
//   }
   public interface GetItemsCallBack {

      void onResponse(JSONObject response);

      void onError(String error);

   }
   public void getAllItems(GetItemsCallBack getItemsCallBack) {

      pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);

      pDialog.getProgressHelper().setBarColor(Color.parseColor("#115571"));
      pDialog.setTitleText("Loading ...");
      pDialog.setCancelable(false);
      pDialog.show();
      //http://10.0.0.22:8085/GetVanAllData?STRNO=1&CONO=295
      if (!ipAddress.equals("") || !CONO.equals(""))
         link = "http://" + ipAddress +  headerDll + "/GetVanAllData?STRNO=1&CONO=" + CONO;

      Log.e("getItems_Link", link);

      JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(link, new Response.Listener<JSONObject>() {

         @Override
         public void onResponse(JSONObject response) {

            getItemsCallBack.onResponse(response);
            pDialog.dismissWithAnimation();
//                GeneralMethod.showSweetDialog(context, 1, "Items Saved", null);
//            Log.e("getItems_Response", response + "");

         }
      }, new Response.ErrorListener() {
         @Override
         public void onErrorResponse(VolleyError error) {

            getItemsCallBack.onError(error.getMessage() + "");
            pDialog.dismissWithAnimation();
            if ((error.getMessage() + "").contains("SSLHandshakeException") || (error.getMessage() + "").equals("null")) {

               GeneralMethod.showSweetDialog(context, 0, null, "Connection to server failed");

            } else if ((error.getMessage() + "").contains("ConnectException")) {

               GeneralMethod.showSweetDialog(context, 0, "Connection Failed", null);

            } else if ((error.getMessage() + "").contains("NoRouteToHostException")) {

               GeneralMethod.showSweetDialog(context, 3, "", "Please check the entered IP info");

            } else if ((error.getMessage() + "").contains("No Data Found")) {

               GeneralMethod.showSweetDialog(context, 3, "", "No Items Found");

            }
            Log.e("getItems_Error", error.getMessage() + "");

         }
      });

      jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(30 * 1000, 2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
      RequestQueueSingleton.getInstance(context.getApplicationContext()).addToRequestQueue(jsonObjectRequest);

   }
   public interface GetCustomersCallBack {

      void onResponse(JSONArray response);

      void onError(String error);

   }
   public void getAllCustomers(GetCustomersCallBack getCustomersCallBack) {

      pDialog2 = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);

      pDialog2.getProgressHelper().setBarColor(Color.parseColor("#115571"));
      pDialog2.setTitleText("Loading ...");
      pDialog2.setCancelable(false);
      pDialog2.show();
      if (!ipAddress.equals("")  || !CONO.equals(""))
         link = "http://" + ipAddress +  headerDll + "/ADMGetCustomer?CONO=" + CONO;
//        else
//            link = "http://" + ipEdt.getText().toString().trim() + ":" +
//                    portEdt.getText().toString().trim() +
//                    headerDll + "/ADMGetCustomer?CONO=" + coNoEdt.getText().toString().trim();

      Log.e("getCustms_Link", link);

      JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(link, new Response.Listener<JSONArray>() {
         @Override
         public void onResponse(JSONArray response) {

            getCustomersCallBack.onResponse(response);
            pDialog2.dismissWithAnimation();

            Log.e("getCustms_Response", response + "");

         }
      }, new Response.ErrorListener() {
         @Override
         public void onErrorResponse(VolleyError error) {

            getCustomersCallBack.onError(error.getMessage() + "");
            pDialog2.dismissWithAnimation();
            if ((error.getMessage() + "").contains("SSLHandshakeException") || (error.getMessage() + "").equals("null")) {

               GeneralMethod.showSweetDialog(context, 0, null, "Connection to server failed");

            } else if ((error.getMessage() + "").contains("ConnectException")) {

               GeneralMethod.showSweetDialog(context, 0, "Connection Failed", null);

            } else if ((error.getMessage() + "").contains("NoRouteToHostException")) {

               GeneralMethod.showSweetDialog(context, 3, "", "Please check the entered IP info");

            } else if ((error.getMessage() + "").contains("No Data Found")) {

               GeneralMethod.showSweetDialog(context, 3, "", "No Customers Found");

            }
            Log.e("getCustms_Error", error.getMessage() + "");

         }
      });

      jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
      RequestQueueSingleton.getInstance(context.getApplicationContext()).addToRequestQueue(jsonArrayRequest);

   }
   public interface GetUsersCallBack {

      void onResponse(JSONArray response);

      void onError(String error);

   }
   public interface GetItemsBalanceCallBack {

      void onResponse( JSONObject response);

      void onError(String error);

   }
   public interface GetVendorCallBack {

      void onResponse(JSONArray response);

      void onError(String error);

   }
   public void getAllUsers(GetUsersCallBack getUsersCallBack, String ipAddress, String ipPort, String coNo) {

      pDialog3 = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);

      pDialog3.getProgressHelper().setBarColor(Color.parseColor("#115571"));
      pDialog3.setTitleText("Loading ...");
      pDialog3.setCancelable(false);
      pDialog3.show();
      if (!ipAddress.equals("")  || !coNo.equals(""))
         link = "http://" + ipAddress + headerDll.trim() + "/ADMSALESMAN?CONO=" + coNo;

      Log.e("getUsers_Link", link);

      JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(link, new Response.Listener<JSONArray>() {
         @Override
         public void onResponse(JSONArray response) {

            getUsersCallBack.onResponse(response);
            pDialog3.dismissWithAnimation();
          //  GeneralMethod.showSweetDialog(context, 1, "Data Saved", null);
            Log.e("getUsers_Response", response + "");

         }
      }, new Response.ErrorListener() {
         @Override
         public void onErrorResponse(VolleyError error) {

            getUsersCallBack.onError(error.getMessage() + "");
            pDialog3.dismissWithAnimation();
            if ((error.getMessage() + "").contains("SSLHandshakeException") || (error.getMessage() + "").equals("null")) {

               GeneralMethod.showSweetDialog(context, 0, null, "Connection to server failed");

            } else if ((error.getMessage() + "").contains("ConnectException")) {

               GeneralMethod.showSweetDialog(context, 0, "Connection Failed", null);

            } else if ((error.getMessage() + "").contains("NoRouteToHostException")) {

               GeneralMethod.showSweetDialog(context, 3, "", "Please check the entered IP info");

            } else if ((error.getMessage() + "").contains("No Data Found")) {

               GeneralMethod.showSweetDialog(context, 3, "", "No Users Found");
               my_dataBase.usersDao().insertUser(new User("010101", "admin", "2022", 1, 1, 0));

            }
            Log.e("getUsers_Error", error.getMessage() + "");

         }
      });

      jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
      RequestQueueSingleton.getInstance(context.getApplicationContext()).addToRequestQueue(jsonArrayRequest);

   }
   public void getItemsBalance(GetItemsBalanceCallBack getItemsCallBack) {

      pDialog5 = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);

      pDialog5.getProgressHelper().setBarColor(Color.parseColor("#115571"));
      pDialog5.setTitleText("Loading Items Balance");
      pDialog5.setCancelable(false);
      pDialog5.show();
      //http://10.0.0.22:8085/GetVanAllData?STRNO=1&CONO=295
      if (!ipAddress.equals("") || !CONO.equals(""))
         link = "http://" + ipAddress +  headerDll + "/GetVanAllData?STRNO=1&CONO=" + CONO;

      Log.e("gettemsBalance_Link", link);

      JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(link, new Response.Listener<JSONObject>() {

         @Override
         public void onResponse(JSONObject response) {

            getItemsCallBack.onResponse(response);
            pDialog5.dismissWithAnimation();
            GeneralMethod.showSweetDialog(context, 1, "Data Saved", null);
//            Log.e("getItemsBalance_Response", response + "");

         }
      }, new Response.ErrorListener() {
         @Override
         public void onErrorResponse(VolleyError error) {

            getItemsCallBack.onError(error.getMessage() + "");
            pDialog5.dismissWithAnimation();
            if ((error.getMessage() + "").contains("SSLHandshakeException") || (error.getMessage() + "").equals("null")) {

               GeneralMethod.showSweetDialog(context, 0, null, "Connection to server failed");

            } else if ((error.getMessage() + "").contains("ConnectException")) {

               GeneralMethod.showSweetDialog(context, 0, "Connection Failed", null);

            } else if ((error.getMessage() + "").contains("NoRouteToHostException")) {

               GeneralMethod.showSweetDialog(context, 3, "", "Please check the entered IP info");

            } else if ((error.getMessage() + "").contains("No Data Found")) {

               GeneralMethod.showSweetDialog(context, 3, "", "No Items Found");

            }
            Log.e("getItems_Error", error.getMessage() + "");

         }
      });

      jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(30 * 1000, 2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
      RequestQueueSingleton.getInstance(context.getApplicationContext()).addToRequestQueue(jsonObjectRequest);

   }

   public void getAllVendor(GetUsersCallBack GetVendorCallBack, String ipAddress, String ipPort, String coNo) {

      pDialog4 = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);

      pDialog4.getProgressHelper().setBarColor(Color.parseColor("#115571"));
      pDialog4.setTitleText("Loading ...");
      pDialog4.setCancelable(false);
      pDialog4.show();
      if (!ipAddress.equals("")  || !coNo.equals(""))
         link = "http://" + ipAddress + headerDll.trim() + "/GetVendorAll?CONO=" + coNo;

      Log.e("getAllVendor", link);

      JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(link, new Response.Listener<JSONArray>() {
         @Override
         public void onResponse(JSONArray response) {

            GetVendorCallBack.onResponse(response);
            pDialog4.dismissWithAnimation();

            Log.e("getAllVendor", response + "");

         }
      }, new Response.ErrorListener() {
         @Override
         public void onErrorResponse(VolleyError error) {

            GetVendorCallBack.onError(error.getMessage() + "");
            pDialog4.dismissWithAnimation();
            if ((error.getMessage() + "").contains("SSLHandshakeException") || (error.getMessage() + "").equals("null")) {

               GeneralMethod.showSweetDialog(context, 0, null, "Connection to server failed");

            } else if ((error.getMessage() + "").contains("ConnectException")) {

               GeneralMethod.showSweetDialog(context, 0, "Connection Failed", null);

            } else if ((error.getMessage() + "").contains("NoRouteToHostException")) {

               GeneralMethod.showSweetDialog(context, 3, "", "Please check the entered IP info");

            } else if ((error.getMessage() + "").contains("No Data Found")) {

               GeneralMethod.showSweetDialog(context, 3, "", "No Users Found");
               my_dataBase.usersDao().insertUser(new User("010101", "admin", "2022", 1, 1, 0));

            }
            Log.e("getAllVendorError", error.getMessage() + "");

         }
      });

      jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
      RequestQueueSingleton.getInstance(context.getApplicationContext()).addToRequestQueue(jsonArrayRequest);

   }
   public void fetchItemSwitchData(String from,String to) {
      listAllItemSwitch.clear();
      Call<List<ItemSwitch>> myData = apiService.gatItemSwitchDetail(from, to,CONO);

      myData.enqueue(new Callback<List<ItemSwitch>>() {

         @Override
         public void onResponse(Call<List<ItemSwitch>> call, retrofit2.Response<List<ItemSwitch>> response) {

            if (!response.isSuccessful()) {

               Log.e("fetchItemDetailDataonResponse", "not=" + response.message());



            } else {
               Log.e("fetchItemDetailDataonResponse", "onResponse=" + response.message());


               listAllItemSwitch.addAll(response.body());

               my_dataBase.itemSwitchDao().dELETEAll();
               my_dataBase.itemSwitchDao().insertAll(listAllItemSwitch);
               Log.e("listAllItemSwitch", "SIZE=" + listAllItemSwitch.size());

            }
         }

         @Override
         public void onFailure(Call<List<ItemSwitch>> call, Throwable t) {
            Log.e("fetchItemDetailDataonFailure", "=" + t.getMessage());

         }
      });
   }

   public void fetchmaxNo(String VKIND,Context context) {


      Call<Object> myData = apiService.GetmaxNo(CONO,"1",VKIND);

      myData.enqueue(new Callback<Object>() {

         @Override
         public void onResponse(Call<Object> call, retrofit2.Response<Object> response) {

            if (!response.isSuccessful()) {

               Log.e("fetchmaxNo", "not=" + response.message()+"  "+call.request());



            } else {
               Log.e("fetchmaxNo", "onResponse=" + response.body()+" "+ call.request()+"   ");
               try {
//                        JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));
//                        String msg = jsonObject.getString("MAXVHFNO");





                  JSONArray requestArray = null;


                  requestArray = new JSONArray(response.body().toString());
                  Log.e("requestArray", "" + requestArray.length());


                  for (int i = 0; i < requestArray.length(); i++) {
                     JSONObject infoDetail = requestArray.getJSONObject(i);

                     //   infoDetail.get("MAXVHFNO").toString()
                     Log.e("requestArray", "" +   infoDetail.get("MAXVHFNO").toString());
                     if(VKIND.equals("504"))
                     {
                    if(  my_dataBase.maxVoucherDao().getMaxVocherSerials().size()==0)
                    {
                      MaxVoucher maxVoucher=new MaxVoucher();
                       maxVoucher.setVocher(infoDetail.get("MAXVHFNO").toString());
                       maxVoucher.setOrder_requst("");
                       my_dataBase.maxVoucherDao().insert( maxVoucher);

                    }
                      else  my_dataBase.maxVoucherDao().UpdateVouch( infoDetail.get("MAXVHFNO").toString());

                     }
                     else  if(VKIND.equals("505"))
                     {
                        if(  my_dataBase.maxVoucherDao().getMaxVocherSerials().size()==0)
                        {

                           MaxVoucher maxVoucher=new MaxVoucher();
                           maxVoucher.setVocher("");
                           maxVoucher.setOrder_requst(infoDetail.get("MAXVHFNO").toString());
                           my_dataBase.maxVoucherDao().insert( maxVoucher);
                        }
                      else
                         my_dataBase.maxVoucherDao().UpdateOrder( infoDetail.get("MAXVHFNO").toString());

                     }

                  }



               }catch (Exception e){
                  Log.e("Exception", e.getMessage()+"");
               }






//                    JsonObject post = new JsonObject().get(response.body().toString()).getAsJsonObject();
//                    Log.e("fetchmaxNo", "onResponse=" + post.get("MAXVHFNO"));



            }
         }

         @Override
         public void onFailure(Call<Object> call, Throwable t) {
            Log.e("fetchmaxNo", "=" + t.getMessage()+"  "+call.request());



         }
      });
   }
}
