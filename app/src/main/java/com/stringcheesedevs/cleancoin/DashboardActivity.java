package com.stringcheesedevs.cleancoin;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.ActivityTransition;
import com.google.android.gms.location.ActivityTransitionEvent;
import com.google.android.gms.location.ActivityTransitionRequest;
import com.google.android.gms.location.ActivityTransitionResult;
import com.google.android.gms.location.DetectedActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.stringcheesedevs.cleancoin.Models.Car;
import com.stringcheesedevs.cleancoin.Persistence.CleanCoinDAO;
import com.stringcheesedevs.cleancoin.Persistence.CleanCoinDBHelper;
//import com.stringcheesedevs.cleancoin.TestChain.StoreActivity;

import org.w3c.dom.Text;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DashboardActivity extends AppCompatActivity {

    //Sample data set
    //2004,AUDI,A4 AVANT QUATTRO,STATION WAGON - SMALL,1.8,4,AS5,Z,13.2,9,11.3,25,260

    Button toShop;

    public static Context tempcontext;
    public static String[] cardatafiles = {
            "1995-1999 data.out", "2000 data.out", "2001 data.out", "2002 data.out", "2003 data.out", "2004 data.out",
            "2005 data.out", "2006 data.out", "2007 data.out", "2008 data.out", "2009 data.out", "2010 data.out", "2011 data.out",
            "2012 data.out", "2013 data.out", "2014 data.out", "2015 data.out", "2016 data.out", "2017 data.out", "2018 data.out"
    };
    private CleanCoinDAO datasource=null;
    private TextView testmessage;
    private FusedLocationProviderClient mFusedLocationClient;
    private PendingIntent mPendingIntent;
    private TransitionsReceiver mTransitionsReceiver;
    private final String INTENT_ACTION = "com.stringcheesedevs." + "TRANSITIONS_RECEIVER_ACTION";
    private TextView actionText;
    private TextView locationText;
    private LocationRequest locRequest;
    private boolean locationOn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GoogleApiAvailability.getInstance().getErrorDialog(this, GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this), 1);
        setContentView(R.layout.activity_dashboard);
        tempcontext = getApplicationContext();
        actionText = (TextView) findViewById(R.id.activityMessage);
        locationOn = false;
        locationText = (TextView) findViewById(R.id.locText);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        else
            locationOn = true;
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
//        testmessage.setText(datasource.getUserCar().toString());
//
//        toShop = (Button)findViewById(R.id.toshop);
//        toShop.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(DashboardActivity.this, StoreActivity.class);
//                startActivity(intent);
//            }
//        });
        Intent intent = new Intent(INTENT_ACTION);
        mPendingIntent = PendingIntent.getBroadcast(DashboardActivity.this, 0, intent, 0);

        mTransitionsReceiver = new TransitionsReceiver();
        registerReceiver(mTransitionsReceiver, new IntentFilter(INTENT_ACTION));
        setupActivityTransitions();
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

    public void initialDisplay() {
        Intent intent1 = new Intent(this, FirstdataActivity.class);
        startActivity(intent1);
    }

    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length == 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED)
                {
                    Toast toast = Toast.makeText(this, "CleanCoin needs access to your location in order to run properly", Toast.LENGTH_LONG);
                    toast.show();
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

    private void setupActivityTransitions() {
        List<ActivityTransition> transitions = new ArrayList<>();
        transitions.add(
                new ActivityTransition.Builder()
                        .setActivityType(DetectedActivity.IN_VEHICLE)
                        .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_ENTER)
                        .build());
        transitions.add(
                new ActivityTransition.Builder()
                        .setActivityType(DetectedActivity.IN_VEHICLE)
                        .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_EXIT)
                        .build());
        transitions.add(
                new ActivityTransition.Builder()
                        .setActivityType(DetectedActivity.STILL)
                        .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_ENTER)
                        .build());
        transitions.add(
                new ActivityTransition.Builder()
                        .setActivityType(DetectedActivity.STILL)
                        .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_EXIT)
                        .build());
        ActivityTransitionRequest request = new ActivityTransitionRequest(transitions);

        // Register for Transitions Updates.
        Task<Void> task =
                ActivityRecognition.getClient(this)
                        .requestActivityTransitionUpdates(request, mPendingIntent);
        task.addOnSuccessListener(
                new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void result) {
                        Log.i("TransitionsBroadcast", "Transitions Api was successfully registered.");
                    }
                });
        task.addOnFailureListener(
                new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        Log.e("TransitionsBroadcast", "Transitions Api could not be registered: " + e);
                    }
                });
    }


    public class TransitionsReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (!TextUtils.equals(INTENT_ACTION, intent.getAction())) {
                actionText.setText("Received an unsupported action in TransitionsReceiver: action="
                        + intent.getAction());
                return;
            }
            if (ActivityTransitionResult.hasResult(intent)) {
                ActivityTransitionResult result = ActivityTransitionResult.extractResult(intent);
                for (ActivityTransitionEvent event : result.getTransitionEvents()) {
                    String activity = toActivityString(event.getActivityType());
                    String transitionType = toTransitionType(event.getTransitionType());
                    actionText.setText("Transition: " + activity + " (" + transitionType + ")" + "   "
                            + new SimpleDateFormat("HH:mm:ss", Locale.US).format(new Date()));
                    if (activity.equals("IN_VEHICLE") && transitionType.equals("ENTER"))
                    {
                        tryRequest();
                        if (locationOn)
                            recordLocation();
                    }
                }
            }

        }
    }

    private static String toActivityString(int activity) {
        switch (activity) {
            case DetectedActivity.STILL:
                return "STILL";
            case DetectedActivity.IN_VEHICLE:
                return "IN_VEHICLE";
            default:
                return "UNKNOWN";
        }
    }

    private static String toTransitionType(int transitionType) {
        switch (transitionType) {
            case ActivityTransition.ACTIVITY_TRANSITION_ENTER:
                return "ENTER";
            case ActivityTransition.ACTIVITY_TRANSITION_EXIT:
                return "EXIT";
            default:
                return "UNKNOWN";
        }
    }

    private void recordLocation()
    {
        LocationCallback locCallback = new LocationCallback()
        {
            @Override
            public void onLocationResult(LocationResult locResult)
            {
                if (locResult != null)
                {
                    for (Location location : locResult.getLocations())
                    {
                        locationText.setText("Lat: " + location.getLatitude() + " Long: " + location.getLongitude());
                    }
                }
            }
        };
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        mFusedLocationClient.requestLocationUpdates(locRequest, locCallback, null);

    }

    private void tryRequest()
    {
        locRequest = LocationRequest.create();
        locRequest.setInterval(500);
        locRequest.setFastestInterval(100);
        locRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }
}

