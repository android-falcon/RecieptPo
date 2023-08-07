package com.hiaryabeer.receiptapp.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hiaryabeer.receiptapp.Acitvits.MainActivity;
import com.hiaryabeer.receiptapp.R;
import com.hiaryabeer.receiptapp.models.AppDatabase;
import com.hiaryabeer.receiptapp.models.GeneralMethod;
import com.hiaryabeer.receiptapp.models.Items;

import java.util.ArrayList;

public class OrderItemAdapter extends RecyclerView.Adapter<OrderItemAdapter.ViewHolder>{
    ArrayList<Items> itemsList=new ArrayList<>();
    Context context;
    AppDatabase appDatabase;
    private int index = 0;
    public static TextView qtyinvalidrespon;

    public OrderItemAdapter(ArrayList<Items> itemsList, Context context) {
        this.itemsList = itemsList;
        this.context = context;
        appDatabase=AppDatabase.getInstanceDatabase(context);
    }

    public ArrayList<Items> getItemsList() {
        return itemsList;
    }

    public void setItemsList(ArrayList<Items> itemsList) {
        this.itemsList = itemsList;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public OrderItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new OrderItemAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.itemrow, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull OrderItemAdapter.ViewHolder holder, int position) {
        holder.name.setText(itemsList.get(position).getNAME());
        holder.itemcode.setText(itemsList.get(position).getITEMNO());
        holder.qty.setText(itemsList.get(position).getQty()+"");

        holder.reomveItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog dialog = new Dialog(context);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.delete_entry);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.e("removeItemposition", holder.getAdapterPosition() + "");
                        itemsList.remove(holder.getAdapterPosition());
                        notifyDataSetChanged();
                        MainActivity.desRespon.setText("aa");
                        dialog.dismiss();

                    }
                });
                dialog.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
                dialog.setCanceledOnTouchOutside(true);
            }
        });
        holder.qty.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if(!editable.toString().equals(""))
                {
                    try {
                        if (Double.parseDouble(holder.qty.getText().toString())>0)
                        {
                            Log.e("itemsList===",itemsList.size()+"");
                            Log.e("case1","case1");

                            if(MainActivity.VochType==504) {
                                Log.e("case1.1","case1.1 ,itembalnce="+itemsList.get(holder.getAdapterPosition()) .getAviQty());

                                if (itemsList.get(holder.getAdapterPosition()) .getBalanceQty()>= Double.parseDouble(holder.qty.getText().toString())) {
                                    MainActivity.vocher_Items.get(holder.getAdapterPosition()).
                                            setQty(Double.parseDouble(holder.qty.getText().toString()));


                                    double TotalDiscVal = MainActivity.vocher_Items.get(holder.getAdapterPosition()).getQty() * MainActivity.vocher_Items.get(holder.getAdapterPosition()).getF_D() * MainActivity.vocher_Items.get(holder.getAdapterPosition()).getDiscount();
                                    holder.total.setText(GeneralMethod.convertToEnglish((String.  format("%.3f", calculatNetTotal(holder.getAdapterPosition())))));
                                    Log.e("case1.1", "total=," + holder.total.getText());
                                    Log.e("case1.1", "qty=," + MainActivity.vocher_Items.get(holder.getAdapterPosition()).getQty());
                                    MainActivity.desRespon.setText("aa");
                                    itemsList.get(holder.getAdapterPosition()) .setAviQty( itemsList.get(holder.getAdapterPosition()) .getBalanceQty()- MainActivity.vocher_Items.get(holder.getAdapterPosition()).getQty());
                                }
                                else {
                                    GeneralMethod.showSweetDialog(context, 0, "",context.getResources().getString(R.string.aviQTY)+" "+itemsList.get(holder.getAdapterPosition()) .getBalanceQty());
                                    MainActivity.vocher_Items.get(holder.getAdapterPosition()).
                                            setQty(itemsList.get(holder.getAdapterPosition()) .getBalanceQty());
                                    holder.qty.setText(itemsList.get(holder.getAdapterPosition()) .getBalanceQty()+"");
                                    double TotalDiscVal = MainActivity.vocher_Items.get(holder.getAdapterPosition()).getQty() * MainActivity.vocher_Items.get(holder.getAdapterPosition()).getF_D() * MainActivity.vocher_Items.get(holder.getAdapterPosition()).getDiscount();
                                    holder.total.setText(GeneralMethod.convertToEnglish((String.  format("%.3f", calculatNetTotal(holder.getAdapterPosition())))));
                                    Log.e("case1.1", "total=," + holder.total.getText());
                                    Log.e("case1.1", "qty=," + MainActivity.vocher_Items.get(holder.getAdapterPosition()).getQty());
                                    MainActivity.desRespon.setText("aa");
                                    itemsList.get(holder.getAdapterPosition()) .setAviQty( itemsList.get(holder.getAdapterPosition()) .getBalanceQty()- MainActivity.vocher_Items.get(holder.getAdapterPosition()).getQty());

                                }
                            }
                            else
                            { Log.e("case1.2","case1.2");
                                MainActivity.vocher_Items.get(holder.getAdapterPosition()).
                                        setQty(Double.parseDouble(holder.qty.getText().toString()));

                                double TotalDiscVal = itemsList.get(holder.getAdapterPosition()).getQty() * itemsList.get(holder.getAdapterPosition()).getF_D() * itemsList.get(holder.getAdapterPosition()).getDiscount();
                                holder.total.setText(GeneralMethod.convertToEnglish((String.  format("%.3f", calculatNetTotal(holder.getAdapterPosition())))));
                                MainActivity.desRespon.setText("aa");

                            }
                        }
                        else if(Double.parseDouble(holder.qty.getText().toString())<0)
                        {
                            holder.qty.setError("not valid");
                            MainActivity.vocher_Items.get(holder.getAdapterPosition()).
                                    setQty(0);

                            double TotalDiscVal = itemsList.get(holder.getAdapterPosition()).getQty() * itemsList.get(holder.getAdapterPosition()).getF_D() * itemsList.get(holder.getAdapterPosition()).getDiscount();
                            holder.total.setText(GeneralMethod.convertToEnglish((String.  format("%.3f", calculatNetTotal(holder.getAdapterPosition())))));
                            MainActivity.desRespon.setText("aa");
                        } else if(Double.parseDouble(holder.qty.getText().toString())==0)
                        {
                            MainActivity.vocher_Items.get(holder.getAdapterPosition()).
                                    setQty(0);

                            double TotalDiscVal = itemsList.get(holder.getAdapterPosition()).getQty() * itemsList.get(holder.getAdapterPosition()).getF_D() * itemsList.get(holder.getAdapterPosition()).getDiscount();
                            holder.total.setText(GeneralMethod.convertToEnglish((String.  format("%.3f", calculatNetTotal(holder.getAdapterPosition())))));
                            MainActivity.desRespon.setText("aa");

                        }
                    }catch (Exception exception){

                    }


                }else
                {
                    Log.e("case1.3","case1.3");
                    MainActivity. vocher_Items.get(holder.getAdapterPosition()).
                            setQty(1);
                    double TotalDiscVal=itemsList.get(holder.getAdapterPosition()).getQty()*itemsList.get(holder.getAdapterPosition()).getF_D()*itemsList.get(holder.getAdapterPosition()).getDiscount();
                    holder.total.setText(GeneralMethod.convertToEnglish((String.  format("%.3f", calculatNetTotal(holder.getAdapterPosition())))));
                    MainActivity.desRespon.setText("aa");


                }
            }
        });
        holder.price.setText(GeneralMethod.convertToEnglish((String.  format("%.3f", itemsList.get(position).getF_D()*Double.parseDouble(itemsList.get(position).getConvRate()))))+"");
        holder.price.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if(!editable.toString().equals(""))
                {     MainActivity. vocher_Items.get(holder.getAdapterPosition()).setF_D( Double.parseDouble(holder.price.getText().toString()));
                    Log.e("f_d===",MainActivity. vocher_Items.get(holder.getAdapterPosition()).getF_D()+"");
                    double TotalDiscVal=itemsList.get(holder.getAdapterPosition()).getQty()*itemsList.get(holder.getAdapterPosition()).getF_D()*itemsList.get(holder.getAdapterPosition()).getDiscount();
                    holder.total.setText(GeneralMethod.convertToEnglish((String.  format("%.3f", calculatNetTotal(holder.getAdapterPosition())))));
                    MainActivity.desRespon.setText("aa");
                }else
                {
                    MainActivity. vocher_Items.get(holder.getAdapterPosition()).
                            setF_D( 1);
                    Log.e("f_d===",MainActivity. vocher_Items.get(holder.getAdapterPosition()).getF_D()+"");

                    double TotalDiscVal=itemsList.get(holder.getAdapterPosition()).getQty()*itemsList.get(holder.getAdapterPosition()).getF_D()*itemsList.get(holder.getAdapterPosition()).getDiscount();
                    holder.total.setText(GeneralMethod.convertToEnglish((String.  format("%.3f", calculatNetTotal(holder.getAdapterPosition())))));
                    MainActivity.desRespon.setText("aa");
                }
            }
        });
        if(!IsExistsInList(itemsList.get(position).getITEMNO())) holder.free.setText("0");
        else holder.free.setText(MainActivity.vocher_Items.get(index).getFree()+"");
        try {
            holder.    free.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if(!editable.toString().equals(""))
                    {


                        MainActivity.vocher_Items.get(holder.getAdapterPosition()).
                                setFree(Double.parseDouble(holder.free.getText().toString().trim()));



                    }else
                    {
                        MainActivity. vocher_Items.get(holder.getAdapterPosition()).
                                setFree( 0);

                    }
                }
            });
        }catch (Exception e){

        }


        if(!IsExistsInList(itemsList.get(position).getITEMNO())) holder.discount.setText("0");

        else holder.discount.setText(MainActivity.vocher_Items.get(index).getDiscount()+"");

        try {
            holder.    discount.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if(!editable.toString().equals(""))
                    {
                        MainActivity. vocher_Items.get(holder.getAdapterPosition()).
                                setDiscount ( Double.parseDouble(holder.discount.getText().toString().trim())/100);
                        double TotalDiscVal=itemsList.get(holder.getAdapterPosition()).getQty()*itemsList.get(holder.getAdapterPosition()).getF_D()*itemsList.get(holder.getAdapterPosition()).getDiscount();
                        holder.total.setText(GeneralMethod.convertToEnglish((String.  format("%.3f", calculatNetTotal(holder.getAdapterPosition())))));

                    }else
                    {
                        MainActivity. vocher_Items.get(holder.getAdapterPosition()).
                                setDiscount( 0);
                        double TotalDiscVal=itemsList.get(holder.getAdapterPosition()).getQty()*itemsList.get(holder.getAdapterPosition()).getF_D()*itemsList.get(holder.getAdapterPosition()).getDiscount();
                        holder.total.setText(GeneralMethod.convertToEnglish((String.  format("%.3f", calculatNetTotal(holder.getAdapterPosition())))));
                    }
                    MainActivity.desRespon.setText("aa");   }
            });
        }catch (Exception e){


        }

        double TotalDiscVal=itemsList.get(position).getQty()*itemsList.get(position).getF_D()*itemsList.get(position).getDiscount();
        holder.total.setText(GeneralMethod.convertToEnglish((String.  format("%.3f", calculatNetTotal(holder.getAdapterPosition())))));
        //netsales.setText(GeneralMethod.convertToEnglish(String.  format("%.3f",9999999999)));

        qtyinvalidrespon.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(!editable.toString().equals(""))
                {
                    holder.qty.setError("");
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView name,itemcode,total,reomveItem;
        EditText free,discount,price,qty;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            qtyinvalidrespon=itemView.findViewById(R.id.qtyinvalidrespon);

            reomveItem=itemView.findViewById(R.id.reomveItem);
            name=itemView.findViewById(R.id.item_Name_);
            itemcode=itemView.findViewById(R.id.item_code_);
            qty=itemView.findViewById(R.id.item_Qty);
            free=itemView.findViewById(R.id.free);
            discount=itemView.findViewById(R.id.discount);
            price=itemView.findViewById(R.id.price);
            total=itemView.findViewById(R.id.total);

        }
    }
    boolean IsExistsInList(String ItemOCode) {
        index = 0;
        boolean exists = false;
        for (int i = 0; i < MainActivity.vocher_Items.size(); i++)
            if (MainActivity.vocher_Items.get(i).getITEMNO().equals(ItemOCode)) {
                index = i;
                exists = true;
                break;

            }

        return exists;
    }
    double calculatNetTotal(int position){


        ///
        double TotalDiscVal = Double.parseDouble(itemsList.get(position).getConvRate()) *itemsList.get(position).getQty() * itemsList.get(position).getF_D() *itemsList.get(position).getDiscount();


        double   Tax =Double.parseDouble(itemsList.get(position).getConvRate())*itemsList.get(position).getF_D() * itemsList.get(position).getTAXPERC();
        double TaxValue= Tax * itemsList.get(position).getQty();

        double    subtotal =Double.parseDouble(itemsList.get(position).getConvRate())* itemsList.get(position).getQty()*itemsList.get(position).getF_D()- TotalDiscVal-TaxValue;
        double nettotal = subtotal + TaxValue;


        return nettotal;
    }

}
