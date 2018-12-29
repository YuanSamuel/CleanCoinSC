package com.stringcheesedevs.cleancoin.Models;

public class Segment {
    public double distance;
    public double time;
    public double direction;


    //for now
    public static double dirTolerance = 0.0;
    public static double speedTolerance =0.0;


    public Segment(double dis, double t, double dir){
        distance = dis;
        time = t;
        direction = dir;
    }


    public double getSpeed(){
        return distance/time;
    }

    public void appendSegment(Segment s){
        distance +=s.distance;
        time+=s.time;
    }

    public boolean appendable(Segment s){
        return Math.abs(s.getSpeed()-this.getSpeed())<speedTolerance&&Math.abs(this.direction-s.direction)<dirTolerance;
    }
}
