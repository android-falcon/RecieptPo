package com.hiaryabeer.receiptapp.Acitvits;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hiaryabeer.receiptapp.Adapters.DetailsAdapter;
import com.hiaryabeer.receiptapp.Adapters.MasterAdapter;
import com.hiaryabeer.receiptapp.R;
import com.hiaryabeer.receiptapp.models.AppDatabase;
import com.hiaryabeer.receiptapp.models.GeneralMethod;
import com.hiaryabeer.receiptapp.models.ReceiptDetails;
import com.hiaryabeer.receiptapp.models.ReceiptMaster;

import java.util.ArrayList;
import java.util.List;

public class DetailsVochers_report extends AppCompatActivity {
    RecyclerView.LayoutManager layoutManager;
    RecyclerView ordersDetalisRec;
    List<ReceiptDetails> detailsList = new ArrayList<>();
    AppDatabase mydatabase;
    TextView vochernum, Cus_name, date, total,tax,netsales;
   long VohNu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_vochers_report);
        try {
            init();

//            Bundle bundle = getIntent().getExtras();
//
//          VohNu = bundle.getLong("VOHNO");
//
//            Log.e("VohNu==",VohNu+"  ");
            detailsList=mydatabase.receiptDetails_dao().getOrdersByNumber(MasterAdapter.VOH_NUM,MasterAdapter.VOH_ty);
            Log.e("detailsList==",detailsList.size()+"");
            fillDataText();
            filladapter();
        }
        catch (Exception e){
            Log.e("Exception=",e.getMessage());
        }

    }

    private void filladapter() {
        ordersDetalisRec.setAdapter(new DetailsAdapter(detailsList,DetailsVochers_report.this));
    }

    void init(){
        ordersDetalisRec = findViewById(R.id.ordersDetalisRec);
        total = findViewById(R.id.total);
        vochernum = findViewById(R.id.ORDERNO);
        netsales= findViewById(R.id.netsales);
        Cus_name = findViewById(R.id.Cus_name);
        tax= findViewById(R.id.tax);
        Log.e("VOHNO==", VohNu + "");
        mydatabase = AppDatabase.getInstanceDatabase(DetailsVochers_report.this);
        ordersDetalisRec = findViewById(R.id.ordersDetalisRec);
        ordersDetalisRec.setLayoutManager(new LinearLayoutManager(DetailsVochers_report.this));
        date = findViewById(R.id.date);


    }
  void  fillDataText(){
      total.setText("");
      vochernum.setText(detailsList.get(0).getNewVochNum()+"");
      String customerName =mydatabase.customers_dao().getCustmByNumber(detailsList.get(0).getCustomerId()) ;

      Cus_name.setText(customerName+"");
      date    .setText(detailsList.get(0).getDate()+"");
     ReceiptMaster receiptMaster =mydatabase.receiptMaster_dao().getOrderByVOHNO(detailsList.get(0).getNewVochNum(),detailsList.get(0).getVOUCHERTYPE());
      double nettotal=0,tax1=0,con=1;
     for(int i=0;i<detailsList.size();i++)
     {
         try {
             con=Double.parseDouble(detailsList.get(i).getConvRate());
         }
         catch (Exception e){
             con=1;
         }

          nettotal+=detailsList.get(i).getNetTotal()*con;
         tax1+=detailsList.get(i).getTax();
     }
      netsales  .setText(GeneralMethod.convertToEnglish(String.format("%.3f", receiptMaster.getNetTotal())+""));
     // netsales  .setText(GeneralMethod.convertToEnglish(String.format("%.3f", receiptMaster.getNetTotal()))+"");
      tax .setText(GeneralMethod.convertToEnglish(String.format("%.3f",receiptMaster.getTax())+""));
    }
}