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

import com.hiaryabeer.receiptapp.Acitvits.ReceivePO;
import com.hiaryabeer.receiptapp.R;
import com.hiaryabeer.receiptapp.models.AppDatabase;
import com.hiaryabeer.receiptapp.models.GeneralMethod;
import com.hiaryabeer.receiptapp.models.Item_Unit_Details;
import com.hiaryabeer.receiptapp.models.Items;
import com.hiaryabeer.receiptapp.models.POitems;

import java.util.List;

public class SearchAdapterNew extends RecyclerView.Adapter<SearchAdapterNew.ViewHolder>{
    private Context context; //context
    private List<POitems> items; //data source of the list adapter
    public static final int REQUEST_Camera_Barcode = 1;
    double aviQty=0;
    AppDatabase mydatabase;

    //public constructor
    public SearchAdapterNew(Context context, List<POitems> items) {
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

        holder.   textViewItemName.setText(items.get(position).getITEMNAME());
        holder.  textViewprice.setText(items.get(position).getPriceL()+"");

        holder.   textViewqty.setText(items.get(position).getQty()+"");
        Log.e("aviinsearch===", items.get(position).getQty() + "");
        int avi;

        holder.   linearLayout.setOnClickListener(view -> {
            ReceivePO.binding.search.setEnabled(true);

            ReceivePO.item = new Items();

            ReceivePO.  item.setITEMNO(items.get(holder.getAdapterPosition()).getItemOCode());
            Log.e("search,getCALCQTY==",items.get(holder.getAdapterPosition()).getCALCQTY()+"");
            if(!items.get(holder.getAdapterPosition()).getCALCQTY().equals(""))
            if(Double.parseDouble(items.get(holder.getAdapterPosition()).getCALCQTY())>1)
            ReceivePO. item.setF_D(Double.parseDouble(items.get(holder.getAdapterPosition()).getPriceL()));
            else
            ReceivePO. item.setF_D(Double.parseDouble(items.get(holder.getAdapterPosition()).getPriceL()));
            else
            ReceivePO. item.setF_D(Double.parseDouble(items.get(holder.getAdapterPosition()).getPriceL()));


            ReceivePO.  item.setNAME(items.get(holder.getAdapterPosition()).getITEMNAME());
            ReceivePO.    item.setTAXPERC(Double.parseDouble(items.get(holder.getAdapterPosition()).getTTAXPERC() )/ 100);
            ReceivePO.    item.setItemNCode(items.get(holder.getAdapterPosition()).getItemNCode());
            ReceivePO.     item.setWhichUNITSTR(items.get(holder.getAdapterPosition()).getWHICHUNITSTR());
           ReceivePO.   item.setUNITBARCODE(items.get(holder.getAdapterPosition()).getUNITBARCODE());
            ReceivePO.     item.setCALCQTY(items.get(holder.getAdapterPosition()).getCALCQTY());
            ReceivePO.       item.setConvRate(items.get(holder.getAdapterPosition()).getWHICHUQTY());
            if(CheckIsExistsINLocalList(items.get(holder.getAdapterPosition()).getItemOCode())) GeneralMethod.showSweetDialog(context,3, "",context.getResources().getString(R.string.itemexists6));

            ReceivePO.  binding.qty.setEnabled(true);
            ReceivePO. binding.qty.requestFocus();
            ReceivePO.binding.itemCode.setText(items.get(position).getItemOCode());
            Log.e("position6===", position + "");



                ReceivePO. binding.  itemName.setText(items.get(holder.getAdapterPosition()).getITEMNAME());
                ReceivePO. binding.   Poqty.setText( items.get(holder.getAdapterPosition()).getQty() + "");
            ReceivePO.  binding.  covrateShow.setText(GetUnitName(items.get(holder.getAdapterPosition()).getItemOCode(),items.get(holder.getAdapterPosition()).getSHOW_WHICHQTY()));
            ReceivePO. binding.   price.setText(items.get(holder.getAdapterPosition()).getPriceL() + "");
            Log.e("search2,getCALCQTY==",items.get(holder.getAdapterPosition()).getCALCQTY()+"");
            if(!items.get(holder.getAdapterPosition()).getCALCQTY().equals(""))

            {
                Log.e("search2,getCALCQTY==",items.get(holder.getAdapterPosition()).getCALCQTY()+"");
                if(Double.parseDouble(items.get(holder.getAdapterPosition()).getCALCQTY())>1) {
                    Log.e("case1,getCALCQTY==",items.get(holder.getAdapterPosition()).getCALCQTY()+",,"+items.get(holder.getAdapterPosition()).getPriceL());
                    ReceivePO.binding.price.setText(items.get(holder.getAdapterPosition()).getPriceL());

                }
            else
                {
                    Log.e("case2,getCALCQTY==",items.get(holder.getAdapterPosition()).getCALCQTY()+"");
                    ReceivePO.    binding.price.setText(items.get(holder.getAdapterPosition()).getPriceL());
                }

            }
                                           else
                ReceivePO.     binding.price.setText(items.get(holder.getAdapterPosition()).getPriceL());



                ReceivePO. binding.    free.setText("");

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ReceivePO. binding.    qty.setError(null);
                        ReceivePO. binding.   itemCode.setError(null);
                        ReceivePO. binding.  itemName.setText( items.get(holder.getAdapterPosition()).getITEMNAME());
                        ReceivePO. binding.   Poqty.setText( items.get(holder.getAdapterPosition()).getQty()+ "");
                        ReceivePO.  binding.  covrateShow.setText(GetUnitName(items.get(holder.getAdapterPosition()).getItemOCode(),items.get(holder.getAdapterPosition()).getSHOW_WHICHQTY()));
                        //" الوحدة: "+items.get(holder.getAdapterPosition()).getWHICHUNITSTR()+" الحبات:  "+items.get(holder.getAdapterPosition()).getWHICHUQTY()+""

                        if(!items.get(holder.getAdapterPosition()).getCALCQTY().equals(""))

                        {
                            Log.e("search2,getCALCQTY==",items.get(holder.getAdapterPosition()).getCALCQTY()+"");
                            if(Double.parseDouble(items.get(holder.getAdapterPosition()).getCALCQTY())>1) {
                                Log.e("case1,getCALCQTY==",items.get(holder.getAdapterPosition()).getCALCQTY()+",,"+items.get(holder.getAdapterPosition()).getPriceL());
                                ReceivePO.binding.price.setText(items.get(holder.getAdapterPosition()).getPriceL());

                            }
                            else
                            {
                                Log.e("case2,getCALCQTY==",items.get(holder.getAdapterPosition()).getCALCQTY()+"");
                                ReceivePO.    binding.price.setText(items.get(holder.getAdapterPosition()).getPriceL());
                            }

                        }
                        else
                            ReceivePO.     binding.price.setText(items.get(holder.getAdapterPosition()).getPriceL());

                        ReceivePO. binding.    free.setText("");


                        ReceivePO.    binding.    qty.setEnabled(true);
                        ReceivePO.  binding.      qty.requestFocus();
                    }
                }, 100);



            ReceivePO. dialog1.dismiss();

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
    private boolean CheckIsExistsINLocalList(String barcode) {

        boolean flag = false;
        if (ReceivePO.vocher_Items.size() != 0)
            for (int i = 0; i < ReceivePO.vocher_Items.size(); i++) {

                if (
                        (  GeneralMethod.convertToEnglish(ReceivePO.vocher_Items.get(i).getITEMNO()).equals(GeneralMethod.convertToEnglish(barcode))
                                ||
                                GeneralMethod. convertToEnglish(ReceivePO.vocher_Items.get(i).getITEMNO()).equals(GeneralMethod.convertToEnglish(mydatabase.itemSwitchDao().getitemocode(barcode)))

                        ) &&  GeneralMethod. convertToEnglish(ReceivePO.vocher_Items.get(i).getConvRate()).equals(GeneralMethod.convertToEnglish(ReceivePO.item.getConvRate()))
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
    String    GetUnitName(String itemcode,String covrate){
        Log.e("GetUnitName==",itemcode+" "+covrate);
        String name="";
       try {


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
