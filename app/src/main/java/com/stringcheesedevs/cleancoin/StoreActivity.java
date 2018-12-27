package com.stringcheesedevs.cleancoin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class StoreActivity extends AppCompatActivity {

    //End plan for store
    /**
     * Most likely there will be a bunch of different fragments representing different
     * types of store entries. Companies will be able to post their own coupons and
     * make long-term endorsements. Also companies should have access to total cleancoin data
     * to understand how many are out there and how they should post coupon, being able to set coupon price
     * postability will be completely free. Buyers should be able to lodge complaints upon failure to receive
     * the stuff they "bought" with their cleancoin, thus hopefully creating a functional economic ecosystem.
     * */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);


    }
}
