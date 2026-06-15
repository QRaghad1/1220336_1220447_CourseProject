package com.example.a1220336_1220447_courseproject.models;

public class Favorite {
    private int id;
    private int userId;
    private int eventId;

    public Favorite() {}

    public Favorite(int id, int userId, int eventId) {
        this.id = id;
        this.userId = userId;
        this.eventId = eventId;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getEventId() { return eventId; }
    public void setEventId(int eventId) { this.eventId = eventId; }
}