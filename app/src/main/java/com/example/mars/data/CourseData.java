package com.example.mars.data;

public class CourseData {
    int courseid;
    String courseName;
    boolean isPaid;
    String courseFee;

    public CourseData(int courseid,String courseName, boolean isPaid, String courseFee) {
        this.courseName = courseName;
        this.isPaid = isPaid;
        this.courseFee = courseFee;
        this.courseid=courseid;
    }

    public int getCourseId() {
        return courseid;
    }


    public String getCourseName() {
        return courseName;
    }

    public boolean isPaid() {
        return isPaid;
    }

    public String getCourseFee() {
        return courseFee;
    }
}
