package com.stringcheesedevs.cleancoin.Models;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;

public class Trip {
    public ArrayList<Segment>segments;
    public double lat;
    public double longitude;
    public Iterator iterator;
    public Segment current;
    public static String regex = ";";

    public Trip(double startlat, double startlong){
        lat = startlat;
        longitude = startlong;
        segments = new ArrayList<>();
        iterator = segments.iterator();
    }

    public void addSegment(Segment s){
        if (current.appendable(s) && segments.size() != 0){
            current.appendSegment(s);
        }
        else{
            segments.add(s);
            current  = (Segment)iterator.next();
        }
    }

    public String toString(){
        StringBuilder temp = new StringBuilder();
        temp.append(lat); temp.append(regex); temp.append(longitude); temp.append(regex);
        for(Segment s: segments){
            temp.append(s.toString());
            temp.append(regex);
        }
        return temp.toString();
    }

    public static Trip parseTrip(String s){
        StringTokenizer stringTokenizer = new StringTokenizer(s, ";");
        Trip trip = new Trip(Double.parseDouble(stringTokenizer.nextToken()),Double.parseDouble(stringTokenizer.nextToken()));
        while (stringTokenizer.hasMoreTokens()){
            trip.addSegment(Segment.parseSegment(stringTokenizer.nextToken()));
        }
        return trip;
    }

    public double getTotalDistTraveled()
    {
        int dist = 0;
        for (int i = 0; i < segments.size(); i++)
            dist += segments.get(i).distance;
        return dist;
    }
}
