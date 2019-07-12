package com.example.passvault;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Vector;

public class DataBase extends SQLiteOpenHelper {
    private static final String name = "mydatabase";
    private static final int version = 1;
    private static final String table_name = "mytable";
    private static final String description_col = "description";
    private static final String username_col = "username";
    private static final String password_col = "password";

    public DataBase(Context context) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + table_name + " ( " + description_col + " VARCHAR, " + username_col + " VARCHAR, " + password_col + " VARCHAR )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + table_name);
        onCreate(db);
    }

    public void insertRecord(String desc, String uname, String pwd) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(description_col, desc);
        values.put(username_col, uname);
        values.put(password_col, pwd);

        db.insert(table_name, null, values);
        db.close();
    }

    public Vector getDescription() {
        String querry = "SELECT * FROM " + table_name;
        SQLiteDatabase db = this.getReadableDatabase();

        Vector desc_list = new Vector();
        Cursor cursor = db.rawQuery(querry, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            desc_list.add(cursor.getString(0));
            cursor.moveToNext();
        }
        db.close();
        return desc_list;
    }

    public Vector recordAt(CharSequence title) {
        String querry = "SELECT * FROM " + table_name + " WHERE description = '" + title + "'";
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(querry, null);
        cursor.moveToFirst();

        Vector vector = new Vector();
        for (int i = 0; i < 3; i++)
            vector.add(cursor.getString(i));
        return vector;
    }


    public void deleteRecord(String title) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.execSQL("DELETE FROM " + table_name + " WHERE description = '" + title +"'");
    }

    public void updatePwd(String password, String title) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.execSQL("UPDATE " + table_name + " SET " + password_col + " = '" + password + "' WHERE " + description_col + " = '" + title + "'");
    }
}
