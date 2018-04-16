package com.yosir.createmeeting.model;

import java.util.List;

public class Conference {
    private String topic;
    private String description;
    private long time;
    private Location location;
    private String specificAddress;
    private List<String> invitees;
    private List<String> otherSpeakers;
    private boolean isPrivate;
    private boolean meSpeaker;

    public Conference() {
    }

    public Conference(String topic, String description, long time, Location location, String specificAddress, List<String> invitees, List<String> otherSpeakers, boolean isPrivate, boolean meSpeaker) {
        this.topic = topic;
        this.description = description;
        this.time = time;
        this.location = location;
        this.specificAddress = specificAddress;
        this.invitees = invitees;
        this.otherSpeakers = otherSpeakers;
        this.isPrivate = isPrivate;
        this.meSpeaker = meSpeaker;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getSpecificAddress() {
        return specificAddress;
    }

    public void setSpecificAddress(String specificAddress) {
        this.specificAddress = specificAddress;
    }

    public List<String> getInvitees() {
        return invitees;
    }

    public void setInvitees(List<String> invitees) {
        this.invitees = invitees;
    }

    public List<String> getOtherSpeakers() {
        return otherSpeakers;
    }

    public void setOtherSpeakers(List<String> otherSpeakers) {
        this.otherSpeakers = otherSpeakers;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public boolean isMeSpeaker() {
        return meSpeaker;
    }

    public void setMeSpeaker(boolean meSpeaker) {
        this.meSpeaker = meSpeaker;
    }

    @Override
    public String toString() {
        return "Conference{" +
                "topic='" + topic + '\'' +
                ", description='" + description + '\'' +
                ", time=" + time +
                ", location=" + location +
                ", specificAddress='" + specificAddress + '\'' +
                ", invitees=" + invitees +
                ", otherSpeakers=" + otherSpeakers +
                ", isPrivate=" + isPrivate +
                ", meSpeaker=" + meSpeaker +
                '}';
    }
}
