package com.example.a1220336_1220447_courseproject.models;

public class Event {
    private int id;
    private String title;
    private String description;
    private String category;
    private String date;
    private String time;
    private String location;
    private int seats;
    private String image;

    public Event() {}

    public Event(int id, String title, String description, String category,
                 String date, String time, String location, int seats, String image) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.category = category;
        this.date = date;
        this.time = time;
        this.location = location;
        this.seats = seats;
        this.image = image;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public int getSeats() { return seats; }
    public void setSeats(int seats) { this.seats = seats; }

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }
}