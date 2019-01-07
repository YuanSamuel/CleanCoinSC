package com.stringcheesedevs.cleancoin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.ActivityTransitionResult;

public class TransitionBroadcastReceiver extends BroadcastReceiver
{
    public static final String INTENT_ACTION = "com.stringcheesedevs.cleancoin.ACTION_PROCESS_ACTIVITY_TRANSITIONS";
    @Override
    public void onReceive(Context context, Intent intent)
    {
        Toast toast = Toast.makeText(context, "IT WORKS", Toast.LENGTH_LONG);
        toast.show();
        Log.d("TransitionBroadcast", "YAYYY");
        if (intent != null && INTENT_ACTION.equals(intent.getAction()))
        {
            if (ActivityTransitionResult.hasResult(intent))
            {
                Log.d("TransitionBroadcast", "it equals");
                ActivityTransitionResult intentResult = ActivityTransitionResult.extractResult(intent);
                Log.d("TransitionBroadcast", intentResult.toString());
            }
        }
    }
}
