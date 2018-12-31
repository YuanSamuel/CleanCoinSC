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
        if(segments.size()==0){
            segments.add(s);
            current  = (Segment)iterator.next();
        }
        else if (current.appendable(s)){
            current.appendSegment(s);
        }
        else{
            segments.add(s);
            current  = (Segment)iterator.next();
        }
    }

    public String toString(){
        String temp = lat+regex+longitude+regex;
        for(Segment s: segments){
            temp+=s.toString()+regex;
        }
        return temp;
    }

    public static Trip parseTrip(String s){
        StringTokenizer stringTokenizer = new StringTokenizer(s, ";");
        Trip trip = new Trip(Double.parseDouble(stringTokenizer.nextToken()),Double.parseDouble(stringTokenizer.nextToken()));
        while (stringTokenizer.hasMoreTokens()){
            trip.addSegment(Segment.parseSegment(stringTokenizer.nextToken()));
        }
        return trip;
    }


}
