package com.example.mars.data;

public class CourseContentData {

    int courseId;
    String name;
    String url;

    public CourseContentData(int courseId, String name, String url) {
        this.courseId = courseId;
        this.name = name;
        this.url = url;
    }

    public int getCourseId() {
        return courseId;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }
}
