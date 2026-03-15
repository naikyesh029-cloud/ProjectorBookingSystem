package com.pbms.model;

public class Booking {
    private int    bookingId;
    private String projectorId;
    private String bookedBy;
    private String bookingDate;
    private int    slotId;
    private String slotLabel;
    private String purpose;
    private String projectorName;
    private String labName;

    public Booking(int bookingId, String projectorId, String bookedBy,
                   String bookingDate, int slotId, String slotLabel,
                   String purpose, String projectorName, String labName) {
        this.bookingId     = bookingId;
        this.projectorId   = projectorId;
        this.bookedBy      = bookedBy;
        this.bookingDate   = bookingDate;
        this.slotId        = slotId;
        this.slotLabel     = slotLabel;
        this.purpose       = purpose;
        this.projectorName = projectorName;
        this.labName       = labName;
    }

    public int    getBookingId()     { return bookingId; }
    public String getProjectorId()   { return projectorId; }
    public String getBookedBy()      { return bookedBy; }
    public String getBookingDate()   { return bookingDate; }
    public int    getSlotId()        { return slotId; }
    public String getSlotLabel()     { return slotLabel; }
    public String getPurpose()       { return purpose; }
    public String getProjectorName() { return projectorName; }
    public String getLabName()       { return labName; }
}