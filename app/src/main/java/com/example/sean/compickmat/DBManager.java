package com.example.sean.compickmat;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteCantOpenDatabaseException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by Seán on 01/11/2015.
 */
public class DBManager {

    private DatabaseHelper dbHelper;

    private Context context;

    private SQLiteDatabase database;

    public DBManager(Context c) {
        context = c;
    }

    public DBManager open() {
        try {
            dbHelper = new DatabaseHelper(context);
            database = dbHelper.getWritableDatabase();
        } catch (SQLiteCantOpenDatabaseException e) {
            Log.e("DBManager: ", "Can't open database!", e);
        }
        checkSetup();
        return this;
    }

    public void close() {
        try {
            dbHelper.close();
        } catch (SQLException e) {
            Log.e("DBManager: ", "Can't close database!", e);
        }
    }

    //Populate database if empty
    public void checkSetup() {
        if(!tableDataExists(DatabaseHelper.TABLE_PART)) {
            try {
                insertPart("Intel Core i7-6700K", 1, "Speed: 4.0GHz, Cores: 4, TDP: 91W", 450, "i7");
                insertPart("Intel Core i5-6600K", 1, "Speed: 3.5GHz, Cores: 4, TDP: 91W", 300, "i5");
                insertPart("Intel Core i5-6600", 1, "Speed: 3.3GHz, Cores: 4, TDP: 65W", 240, "i5");
                insertPart("Intel Core i7-6700", 1, "Speed: 3.4GHz, Cores: 4, TDP: 65W", 405, "i7");

                insertPart("Noctua NH-D14", 2, "Fan RPM:900 - 1200\nNoise (dbA): 12.6 - 19.8", 80, "d14");
                insertPart("Noctua NH-D15", 2, "Fan RPM:300 - 1500\nNoise (dbA): 19.2 - 24.6", 90, "d15");
                insertPart("Corsair H110", 2, "Fan RPM:1500\nNoise (dbA): 35", 140, "h110");
                insertPart("Cooler Master Hyper 212 EVO", 2, "Fan RPM: 600 - 2000\nNoise (dbA) 9.0 - 36.0", 30, "cm212");

                insertPart("Asus MAXIMUS VIII HERO", 3, "Socket: LGA1151\nForm Factor: ATX", 230, "asusv3");
                insertPart("Asus Z170-A", 3, "Socket: LGA1150\nForm Factor: ATX", 200, "asusz170");
                insertPart("MSI H81M-P33", 3, "Socket: LGA1150\nForm Factor: Micro ATX", 40, "msih81m");
                insertPart("Gigabyte GA-H97M-D3H", 3, "Socket: LGA1150\nForm Factor: Micro ATX", 92, "gigh97");

                insertPart("G.Skill Sniper Series", 4, "Speed: DDR3-2133\nSize: 8GB", 45, "gskill");
                insertPart("Corsair Vengeance Pro", 4, "Speed: DDR3-1866\nSize: 16GB", 84, "corven");
                insertPart("Crucial Ballistix Sport", 4, "Speed: DDR3-1600\nSize: 8GB", 40, "crucball");

                insertPart("Kingston SV300S37A/120G", 5, "Type: SSD\nSize: 120GB", 53, "kingsv");
                insertPart("Samsung 850 EVO MZ-75E250B", 5, "Type: SSD,\nSize: 250GB", 91, "sam850");
                insertPart("Seagate ST1000DX001", 5, "Type: Hybrid\nSize: 1TB", 81, "seast");

                insertPart("MSI GTX 970", 6, "Memory: 4GB\nCore Clock: 1.14Ghz", 400, "gtx970");
                insertPart("Asus STRIX GTX980", 6, "Memory: 4GB\nCore Clock: 1.18Ghz", 560, "gtx980");
                insertPart("MSI R9 380", 6, "Memory: 4GB\nCore Clock: 970MHz", 270, "r9380");
                insertPart("MSI R9 390X", 6, "Memory: 8GB\nCore Clock: 1.05Ghz", 490, "r9390x");

                insertPart("Fractal Design Define R4", 7, "Type: ATX Mid Tower", 120, "r4");
                insertPart("Corsair 750D", 7, "Type: ATX Mid Tower", 175, "c750d");
                insertPart("NZXT H440", 7, "Type: ATX Full Tower", 120, "h440");
                insertPart("Cooler Master HAF 912", 7, "Type: ATX Mid Tower", 100, "haf912");

                insertPart("EVGA SuperNOVA 750", 8, "Efficiency: 80+ Gold\nWatts: 750W", 120, "esn750");
                insertPart("Corsair AX760", 8, "Efficiency: 80+ Platinum\nWatts: 760W", 200, "ax760");
                insertPart("Corsair CX600M", 8, "Efficiency: 80+ Bronze\nWatts: 600W", 90, "cxm600");
                insertPart("EVGA SuperNOVA 650", 8, "Efficiency: 80+ Gold\nWatts: 650W", 110, "esn650");
            } catch (SQLException e) {
                Log.e("DBManager: ", "Error inserting default app data.", e);
            }
        }
    }

    public void insertPart(String name, int category, String desc, double cost, String img) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseHelper.NAME, name);
        contentValue.put(DatabaseHelper.CAT, category);
        contentValue.put(DatabaseHelper.DESC, desc);
        contentValue.put(DatabaseHelper.COST, cost);
        contentValue.put(DatabaseHelper.IMG, img);

        try {
            database.insert(DatabaseHelper.TABLE_PART, null, contentValue);
        } catch (SQLException e) {
            Log.e("DBManager: ", "Error inserting part: " + name, e);
        }
    }

    //Reset a user's build
    public void resetBuild(String email) {
        database.delete(DatabaseHelper.TABLE_BUILD, DatabaseHelper.EMAIL + " = \"" + email + "\"", null);
    }

    //Create/Update a user's build
    public void updateBuildPart(String email, String name, int qty) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseHelper.QTY, qty);
        contentValue.put(DatabaseHelper.NAME, name);
        contentValue.put(DatabaseHelper.EMAIL, email);

        //get part data from user's build
        Cursor bCursor = database.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_BUILD + " WHERE email = \""+email+"\" AND name = \""+name+"\"", null);

        if (bCursor.moveToFirst())
        {
            //update
            try {
                database.update(DatabaseHelper.TABLE_BUILD, contentValue, "name = " + name + " AND email = " + email, null);
            } catch (SQLException e) {
                Log.e("DBManager: ", "Error updating part information.", e);
            }
        } else {
            //insert
            try {
                database.insert(DatabaseHelper.TABLE_BUILD, null, contentValue);
                //dbHelper.close();
            } catch (SQLException e) {
                Log.e("DBManager: ", "Error inserting part information.", e);
            }
        }
    }

    //Load a registered user's pre-existing build
    public ChosenParts loadExistingUser(String email) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseHelper.EMAIL, email);

        Cursor bCursor = database.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_BUILD + " WHERE email = \""+email+"\"", null);
        ChosenParts list = new ChosenParts();

        if (bCursor != null) {
            bCursor.moveToFirst();

            //Load the build parts relevant to this user into the app's ChosenParts list
            do {
                String name = bCursor.getString(bCursor.getColumnIndex("name")); //get part name
                int qty = bCursor.getInt(bCursor.getColumnIndex("qty"));

                //get part data from database
                Cursor pCursor = database.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_PART + " WHERE name = \"" + name + "\"", null);
                pCursor.moveToFirst();
                double cost = Double.parseDouble(pCursor.getString(pCursor.getColumnIndex("cost")));
                String desc = pCursor.getString(pCursor.getColumnIndex("description"));
                String img = pCursor.getString(pCursor.getColumnIndex("image"));
                String cat = pCursor.getString(pCursor.getColumnIndex("category"));
                Part p = new Part(name, cost, desc, img, cat);
                //System.out.println(name+" "+cost+" "+desc+" "+img+" "+cat);

                //Add part to ChosenParts list
                for(int i = 0 ; i < qty ; i++) {
                    list.addPart(p);
                }
            } while (bCursor.moveToNext());
        }
        return list;
    }

    //Used alongside loadExistingUser to load their budget back into the app
    public double loadExistingBudget(String email) {
        Cursor uCursor = database.rawQuery("SELECT budget FROM " + DatabaseHelper.TABLE_USER + " WHERE email = \""+email+"\"", null);
        uCursor.moveToFirst();
        return Double.parseDouble(uCursor.getString(uCursor.getColumnIndex("budget")));
    }

    //Insert data for newly registered user
    public void insertUser(String name, String email, double budget) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseHelper.NAME, name);
        contentValue.put(DatabaseHelper.EMAIL, email);
        contentValue.put(DatabaseHelper.BUDGET, budget);

        try {
            database.insert(DatabaseHelper.TABLE_USER, null, contentValue);
        } catch (SQLException e) {
            Log.e("DBManager: ", "Error creating new user.", e);
        }
    }

    //Update a user entry
    public void updateUser(String name, String email, double budget) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseHelper.NAME, name);
        contentValue.put(DatabaseHelper.EMAIL, email);
        contentValue.put(DatabaseHelper.BUDGET, budget);

        try {
            database.update(DatabaseHelper.TABLE_USER, contentValue, "WHERE email = \"" + email + "\"", null);
        } catch (SQLException e) {
            Log.e("DBManager: ", "Error updating user.", e);
        }
    }

    //Fetch a parts cursor based on the spinner from the UI
    public Cursor fetchParts(String category) {
        String[] columns = new String[] { DatabaseHelper._ID, DatabaseHelper.NAME, DatabaseHelper.DESC, DatabaseHelper.IMG, DatabaseHelper.COST, DatabaseHelper.CAT};
        Cursor cursor = null;
        try {
            switch(category) {
                case "CPU":             cursor = database.query(DatabaseHelper.TABLE_PART, columns, "category=1", null, null, null, null);
                                        break;
                case "CPU Cooler":      cursor = database.query(DatabaseHelper.TABLE_PART, columns, "category=2", null, null, null, null);
                                        break;
                case "Motherboard":     cursor = database.query(DatabaseHelper.TABLE_PART, columns, "category=3", null, null, null, null);
                                        break;
                case "Memory":          cursor = database.query(DatabaseHelper.TABLE_PART, columns, "category=4", null, null, null, null);
                                        break;
                case "Storage":         cursor = database.query(DatabaseHelper.TABLE_PART, columns, "category=5", null, null, null, null);
                                        break;
                case "Video Card":      cursor = database.query(DatabaseHelper.TABLE_PART, columns, "category=6", null, null, null, null);
                                        break;
                case "Case":            cursor = database.query(DatabaseHelper.TABLE_PART, columns, "category=7", null, null, null, null);
                                        break;
                case "Power Supply":    cursor = database.query(DatabaseHelper.TABLE_PART, columns, "category=8", null, null, null, null);
                                        break;
            }
            if (cursor != null) {
                cursor.moveToFirst();
            }
        } catch (SQLException e) {
            Log.e("DBManager: ", "", e);
        }
        return cursor;
    }

    //Check for empty table
    public boolean tableDataExists(String table) {
        Cursor mCursor = database.rawQuery("SELECT * FROM " + table, null);
        Boolean rowExists;

        if (mCursor.moveToFirst()) {
            rowExists = true;
        } else {
            rowExists = false;
        }
        return rowExists;
    }

    //Check for empty table
    public boolean userExists(String email) {
        Cursor mCursor = database.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_USER + " WHERE email = \"" + email + "\"", null);
        Boolean userExists;

        if (mCursor.moveToFirst()) {
            userExists = true;
        } else {
            userExists = false;
        }
        return userExists;
    }
}

