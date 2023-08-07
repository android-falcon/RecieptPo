package com.hiaryabeer.receiptapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hiaryabeer.receiptapp.R;
import com.hiaryabeer.receiptapp.models.GeneralMethod;
import com.hiaryabeer.receiptapp.models.ReceiptDetails;

import java.util.List;

public class DetailsAdapter extends RecyclerView.Adapter<DetailsAdapter.ViewHolder>{
   List<ReceiptDetails>list;
   Context context;

   public DetailsAdapter(List<ReceiptDetails> list, Context context) {
      this.list = list;
      this.context = context;
   }

   @NonNull
   @Override
   public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.deatailsrow, parent, false));

   }

   @Override
   public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
      holder.item_code_.setText(list.get(position).getItemNo()+"");
      holder.item_Name_.setText(list.get(position).getItemName()+"");
      holder.item_Qty.setText(list.get(position).getQty()+"");
      double ConvRate=1;
      try {
          ConvRate=Double.parseDouble(list.get(position).getConvRate());
      }catch (Exception e){
          ConvRate=1;
      }

      holder.item_price.setText(GeneralMethod.convertToEnglish(String.  format("%.3f",(list.get(position).getPrice()*ConvRate))));
      //GeneralMethod.convertToEnglish(String.format("%.3f", Math.abs(totalnetsal - AllVocherDiscount)))
      holder.discount_.setText(GeneralMethod.convertToEnglish(String.format("%.3f",list.get(position).getDiscount()))+"");
      holder.free.setText(list.get(position).getFree()+"");
      holder.tax.setText(GeneralMethod.convertToEnglish(String.format("%.3f",list.get(position).getTax()))+"");

   }

   @Override
   public int getItemCount() {
      return list.size();
   }

   class ViewHolder extends RecyclerView.ViewHolder{
      TextView item_code_,item_Name_,item_Qty,item_price,discount_,free,tax;

      public ViewHolder(@NonNull View itemView) {
         super(itemView);

         item_code_=itemView.findViewById(R.id.item_code_);
         item_Name_=itemView.findViewById(R.id.item_Name_);
         item_Qty=itemView.findViewById(R.id.item_Qty);
         item_price=itemView.findViewById(R.id.item_price);
         free=itemView.findViewById(R.id.free);
         discount_=itemView.findViewById(R.id.discount_);
         tax=itemView.findViewById(R.id.tax);
      }
   }
}
