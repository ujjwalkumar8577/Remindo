package com.ujjwalkumar.remindo;

public class UnitReminder {
    private int index;
    private int parentID;
    private int frequency;
    private String name;
    private String type;
    private String repeat;
    private long time;

    public UnitReminder(int index, int parentID, int frequency, String name, String type, String repeat, long time) {
        this.index = index;
        this.parentID = parentID;
        this.frequency = frequency;
        this.name = name;
        this.type = type;
        this.repeat = repeat;
        this.time = time;
    }

    public int getIndex() {
        return index;
    }

    public int getParentID() {
        return parentID;
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

    public long getTime() {
        return time;
    }

    private int generateID() {
        return 0;
    }
}
