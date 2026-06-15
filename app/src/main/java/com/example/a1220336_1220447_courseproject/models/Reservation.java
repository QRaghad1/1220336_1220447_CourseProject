package com.example.a1220336_1220447_courseproject.models;

public class Reservation {
    private int id;
    private int userId;
    private int eventId;
    private String reservationDate;
    private int quantity;
    private String type;
    private String status;

    public Reservation() {}

    public Reservation(int id, int userId, int eventId, String reservationDate,
                       int quantity, String type, String status) {
        this.id = id;
        this.userId = userId;
        this.eventId = eventId;
        this.reservationDate = reservationDate;
        this.quantity = quantity;
        this.type = type;
        this.status = status;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getEventId() { return eventId; }
    public void setEventId(int eventId) { this.eventId = eventId; }

    public String getReservationDate() { return reservationDate; }
    public void setReservationDate(String reservationDate) { this.reservationDate = reservationDate; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}