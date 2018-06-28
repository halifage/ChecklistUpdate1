package com.halifapps.listviewtest.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.halifapps.listviewtest.data.ChecklistNotesContract.Checklist.COLUMN_CHECKED;
import static com.halifapps.listviewtest.data.ChecklistNotesContract.Checklist.COLUMN_NOTE;
import static com.halifapps.listviewtest.data.ChecklistNotesContract.Checklist.COLUMN_TITLE;
import static com.halifapps.listviewtest.data.ChecklistNotesContract.Checklist.COLUMN_UNCHECKED;
import static com.halifapps.listviewtest.data.ChecklistNotesContract.Checklist.TABLE_NAME;
import static com.halifapps.listviewtest.data.ChecklistNotesContract.Checklist._ID;

public class ChecklistDBHelper extends SQLiteOpenHelper {


    public static final String DATABASE_NAME = "checklist.db";
    public static final String CREATE_CHECKLIST_TABLE = "CREATE TABLE " + TABLE_NAME + "("
            + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_UNCHECKED + " TEXT, "
            + COLUMN_CHECKED + " TEXT,"
            + COLUMN_TITLE + " TEXT,"
            + COLUMN_NOTE + " TEXT);";
    public static int DATABASE_VERSION = 2;

    public static final String QUERY = "SELECT * FROM " + TABLE_NAME;

    public static final String DROP_TABLE = "DROP TABLE IF EXISTS ";

    public ChecklistDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_CHECKLIST_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE + TABLE_NAME);
        onCreate(db);
    }

    public boolean delete(String text, SQLiteDatabase db) {
        return db.delete(TABLE_NAME, COLUMN_UNCHECKED + " LIKE " + text, null) > 0;
    }

}
