package com.stringcheesedevs.cleancoin.Persistence;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.stringcheesedevs.cleancoin.DashboardActivity;
import com.stringcheesedevs.cleancoin.Models.Car;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class CleanCoinDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "DB_CLEANCOIN";
    private static final int DATABASE_VERSION = 1;
    public static final String CARDATA_TABLE_NAME = "TBL_CARDATA";
    public static final String USERS_TABLE_NAME = "TBL_USERDATA";

    private static final String USERS_TABLE_CREATE =
            "CREATE TABLE " + USERS_TABLE_NAME + " (" +
                    "ID" + " TEXT primary key, " +
                    "YEAR" + " INT, " +
                    "BRAND" + " TEXT, " +
                    "MODEL" + " TEXT, " +
                    "CARCLASS" + " TEXT, " +
                    "FIRSTNAME" + " TEXT, " +
                    "LASTNAME" + " TEXT, " +
                    "AGE" + " INT);";

    private static final String CARDATA_TABLE_CREATE =
            "CREATE TABLE " + CARDATA_TABLE_NAME + " (" +
                    "ID" + " TEXT primary key, " +
                    "YEAR" + " INT, " +
                    "BRAND" + " TEXT, " +
                    "MODEL" + " TEXT, " +
                    "ENGINESIZE" + " DOUBLE, " +
                    "CYLINDERS" + " INT, " +
                    "TRANSMISSION" + " TEXT, " +
                    "FUELTYPE" + " TEXT, " +
                    "HWYCONSUMPTION" + " DOUBLE, " +
                    "CITYCONSUMPTION" + " DOUBLE, " +
                    "COMBCONSUMPTION" + " DOUBLE, " +
                    "COMBMPG" + " INT, " +
                    "EMMISIONS" + " INT, " + 
                    "CARCLASS" + " TEXT);";

    CleanCoinDBHelper (Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //create tables below
    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(CARDATA_TABLE_CREATE);
        db.execSQL(USERS_TABLE_CREATE);
        try{
            loadCarData(db);
        }
        catch (IOException e){
            Log.d("!@#$%^","filenotfound or already loaded",e);
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i2) {
        db.execSQL("DROP TABLE IF EXISTS " + CARDATA_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + USERS_TABLE_NAME);
        onCreate(db);
    }

    public void addfromCSV(Car m,SQLiteDatabase database){
        ContentValues values = new ContentValues();
        String id = java.util.UUID.randomUUID().toString();
        values.put("ID",id);
        values.put("YEAR",m.year);
        values.put("BRAND",m.brand);
        values.put("MODEL",m.model);
        values.put("ENGINESIZE",m.enginesize);
        values.put("CYLINDERS",m.cylinders);
        values.put("TRANSMISSION",m.transmission);
        values.put("FUELTYPE",m.fueltype);
        values.put("HWYCONSUMPTION",m.highwayconsumption);
        values.put("CITYCONSUMPTION",m.cityconsumption);
        values.put( "COMBCONSUMPTION",m.combinedconsuption);
        values.put("COMBMPG",m.combinedmpg);
        values.put("EMMISIONS",m.emmissions);
        values.put("CARCLASS",m.carclass);
        long insertID = database.insert(CARDATA_TABLE_NAME, null, values);
    }

    public void loadCarData(SQLiteDatabase database)throws IOException{
        DashboardActivity d = new DashboardActivity();
        try{
            ArrayList<Car> fs = d.loadCarsData();
            for(Car car:fs){
                addfromCSV(car,database);
            }}
        catch(Exception e){
            Log.d("LOADERROR","problem with data loading");
        }

    }
}