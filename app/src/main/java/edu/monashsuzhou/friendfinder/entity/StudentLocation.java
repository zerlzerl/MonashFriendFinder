package edu.monashsuzhou.friendfinder.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class StudentLocation implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer locId;
    private String locName;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String locDate;
    private String locTime;
    private StudentProfile studentId;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getLocId() {
        return locId;
    }

    public StudentLocation setLocId(Integer locId) {
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

    public BigDecimal getLatitude() {
        return latitude;
    }

    public StudentLocation setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
        return this;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public StudentLocation setLongitude(BigDecimal longitude) {
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

    public StudentProfile getStudentId() {
        return studentId;
    }

    public StudentLocation setStudentId(StudentProfile studentId) {
        this.studentId = studentId;
        return this;
    }
}
