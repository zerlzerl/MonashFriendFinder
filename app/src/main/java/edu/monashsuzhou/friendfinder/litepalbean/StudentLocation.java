package edu.monashsuzhou.friendfinder.litepalbean;

import org.litepal.crud.LitePalSupport;


public class StudentLocation extends LitePalSupport {
    private int locId;
    private String locName;
    private double latitude;
    private double longitude;
    private String locDate;
    private String locTime;
    private int studentId;

    public int getLocId() {
        return locId;
    }

    public StudentLocation setLocId(int locId) {
        this.locId = locId;
        return this;
    }

    public String getLocName() {
        return locName;
    }

    public StudentLocation setLocName(String locName) {
        this.locName = locName;
        return this;
    }

    public double getLatitude() {
        return latitude;
    }

    public StudentLocation setLatitude(double latitude) {
        this.latitude = latitude;
        return this;
    }

    public double getLongitude() {
        return longitude;
    }

    public StudentLocation setLongitude(double longitude) {
        this.longitude = longitude;
        return this;
    }

    public String getLocDate() {
        return locDate;
    }

    public StudentLocation setLocDate(String locDate) {
        this.locDate = locDate;
        return this;
    }

    public String getLocTime() {
        return locTime;
    }

    public StudentLocation setLocTime(String locTime) {
        this.locTime = locTime;
        return this;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }
}
