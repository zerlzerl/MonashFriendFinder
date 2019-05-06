package edu.monashsuzhou.friendfinder.litepalbean;

import org.litepal.crud.LitePalSupport;

public class MiniStudent extends LitePalSupport {
    private int studentid;
    private String email;//
    private String firstname;
    private String lastname;//
    private double latitude;
    private double longtude;
    private double distance;
    private String locname;
    private int friendMarker;
    private int matchingMarker;

    public MiniStudent(){

    }

    public MiniStudent(int studentid, String email, String firstname, String lastname, double latitude, double longtude,
                       int friendMarker, int matchingMarker, double distance, String locname) {
        this.studentid = studentid;
        this.email = email;
        this.firstname = firstname;
        this.lastname = lastname;
        this.latitude = latitude;
        this.longtude = longtude;
        this.friendMarker = friendMarker;
        this.matchingMarker = matchingMarker;
        this.distance = distance;
        this.locname = locname;
    }

    public String getLocname() {
        return locname;
    }

    public void setLocname(String locname) {
        this.locname = locname;
    }

    public int getFriendMarker() {
        return friendMarker;
    }

    public void setFriendMarker(int friendMarker) {
        this.friendMarker = friendMarker;
    }

    public int getMatchingMarker() {
        return matchingMarker;
    }

    public void setMatchingMarker(int matchingMarker) {
        this.matchingMarker = matchingMarker;
    }

    public void setStudentid(int studentid) {
        this.studentid = studentid;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongtude(double longtude) {
        this.longtude = longtude;
    }

    public int getStudentid() {
        return studentid;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongtude() {
        return longtude;
    }
}
