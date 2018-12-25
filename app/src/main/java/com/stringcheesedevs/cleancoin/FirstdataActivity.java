package com.stringcheesedevs.cleancoin;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.stringcheesedevs.cleancoin.Models.User;
import com.stringcheesedevs.cleancoin.Persistence.CleanCoinDAO;

import java.util.ArrayList;

public class FirstdataActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private CleanCoinDAO datasource=null;
    private Button submit;
    public User user = new User();
    private Spinner yearspin;
    private Spinner brandspin;
    private Spinner modelspin;
    private Spinner classspin;
    private EditText ageenter;
    private EditText firstnameenter;
    private EditText lastnameenter;
    public int currentspin = 0;
    public int year;
    public String brand;
    public String model;

    public ArrayList<String> years;
    public ArrayList<String> brands;
    public ArrayList<String> models;
    public ArrayList<String> classes;
    public ArrayAdapter<String>yearadapter;
    public ArrayAdapter<String>brandadapter;
    public ArrayAdapter<String>modeladapter;
    public ArrayAdapter<String>classadapter;



    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firstdata);
        datasource = new CleanCoinDAO(this.getApplicationContext());
        datasource.open();

        yearspin = (Spinner)findViewById(R.id.yearspinner);
        brandspin = (Spinner)findViewById(R.id.brandspinner);
        modelspin = (Spinner)findViewById(R.id.modelspinner);
        classspin = (Spinner)findViewById(R.id.carclassspinner);
        ageenter = (EditText)findViewById(R.id.age);
        firstnameenter = (EditText)findViewById(R.id.firstname);
        lastnameenter = (EditText)findViewById(R.id.lastname);

        years = datasource.getAllUniques(1);

        yearadapter = new ArrayAdapter<>(FirstdataActivity.this,
                android.R.layout.simple_spinner_item,years);

        yearadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearspin.setAdapter(yearadapter);
        yearspin.setOnItemSelectedListener(this);



        submit = (Button)findViewById(R.id.submitbutton);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //probably a good idea to put some try-catches over here in case
                user.age = Integer.parseInt(ageenter.getText().toString());
                user.firstname = firstnameenter.getText().toString();
                user.lastname = lastnameenter.getText().toString();
                datasource.addUser(user);
                datasource.close();
                openDashboard();
            }
        });
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String[]temp;
        int[]tempinds;

        switch (parent.getId()){
            case R.id.yearspinner:
                year = Integer.parseInt(years.get(position));
                currentspin++;
                temp = new String[1];
                temp[0] = years.get(position);
                tempinds = new int[1];
                tempinds[0] = 1;
                brands = datasource.getAllUniques(2,temp ,tempinds);
                brandadapter = new ArrayAdapter<>(FirstdataActivity.this,
                        android.R.layout.simple_spinner_item,brands);

                brandadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                brandspin.setAdapter(brandadapter);
                brandspin.setOnItemSelectedListener(this);
                break;
            case R.id.brandspinner:
                brand = brands.get(position);
                currentspin++;
                temp = new String[2];
                temp[0] = year+"";
                temp[1] = brand;
                tempinds = new int[2];
                tempinds[0] = 1;
                tempinds[1] = 2;
                models = datasource.getAllUniques(3,temp ,tempinds);
                modeladapter = new ArrayAdapter<>(FirstdataActivity.this,
                        android.R.layout.simple_spinner_item,models);

                modeladapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                modelspin.setAdapter(modeladapter);
                modelspin.setOnItemSelectedListener(this);
                break;
            case R.id.modelspinner:
                model = models.get(position);
                currentspin++;
                temp = new String[3];
                temp[0] = year+"";
                temp[1] = brand;
                temp[2] = model;
                tempinds = new int[3];
                tempinds[0] = 1;
                tempinds[1] = 2;
                tempinds[2] = 3;
                classes = datasource.getAllUniques(4,temp ,tempinds);
                classadapter = new ArrayAdapter<>(FirstdataActivity.this,
                        android.R.layout.simple_spinner_item,classes);

                classadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                classspin.setAdapter(classadapter);
                classspin.setOnItemSelectedListener(this);
                break;
            case R.id.carclassspinner:
                user.carID = datasource.getCarStat(year,brand,model,classes.get(position),0,1);
                break;
            default:
                break;

        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void openDashboard(){
        Intent intent1 = new Intent(this,DashboardActivity.class);
        startActivity(intent1);
    }
}
