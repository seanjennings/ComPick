package com.example.sean.compickmat;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Seán on 01/11/2015.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    // Table Names
    public static final String TABLE_PART = "PART";
    public static final String TABLE_CATEGORY = "CATEGORY";
    public static final String TABLE_USER = "USER";
    public static final String TABLE_BUILD = "BUILD";

    // Table columns
    public static final String _ID = "_id";
    public static final String NAME = "name";
    public static final String CAT = "category";
    public static final String DESC = "description";
    public static final String COST = "cost";
    public static final String IMG = "image";
    public static final String EMAIL = "email";
    public static final String BUDGET = "budget";
    public static final String QTY = "qty";

    // Database Information
    static final String DB_NAME = "JOURNALDEV_COMPICK.DB";

    // database version
    static final int DB_VERSION = 1;

    // Creating table queries
    private static final String CREATE_TABLE_PART = "create table " + TABLE_PART + "(" + _ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + NAME + " TEXT UNIQUE NOT NULL, " + CAT + " INTEGER, " + DESC + " TEXT, " + COST + " REAL, " + IMG + " TEXT);";
    private static final String CREATE_TABLE_CATEGORY = "create table " + TABLE_CATEGORY + "(" + _ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + NAME + " INTEGER);";
    private static final String CREATE_TABLE_USER = "create table " + TABLE_USER + "(" + _ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + NAME + " TEXT NOT NULL, " + EMAIL + " TEXT, " + BUDGET + " REAL);";
    private static final String CREATE_TABLE_BUILD = "create table " + TABLE_BUILD + "(" + _ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + EMAIL + " TEXT NOT NULL, " + NAME + " TEXT NOT NULL, " + QTY + " INTEGER);";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_PART);
        db.execSQL(CREATE_TABLE_CATEGORY);
        db.execSQL(CREATE_TABLE_USER);
        db.execSQL(CREATE_TABLE_BUILD);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PART);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BUILD);
        onCreate(db);
    }
}

