package com.stringcheesedevs.cleancoin;


import android.content.Context;
import android.util.Log;

import com.stringcheesedevs.cleancoin.Models.Car;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;

public class DataParser {

    public int year;
    public String brand;
    public String model;
    public String carclass;
    //(L)
    public double enginesize;
    public int cylinders;
    public String transmission;
    public char fueltype;
    //(L/100km)
    public double highwayconsumption;
    //(L/100km)
    public double cityconsumption;
    //(L/100km)
    public double combinedconsuption;
    //(mpg)
    public int combinedmpg;
    //(g/km)
    public int emmissions;

    public DataParser(){

    }

    public ArrayList<Car> loadCars (String filepath, Context context)throws IOException {
        ArrayList<Car>cars = new ArrayList<>();
        Car temp = new Car();
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(context.getAssets().open(filepath)));
            StringTokenizer tempscan;
            String line = bufferedReader.readLine();

            while(line.length()!=0){
                tempscan = new StringTokenizer(bufferedReader.readLine(),",");

                temp.year = Integer.parseInt(tempscan.nextToken());
                temp.brand = tempscan.nextToken();
                temp.model = tempscan.nextToken();
                temp.carclass = tempscan.nextToken();
                temp.enginesize = Double.parseDouble(tempscan.nextToken());
                temp.cylinders = Integer.parseInt(tempscan.nextToken());
                temp.transmission = tempscan.nextToken();
                temp.fueltype = tempscan.nextToken();
                temp.highwayconsumption = Double.parseDouble(tempscan.nextToken());
                temp.cityconsumption = Double.parseDouble(tempscan.nextToken());
                temp.combinedconsuption = Double.parseDouble(tempscan.nextToken());
                temp.combinedmpg = Integer.parseInt(tempscan.nextToken());
                temp.emmissions = Integer.parseInt(tempscan.nextToken());

                cars.add(temp);
                line = bufferedReader.readLine();
                temp = new Car();

            }


            bufferedReader.close();
        }
        catch (Exception e){
            Log.d("ScannerError","Scanner not working",e);
        }
        return cars;
    }

}
