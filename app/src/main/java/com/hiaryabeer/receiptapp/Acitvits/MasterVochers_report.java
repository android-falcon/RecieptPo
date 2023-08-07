package com.hiaryabeer.receiptapp.Acitvits;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputLayout;
import com.hiaryabeer.receiptapp.Adapters.MasterAdapter;
import com.hiaryabeer.receiptapp.R;
import com.hiaryabeer.receiptapp.models.AppDatabase;
import com.hiaryabeer.receiptapp.models.CustomerInfo;
import com.hiaryabeer.receiptapp.models.GeneralMethod;
import com.hiaryabeer.receiptapp.models.HeaderFooterPageEvent;
import com.hiaryabeer.receiptapp.models.ReceiptMaster;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MasterVochers_report extends AppCompatActivity {
    EditText fromdate,todate;
    static final long ONE_DAY = 24 * 60 * 60 * 1000L;
    AppCompatButton preview;
    Date currentTimeAndDate;
    SimpleDateFormat df;
    Calendar myCalendar;
    RecyclerView recyclerView;
    AppDatabase mydatabase;
    private TextInputLayout customer_textInput;
    String Cus_selection="";
    int Cus_pos = 0;
    private List<CustomerInfo> customerInfoList = new ArrayList<>();
    private List<CustomerInfo> VendorInfoList = new ArrayList<>();
    private List<String> customerNames = new ArrayList<>();
    int VochType=504;
    public int paymethod = 0;
  File  file;
  List<ReceiptMaster> MastersVocher=new ArrayList<>();
    List<ReceiptMaster> filteredItems=new ArrayList<>();
    private AutoCompleteTextView customerTv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master_vochers_report);
        init();
        Document document = new Document(PageSize.A4, 36, 36, 90, 36);
        PdfWriter writer = null;
        try {
            String directory_path = Environment.getExternalStorageDirectory().getPath() + "/Receipts/";
            file = new File(directory_path);
            if (!file.exists()) {
                file.mkdirs();
            }
            String targetPdf = directory_path + "Vochers";

            File path = new File(targetPdf);

            writer = PdfWriter.getInstance(document, new FileOutputStream(path));
            // add header and footer
            HeaderFooterPageEvent event = new HeaderFooterPageEvent();
            writer.setPageEvent(event);

            // write to document
            document.open();
        } catch (DocumentException e) {
            e.printStackTrace();
            Log.e("DocumentException=", e.getMessage()+"");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.e("FileNotFoundException=", e.getMessage()+"");
        }


        try {
            document.add(new Paragraph("Adding a header to PDF Document using iText."));
            document.newPage();
            document.add(new Paragraph("Adding a footer to PDF Document using iText."));
            document.close();

        } catch (DocumentException e) {
            e.printStackTrace();
            Log.e("DocumentException=", e.getMessage()+"");
        }


        final RadioGroup payradioGroup = (RadioGroup) findViewById(R.id.payradioGroup);
        payradioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                View radioButton = payradioGroup.findViewById(checkedId);
                int index = payradioGroup.indexOfChild(radioButton);

                // Add logic here

                switch (index) {
                    case 0: // first button  ذمم

                        paymethod = 0;

                        //     Toast.makeText(getApplicationContext(), "Selected button number " + index, 500).show();
                        break;
                    case 1: // secondbutton  نقدا
                        paymethod = 1;

                        //      Toast.makeText(getApplicationContext(), "Selected button number " + index, 500).show();
                        break;
                }
            }
        });
        customerInfoList.clear();
        customerInfoList = mydatabase.customers_dao().getAllCustms();
        fillCustomerspinner(customerInfoList);
        final RadioGroup voch_typeradio = (RadioGroup) findViewById(R.id.radioGroup);
        voch_typeradio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                View radioButton = voch_typeradio.findViewById(checkedId);
                int index = voch_typeradio.indexOfChild(radioButton);

                // Add logic here

                switch (index) {
                    case 0: // first button  طلب شراء

               {
                            VochType = 505;
                            VendorInfoList.clear();
                            VendorInfoList = mydatabase.customers_dao().getAllVendor();
                            fillCustomerspinner(VendorInfoList);
                            customerTv.setText("");
                        }
                        break;
                    case 1: // secondbutton فاتورة
               {
                            VochType = 504;
                            customerInfoList.clear();
                            customerInfoList = mydatabase.customers_dao().getAllCustms();
                            customerTv.setText("");
                            fillCustomerspinner(customerInfoList);
                        }
                        break;
                }

            }
        });

        MastersVocher.clear();
        MastersVocher=mydatabase.receiptMaster_dao().getAllOrders();
       fillAdapter(MastersVocher);


        currentTimeAndDate = Calendar.getInstance().getTime();
        df = new SimpleDateFormat("dd/MM/yyyy");
        myCalendar = Calendar.getInstance();
        String today = df.format(currentTimeAndDate);

        Calendar date = Calendar.getInstance();

        int dayOfWeek=myCalendar.get(Calendar.DAY_OF_WEEK);
        Log.e("dayOfWeek=",dayOfWeek+"");
        fromdate.setText(GeneralMethod.convertToEnglish(today));
        todate.setText(GeneralMethod.convertToEnglish(today));
        fromdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(MasterVochers_report.this, openDatePickerDialog(0), myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        todate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(MasterVochers_report.this, openDatePickerDialog(1), myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

    }
    void init(){
//        List<Date> dates = getDates("2012-02-01", "2012-03-01");
//        for(Date date:dates)
//            Log.e("date",date);
        List<Date> getDates=getDates("2012-02-01", "2012-03-01");
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        Log.e("date",sdf.format(getDates.get(0))+"");
        fromdate=findViewById(R.id.from_date);
        todate=findViewById(R.id.to_date);
        customerTv = findViewById(R.id.customerTv);
        customer_textInput = findViewById(R.id.customer_textInput);
        customerTv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long rowId) {
                Cus_selection = (String) parent.getItemAtPosition(position);
                Cus_pos = position;
                Log.e("Cus_selection==", Cus_selection);
                customer_textInput.setError(null);
                customer_textInput.clearFocus();
            }
        });
        mydatabase=AppDatabase.getInstanceDatabase(MasterVochers_report.this);
        recyclerView=findViewById(R.id.tableData);
        recyclerView.setLayoutManager(new LinearLayoutManager(MasterVochers_report.this));
        preview=findViewById(R.id.save);
        preview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filteredItems.clear();
                MastersVocher.clear();
                Log.e("paymethod==",paymethod+"");
                MastersVocher=mydatabase.receiptMaster_dao().getAllOrdersbydate(paymethod);
                if (!fromdate.getText().toString().equals("") && !todate.getText().toString().equals("")) {

                    for (int n = 0; n < MastersVocher.size(); n++) {

                        if (filters(n)) {

                            filteredItems.add(MastersVocher.get(n));


                        }
                    }
                    fillAdapter(filteredItems);
                }

       //         fillAdapter(filteredItems);
            }
        });



    }
  void   fillAdapter(  List<ReceiptMaster>receiptMasters){
          Log.e("fillAdapter listsize=",receiptMasters.size()+"");
      recyclerView.setAdapter(new MasterAdapter(receiptMasters,MasterVochers_report.this));
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public boolean filters(int n) {
        String fromDate = fromdate.getText().toString().trim();
        String toDate = todate.getText().toString();
   //     String customerName =mydatabase.customers_dao().getCustmByNumber(MastersVocher.get(n).getCustomerId()) ;
        String customerName =customerNames.get(Cus_pos);
        Log.e("customerName==",customerName+"");
        String date = MastersVocher.get(n).getDate();
        int vType = MastersVocher.get(n).getVOUCHERTYPE();
        int pMethod = MastersVocher.get(n).getPaymethod();
        Log.e("vType==",vType+"  "+pMethod);
        try {
            if (!Cus_selection.toString().equals("")) {
                Log.e("tag2", "*****" + date + "***" + fromDate+" todate"+ toDate);
                if (
                        //(Cus_selection.equals(customerName)) &&
                        (formatDate(date).after(formatDate(fromDate)) || formatDate(date).equals(formatDate(fromDate))) &&
                        (formatDate(date).before(formatDate(toDate)) || formatDate(date).equals(formatDate(toDate)))
//                                &&
//                        (vType == VochType || VochType == 1) && (pMethod == paymethod || paymethod == 2)
                )
                    return true;
            } else {
                Log.e("tag", "*****" + date + "***" + fromDate+" todate"+ toDate);
                if ((formatDate(date).after(formatDate(fromDate)) || formatDate(date).equals(formatDate(fromDate))) &&
                        (formatDate(date).before(formatDate(toDate)) || formatDate(date).equals(formatDate(toDate)))
//
//                        (vType == VochType || VochType == 1) && (pMethod == paymethod || paymethod == 2)
                )
                    return true;
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return false;
    }
    public DatePickerDialog.OnDateSetListener openDatePickerDialog(final int flag) {
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(flag);
            }

        };
        return date;
    }
    private void updateLabel(int flag) {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        if (flag == 0)
            fromdate.setText(sdf.format(myCalendar.getTime()));
        else
            todate.setText(sdf.format(myCalendar.getTime()));

    }
//    public void fillCustomerspinner(List<CustomerInfo> customerInfoList) {
//        // customerInfoList.clear();
//        customerNames.clear();
//
//
//        for (int i = 0; i < customerInfoList.size(); i++) {
//
//            customerNames.add(customerInfoList.get(i).getCustomerName());
//
//        }
//
//        ArrayAdapter<String> customersAdapter = new ArrayAdapter<>(
//                this, android.R.layout.simple_dropdown_item_1line, customerNames);
//
//        customerTv.setAdapter(customersAdapter);
//    }

    public void fillCustomerspinner(List<CustomerInfo> customerInfoList) {
        // customerInfoList.clear();
        customerNames.clear();

        customerNames=mydatabase.receiptMaster_dao().getCustomers();
        for (int i = 0; i < customerInfoList.size(); i++) {

            customerNames.add(customerInfoList.get(i).getCustomerName());

        }

        ArrayAdapter<String> customersAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_dropdown_item_1line, customerNames);

        customerTv.setAdapter(customersAdapter);
    }

    public Date formatDate (String date) throws ParseException {

        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        Date d = sdf.parse(date);
        return d ;
    }

    private static List<Date> getDates(String dateString1, String dateString2)
    {
        ArrayList<Date> dates = new ArrayList<Date>();
        DateFormat df1 = new SimpleDateFormat("yyyy-mm-dd");

        Date date1 = null;
        Date date2 = null;

        try {
            date1 = df1 .parse(dateString1);
            date2 = df1 .parse(dateString2);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);


        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);

        while(!cal1.after(cal2))
        {
            dates.add(cal1.getTime());
            cal1.add(Calendar.DATE, 1);
        }
        return dates;
    }
}