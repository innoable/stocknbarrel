package com.innoble.stocknbarrel.model;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by At3r on 5/19/2016.
 */
public class User extends DataEntity {

    private String name;
    private String email;
    private float budget;

    public User(String name, String email, float budget) {
        super();
        this.name =  name;
        this.email = email;
        this.budget = budget;
    }

    public User (){
        super();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getBudget() {
        return budget;
    }

    public void setBudget(float budget) {
        this.budget = budget;
    }

    @Override
    public String toString()
    {
        return "\nName: " + name
                + "\n Email: " + email
                + "\n Budget: " + budget;
    }

    @Override
    public boolean equals(Object u){
        if (u instanceof User) {
            User user = (User) u;

            return (user.email.trim().toLowerCase() ==  this.email.trim().toLowerCase());
        }
        return false;

    }

    // Database table
    public static final String TABLE_USER = "user";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_BUDGET = "budget";

    // Database creation SQL statement

    private static final String DATABASE_CREATE = "create table "
            + TABLE_USER
            + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_NAME + " text,"
            + COLUMN_EMAIL + " text,"
            + COLUMN_BUDGET + " real"
            + ");";

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion,
                                 int newVersion) {
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        onCreate(database);
    }

    @Override
    public void insert (SQLiteDatabase database) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME, name);
        contentValues.put(COLUMN_EMAIL, email);
        contentValues.put(COLUMN_BUDGET, budget);
        long result = database.insert(TABLE_USER, null, contentValues);
        setId(result);
    }
}
