package edu.monashsuzhou.friendfinder.litepalbean;

import org.litepal.crud.LitePalSupport;


public class StudentProfile extends LitePalSupport {
    private int studentId;
    private String email;
    private String password;
    private String firstName;
    private String surname;
    private String dateOfBirth;
    private String gender;
    private String course;
    private String studyMode;
    private String address;
    private String suburb;
    private String nationality;
    private String nativeLanguage;
    private String favouriteSport;
    private String favouriteMovie;
    private String favouriteUnit;
    private String currentJob;
    private String subscriptionData;
    private String subscriptionTime;

    public int getStudentId() {
        return studentId;
    }

    public StudentProfile setStudentId(int studentId) {
        this.studentId = studentId;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public StudentProfile setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public StudentProfile setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public StudentProfile setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getSurname() {
        return surname;
    }

    public StudentProfile setSurname(String surname) {
        this.surname = surname;
        return this;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public StudentProfile setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
        return this;
    }

    public String getGender() {
        return gender;
    }

    public StudentProfile setGender(String gender) {
        this.gender = gender;
        return this;
    }

    public String getCourse() {
        return course;
    }

    public StudentProfile setCourse(String course) {
        this.course = course;
        return this;
    }

    public String getStudyMode() {
        return studyMode;
    }

    public StudentProfile setStudyMode(String studyMode) {
        this.studyMode = studyMode;
        return this;
    }

    public String getAddress() {
        return address;
    }

    public StudentProfile setAddress(String address) {
        this.address = address;
        return this;
    }

    public String getSuburb() {
        return suburb;
    }

    public StudentProfile setSuburb(String suburb) {
        this.suburb = suburb;
        return this;
    }

    public String getNationality() {
        return nationality;
    }

    public StudentProfile setNationality(String nationality) {
        this.nationality = nationality;
        return this;
    }

    public String getNativeLanguage() {
        return nativeLanguage;
    }

    public StudentProfile setNativeLanguage(String nativeLanguage) {
        this.nativeLanguage = nativeLanguage;
        return this;
    }

    public String getFavouriteSport() {
        return favouriteSport;
    }

    public StudentProfile setFavouriteSport(String favouriteSport) {
        this.favouriteSport = favouriteSport;
        return this;
    }

    public String getFavouriteMovie() {
        return favouriteMovie;
    }

    public StudentProfile setFavouriteMovie(String favouriteMovie) {
        this.favouriteMovie = favouriteMovie;
        return this;
    }

    public String getFavouriteUnit() {
        return favouriteUnit;
    }

    public StudentProfile setFavouriteUnit(String favouriteUnit) {
        this.favouriteUnit = favouriteUnit;
        return this;
    }

    public String getCurrentJob() {
        return currentJob;
    }

    public StudentProfile setCurrentJob(String currentJob) {
        this.currentJob = currentJob;
        return this;
    }

    public String getSubscriptionData() {
        return subscriptionData;
    }

    public StudentProfile setSubscriptionData(String subscriptionData) {
        this.subscriptionData = subscriptionData;
        return this;
    }

    public String getSubscriptionTime() {
        return subscriptionTime;
    }

    public StudentProfile setSubscriptionTime(String subscriptionTime) {
        this.subscriptionTime = subscriptionTime;
        return this;
    }
}
