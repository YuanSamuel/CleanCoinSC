package com.stringcheesedevs.cleancoin;

import android.content.Context;

import com.stringcheesedevs.cleancoin.Models.Car;
import com.stringcheesedevs.cleancoin.Models.Segment;
import com.stringcheesedevs.cleancoin.Models.Trip;
import com.stringcheesedevs.cleancoin.Persistence.CleanCoinDAO;

import java.util.ArrayList;

public class EmissionsCalc
{
    private CleanCoinDAO datasource=null;
    public EmissionsCalc(Context c)
    {
        datasource = new CleanCoinDAO(c);
    }

    /**
     *
     * TODO: Find change in efficiencies in trips, 
     */

    /**
     *
     * @param milesTraveled i wonder what this variable is
     * @return how much fuel used given miles traveled
     */
    public double getTripEmissions(double milesTraveled)
    {
        datasource.open();
        Car myCar = datasource.getUserCar();
        myCar.setMPG();
        return myCar.combinedmpg * milesTraveled;
    }

    /**
     *
     * @param mpg miles per gallon
     * @return percent efficiency compared to the average car
     * If returned number is negative, more efficient than normal car.
     * If returned number is positive, less efficient than normal car.
     */
    public double compareToAvg(double mpg)
    {
        return (mpg - 24.7) / 24.7 * 100;
    }

    /**
     *
     * @param myMPG mpg of user
     * @param otherMPG mpg of other
     * @return percent efficiency compared to some other MPG.
     */
    public double compareToOther(double myMPG, double otherMPG)
    {
        return (myMPG - otherMPG) / otherMPG * 100;
    }

    /**
     *
     * @param mpg miles per gallon
     * @param timeInCruise time cruising in seconds
     * @return total emissions produced when cruising
     */
    public double getEmissionsWCruise(double mpg, double timeInCruise)
    {
        return mpg * 107 / 100 * timeInCruise / 3600;
    }

    /**
     *
     * @param speeds all speeds in mpg
     * @param times times driven at each speed in seconds
     * @param onHighway if car is on highway or not
     * @return how much fuel used
     */
    public double getTripEmissions(ArrayList<Double> speeds, ArrayList<Double> times, ArrayList<Boolean> onHighway)
    {
        datasource.open();
        Car myCar = datasource.getUserCar();
        myCar.setMPG();
        double emissions = 0;
        for (int i = 0; i < speeds.size(); i++)
        {
            double time = times.get(i) / 3600;
            double wEfficiency = (onHighway.get(i)) ? myCar.highwayMPG : myCar.cityMPG;
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

    /**
     *
     * @param mpg miles per gallon
     * @param times times driven with each mpg in seconds
     * @return how much fuel used
     */
    public double getTripEmissions(ArrayList<Double> mpg, ArrayList<Double> times)
    {
        double emissions = 0;
        for (int i = 0; i < mpg.size(); i++)
            emissions += mpg.get(i) * times.get(i) / 3600;
        return emissions;
    }

    /**
     *
     * @param mpg miles per gallon
     * @param timeIdling time idling in seconds
     * @return gallons of fuel used when idling
     */
    public double gallonsWIdling(double mpg, double timeIdling)
    {
        return 0.5 * timeIdling / 60 * 1 / mpg;
    }

    public double getTripEmissions(Trip trip)
    {
        ArrayList<Double> speeds = new ArrayList<Double>();
        ArrayList<Double> times = new ArrayList<Double>();
        ArrayList<Boolean> highway = new ArrayList<Boolean>();
        for (int i = 0; i < trip.segments.size(); i++)
        {
            speeds.add(trip.segments.get(i).getSpeed());
            times.add(trip.segments.get(i).time);
            highway.add(trip.segments.get(i).onHighway);
        }
        return getTripEmissions(speeds, times, highway);
    }

    /**
     *
     * @param list
     * @return list where the first element is the MPG of all previous trips
     *          the second element is the MPG of the most recent trip
     *          the third element is the percent efficiency of the most recent trip compared to all the other ones
     */
    public ArrayList<Double> getRecentEfficiency(ArrayList<Trip> list)
    {
        double dist = 0;
        double emissions = 0;
        for (int i = 0; i < list.size() - 1; i++)
        {
            dist += list.get(i).getTotalDistTraveled();
            emissions += getTripEmissions(list.get(i));
        }
        dist /= 5280;
        double beforeEfficiency = (emissions > 0) ? dist / emissions : 0;
        double nowEfficiency = list.get(list.size() - 1).getTotalDistTraveled() / getTripEmissions(list.get(list.size() - 1));
        ArrayList<Double> ret = new ArrayList<>();
        ret.add(beforeEfficiency);
        ret.add(nowEfficiency);
        ret.add(compareToOther(nowEfficiency, beforeEfficiency));
        return ret;
    }
}