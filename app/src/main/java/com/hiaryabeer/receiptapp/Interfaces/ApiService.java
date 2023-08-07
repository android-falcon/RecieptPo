package com.hiaryabeer.receiptapp.Interfaces;

import com.google.gson.JsonObject;
import com.hiaryabeer.receiptapp.models.ItemSwitch;
import com.hiaryabeer.receiptapp.models.Items;
import com.hiaryabeer.receiptapp.models.POitems;
import com.hiaryabeer.receiptapp.models.PoInfo;
import com.hiaryabeer.receiptapp.models.ReceiptDetails;
import com.hiaryabeer.receiptapp.models.ReceiptMaster;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {
 @GET("GetVanAllData")
 Call <Items> gatItemInfoDetail(@Query("STRNO") String STRNO, @Query("CONO") String ComNo);



 @GET("GetJRDItemSwich")
 Call<List<ItemSwitch>> gatItemSwitchDetail(@Query("FROM_DATE") String FROM_DATE, @Query("TO_DATE") String TO_DATE  , @Query("CONO") String ComNo);


 @POST("ExportSALES_VOUCHER_D")
 Call<ResponseBody> saveReceiptDetail(ArrayList<ReceiptDetails> ArrayList);
 @POST("ExportSALES_VOUCHER_M")
 Call<ResponseBody> saveReceiptMaster( ArrayList<ReceiptMaster> ArrayList);

 @GET("GetOfferHeader")
 Call<List<PoInfo>> GetPoInfo(@Query("CONO") String ComNo, @Query("VHFNO") String VHFNO  );

 @GET("GetOfferTransActn")
 Call<List<POitems>> GetPoItems(@Query("CONO") String ComNo, @Query("VHFNO") String VHFNO  );
 @GET("GetmaxNo")
 Call<Object> GetmaxNo(@Query("CONO") String ComNo, @Query("STRNO") String STRNO , @Query("VKIND") String VKIND  );


}
