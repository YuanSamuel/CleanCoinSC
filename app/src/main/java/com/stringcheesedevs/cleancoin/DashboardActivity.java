package com.stringcheesedevs.cleancoin;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.stringcheesedevs.cleancoin.Models.Car;
import com.stringcheesedevs.cleancoin.Persistence.CleanCoinDAO;
import com.stringcheesedevs.cleancoin.Persistence.CleanCoinDBHelper;

import java.io.IOException;
import java.util.ArrayList;

public class DashboardActivity extends AppCompatActivity {

    //Sample data set
    //2004,AUDI,A4 AVANT QUATTRO,STATION WAGON - SMALL,1.8,4,AS5,Z,13.2,9,11.3,25,260

    public static Context tempcontext;
    public static String[] cardatafiles = {
            "1995-1999 data.out", "2000 data.out", "2001 data.out", "2002 data.out", "2003 data.out", "2004 data.out",
            "2005 data.out", "2006 data.out", "2007 data.out", "2008 data.out", "2009 data.out", "2010 data.out", "2011 data.out",
            "2012 data.out", "2013 data.out", "2014 data.out", "2015 data.out", "2016 data.out", "2017 data.out", "2018 data.out"
    };
    private CleanCoinDAO datasource=null;
    private TextView testmessage;
    private FusedLocationProviderClient mFusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GoogleApiAvailability.getInstance().getErrorDialog(this, GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this), 1);
        setContentView(R.layout.activity_dashboard);
        tempcontext = getApplicationContext();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 1);


        datasource = new CleanCoinDAO(this.getApplicationContext());
        datasource.open();

        if(!datasource.isSignedIn()){
            initialDisplay();
        }
        //Do this to get all occurrences of a specific type of data, eg. this example returns a list of all the years in the data
        //datasource.getAllUniques(1);
        //Enter in a specific set of details, shown below, and get specific answer based on allColumns list index
        //datasource.getCarStat(2004,"AUDI","A4 AVANT QUATTRO","STATION WAGON - SMALL",12);
        //Gets the complete list of Car objects
        //datasource.getAllCarData();
        testmessage = findViewById(R.id.testmessage);
        testmessage.setText(datasource.getUserCar().toString());
    }

    public ArrayList<Car> loadCarsData() throws IOException {
        DataParser f = new DataParser();
        ArrayList<Car>cs = new ArrayList<>();
        try {
            for (String filename: cardatafiles){
                cs.addAll(f.loadCars(filename, tempcontext));
            }
        }
        catch (Exception e){
            Log.d("Loading issue", "Data unable to parse from textfiles",e);
        }
        return cs;
    }

    public void initialDisplay(){
        Intent intent1 = new Intent(this,FirstdataActivity.class);
        startActivity(intent1);
    }

    public void onResume()
    {
        super.onResume();

    }

    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length == 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED)
                {
                    Toast toast = Toast.makeText(this, "CleanCoin needs access to you location in order to run properly", Toast.LENGTH_LONG);
                    toast.show();
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }
}
