package com.oceanview.entity;

public class Room {

    private int roomId;
    private String roomType;
    private double rate;
    private String availabilityStatus;

    public Room() {}

    public Room(int roomId, String roomType, double rate, String availabilityStatus) {
        this.roomId = roomId;
        this.roomType = roomType;
        this.rate = rate;
        this.availabilityStatus = availabilityStatus;
    }
    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public String getAvailabilityStatus() {
        return availabilityStatus;
    }

    public void setAvailabilityStatus(String availabilityStatus) {
        this.availabilityStatus = availabilityStatus;
    }
}