package com.hiaryabeer.receiptapp.models;


import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Database;
import androidx.room.Entity;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.hiaryabeer.receiptapp.Interfaces.Customers_Dao;
import com.hiaryabeer.receiptapp.Interfaces.ItemMaster_ItemBalanceDao;
import com.hiaryabeer.receiptapp.Interfaces.ItemSwitchDao;
import com.hiaryabeer.receiptapp.Interfaces.ItemUnits_Dao;
import com.hiaryabeer.receiptapp.Interfaces.ItemsBalanceDao;
import com.hiaryabeer.receiptapp.Interfaces.Items_Dao;
import com.hiaryabeer.receiptapp.Interfaces.MaxVoucherDao;
import com.hiaryabeer.receiptapp.Interfaces.ReceiptDetails_Dao;
import com.hiaryabeer.receiptapp.Interfaces.ReceiptMaster_Dao;
import com.hiaryabeer.receiptapp.Interfaces.Users_Dao;

@Database(entities = {Items.class,ReceiptMaster.class,ReceiptDetails.class,Item_Unit_Details.class,CustomerInfo.class,User.class,ItemsBalance.class,ItemSwitch.class,MaxVoucher.class}, version =32)

public abstract class AppDatabase extends RoomDatabase {
    public abstract Items_Dao itemsDao();
    public abstract ItemUnits_Dao itemUnitsDao();
    public abstract Customers_Dao customers_dao();
    public abstract ReceiptMaster_Dao receiptMaster_dao();
    public abstract ReceiptDetails_Dao receiptDetails_dao();
    private static AppDatabase InstanceDatabase;
    public abstract Users_Dao usersDao();
    public abstract ItemsBalanceDao itemsBalanceDao();
    public abstract ItemMaster_ItemBalanceDao itemMaster_itemBalanceDao();
    public abstract ItemSwitchDao itemSwitchDao();
    public abstract MaxVoucherDao maxVoucherDao();
    public static String DatabaseName = "ReceiptSystem_Database";





    static final Migration MIGRATION_13_14 = new Migration(13, 14) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {

            database.execSQL("            CREATE TABLE ItemsBalance_TABLE (\n" +
                    "                    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,\n" +
                            "                    COMAPNYNO TEXT,\n" +

                    "                    ItemOCode TEXT,\n" +
                    "                    QTY TEXT,\n" +
                    "                    STOCK_CODE TEXT)\n" +


                    "\t\t\t\t");


        }
    };
    static final Migration MIGRATION_16_17 = new Migration(16, 17) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {

            database.execSQL("            CREATE TABLE ItemSwitch_TABLE (\n" +
                    "                    SERIAL INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,\n" +
                    "                    item_Switch TEXT,\n" +

                    "                    item_NAMEA TEXT,\n" +
                    "                    item_OCODE TEXT,\n" +
                    "                    item_NCODE TEXT)\n" +


                    "\t\t\t\t");


        }
    };
    static final Migration MIGRATION_14_15 = new Migration(14, 15) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {

            database.execSQL("ALTER TABLE Items_Table ADD COLUMN AviQty REAL DEFAULT 0 NOT NULL");


        }
    };
    static final Migration MIGRATION_15_16 = new Migration(15, 16) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {


            database.execSQL("ALTER TABLE Items_Table ADD COLUMN BalanceQty REAL DEFAULT 0 NOT NULL");


        }
    };
    static final Migration MIGRATION_17_18 = new Migration(17, 18) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {


            database.execSQL("ALTER TABLE ReceiptMaster_Table ADD COLUMN TransNo TEXT");
            database.execSQL("ALTER TABLE ReceiptDetails_Table ADD COLUMN TransNo TEXT");

        }
    };
    static final Migration MIGRATION_18_19 = new Migration(18, 19) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {


            database.execSQL("ALTER TABLE ReceiptMaster_Table ADD COLUMN Cust_Name TEXT");


        }
    };
    static final Migration MIGRATION_19_20 = new Migration(19, 20) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {


            database.execSQL("ALTER TABLE Items_Table ADD COLUMN ItemNCode TEXT");


        }
    };
    static final Migration MIGRATION_20_21 = new Migration(20, 21) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {


            database.execSQL("ALTER TABLE Item_Unit_Details ADD COLUMN SALEPRICE REAL DEFAULT 1 NOT NULL");
            database.execSQL("ALTER TABLE Item_Unit_Details ADD COLUMN ITEMBARCODE TEXT");


        }
    };
    static final Migration MIGRATION_21_22 = new Migration(21, 22) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {



            database.execSQL("ALTER TABLE Items_Table ADD COLUMN ConvRate TEXT");


        }
    };
    static final Migration MIGRATION_22_23 = new Migration(22, 23) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {



            database.execSQL("ALTER TABLE ReceiptDetails_Table ADD COLUMN ConvRate TEXT");


        }
    };
    static final Migration MIGRATION_23_24 = new Migration(23, 24) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {



            database.execSQL("ALTER TABLE Items_Table ADD COLUMN WhichUNITSTR TEXT");


        }
    };
    static final Migration MIGRATION_24_25 = new Migration(24, 25) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {



            database.execSQL("ALTER TABLE Items_Table ADD COLUMN UNITBARCODE TEXT");
            database.execSQL("ALTER TABLE Items_Table ADD COLUMN CALCQTY TEXT");



        }
    };

    static final Migration MIGRATION_25_26 = new Migration(25, 26) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {



            database.execSQL("ALTER TABLE ReceiptDetails_Table ADD COLUMN UNITBARCODE TEXT");
            database.execSQL("ALTER TABLE ReceiptDetails_Table ADD COLUMN CALCQTY TEXT");



        }
    };
    static final Migration MIGRATION_26_27 = new Migration(26, 27) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {

            database.execSQL("            CREATE TABLE MaxVoucher_TABLE (\n" +
                    "                    SERIAL INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,\n" +
                    "                    order_requst TEXT,\n" +

                    "                    vocher TEXT)\n" +



                    "\t\t\t\t");


        }
    };
    static final Migration MIGRATION_27_28 = new Migration(27, 28) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE ReceiptDetails_Table ADD COLUMN NewVochNum INTEGER DEFAULT 1 NOT NULL");
            database.execSQL("ALTER TABLE ReceiptMaster_Table ADD COLUMN NewVochNum INTEGER DEFAULT 1 NOT NULL");



        }
    };


    static final Migration MIGRATION_29_30 = new Migration(29, 30) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE ReceiptMaster_Table ADD COLUMN NOTE TEXT ");



        }
    };

    static final Migration MIGRATION_31_32 = new Migration(29, 30) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE Items_Table ADD COLUMN LSPRICE TEXT ");



        }
    };

    public static synchronized AppDatabase getInstanceDatabase(Context context) {

        if (InstanceDatabase == null) {

            InstanceDatabase = Room.databaseBuilder(context, AppDatabase.class, DatabaseName)
                    .allowMainThreadQueries()
                      .addMigrations(MIGRATION_13_14,MIGRATION_14_15,MIGRATION_15_16,MIGRATION_16_17,MIGRATION_17_18,MIGRATION_18_19,MIGRATION_19_20,MIGRATION_20_21,MIGRATION_21_22,MIGRATION_22_23,MIGRATION_23_24,MIGRATION_24_25,MIGRATION_25_26,MIGRATION_13_14,MIGRATION_26_27,MIGRATION_27_28,MIGRATION_29_30,MIGRATION_31_32)
                    .fallbackToDestructiveMigration()
                    .build();

        }

        return InstanceDatabase;

    }

}
