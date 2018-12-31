package com.stringcheesedevs.cleancoin.Models;

import java.util.StringTokenizer;

public class Segment {
    public double distance;
    public double time;
    public double direction;
    public static String regex = ",";


    //for now
    public static double dirTolerance = 0.0;
    public static double speedTolerance =0.0;

    public Segment(){}



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

    public String toString(){
        return distance+regex+time+regex+direction;
    }

    public static Segment parseSegment(String s){
        StringTokenizer stringTokenizer = new StringTokenizer(s,",");
        Segment segment = new Segment();
        segment.distance = Double.parseDouble(stringTokenizer.nextToken());
        segment.time = Double.parseDouble(stringTokenizer.nextToken());
        segment.direction = Double.parseDouble(stringTokenizer.nextToken());
        return segment;
    }
}
