package edu.monashsuzhou.friendfinder.litepalbean;

import org.litepal.LitePal;

import java.util.List;

public class DatabaseHelper {
    public DatabaseHelper(){

    }

    public void insertLocation(StudentLocation sl){
        sl.save();
    }

    public void insertStudent(StudentProfile sp){
        sp.save();
    }


    public void insertMiniStudent(MiniStudent ms){
        ms.save();
    }

    public void insertMatchingStudent(MiniStudent ms){
        ms.setMatchingMarker(1);
        ms.save();
    }

    public void insertFriend(MiniStudent ms,double mLongtitude, double mLatitude){
        ms.setFriendMarker(1);
        double longtitude = ms.getLongtude();
        double latitude = ms.getLatitude();
        double distance = Math.pow((longtitude - mLongtitude),2) + Math.pow((latitude - mLatitude),2);
        distance = Math.sqrt(distance);
        ms.setDistance(distance);
        ms.save();
    }

    public MiniStudent getMiniStudent(String stuid){
        List<MiniStudent> ms = LitePal.where("studentid = ?" ,stuid).find(MiniStudent.class);
        if (ms.size() > 0){
            return ms.get(0);
        } else {
            return null;
        }
    }

    public List<MiniStudent> getMatchingStudent(){
        List<MiniStudent> miniStu_list = LitePal.where("matchingmarker = ?","1").find(MiniStudent.class);
        return miniStu_list;
    }

    public List<MiniStudent> getFriend(){
        List<MiniStudent> miniStu_list = LitePal.where("friendmarker = ?","1").find(MiniStudent.class);
        return miniStu_list;
    }

    public List<StudentProfile> getAllStudent(){
        List<StudentProfile> sp_list = LitePal.findAll(StudentProfile.class);
        return sp_list;
    }

    public StudentProfile getStudent(String stu_id){
        List<StudentProfile> sp_list = LitePal.where("studentid = ?", stu_id).find(StudentProfile.class);
        if (sp_list.size() > 0){
            return sp_list.get(0);
        } else {
            return null;
        }
    }
}
