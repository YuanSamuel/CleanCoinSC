package com.stringcheesedevs.cleancoin.Persistence;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import android.content.Context;

import com.stringcheesedevs.cleancoin.Models.Car;

import java.util.ArrayList;
import java.util.List;

public class CleanCoinDAO{

    private SQLiteDatabase database;
    private CleanCoinDBHelper dbHelper;
    public static String [] allColumns = {
        "ID",
        "YEAR",
        "BRAND",
        "MODEL",
        "ENGINESIZE",
        "CYLINDERS",
        "TRANSMISSION",
        "FUELTYPE",
        "HWYCONSUMPTION",
        "CITYCONSUMPTION",
        "COMBCONSUMPTION",
        "COMBMPG",
        "EMMISIONS",
        "CARCLASS"
    };

    public CleanCoinDAO(Context context){
        try{
            dbHelper = new CleanCoinDBHelper(context);}
        catch (Exception e){
            Log.d("DBLoadError","DBHelper Constructor not working",e);
        }
    }

    public void open() throws SQLException {
        database=dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    /**Input in index in allColumns of stat you would like to get the result of based on search*/

    public int getCarStat(int year, String brand, String model, String carclass, int index){
        int stat = -1;

        // select query
        String sql = "";
        sql += "SELECT "+allColumns[index]+ " FROM " + dbHelper.CARDATA_TABLE_NAME;
        sql += " WHERE " + allColumns[1] + " = " + year;
        sql += " AND "+ allColumns[2]+" LIKE '%" + brand + "%'";
        sql += " AND "+ allColumns[3]+" LIKE '%" + model + "%'";
        sql += " AND "+ allColumns[13]+" LIKE '%" + carclass + "%'";
        sql += " ORDER BY " + allColumns[1] + " DESC";
        sql += " LIMIT 0,1";

        database = dbHelper.getWritableDatabase();

        // execute the query
        Cursor cursor = database.rawQuery(sql, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {

            stat = Integer.parseInt(cursor.getString(cursor.getColumnIndex(allColumns[index])));
        }

        cursor.close();
        database.close();

        // return the list of records
        return stat;
    }

    public ArrayList<Car> getAllCarData(){

        ArrayList<Car> recordsList = new ArrayList<>();

        // select query
        String sql = "";
        sql += "SELECT * FROM " + dbHelper.CARDATA_TABLE_NAME;

        database = dbHelper.getWritableDatabase();

        // execute the query
        Cursor cursor = database.rawQuery(sql, null) ;
        Car temp = new Car();

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                // int productId = Integer.parseInt(cursor.getString(cursor.getColumnIndex(fieldProductId)));
                temp.year = Integer.parseInt(cursor.getString(cursor.getColumnIndex(allColumns[1])));
                temp.brand = cursor.getString(cursor.getColumnIndex(allColumns[2]));
                temp.model = cursor.getString(cursor.getColumnIndex(allColumns[3]));
                temp.carclass = cursor.getString(cursor.getColumnIndex(allColumns[13]));
                temp.enginesize = Double.parseDouble(cursor.getString(cursor.getColumnIndex(allColumns[4])));
                temp.cylinders = Integer.parseInt(cursor.getString(cursor.getColumnIndex(allColumns[5])));
                temp.transmission = cursor.getString(cursor.getColumnIndex(allColumns[6]));
                temp.fueltype = cursor.getString(cursor.getColumnIndex(allColumns[7]));
                temp.highwayconsumption = Double.parseDouble(cursor.getString(cursor.getColumnIndex(allColumns[8])));
                temp.cityconsumption = Double.parseDouble(cursor.getString(cursor.getColumnIndex(allColumns[9])));
                temp.combinedconsuption = Double.parseDouble(cursor.getString(cursor.getColumnIndex(allColumns[10])));
                temp.combinedmpg = Integer.parseInt(cursor.getString(cursor.getColumnIndex(allColumns[11])));
                temp.emmissions = Integer.parseInt(cursor.getString(cursor.getColumnIndex(allColumns[12])));

                // add to list
                recordsList.add(temp);

            } while (cursor.moveToNext());
        }

        cursor.close();
        database.close();

        // return the list of records
        return recordsList;
    }
    
    /**Input in index in allColumns of column you would like to get the unique results of*/

    public ArrayList<String> getAllUniques(int index) {

        ArrayList<String> recordsList = new ArrayList<>();

        // select query
        String sql = "";
        sql += "SELECT DISTINCT "+allColumns[index]+" FROM " + dbHelper.CARDATA_TABLE_NAME;
        sql += " GROUP BY " + allColumns[index];
        sql += " ORDER BY " + allColumns[index] + " DESC";

        database = dbHelper.getWritableDatabase();

        // execute the query
        Cursor cursor = database.rawQuery(sql, null);
        String item;

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                // int productId = Integer.parseInt(cursor.getString(cursor.getColumnIndex(fieldProductId)));
                item = cursor.getString(cursor.getColumnIndex(allColumns[index]));


                // add to list
                recordsList.add(item);

            } while (cursor.moveToNext());
        }

        cursor.close();
        database.close();

        // return the list of records
        return recordsList;
    }
}
