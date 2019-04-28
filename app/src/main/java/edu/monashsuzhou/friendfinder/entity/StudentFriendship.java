package edu.monashsuzhou.friendfinder.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

public class StudentFriendship implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer friendshipId;
    private String startingDate;
    private String endDate;
    private StudentProfile studentId;
    private StudentProfile friendId;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getFriendshipId() {
        return friendshipId;
    }

    public StudentFriendship setFriendshipId(Integer friendshipId) {
        this.friendshipId = friendshipId;
        return this;
    }

    public String getStartingDate() {
        return startingDate;
    }

    public StudentFriendship setStartingDate(String startingDate) {
        this.startingDate = startingDate;
        return this;
    }

    public String getEndDate() {
        return endDate;
    }

    public StudentFriendship setEndDate(String endDate) {
        this.endDate = endDate;
        return this;
    }

    public StudentProfile getStudentId() {
        return studentId;
    }

    public StudentFriendship setStudentId(StudentProfile studentId) {
        this.studentId = studentId;
        return this;
    }

    public StudentProfile getFriendId() {
        return friendId;
    }

    public StudentFriendship setFriendId(StudentProfile friendId) {
        this.friendId = friendId;
        return this;
    }
}
