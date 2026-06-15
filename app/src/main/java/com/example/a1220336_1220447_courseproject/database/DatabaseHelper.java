package com.example.a1220336_1220447_courseproject.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.a1220336_1220447_courseproject.models.Event;
import com.example.a1220336_1220447_courseproject.models.Favorite;
import com.example.a1220336_1220447_courseproject.models.Reservation;
import com.example.a1220336_1220447_courseproject.models.User;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "university_events.db";
    private static final int DB_VERSION = 1;

    // Tables
    private static final String TABLE_USERS = "users";
    private static final String TABLE_EVENTS = "events";
    private static final String TABLE_FAVORITES = "favorites";
    private static final String TABLE_RESERVATIONS = "reservations";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Users table
        db.execSQL("CREATE TABLE " + TABLE_USERS + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "firstName TEXT," +
                "lastName TEXT," +
                "email TEXT UNIQUE," +
                "password TEXT," +
                "gender TEXT," +
                "major TEXT," +
                "phone TEXT," +
                "profileImage TEXT," +
                "isAdmin INTEGER DEFAULT 0)");

        // Events table
        db.execSQL("CREATE TABLE " + TABLE_EVENTS + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "title TEXT," +
                "description TEXT," +
                "category TEXT," +
                "date TEXT," +
                "time TEXT," +
                "location TEXT," +
                "seats INTEGER," +
                "image TEXT)");

        // Favorites table
        db.execSQL("CREATE TABLE " + TABLE_FAVORITES + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "userId INTEGER," +
                "eventId INTEGER)");

        // Reservations table
        db.execSQL("CREATE TABLE " + TABLE_RESERVATIONS + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "userId INTEGER," +
                "eventId INTEGER," +
                "reservationDate TEXT," +
                "quantity INTEGER," +
                "type TEXT," +
                "status TEXT)");

        // Insert default admin
        db.execSQL("INSERT INTO " + TABLE_USERS + " (firstName, lastName, email, password, gender, major, phone, isAdmin) " +
                "VALUES ('Admin', 'Admin', 'admin@admin.com', 'Admin123!', 'Male', 'CS', '0000000', 1)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVORITES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RESERVATIONS);
        onCreate(db);
    }

    // ========== USER METHODS ==========

    public long addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("firstName", user.getFirstName());
        values.put("lastName", user.getLastName());
        values.put("email", user.getEmail());
        values.put("password", user.getPassword());
        values.put("gender", user.getGender());
        values.put("major", user.getMajor());
        values.put("phone", user.getPhone());
        values.put("profileImage", user.getProfileImage());
        values.put("isAdmin", user.isAdmin() ? 1 : 0);
        return db.insert(TABLE_USERS, null, values);
    }

    public User getUserByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE email=?", new String[]{email});
        if (cursor.moveToFirst()) {
            User user = new User(
                    cursor.getInt(0), cursor.getString(1), cursor.getString(2),
                    cursor.getString(3), cursor.getString(4), cursor.getString(5),
                    cursor.getString(6), cursor.getString(7), cursor.getString(8),
                    cursor.getInt(9) == 1);
            cursor.close();
            return user;
        }
        cursor.close();
        return null;
    }

    public boolean updateUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("firstName", user.getFirstName());
        values.put("lastName", user.getLastName());
        values.put("password", user.getPassword());
        values.put("phone", user.getPhone());
        values.put("profileImage", user.getProfileImage());
        return db.update(TABLE_USERS, values, "id=?", new String[]{String.valueOf(user.getId())}) > 0;
    }

    public List<User> getAllUsers() {
        List<User> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE isAdmin=0", null);
        while (cursor.moveToNext()) {
            list.add(new User(cursor.getInt(0), cursor.getString(1), cursor.getString(2),
                    cursor.getString(3), cursor.getString(4), cursor.getString(5),
                    cursor.getString(6), cursor.getString(7), cursor.getString(8),
                    cursor.getInt(9) == 1));
        }
        cursor.close();
        return list;
    }

    public boolean deleteUser(int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_USERS, "id=?", new String[]{String.valueOf(userId)}) > 0;
    }

    // ========== EVENT METHODS ==========

    public long addEvent(Event event) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title", event.getTitle());
        values.put("description", event.getDescription());
        values.put("category", event.getCategory());
        values.put("date", event.getDate());
        values.put("time", event.getTime());
        values.put("location", event.getLocation());
        values.put("seats", event.getSeats());
        values.put("image", event.getImage());
        return db.insert(TABLE_EVENTS, null, values);
    }

    public List<Event> getAllEvents() {
        List<Event> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_EVENTS, null);
        while (cursor.moveToNext()) {
            list.add(new Event(cursor.getInt(0), cursor.getString(1), cursor.getString(2),
                    cursor.getString(3), cursor.getString(4), cursor.getString(5),
                    cursor.getString(6), cursor.getInt(7), cursor.getString(8)));
        }
        cursor.close();
        return list;
    }

    public boolean updateEvent(Event event) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title", event.getTitle());
        values.put("description", event.getDescription());
        values.put("category", event.getCategory());
        values.put("date", event.getDate());
        values.put("time", event.getTime());
        values.put("location", event.getLocation());
        values.put("seats", event.getSeats());
        values.put("image", event.getImage());
        return db.update(TABLE_EVENTS, values, "id=?", new String[]{String.valueOf(event.getId())}) > 0;
    }

    public boolean deleteEvent(int eventId) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_EVENTS, "id=?", new String[]{String.valueOf(eventId)}) > 0;
    }

    // ========== FAVORITE METHODS ==========

    public long addFavorite(int userId, int eventId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("userId", userId);
        values.put("eventId", eventId);
        return db.insert(TABLE_FAVORITES, null, values);
    }

    public boolean removeFavorite(int userId, int eventId) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_FAVORITES, "userId=? AND eventId=?",
                new String[]{String.valueOf(userId), String.valueOf(eventId)}) > 0;
    }

    public boolean isFavorite(int userId, int eventId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT id FROM " + TABLE_FAVORITES +
                        " WHERE userId=? AND eventId=?",
                new String[]{String.valueOf(userId), String.valueOf(eventId)});
        boolean result = cursor.moveToFirst();
        cursor.close();
        return result;
    }

    public List<Event> getFavoriteEvents(int userId) {
        List<Event> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT e.* FROM " + TABLE_EVENTS + " e " +
                "JOIN " + TABLE_FAVORITES + " f ON e.id = f.eventId " +
                "WHERE f.userId=?", new String[]{String.valueOf(userId)});
        while (cursor.moveToNext()) {
            list.add(new Event(cursor.getInt(0), cursor.getString(1), cursor.getString(2),
                    cursor.getString(3), cursor.getString(4), cursor.getString(5),
                    cursor.getString(6), cursor.getInt(7), cursor.getString(8)));
        }
        cursor.close();
        return list;
    }

    // ========== RESERVATION METHODS ==========

    public long addReservation(Reservation reservation) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("userId", reservation.getUserId());
        values.put("eventId", reservation.getEventId());
        values.put("reservationDate", reservation.getReservationDate());
        values.put("quantity", reservation.getQuantity());
        values.put("type", reservation.getType());
        values.put("status", reservation.getStatus());
        return db.insert(TABLE_RESERVATIONS, null, values);
    }

    public List<Reservation> getUserReservations(int userId) {
        List<Reservation> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_RESERVATIONS + " WHERE userId=?",
                new String[]{String.valueOf(userId)});
        while (cursor.moveToNext()) {
            list.add(new Reservation(cursor.getInt(0), cursor.getInt(1), cursor.getInt(2),
                    cursor.getString(3), cursor.getInt(4), cursor.getString(5), cursor.getString(6)));
        }
        cursor.close();
        return list;
    }

    public List<Reservation> getAllReservations() {
        List<Reservation> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_RESERVATIONS, null);
        while (cursor.moveToNext()) {
            list.add(new Reservation(cursor.getInt(0), cursor.getInt(1), cursor.getInt(2),
                    cursor.getString(3), cursor.getInt(4), cursor.getString(5), cursor.getString(6)));
        }
        cursor.close();
        return list;
    }
}