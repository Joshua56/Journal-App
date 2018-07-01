package com.joshua.journalapp.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class NoteDbHelper extends SQLiteOpenHelper {
    // The database name
    private static final String DATABASE_NAME = "notelist.db";
    // If you change the database schema, you must increment the database version
    private static final int DATABASE_VERSION = 1;
    SQLiteDatabase sqLiteDatabase;

    // Constructor
    public NoteDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Create a table to hold Notelist data
        final String SQL_CREATE_NOTELIST_TABLE = "CREATE TABLE " + NoteContract.NotelistEntry.TABLE_NAME + " (" +
                NoteContract.NotelistEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                NoteContract.NotelistEntry.COLUMN_DETAILS + " TEXT NOT NULL " +
                "); ";

        //  Execute the query by calling execSQL on sqLiteDatabase and pass the string query SQL_CREATE_NOTELABLE
        sqLiteDatabase.execSQL(SQL_CREATE_NOTELIST_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + NoteContract.NotelistEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public long addNote(String detials) {
        ContentValues cv = new ContentValues();

        cv.put(NoteContract.NotelistEntry.COLUMN_DETAILS, detials);
        return sqLiteDatabase.insert(NoteContract.NotelistEntry.TABLE_NAME, null, cv);


    }

}

