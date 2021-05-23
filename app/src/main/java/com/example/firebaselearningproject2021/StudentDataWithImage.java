package com.example.firebaselearningproject2021;

public class StudentDataWithImage {
    String Course, Duration, Name, ProfImage;

    public StudentDataWithImage(String course, String duration, String name, String profImage) {
        Course = course;
        Duration = duration;
        Name = name;
        ProfImage = profImage;
    }

    public String getCourse() {
        return Course;
    }

    public void setCourse(String course) {
        Course = course;
    }

    public String getDuration() {
        return Duration;
    }

    public void setDuration(String duration) {
        Duration = duration;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getProfImage() {
        return ProfImage;
    }

    public void setProfImage(String profImage) {
        ProfImage = profImage;
    }
}
