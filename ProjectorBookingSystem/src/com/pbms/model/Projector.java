package com.pbms.model;

public class Projector {
    private String  projectorId;
    private String  name;
    private String  location;
    private boolean isActive;

    public Projector(String projectorId, String name, String location, boolean isActive) {
        this.projectorId = projectorId;
        this.name        = name;
        this.location    = location;
        this.isActive    = isActive;
    }

    public String  getProjectorId() { return projectorId; }
    public String  getName()        { return name; }
    public String  getLocation()    { return location; }
    public boolean isActive()       { return isActive; }

    @Override
    public String toString() { return name + " (" + location + ")"; }
}
