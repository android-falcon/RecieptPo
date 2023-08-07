package com.hiaryabeer.receiptapp.Adapters;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hiaryabeer.receiptapp.Acitvits.MainActivity;
import com.hiaryabeer.receiptapp.R;
import com.hiaryabeer.receiptapp.models.AppDatabase;
import com.hiaryabeer.receiptapp.models.Items;

import java.util.List;

public class Adapter extends BaseAdapter {
    private Context context; //context
    private List<Items> items; //data source of the list adapter
    public static final int REQUEST_Camera_Barcode = 1;

AppDatabase mydatabase;

    //public constructor
    public Adapter(Context context, List<Items> items) {
        this.context = context;
        this.items = items;
        mydatabase=AppDatabase.getInstanceDatabase(context);
//        this.my_database = RoomAllData.getInstanceDataBase(context);
//        this.serialTransfers = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return items.size(); //returns total of items in the list
    }

    @Override
    public Object getItem(int position) {
        return items.get(position); //returns list item at the specified position
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // inflate the layout for each list row
        if (convertView == null) {
            convertView = LayoutInflater.from(context).
                    inflate(R.layout.searchrec, parent, false);
        }

        // get current item to be displayed
        Items currentItem = (Items) getItem(position);


        // get the TextView for item name and item description
        TextView textViewItemName = (TextView)
                convertView.findViewById(R.id.itemname);
        TextView textViewprice = (TextView)
                convertView.findViewById(R.id.price);
        TextView textViewqty = (TextView)
                convertView.findViewById(R.id.qty);



        LinearLayout linearLayout = convertView.findViewById(R.id.linear);
//        linearLayout.setVisibility(View.GONE);
//        TextView icAddSerial = convertView.findViewById(R.id.icAddSerial);
        LinearLayout parentLinear = convertView.findViewById(R.id.parentLinear);
      //  double itembalnce=mydatabase.itemsBalanceDao().getBalance(items.get(position).getITEMNO());

//   if(MainActivity.VochType==504) {
//
//       if (itembalnce > 0) {
//           linearLayout.setVisibility(View.GONE);
//           textViewprice.setVisibility(View.GONE);
//           textViewItemName.setVisibility(View.GONE);
//           Log.e("itembalnce44===", itembalnce + "");
//       } else {
//           linearLayout.setVisibility(View.VISIBLE);
//           textViewprice.setVisibility(View.VISIBLE);
//           textViewItemName.setVisibility(View.VISIBLE);
//           Log.e("itembalnce55===", itembalnce + "");
//       }
//   }
//   else {
//       Log.e("itembalnce55===",   "else");
//       linearLayout.setVisibility(View.VISIBLE);
//       textViewprice.setVisibility(View.VISIBLE);
//       textViewItemName.setVisibility(View.VISIBLE);
//   }

        textViewItemName.setText(items.get(position).getNAME());
        textViewprice.setText(items.get(position).getF_D()+"");
        double itembalnce=mydatabase.itemsBalanceDao().getBalance(items.get(position).getITEMNO());
        textViewqty.setText(itembalnce+"");

            linearLayout.setOnClickListener(view -> {
              MainActivity.search.setEnabled(true);
              MainActivity.itemcode.setText(items.get(position).getITEMNO());
              Log.e("position6===", position + "");
                MainActivity.item = mydatabase.itemsDao().getItembyCode(MainActivity.itemcode.getText().toString().trim());

                if ( MainActivity.item != null) {


                    MainActivity.   itemname.setText( MainActivity.item.getNAME());
                    MainActivity.   itemqty.setText( MainActivity.item.getQty() + "");

                    MainActivity.    itemprice.setText( String.valueOf(String.format("%.3f",MainActivity.item.getF_D())) + "");

                    MainActivity.     free.setText("");

                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            MainActivity.     itemqty.setError(null);
                            MainActivity.    itemcode.setError(null);
                          MainActivity.  itemname.setText( MainActivity.item.getNAME());
                            MainActivity.    itemqty.setText( MainActivity.item.getQty() + "");
                            MainActivity.    itemprice.setText( String.valueOf(String.format("%.3f",MainActivity.item.getF_D())) + "");
                            MainActivity.     free.setText("");

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



        return convertView;
    }

}