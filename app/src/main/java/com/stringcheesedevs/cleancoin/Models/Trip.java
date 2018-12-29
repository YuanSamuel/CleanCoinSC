package com.stringcheesedevs.cleancoin.Models;

import java.util.ArrayList;
import java.util.Iterator;

public class Trip {
    public ArrayList<Segment>segments;
    public double lat;
    public double longitude;
    public Iterator iterator;
    public Segment current;

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
}
