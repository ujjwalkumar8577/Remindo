package com.ujjwalkumar.remindo;

import java.util.Calendar;

public class Reminder {
    private int ID;
    private int frequency;
    private String name;
    private String type;
    private String repeat;
    private long startTime;
    private long[] time;

    public Reminder(int frequency, String name, String type, String repeat, long startTime) {
        this.frequency = frequency;
        this.name = name;
        this.type = type;
        this.repeat = repeat;
        this.startTime = startTime;
        ID = generateID();

        Calendar cal = Calendar.getInstance();
        time = new long[frequency];
        for(int i=0; i<frequency; i++) {
            cal.setTimeInMillis(startTime);
            switch (repeat) {
                case "Every Day":
                    cal.add(Calendar.DAY_OF_MONTH, i);
                    break;
                case "Every Week":
                    cal.add(Calendar.DAY_OF_MONTH, 7*i);
                    break;
                case "Every Month":
                    cal.add(Calendar.MONTH, i);
                    break;
                case "Every Quarter":
                    cal.add(Calendar.MONTH, 3*i);
                    break;
                case "Every Year":
                    cal.add(Calendar.YEAR, i);
                    break;
                default:
                    break;
            }
            time[i] = cal.getTimeInMillis();
        }
    }

    public int getID() {
        return ID;
    }

    public int getFrequency() {
        return frequency;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getRepeat() {
        return repeat;
    }

    public long getStartTime() {
        return startTime;
    }

    public long[] getTime() {
        return time;
    }

    public long getTime(int i) {
        if(i<frequency)
            return time[i];
        else
            return -1;
    }

    private int generateID() {
        return 0;
    }
}
