package com.romainroche.alarms.data;

/**
 * Created by rroche on 11/08/2017.
 */

public class Alarm {

    private long duration;
    public String name;
    public int color;

    public Alarm(long seconds, String name, int color) {
        this.setSecondsDuration(seconds);
        this.name = name;
        this.color = color;
    }

    public long getSecondsDuration() { return this.duration / 1000; }
    public void setSecondsDuration(long seconds) {this.duration = seconds * 1000;}

    public long getMillisecondsDuration() { return this.duration; }
    public void setMillisecondsDuration(long duration) { this.duration = duration; }

}
