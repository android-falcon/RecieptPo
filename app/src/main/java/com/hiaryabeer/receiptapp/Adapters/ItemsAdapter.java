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

import com.hiaryabeer.receiptapp.Acitvits.MainActivity2;
import com.hiaryabeer.receiptapp.R;
import com.hiaryabeer.receiptapp.models.AppDatabase;
import com.hiaryabeer.receiptapp.models.GeneralMethod;
import com.hiaryabeer.receiptapp.models.Items;

import java.util.ArrayList;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ViewHolder>{
ArrayList<Items>itemsList=new ArrayList<>();
Context context;
AppDatabase appDatabase;
   private int index = 0;
   public static  TextView qtyinvalidrespon;
   public ItemsAdapter(ArrayList<Items> itemsList, Context context) {
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
   public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

      return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.itemrow, parent, false));

   }

   @Override
   public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
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
                  MainActivity2.desRespon.setText("aa");
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

                     if(MainActivity2.VochType==504) {
                        Log.e("case1.1","case1.1 ,itembalnce="+itemsList.get(holder.getAdapterPosition()) .getAviQty());

                        if (itemsList.get(holder.getAdapterPosition()) .getBalanceQty()>= Double.parseDouble(holder.qty.getText().toString())) {
                           MainActivity2.vocher_Items.get(holder.getAdapterPosition()).
                                   setQty(Double.parseDouble(holder.qty.getText().toString()));



                           holder.total.setText(GeneralMethod.convertToEnglish((String.  format("%.3f", calculatNetTotal(holder.getAdapterPosition())))));
                           Log.e("case1.1", "total=," + holder.total.getText());
                           Log.e("case1.1", "qty=," + MainActivity2.vocher_Items.get(holder.getAdapterPosition()).getQty());
                           MainActivity2.desRespon.setText("aa");
                           itemsList.get(holder.getAdapterPosition()) .setAviQty( itemsList.get(holder.getAdapterPosition()) .getBalanceQty()- MainActivity2.vocher_Items.get(holder.getAdapterPosition()).getQty());
                        }
                        else {
                           GeneralMethod.showSweetDialog(context, 0, "",context.getResources().getString(R.string.aviQTY)+" "+itemsList.get(holder.getAdapterPosition()) .getBalanceQty());
                           MainActivity2.vocher_Items.get(holder.getAdapterPosition()).
                                   setQty(itemsList.get(holder.getAdapterPosition()) .getBalanceQty());
                           holder.qty.setText(itemsList.get(holder.getAdapterPosition()) .getBalanceQty()+"");

                           holder.total.setText(GeneralMethod.convertToEnglish((String.  format("%.3f", calculatNetTotal(holder.getAdapterPosition())))));
                           Log.e("case1.1", "total=," + holder.total.getText());
                           Log.e("case1.1", "qty=," + MainActivity2.vocher_Items.get(holder.getAdapterPosition()).getQty());
                           MainActivity2.desRespon.setText("aa");
                           itemsList.get(holder.getAdapterPosition()) .setAviQty( itemsList.get(holder.getAdapterPosition()) .getBalanceQty()- MainActivity2.vocher_Items.get(holder.getAdapterPosition()).getQty());

                        }
                     }
                     else
                     { Log.e("case1.2","case1.2");
                        MainActivity2.vocher_Items.get(holder.getAdapterPosition()).
                                setQty(Double.parseDouble(holder.qty.getText().toString()));

                        double TotalDiscVal = itemsList.get(holder.getAdapterPosition()).getQty() * itemsList.get(holder.getAdapterPosition()).getF_D() * itemsList.get(holder.getAdapterPosition()).getDiscount();
                        holder.total.setText(GeneralMethod.convertToEnglish((String.  format("%.3f", calculatNetTotal(holder.getAdapterPosition())))));

                        MainActivity2.desRespon.setText("aa");

                     }
                  }
                  else if(Double.parseDouble(holder.qty.getText().toString())<0)
                  {
                     holder.qty.setError("not valid");
                     MainActivity2.vocher_Items.get(holder.getAdapterPosition()).
                             setQty(0);

                     double TotalDiscVal = itemsList.get(holder.getAdapterPosition()).getQty() * itemsList.get(holder.getAdapterPosition()).getF_D() * itemsList.get(holder.getAdapterPosition()).getDiscount();
                     holder.total.setText(GeneralMethod.convertToEnglish((String.  format("%.3f", calculatNetTotal(holder.getAdapterPosition())))));
                     MainActivity2.desRespon.setText("aa");
                  } else if(Double.parseDouble(holder.qty.getText().toString())==0)
                  {
                     MainActivity2.vocher_Items.get(holder.getAdapterPosition()).
                             setQty(0);

                     double TotalDiscVal = itemsList.get(holder.getAdapterPosition()).getQty() * itemsList.get(holder.getAdapterPosition()).getF_D() * itemsList.get(holder.getAdapterPosition()).getDiscount();
                     holder.total.setText(GeneralMethod.convertToEnglish((String.  format("%.3f", calculatNetTotal(holder.getAdapterPosition())))));
                     MainActivity2.desRespon.setText("aa");

                  }
               }catch (Exception exception){

               }


            }else
            {
               Log.e("case1.3","case1.3");
               MainActivity2. vocher_Items.get(holder.getAdapterPosition()).
                       setQty(1);
                    double TotalDiscVal=itemsList.get(holder.getAdapterPosition()).getQty()*itemsList.get(holder.getAdapterPosition()).getF_D()*itemsList.get(holder.getAdapterPosition()).getDiscount();
               holder.total.setText(GeneralMethod.convertToEnglish((String.  format("%.3f", calculatNetTotal(holder.getAdapterPosition())))));

               MainActivity2.desRespon.setText("aa");


            }
         }
      });
      holder.price.setText(itemsList.get(position).getF_D()+"");
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
            {     MainActivity2. vocher_Items.get(holder.getAdapterPosition()).setF_D( Double.parseDouble(holder.price.getText().toString()));
                     Log.e("f_d===",MainActivity2. vocher_Items.get(holder.getAdapterPosition()).getF_D()+"");
               double TotalDiscVal=itemsList.get(holder.getAdapterPosition()).getQty()*itemsList.get(holder.getAdapterPosition()).getF_D()*itemsList.get(holder.getAdapterPosition()).getDiscount();
              Log.e("position=",""+position);
              try {
                 holder.total.setText(GeneralMethod.convertToEnglish((String.  format("%.3f", calculatNetTotal(holder.getAdapterPosition())))));
                 MainActivity2.desRespon.setText("aa");
              }catch (Exception e){
                 Log.e("Exception=",""+e.getMessage());
              }

            }else
            {
               MainActivity2. vocher_Items.get(holder.getAdapterPosition()).
                       setF_D( 1);
               Log.e("f_d===",itemsList.get(holder.getAdapterPosition()).getF_D()+"");

               double TotalDiscVal=itemsList.get(holder.getAdapterPosition()).getQty()*itemsList.get(holder.getAdapterPosition()).getF_D()*itemsList.get(holder.getAdapterPosition()).getDiscount();

               holder.total.setText(GeneralMethod.convertToEnglish((String.  format("%.3f", calculatNetTotal(holder.getAdapterPosition())))));
               MainActivity2.desRespon.setText("aa");
            }
         }
      });
     if(!IsExistsInList(itemsList.get(position).getITEMNO())) holder.free.setText("0");
     else holder.free.setText(MainActivity2.vocher_Items.get(index).getFree()+"");
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


                 MainActivity2.vocher_Items.get(holder.getAdapterPosition()).
                                 setFree(Double.parseDouble(holder.free.getText().toString().trim()));



              }else
              {
                 MainActivity2. vocher_Items.get(holder.getAdapterPosition()).
                         setFree( 0);

              }
           }
        });
     }catch (Exception e){

     }


      if(!IsExistsInList(itemsList.get(position).getITEMNO())) holder.discount.setText("0");

      else holder.discount.setText(MainActivity2.vocher_Items.get(index).getDiscount()+"");

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
                  MainActivity2. vocher_Items.get(holder.getAdapterPosition()).
                          setDiscount ( Double.parseDouble(holder.discount.getText().toString().trim())/100);
                  double TotalDiscVal=itemsList.get(holder.getAdapterPosition()).getQty()*itemsList.get(holder.getAdapterPosition()).getF_D()*itemsList.get(holder.getAdapterPosition()).getDiscount();

                  holder.total.setText(GeneralMethod.convertToEnglish((String.  format("%.3f", calculatNetTotal(holder.getAdapterPosition())))));

               }else
               {
                  MainActivity2. vocher_Items.get(holder.getAdapterPosition()).
                          setDiscount( 0);
                  double TotalDiscVal=itemsList.get(holder.getAdapterPosition()).getQty()*itemsList.get(holder.getAdapterPosition()).getF_D()*itemsList.get(holder.getAdapterPosition()).getDiscount();
                  holder.total.setText(GeneralMethod.convertToEnglish((String.  format("%.3f", calculatNetTotal(holder.getAdapterPosition())))));
               }
               MainActivity2.desRespon.setText("aa");   }
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
      for (int i = 0; i < MainActivity2.vocher_Items.size(); i++)
         if (MainActivity2.vocher_Items.get(i).getITEMNO().equals(ItemOCode)) {
            index = i;
            exists = true;
            break;

         }

      return exists;
   }
   double calculatNetTotal(int position){
      double TotalDiscVal = itemsList.get(position).getQty() * itemsList.get(position).getF_D() *itemsList.get(position).getDiscount();


      double   Tax =itemsList.get(position).getF_D() * itemsList.get(position).getTAXPERC();
      double TaxValue= Tax * itemsList.get(position).getQty();

      double    subtotal = itemsList.get(position).getQty()*itemsList.get(position).getF_D()- TaxValue- TotalDiscVal;
      double nettotal = subtotal + TaxValue;


      return nettotal;
   }
}
