package com.stringcheesedevs.cleancoin;

import android.content.Context;

import com.stringcheesedevs.cleancoin.Models.Car;
import com.stringcheesedevs.cleancoin.Persistence.CleanCoinDAO;

import java.util.ArrayList;

public class EmissionsCalc
{
    private CleanCoinDAO datasource=null;
    public EmissionsCalc(Context c)
    {
        datasource = new CleanCoinDAO(c);
    }

    //Only given miles traveled, determine how much fuel used
    public double getTripEmissions(double milesTraveled)
    {
        datasource.open();
        Car myCar = datasource.getUserCar();
        myCar.setMPG();
        return myCar.combinedmpg * milesTraveled;
    }

    //Given speeds, time driven at each speed, and if the car is on the highway or not, determine how much fuel used
    public double getTripEmissions(ArrayList<Double> speeds, ArrayList<Double> times, boolean onHighway)
    {
        datasource.open();
        Car myCar = datasource.getUserCar();
        myCar.setMPG();
        double usedAvg = onHighway ? myCar.highwayMPG : myCar.cityMPG;
        double emissions = 0;
        for (int i = 0; i < speeds.size(); i++)
        {
            double time = times.get(i) / 60;
            double wEfficiency = usedAvg;
            double speed = speeds.get(i);
            if (speed >= 60)
            {
                speed = speed - 55;
                if (speed < 10)
                    wEfficiency *= 97 / 100;
                else if (speed < 15)
                    wEfficiency *= 92 / 100;
                else if (speed < 20)
                    wEfficiency *= 83 / 100;
                else if (speed < 25)
                    wEfficiency *= 77 / 100;
                else
                    wEfficiency *= 72 / 100;
            }
            emissions += wEfficiency * time;
        }
        return emissions;
    }

    //Determine how much better efficiency is compared to average car.
    //@return percent efficiency compared to average car.
    //If returned number is negative, more efficient than normal car.
    //If returned number is positive, less efficient than normal car.
    public double compareToAvg(double mpg)
    {
        return (mpg - 24.7) / 24.7 * 100;
    }

    //Determine how much better efficiency is compared to other MPG.
    //@return percent efficiency compared to some other MPG.
    public double compareToOther(double myMPG, double otherMPG)
    {
        return (myMPG - otherMPG) / otherMPG * 100;
    }

    
}