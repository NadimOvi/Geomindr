package com.example.harish.geomindr.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    // Name of the database.
    private static final String DATABASE_NAME = "reminder.db";
    // Name of the table in the database.
    private static final String TABLE_NAME = "reminder_info_table";
    // Fields present in the table.
    private static final String COL_0 = "REMINDER_ID";
    private static final String COL_1 = "TASK_ID";
    private static final String COL_2 = "REMINDER_TYPE";
    private static final String COL_3 = "TITLE";
    private static final String COL_4 = "NAME";
    private static final String COL_5 = "NUMBER";
    private static final String COL_6 = "ARRIVAL_MESSAGE";
    private static final String COL_7 = "DEPARTURE_MESSAGE";
    private static final String COL_8 = "LOCATION_NAME";
    private static final String COL_9 = "LOCATION_LATITUDE";
    private static final String COL_10 = "LOCATION_LONGITUDE";
    private static final String COL_11 = "RADIUS";
    private static final String COL_12 = "REMINDER_STATUS";
    // Singleton instance of the database.
    private static DatabaseHelper instance = null;

    // Constructor to instantiate an object of DatabaseHelper class.
    // This will create the database if it doesn't exist.
    // The constructor is private to ensure that there is only one instance
    // of the database present in the application at a time.
    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    // Get instance of the database.
    public static DatabaseHelper getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        if (instance == null) {
            instance = new DatabaseHelper(context.getApplicationContext());
        }
        return instance;
    }

    // Method to create the table in the database.
    // This method will only run if the database file do not exist.
    // REMINDER_ID is the primary key of the database.
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_NAME + "(" +
                "REMINDER_ID INTEGER PRIMARY KEY," +
                "TASK_ID INTEGER," +
                "REMINDER_TYPE INTEGER," +
                "TITLE TEXT," +
                "NAME TEXT," +
                "NUMBER TEXT," +
                "ARRIVAL_MESSAGE TEXT," +
                "DEPARTURE_MESSAGE TEXT," +
                "LOCATION_NAME TEXT," +
                "LOCATION_LATITUDE REAL," +
                "LOCATION_LONGITUDE REAL," +
                "RADIUS INTEGER," +
                "REMINDER_STATUS INTEGER)");
    }

    // Upgrade the database (if required)
    // This method is called when version of our DB changes which means underlying table structure changes etc.
    // It will first delete the old database and then will call onCreate() method to create a new database.
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    // Method to insert record into the database.
    // It returns -1 if record is not inserted in the database.
    // It returns a number >= 0 if record is inserted in the database.
    public long insertRecord(int reminderId, int taskId, int reminderType, String title, String name, String number,
                      String arrivalMessage, String departureMessage, String locationName,
                      double locationLatitude, double locationLongitude, int radius) {
        // Get the database instance.
        SQLiteDatabase db = this.getWritableDatabase();
        // ContentValues provide an empty set of name-value pair.
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_0, reminderId);
        contentValues.put(COL_1, taskId);
        contentValues.put(COL_2, reminderType);
        contentValues.put(COL_3, title);
        contentValues.put(COL_4, name);
        contentValues.put(COL_5, number);
        contentValues.put(COL_6, arrivalMessage);
        contentValues.put(COL_7, departureMessage);
        contentValues.put(COL_8, locationName);
        contentValues.put(COL_9, locationLatitude);
        contentValues.put(COL_10, locationLongitude);
        contentValues.put(COL_11, radius);
        // Status = 0 means reminder is not triggered.
        contentValues.put(COL_12, 0);

        // Insert the record to the specified table in the database and
        // return the result of the operation.
        return db.insert(TABLE_NAME, null, contentValues);
    }

    // Method to retrieve all records from the specified table in the database.
    public Cursor getAllRecords() {
        // Get the database instance.
        SQLiteDatabase db = this.getWritableDatabase();
        // Select all records from the specified table in the database.
        return db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
    }


    // Method to update status of a reminder record.
    // Primary key, i.e, REMINDER_ID of the reminder is used for upgrading the record.
    public int updateStatus(int reminderId, int status){
        // Get the database instance.
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        // ContentValues provide an empty set of name-value pair.
        ContentValues contentValues = new ContentValues();
        // status = 1 means that the reminder has been triggered.
        contentValues.put(COL_12, status);

        // Update the specified table in the database and
        // return the result of the operation.
        return sqLiteDatabase.update(TABLE_NAME, contentValues, "REMINDER_ID = ?", new String[] {String.valueOf(reminderId)});
    }

    // Method to update TASK_ID of a reminder record.
    // Primary key, i.e, REMINDER_ID of the reminder is used for upgrading the record.
    public int updateTaskId(int reminderId, int taskId){
        // Get the database instance.
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        // ContentValues provide an empty set of name-value pair.
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1, taskId);

        // Update the specified table in the database and
        // return the result of the operation.
        return sqLiteDatabase.update(TABLE_NAME, contentValues, "REMINDER_ID = ?", new String[] {String.valueOf(reminderId)});
    }

    // Delete a record from the specified table in the database.
    // Primary key, i.e, REMINDER_ID is used to delete the record
    public long deleteTask(int reminderId) {
        // Get the database instance.
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete the record from the specified table in the database and
        // return the result of the operation.
        return db.delete(TABLE_NAME, "REMINDER_ID = ?", new String[]{String.valueOf(reminderId)});
    }
}