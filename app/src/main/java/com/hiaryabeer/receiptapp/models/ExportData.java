package com.hiaryabeer.receiptapp.models;

import static android.content.Context.MODE_PRIVATE;
import static com.hiaryabeer.receiptapp.Acitvits.Login.SETTINGS_PREFERENCES;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.HttpResponse;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.NameValuePair;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.client.HttpClient;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.client.entity.UrlEncodedFormEntity;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.client.methods.HttpPost;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.impl.client.DefaultHttpClient;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.message.BasicNameValuePair;
import com.hiaryabeer.receiptapp.Acitvits.Login;
import com.hiaryabeer.receiptapp.Interfaces.ApiService;
import com.hiaryabeer.receiptapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ExportData {
    List<ReceiptDetails> items;
    List<ReceiptMaster> vouchers;
    JSONObject vouchersObject;
    ApiService apiService;
    private  Context context;
    public static String ipAddress = "", CONO = "",ipPort="", headerDll = "", link = "";
    public static SweetAlertDialog pdSaveD,pdSaveM,pdStosk;
    AppDatabase my_dataBase;
    SharedPreferences sharedPref;
    private JSONArray jsonArrayVouchers, jsonArrayItems;
    ImportData importData;
    public List<Items> allItemsList;
    public List<Item_Unit_Details> allUnitDetails;
    SweetAlertDialog progressSave, pdVoucher;

    public ExportData(Context context) {
        this.context = context;
        my_dataBase = AppDatabase.getInstanceDatabase(context);
        try {
            getIpAddress();
            link = "http://" + ipAddress.trim() +":"+ ipPort.trim() + headerDll.trim();

            Retrofit retrofit = RetrofitInstance.getInstance(link);
            Log.e("link",link+"");
            apiService = retrofit.create(ApiService.class);
            sharedPref = context.getSharedPreferences(SETTINGS_PREFERENCES, MODE_PRIVATE);

        } catch (Exception e) {
            Log.e("Exception==",e.getMessage());
               }

    }


    private void getIpAddress() {
   headerDll="";
        //headerDll= "/Falcons/VAN.Dll/";
        importData=new ImportData(context);
        ipAddress = Login.ipAddress;
        ipPort = Login.ipPort;
        CONO =Login.coNo;

    }
    private void getVouchersDetail(String VHFTYP) {
        items = my_dataBase.receiptDetails_dao().getAllOrdersConfirmBytype(VHFTYP);
        jsonArrayItems = new JSONArray();
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getIsPosted() == 0) {

                jsonArrayItems.put(items.get(i).getJSONObjectDelphi());

            }

        }
        try {
            vouchersObject = new JSONObject();
            vouchersObject.put("JSN", jsonArrayItems);
            Log.e("getVouchersDetail", "" + vouchersObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void exportVoucherDetail(String VHTYPE) {//2
        getVouchersDetail(VHTYPE);
        new JSONTaskDelphiDetail(VHTYPE).execute();
    }
    public void exportSalesVoucherM(String VHTYPE) {
        getVouchers(VHTYPE);

        pdVoucher = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        pdVoucher.getProgressHelper().setBarColor(Color.parseColor("#FDD835"));
        pdVoucher.setTitleText(context.getResources().getString(R.string.savedata2));
        pdVoucher.setCancelable(false);
        pdVoucher.show();
        new JSONTaskDelphi( VHTYPE).execute();

    }
    private void getVouchers(String VHTYPE ) {

        //vouchers = my_dataBase.receiptMaster_dao().getAllOrdersConfirm();// from voucher master

        vouchers = my_dataBase.receiptMaster_dao().getAllOrdersConfirmByType(VHTYPE);// from voucher master
        jsonArrayVouchers = new JSONArray();
        for (int i = 0; i < vouchers.size(); i++) {
            if (vouchers.get(i).getIsPosted() == 0) {
                jsonArrayVouchers.put(vouchers.get(i).getJSONObjectDelphi());
            }
        }
        try {
            vouchersObject = new JSONObject();
            vouchersObject.put("JSN", jsonArrayVouchers);
            Log.e("vouchersObject", "" + vouchersObject.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void SaveResciptMaster_CallData(ArrayList<ReceiptMaster>ArrayList) {

        Call<ResponseBody> myData = apiService.saveReceiptMaster(ArrayList);
        Log.e("AllItems_fetchCallData", "AllItems_fetchCallData");
        myData.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String s=response.body().string();
                    pdSaveM.dismiss();
                }catch (Exception exception){
                    Log.e("exception", "=" + exception.getMessage());

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                Log.e("onFailure333", "=" + throwable.getMessage());
                //  Toast.makeText(context, "throwable333"+throwable.getMessage(), Toast.LENGTH_SHORT).show();

                pdSaveM.dismiss();

            }
        });
    }

    public void SaveResciptDetalis(ArrayList<ReceiptDetails>ArrayList){
        try {


            pdSaveD = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
            pdSaveD.getProgressHelper().setBarColor(Color.parseColor("#FDD835"));
            pdSaveD.setTitleText(context.getResources().getString(R.string.savedata));
            pdSaveD.setCancelable(false);
            pdSaveD.show();
            Log.e("context", context.getClass().getName().toString());


            if (!ipAddress.equals(""))
            {  Log.e("ipAddress", ipAddress);
                SaveResciptDetalis_CallData(ArrayList);

            }
            else
                Toast.makeText(context, "Fill Ip", Toast.LENGTH_SHORT).show();

        }catch (Exception e){
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();

        }

    }
    public void SaveResciptMaster(ArrayList<ReceiptMaster>ArrayList){
        try {


            pdSaveM = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
            pdSaveM.getProgressHelper().setBarColor(Color.parseColor("#FDD835"));
            pdSaveM.setTitleText(context.getResources().getString(R.string.savedata));
            pdSaveM.setCancelable(false);
            pdSaveM.show();
            Log.e("context", context.getClass().getName().toString());


            if (!ipAddress.equals(""))
            {  Log.e("ipAddress", ipAddress);
                SaveResciptMaster_CallData(ArrayList);

            }
            else
                Toast.makeText(context, "Fill Ip", Toast.LENGTH_SHORT).show();

        }catch (Exception e){
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();

        }

    }
    public void SaveResciptDetalis_CallData(ArrayList<ReceiptDetails>ArrayList) {

        Call<ResponseBody> myData = apiService.saveReceiptDetail(ArrayList);
        Log.e("AllItems_fetchCallData", "AllItems_fetchCallData");
        myData.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String s=response.body().string();
                    pdSaveD.dismiss();
                }catch (Exception exception){
                    Log.e("exception", "=" + exception.getMessage());

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                Log.e("onFailure333", "=" + throwable.getMessage());
                //  Toast.makeText(context, "throwable333"+throwable.getMessage(), Toast.LENGTH_SHORT).show();
                pdSaveD.dismiss();


            }
        });
    }

    private class JSONTaskDelphi extends AsyncTask<String, String, String> {
        private String JsonResponse = null;
        private HttpURLConnection urlConnection = null;
        private BufferedReader reader = null;

        public String salesNo = "", finalJson;
        String VHTYPE;

        public JSONTaskDelphi(String VHTYPE) {
            this.VHTYPE = VHTYPE;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected String doInBackground(String... params) {
            URLConnection connection = null;
            BufferedReader reader = null;

            try {
                if (!ipAddress.equals("")) {


                    //  String data= "{\"JSN\":[{\"COMAPNYNO\":290,\"VOUCHERYEAR\":\"2021\",\"VOUCHERNO\":\"1212\",\"VOUCHERTYPE\":\"3\",\"VOUCHERDATE\":\"24/03/2020\",\"SALESMANNO\":\"5\",\"CUSTOMERNO\":\"123456\",\"VOUCHERDISCOUNT\":\"50\",\"VOUCHERDISCOUNTPERCENT\":\"10\",\"NOTES\":\"AAAAAA\",\"CACR\":\"1\",\"ISPOSTED\":\"0\",\"PAYMETHOD\":\"1\",\"NETSALES\":\"150.720\"}]}";

                    link = "http://" + ipAddress.trim()  + headerDll.trim() + "/ExportSALES_VOUCHER_M";


                    Log.e("link", "" + link);
                }
            } catch (Exception e) {
                //progressDialog.dismiss();

            }

//********************************************************
            String ipAddress = "";
            Log.e("tagexPORT", "JsonResponse");


            try {
                HttpClient client = new DefaultHttpClient();
                HttpPost request = new HttpPost();
                try {
                    request.setURI(new URI(link));
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                    Handler h = new Handler(Looper.getMainLooper());
                    h.post(new Runnable() {
                        public void run() {
                            new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("check Connection")
                                    .show();


//                        Toast.makeText(context, "check Connection", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("CONO", CONO.trim()));
                nameValuePairs.add(new BasicNameValuePair("JSONSTR", vouchersObject.toString().trim()));
                Log.e("nameValuePairs", "JSONSTR" + vouchersObject.toString().trim());


                request.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));


                HttpResponse response = client.execute(request);


                BufferedReader in = new BufferedReader(new
                        InputStreamReader(response.getEntity().getContent()));

                StringBuffer sb = new StringBuffer("");
                String line = "";

                while ((line = in.readLine()) != null) {
                    sb.append(line);
                }

                in.close();


                JsonResponse = sb.toString();
                Log.e("JsonResponse", "ExporVoucher" + JsonResponse);


                //*******************************************


            } catch (Exception e) {
            }
            return JsonResponse;

        }


        @Override
        protected void onPostExecute(final String result) {
            super.onPostExecute(result);
//            progressDialog.dismiss();
          // comm
            Log.e("onPostExecute", "---1---" + result);

            if (result != null && !result.equals("")) {
                if (result.contains("Saved Successfully")) {

                    exportVoucherDetail(VHTYPE);// 2
                } else {
                    pdVoucher.dismissWithAnimation();

                }
//                Toast.makeText(context, "onPostExecute"+result, Toast.LENGTH_SHORT).show();


            } else {
                pdVoucher.dismissWithAnimation();
                Handler h = new Handler(Looper.getMainLooper());
                h.post(new Runnable() {
                    public void run() {
                        new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("check Connection")
                                .show();


                    }
                });
            }
        }
    }
    private class JSONTaskDelphiDetail extends AsyncTask<String, String, String> {
        private String JsonResponse = null;
        private HttpURLConnection urlConnection = null;
        private BufferedReader reader = null;
//        SweetAlertDialog pdItem=null;
       String VHTYPE;

        public JSONTaskDelphiDetail(String VHTYPE) {
            this.VHTYPE = VHTYPE;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            pdItem = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
//            pdItem.getProgressHelper().setBarColor(Color.parseColor("#FDD835"));
//            pdItem.setTitleText("export");
//            pdItem.setCancelable(false);
//            pdItem.show();
        }

        @Override
        protected String doInBackground(String... params) {

//                try {
            //  URL_TO_HIT = "http://"+ipAddress.trim()+":" + ipWithPort.trim() +"/ExportSALES_VOUCHER_D?CONO="+CONO.trim()+"&JSONSTR="+vouchersObject.toString().trim();

//
            String link = "http://" + ipAddress.trim()  + headerDll.trim() + "/ExportSALES_VOUCHER_D";
            Log.e("link==", link + "");


            String ipAddress = "";
            Log.e("tagexPORT", "JsonResponse");


            try {
                HttpClient client = new DefaultHttpClient();
                HttpPost request = new HttpPost();
                try {
                    request.setURI(new URI(link));
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("CONO", CONO.trim()));
                nameValuePairs.add(new BasicNameValuePair("JSONSTR", vouchersObject.toString().trim()));
                Log.e("nameValuePairs", "Details=JSONSTR" + vouchersObject.toString().trim());


                request.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));


                HttpResponse response = client.execute(request);


                BufferedReader in = new BufferedReader(new
                        InputStreamReader(response.getEntity().getContent()));

                StringBuffer sb = new StringBuffer("");
                String line = "";

                while ((line = in.readLine()) != null) {
                    sb.append(line);
                }

                in.close();


                JsonResponse = sb.toString();
                Log.e("JsonResponse", "ExportSerial" + JsonResponse);


                //*******************************************


            } catch (Exception e) {
            }
            return JsonResponse;
        }


        @Override
        protected void onPostExecute(final String result) {
            super.onPostExecute(result);
            // Log.e("onPostExecute","---2---"+result);
//            pdItem.dismiss();
            // Log.e("onPostExecute","ExportSALES_VOUCHER_D"+result);
            pdVoucher.setTitle("Export SALES_VOUCHER_Detail");
            if (result != null && !result.equals("")) {
                if (result.contains("Saved Successfully")) {

                    updateVoucherExported(VHTYPE);// 3


                    pdVoucher.dismissWithAnimation();
                    //8888

                }


            } else {
                pdVoucher.dismissWithAnimation();
//                Toast.makeText(context, "onPostExecute", Toast.LENGTH_SHORT).show();
            }

        }
    }



      private void updateVoucherExported(String VHTYPE) {// 3
        Log.e("updateVoucherExported", "trueee");
       // my_dataBase.receiptMaster_dao().updateVoucher();
     //   my_dataBase.receiptDetails_dao().updateVoucherDetails();

          my_dataBase.receiptMaster_dao().updateVoucherByType(VHTYPE);
           my_dataBase.receiptDetails_dao().updateVoucherDetailsByType(VHTYPE);

        Log.e("onPostExecute", "updateVoucherExported---3---");

        new JSONTaskSaveVouchers(VHTYPE).execute();

    }
    private class JSONTaskSaveVouchers extends AsyncTask<String, String, String> {
        private String JsonResponse = null;
        private HttpURLConnection urlConnection = null;
        private BufferedReader reader = null;
        //        SweetAlertDialog pdItem=null;
        public  String salesNo="",finalJson;

        String VHFTYP;
        String _VHFTYPE;
        public JSONTaskSaveVouchers(String VHFTYP) {
            this.VHFTYP = VHFTYP;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressSave = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
            progressSave.getProgressHelper().setBarColor(Color.parseColor("#FDD835"));
            progressSave.setTitleText(context.getResources().getString(R.string.savedata));
            progressSave.setCancelable(false);
            progressSave.show();

        }

        @Override
        protected String doInBackground(String... params) {


            try {
                String link = "http://"+ipAddress.trim() + headerDll.trim()+"/SaveVouchers";
                // Log.e("ipAdress", "ip -->" + ip);
                if(VHFTYP.equals("504")) _VHFTYPE="1";
                else
                    _VHFTYPE="0";

                String data = "CONO="+CONO.trim()+"&STRNO=" +"1"+"&VHFTYPE="+_VHFTYPE;
                Log.e("tag_link", "ExportData -->" + link);
                Log.e("tag_data", "ExportData -->" + data);

////
                URL url = new URL(link);

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                httpURLConnection.setRequestMethod("POST");



                DataOutputStream wr = new DataOutputStream(httpURLConnection.getOutputStream());
                wr.writeBytes(data);
                wr.flush();
                wr.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                StringBuffer stringBuffer = new StringBuffer();

                while ((JsonResponse = bufferedReader.readLine()) != null) {
                    stringBuffer.append(JsonResponse + "\n");
                }

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

                Log.e("tag", "ExportData -->" + stringBuffer.toString());

                return stringBuffer.toString();

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("tag", "Error closing stream", e);
                    }
                }
            }
            return null;

        }


        @Override
        protected void onPostExecute(final String result) {
            super.onPostExecute(result);
            progressSave.setTitle("Saved Vouchers");
            Log.e("onPostExecute","---15----"+result);

            if (result != null && !result.equals("")) {


            } else {
                progressSave.dismissWithAnimation();

            }
            progressSave.dismissWithAnimation();

            new JSONTaskEXPORT_STOCK(VHFTYP).execute();
        }
    }
    private class JSONTaskEXPORT_STOCK extends AsyncTask<String, String, String> {
        private String JsonResponse = null;
        private HttpURLConnection urlConnection = null;
        private BufferedReader reader = null;

        String VHFTYP;
        String _VHFTYPE;
        public JSONTaskEXPORT_STOCK(String VHFTYP) {
            this.VHFTYP = VHFTYP;
        }

        @Override
        protected void onPreExecute() {

            pdStosk = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
            pdStosk.getProgressHelper().setBarColor(Color.parseColor("#FDD835"));
            pdStosk.setTitleText(context.getResources().getString(R.string.exportstock));
            pdStosk.setCancelable(false);
            pdStosk.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                //http://10.0.0.22:8085/EXPORTTOSTOCK?CONO=295&STRNO=4
                String link = "http://"+ipAddress.trim() + headerDll.trim()+"/NewEXPORTTOSTOCK";
                if(VHFTYP.equals("504")) _VHFTYPE="1";
                else
                    _VHFTYPE="0";

                String data = "CONO="+CONO.trim()+"&STRNO=" +"1"+"&VHFTYPE="+_VHFTYPE;
                Log.e("tag_link", "ExportData -->" + link);
                Log.e("tag_data", "ExportData -->" + data);


////
                URL url = new URL(link);

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                httpURLConnection.setRequestMethod("POST");



                DataOutputStream wr = new DataOutputStream(httpURLConnection.getOutputStream());
                wr.writeBytes(data);
                wr.flush();
                wr.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                StringBuffer stringBuffer = new StringBuffer();

                while ((JsonResponse = bufferedReader.readLine()) != null) {
                    stringBuffer.append(JsonResponse + "\n");
                }

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

                Log.e("tag", "ExportData -->" + stringBuffer.toString());

                return stringBuffer.toString();

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("tag", "Error closing stream", e);
                    }
                }
            }
            return null;

        }


        @Override
        protected void onPostExecute(final String result) {
            super.onPostExecute(result);

            Log.e("onPostExecute","EXPORT_STOCK---18----"+result);

            if (result != null && !result.equals("")) {
                if(result.contains("Saved Successfully")) {
                    Log.e("EXPORT_STOCK","result_start");
                    pdStosk.dismissWithAnimation();
               if(!VHFTYP.equals("504"))
                   GeneralMethod.showSweetDialog(context, 1, "Export Successfully", null);


                }else {
                    pdStosk.dismissWithAnimation();
                    GeneralMethod.showSweetDialog(context, 0, "Exported Failed", null);
                }


            } else {
                pdStosk.dismissWithAnimation();
            }

   if(VHFTYP.equals("504"))
    exportSalesVoucherM("505");
        }
    }
}
