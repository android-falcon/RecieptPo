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
import com.hiaryabeer.receiptapp.R;
import com.hiaryabeer.receiptapp.models.AppDatabase;
import com.hiaryabeer.receiptapp.models.GeneralMethod;
import com.hiaryabeer.receiptapp.models.ItemSwitch;
import com.hiaryabeer.receiptapp.models.Item_Unit_Details;
import com.hiaryabeer.receiptapp.models.Items;

import java.util.List;

public class OrderSearchAdapter extends RecyclerView.Adapter<OrderSearchAdapter.ViewHolder>{
    private Context context; //context
    private List<Items> items; //data source of the list adapter
    public static final int REQUEST_Camera_Barcode = 1;
    double aviQty=0;
    AppDatabase mydatabase;

    //public constructor
    public OrderSearchAdapter(Context context, List<Items> items) {
        this.context = context;
        this.items = items;
        mydatabase=AppDatabase.getInstanceDatabase(context);
//        this.my_database = RoomAllData.getInstanceDataBase(context);
//        this.serialTransfers = new ArrayList<>();
    }
    @NonNull
    @Override
    public OrderSearchAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OrderSearchAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.searchrec, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull OrderSearchAdapter.ViewHolder holder, int position) {

        holder.   textViewItemName.setText(items.get(position).getNAME());
        holder.  textViewprice.setText(items.get(position).getF_D()+"");

        holder.   textViewqty.setText(GeneralMethod.convertToEnglish(String.valueOf(String.format("%.3f",items.get(position).getBalanceQty())+"")));
        Log.e("aviinsearch===", items.get(position).getAviQty() + "");
        int avi;
        if (MainActivity.VochType == 504) {
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
            MainActivity.search.setEnabled(true);
            MainActivity.itemcode.setText(items.get(position).getITEMNO());
            Log.e("position6===", position + "");
            MainActivity.item = mydatabase.itemsDao().getItembyCode(MainActivity.itemcode.getText().toString().trim());

            if ( MainActivity.item != null) {

                if(CheckIsExistsINLocalList(MainActivity.item.getITEMNO())==1||CheckIsExistsINLocalList(MainActivity.item.getITEMNO())==0) GeneralMethod.showSweetDialog(context,3, "",context.getResources().getString(R.string.itemexists6));

                MainActivity.   itemname.setText( MainActivity.item.getNAME());
                MainActivity.   itemqty.setText( MainActivity.item.getQty() + "");
                MainActivity.    itemprice.setText(convertToEnglish( String.valueOf(String.format("%.3f",MainActivity.item.getF_D()))) + "");

                MainActivity.     free.setText("");

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        MainActivity.     itemqty.setError(null);
                        MainActivity.    itemcode.setError(null);
                        MainActivity.  itemname.setText( MainActivity.item.getNAME());
                        MainActivity.    itemqty.setText( MainActivity.item.getQty() + "");
                        MainActivity.    itemprice.setText( String.valueOf(convertToEnglish( String.valueOf(String.format("%.3f",MainActivity.item.getF_D()))) + ""));
                        MainActivity.     free.setText("");
                        MainActivity.     covrate_show.setText(GetUnitName(   MainActivity.item.getITEMNO()));
                        MainActivity.    item.setConvRate("1");
                        String unitid= mydatabase.itemUnitsDao().getUnitidbyitemnumAndBarcode(MainActivity.   item.getITEMNO(),MainActivity.itemcode.getText().toString().trim());

                        MainActivity.      item.  setWhichUNITSTR(unitid);
                        //         addItem(item.getITEMNO());
                        //    itemqty.requestFocus();
                        MainActivity.      itemqty.setEnabled(true);
                        MainActivity.      itemqty.requestFocus();
                    }
                }, 100);
            } else {

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("else3==", "else");
                        MainActivity.itemcode.setError("");
                        MainActivity.  itemcode.requestFocus();
                        MainActivity.   itemqty.setEnabled(false);
                    }
                }, 100);
            }

            MainActivity. dialog1.dismiss();

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
        if (MainActivity.vocher_Items.size() != 0) {
            for (int i = 0; i < MainActivity.vocher_Items.size(); i++) {

                if (
                        GeneralMethod.convertToEnglish(MainActivity.vocher_Items.get(i).getITEMNO()).equals(GeneralMethod.convertToEnglish(barcode))

                ) {
                    if (MainActivity.vocher_Items.get(i).getAviQty() > 0) {
                        flag = 1;
                        aviQty=MainActivity.vocher_Items.get(i).getAviQty();
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
    public  String convertToEnglish(String value) {
        String newValue = (((((((((((value + "").replaceAll("١", "1")).replaceAll("٢", "2")).replaceAll("٣", "3")).replaceAll("٤", "4")).replaceAll("٥", "5")).replaceAll("٦", "6")).replaceAll("٧", "7")).replaceAll("٨", "8")).replaceAll("٩", "9")).replaceAll("٠", "0").replaceAll("٫", "."));
        return newValue;
    }
    String    GetUnitName(String itemcode){
       String name="";
       try {



        Item_Unit_Details unitDetails=mydatabase.itemUnitsDao().getunitID1( itemcode);
        if(unitDetails!=null)
            name=unitDetails.getUnitId();
        Log.e("name==",name+"");}

      catch (Exception e){
          name="";
      }
        return name;

    }
}
