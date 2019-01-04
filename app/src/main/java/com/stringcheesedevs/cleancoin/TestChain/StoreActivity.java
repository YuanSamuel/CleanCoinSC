package com.stringcheesedevs.cleancoin.TestChain;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.stringcheesedevs.cleancoin.R;
import com.stringcheesedevs.cleancoin.RecyclerViewAdapter;

import java.util.ArrayList;

public class StoreActivity extends AppCompatActivity {

    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mImages = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);
        mNames.add("Coupon 1");
        mNames.add("Coupon 2");
        mNames.add("Coupon 3");
        mNames.add("Coupon 4");
        mNames.add("Coupon 5");
        mNames.add("Coupon 6");
        mNames.add("Coupon 7");
        mNames.add("Coupon 8");
        mNames.add("Coupon 9");
        mNames.add("Coupon 10");
        mNames.add("Coupon 11");
        mNames.add("Coupon 12");
        initRecyclerView();
    }

    private void initRecyclerView(){
        RecyclerView recyclerView = findViewById(R.id.shop_recycleview);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this, mNames, mImages);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
