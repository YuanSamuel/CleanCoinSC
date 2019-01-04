package com.stringcheesedevs.cleancoin.Models;

public class Car {

    public int year;
    public String brand;
    public String model;
    public String carclass;
    //(L)
    public double enginesize;
    public int cylinders;
    public String transmission;
    public String fueltype;
    //(L/100km)
    public double highwayconsumption;
    //(L/100km)
    public double cityconsumption;
    //(mpg)
    public double highwayMPG;
    //(mpg)
    public double cityMPG;
    //(L/100km)
    public double combinedconsuption;
    //(mpg)
    public int combinedmpg;
    //(g/km)
    public int emmissions;

    public Car()
    {

    }

    public String toString(){

        return year+brand+model+carclass+enginesize+cylinders+transmission+fueltype+highwayconsumption+cityconsumption+combinedconsuption+combinedmpg+emmissions;
    }
    public void setMPG()
    {
        highwayMPG = 235.215 / highwayconsumption;
        cityMPG = 235.215 / cityconsumption;
    }

}
