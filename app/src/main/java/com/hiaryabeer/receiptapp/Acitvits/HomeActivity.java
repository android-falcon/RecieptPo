package com.hiaryabeer.receiptapp.Acitvits;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.hiaryabeer.receiptapp.R;
import com.hiaryabeer.receiptapp.databinding.ActivityHomeBinding;
import com.hiaryabeer.receiptapp.models.AppDatabase;
import com.hiaryabeer.receiptapp.models.ImportData;

public class HomeActivity extends AppCompatActivity {
    ActivityHomeBinding binding;
    AppDatabase mydatabase;
    ImportData importData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this, R.layout.activity_home);
        mydatabase= AppDatabase.getInstanceDatabase(HomeActivity.this);
        importData=new ImportData(HomeActivity.this);
//        try {
//            if(mydatabase.itemSwitchDao().getAll().size()==0)
//                importData. fetchItemSwitchData("01/01/2020","01/12/2040");
//        }catch (Exception e){
//
//        }

        binding.orderRequst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        binding.vocher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, ReceivePO.class);
                startActivity(intent);
            }
        });
        binding.VochersReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, MasterVochers_report.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(HomeActivity.this, Login.class);
        startActivity(intent);

    }
    
}