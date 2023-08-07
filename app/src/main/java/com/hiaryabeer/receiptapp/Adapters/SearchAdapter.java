package com.hiaryabeer.receiptapp.Adapters;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hiaryabeer.receiptapp.Acitvits.MainActivity;
import com.hiaryabeer.receiptapp.Acitvits.MainActivity2;
import com.hiaryabeer.receiptapp.R;
import com.hiaryabeer.receiptapp.models.AppDatabase;
import com.hiaryabeer.receiptapp.models.GeneralMethod;
import com.hiaryabeer.receiptapp.models.Items;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder>{
    private Context context; //context
    private List<Items> items; //data source of the list adapter
    public static final int REQUEST_Camera_Barcode = 1;
    double aviQty=0;
    AppDatabase mydatabase;

    //public constructor
    public SearchAdapter(Context context, List<Items> items) {
        this.context = context;
        this.items = items;
        mydatabase=AppDatabase.getInstanceDatabase(context);
//        this.my_database = RoomAllData.getInstanceDataBase(context);
//        this.serialTransfers = new ArrayList<>();
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.searchrec, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.   textViewItemName.setText(items.get(position).getNAME());
        holder.  textViewprice.setText(items.get(position).getF_D()+"");

        holder.   textViewqty.setText(GeneralMethod.convertToEnglish(String.valueOf(String.format("%.3f",items.get(position).getBalanceQty())+"")));
        Log.e("aviinsearch===", items.get(position).getAviQty() + "");
        int avi;
        if (MainActivity2.VochType == 504) {
          avi=CheckIsExistsINLocalList(items.get(position).getITEMNO());
    if(avi>0)
    {
      if(avi==1)  holder.   textViewqty.setText(aviQty+"");

        holder.   linearLayout.setVisibility(View.VISIBLE);
        holder.  View_.setVisibility(View.VISIBLE);
    }
  else   {
      holder.   linearLayout.setVisibility(View.GONE);
        holder.  View_.setVisibility(View.GONE);
  }}
        else {
            holder.   linearLayout.setVisibility(View.VISIBLE);
            holder.  View_.setVisibility(View.VISIBLE);

        }
        holder.   linearLayout.setOnClickListener(view -> {
            MainActivity2.search.setEnabled(true);
            MainActivity2.itemcode.setText(items.get(position).getITEMNO());
            Log.e("position6===", position + "");
            MainActivity2.item = mydatabase.itemsDao().getItembyCode(MainActivity2.itemcode.getText().toString().trim());

            if ( MainActivity2.item != null) {


                MainActivity2.   itemname.setText( MainActivity2.item.getNAME());
                MainActivity2.   itemqty.setText( MainActivity2.item.getQty() + "");
                MainActivity2.    itemprice.setText( MainActivity2.item.getF_D() + "");

                MainActivity2.     free.setText("");

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        MainActivity2.     itemqty.setError(null);
                        MainActivity2.    itemcode.setError(null);
                        MainActivity2.  itemname.setText( MainActivity2.item.getNAME());
                        MainActivity2.    itemqty.setText( MainActivity2.item.getQty() + "");
                        MainActivity2.    itemprice.setText( MainActivity2.item.getF_D() + "");
                        MainActivity2.     free.setText("");
                        MainActivity2.    item.setConvRate("1");
                        String unitid= mydatabase.itemUnitsDao().getUnitidbyitemnumAndBarcode(MainActivity2.   item.getITEMNO(),MainActivity2.itemcode.getText().toString().trim());

                        MainActivity2.      item.  setWhichUNITSTR(unitid);
                        //         addItem(item.getITEMNO());
                        //    itemqty.requestFocus();
                        MainActivity2.      itemqty.setEnabled(true);
                        MainActivity2.      itemqty.requestFocus();
                    }
                }, 100);
            } else {

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("else3==", "else");
                        MainActivity2.itemcode.setError("");
                        MainActivity2.  itemcode.requestFocus();
                        MainActivity2.   itemqty.setEnabled(false);
                    }
                }, 100);
            }

            MainActivity2. dialog1.dismiss();

        });
    }

    @Override
    public int getItemCount() {
        return items.size(); //returns total of items in the list
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView textViewItemName,textViewprice,textViewqty;
        LinearLayout linearLayout ,parentLinear;
View View_;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            View_=itemView.findViewById(R.id.View_);
          textViewItemName = (TextView)
                  itemView.findViewById(R.id.itemname);
         textViewprice = (TextView)
                 itemView.findViewById(R.id.price);
        textViewqty = (TextView)
                itemView.findViewById(R.id.qty);
            parentLinear = itemView.findViewById(R.id.parentLinear);
            linearLayout= itemView.findViewById(R.id.linear);
        }
    }
//    private boolean CheckIsExistsINLocalList(String barcode) {
//
//
//        boolean flag = false;
//        if (MainActivity.vocher_Items.size() != 0) {
//            for (int i = 0; i < MainActivity.vocher_Items.size(); i++) {
//
//                if (
//                        GeneralMethod.convertToEnglish(MainActivity.vocher_Items.get(i).getITEMNO()).equals(GeneralMethod.convertToEnglish(barcode))
//
//                ) {
//                    if (MainActivity.vocher_Items.get(i).getAviQty() > 0) {
//                        flag = true;
//                        aviQty=MainActivity.vocher_Items.get(i).getAviQty();
//                        break;
//                    } else {
//                        flag = false;
//                        break;
//                    }
//
//                } else {
//                    flag = true;
//                    aviQty=MainActivity.vocher_Items.get(i).getBalanceQty();
//                    continue;
//                }
//            }
//        }else
//        {
//            flag = true;
//        }
//        return flag;
//
//
//    }
    private int CheckIsExistsINLocalList(String barcode) {


        int flag = 0;
        if (MainActivity2.vocher_Items.size() != 0) {
            for (int i = 0; i < MainActivity2.vocher_Items.size(); i++) {

                if (
                        GeneralMethod.convertToEnglish(MainActivity2.vocher_Items.get(i).getITEMNO()).equals(GeneralMethod.convertToEnglish(barcode))

                ) {
                    if (MainActivity2.vocher_Items.get(i).getAviQty() > 0) {
                        flag = 1;
                        aviQty=MainActivity2.vocher_Items.get(i).getAviQty();
                        break;
                    } else {
                        flag = 0;
                        break;
                    }

                } else {
                    flag = 3;

                    continue;
                }
            }
        }else
        {
            flag = 2;
        }
        return flag;


    }
}
