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

    //@return how much fuel used given miles traveled
    //@param milesTraveled in miles lol
    public double getTripEmissions(double milesTraveled)
    {
        datasource.open();
        Car myCar = datasource.getUserCar();
        myCar.setMPG();
        return myCar.combinedmpg * milesTraveled;
    }

    //@return percent efficiency compared to average car.
    //@param mpg
    //If returned number is negative, more efficient than normal car.
    //If returned number is positive, less efficient than normal car.
    public double compareToAvg(double mpg)
    {
        return (mpg - 24.7) / 24.7 * 100;
    }

    //@return percent efficiency compared to some other MPG.
    //@param mpg of user, mpg of other
    public double compareToOther(double myMPG, double otherMPG)
    {
        return (myMPG - otherMPG) / otherMPG * 100;
    }

    //Return total emissions produced when cruising
    //@param time in minutes
    public double getEmissionsWCruise(double mpg, double timeInCruise)
    {
        return mpg * 107 / 100 * timeInCruise / 60;
    }

    //@return how much fuel
    //@param speeds in mpg, time driven at each speed in minutes, if car is on highway or not
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

    //@return fuel used
    //@param mpg, times driven with each mpg in minutes
    public double getTripEmissions(ArrayList<Double> mpg, ArrayList<Double> times)
    {
        double emissions = 0;
        for (int i = 0; i < mpg.size(); i++)
            emissions += mpg.get(i) * times.get(i) / 60;
        return emissions;
    }

    //@return gallons of fuel used when idling
    //@param mpg, time idling in minutes
    public double gallonsWIdling(double mpg, double timeIdling)
    {
        return 0.5 * timeIdling * 1 / mpg;
    }
}