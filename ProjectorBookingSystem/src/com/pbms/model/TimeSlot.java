package com.pbms.model;

public class TimeSlot {
    private int    slotId;
    private String startTime;
    private String endTime;
    private String label;

    public TimeSlot(int slotId, String startTime, String endTime, String label) {
        this.slotId    = slotId;
        this.startTime = startTime;
        this.endTime   = endTime;
        this.label     = label;
    }

    public int    getSlotId()    { return slotId; }
    public String getStartTime() { return startTime; }
    public String getEndTime()   { return endTime; }
    public String getLabel()     { return label; }

    @Override
    public String toString() { return label; }
}
