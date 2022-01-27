package com.anne.linger.mareu.model;

import java.time.LocalDate;
import java.util.List;

/**
*Model class for a meeting
*/
public class Meeting {
    private String name;
    private Room room;
    private LocalDate date;
    private String time;
    private String duration;
    private List<String> collaborators;
    private String topic;

    public Meeting(String name, Room room, LocalDate date, String time, String duration, List<String> collaborators, String topic) {
        this.name = name;
        this.room = room;
        this.date = date;
        this.time = time;
        this.duration = duration;
        this.collaborators = collaborators;
        this.topic = topic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public List<String> getCollaborators() {
        return collaborators;
    }

    public void setCollaborators(List<String> collaborators) {
        this.collaborators = collaborators;
    }
    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }
}
