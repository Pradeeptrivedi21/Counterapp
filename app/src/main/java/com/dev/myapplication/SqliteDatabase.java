package com.dev.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class SqliteDatabase extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 5;
    private static final String DATABASE_NAME = "Counter";
    private static final String TABLE_TASK = "Countertask";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_NAME = "Name";
    private static final String COLUMN_DESCRIPTION = "Description";
    private static final String COLUMN_DURATION = "RemainingDuration";
    private static final String COLUMN_DATE = "CDate";
    private static final String COLUMN_TIME = "CTime";
    SqliteDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE "
                + TABLE_TASK + "(" + COLUMN_ID
                + " INTEGER PRIMARY KEY,"
                + COLUMN_NAME + " TEXT,"
                + COLUMN_DESCRIPTION + " TEXT,"
                + COLUMN_DATE + " TEXT,"
                + COLUMN_TIME + " TEXT,"
                + COLUMN_DURATION + " TEXT"
                + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASK);
        onCreate(db);
    }
    ArrayList<Contacts> listContacts() {
        String sql = "select * from " + TABLE_TASK;
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Contacts> storeContacts = new ArrayList<>();
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            do {
                int id = Integer.parseInt(cursor.getString(0));
                String name = cursor.getString(1);
                String phno = cursor.getString(2);
                String date = cursor.getString(3);
                String time = cursor.getString(4);
                String duration = cursor.getString(5);
                storeContacts.add(new Contacts(id, name, phno,duration,date,time));
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return storeContacts;
    }
    void addContacts(Contacts contacts) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, contacts.getName());
        values.put(COLUMN_DESCRIPTION, contacts.getDescription());
        values.put(COLUMN_DATE, contacts.getDate());
        values.put(COLUMN_TIME, contacts.getTime());
        values.put(COLUMN_DURATION, contacts.getDuratiom());
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_TASK, null, values);
    }
    void updateContacts(Contacts contacts) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, contacts.getName());
        values.put(COLUMN_DESCRIPTION, contacts.getDescription());
        values.put(COLUMN_DURATION, contacts.getDuratiom());
        SQLiteDatabase db = this.getWritableDatabase();
        db.update(TABLE_TASK, values, COLUMN_ID + " = ?", new String[]{String.valueOf(contacts.getId())});
    }
    void deleteContact(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TASK, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
    }
}
