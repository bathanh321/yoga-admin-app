package com.example.yogaapp.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class YogaCourse {
    @SerializedName("_id")
    private String id;

    @SerializedName("dayOfWeek")
    private String dayOfWeek;

    @SerializedName("courseTime")
    private String courseTime;

    @SerializedName("pricePerClass")
    private double pricePerClass;

    @SerializedName("classType")
    private String classType;

    @SerializedName("location")
    private String location;

    @SerializedName("classes")
    private List<String> classes;

    public YogaCourse () {}

    public YogaCourse(String id, String dayOfWeek, String courseTime, double pricePerClass, String classType, String location, List<String> classes) {
        this.id = id;
        this.dayOfWeek = dayOfWeek;
        this.courseTime = courseTime;
        this.pricePerClass = pricePerClass;
        this.classType = classType;
        this.location = location;
        this.classes = classes;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getDayOfWeek() { return dayOfWeek; }
    public void setDayOfWeek(String dayOfWeek) { this.dayOfWeek = dayOfWeek; }

    public String getCourseTime() { return courseTime; }
    public void setCourseTime(String courseTime) { this.courseTime = courseTime; }

    public double getPricePerClass() { return pricePerClass; }
    public void setPricePerClass(double pricePerClass) { this.pricePerClass = pricePerClass; }

    public String getClassType() { return classType; }
    public void setClassType(String classType) { this.classType = classType; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public List<String> getClasses() { return classes; }
    public void setClasses(List<String> classes) { this.classes = classes; }
}
