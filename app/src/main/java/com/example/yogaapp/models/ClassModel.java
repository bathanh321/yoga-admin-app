package com.example.yogaapp.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ClassModel {
    @SerializedName("_id")
    private String id;

    @SerializedName("teacherName")
    private String teacherName;

    @SerializedName("description")
    private String description;

    @SerializedName("date")
    private Date date;

    @SerializedName("duration")
    private int duration;

    @SerializedName("capacity")
    private int capacity;

    @SerializedName("participants")
    private List<String> participants;

    @SerializedName("yogaCourseId")
    private String yogaCourseId;

    public ClassModel() {
        this.participants = new ArrayList<>();
    }

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTeacherName() { return teacherName; }
    public void setTeacherName(String teacherName) { this.teacherName = teacherName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }

    public int getDuration() { return duration; }
    public void setDuration(int duration) { this.duration = duration; }

    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }

    public List<String> getParticipants() { return participants; }
    public void setParticipants(List<String> participants) { this.participants = participants; }

    public String getYogaCourseId() { return yogaCourseId; }
    public void setYogaCourseId(String yogaCourseId) { this.yogaCourseId = yogaCourseId; }
}