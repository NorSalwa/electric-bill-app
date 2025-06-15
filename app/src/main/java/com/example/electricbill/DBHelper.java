package com.example.electricbill;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.*;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "electricity.db";
    private static final int DB_VERSION = 1;
    private static final String TABLE_NAME = "bills";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + " (" +
                "month TEXT, " +
                "unit INTEGER, " +
                "rebate REAL, " +
                "total REAL, " +
                "final REAL)";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void insertData(String month, int unit, double rebate, double total, double finalCost) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("month", month);
        values.put("unit", unit);
        values.put("rebate", rebate);
        values.put("total", total);
        values.put("final", finalCost);
        db.insert(TABLE_NAME, null, values);
    }

    public ArrayList<String> getAllMonthsAndFinalCosts() {
        ArrayList<String> list = new ArrayList<>();
        Cursor cursor = getReadableDatabase().rawQuery("SELECT month, final FROM " + TABLE_NAME, null);
        while (cursor.moveToNext()) {
            list.add(cursor.getString(0) + " - RM " + String.format("%.2f", cursor.getDouble(1)));
        }
        cursor.close();
        return list;
    }

    public String getMonthByPosition(int position) {
        Cursor cursor = getReadableDatabase().rawQuery("SELECT month FROM " + TABLE_NAME, null);
        cursor.moveToPosition(position);
        String month = cursor.getString(0);
        cursor.close();
        return month;
    }

    public Cursor getDetailsByMonth(String month) {
        return getReadableDatabase().rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE month=?", new String[]{month});
    }

    public ArrayList<HistoryActivity.Record> getAllRecords() {
        ArrayList<HistoryActivity.Record> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        if (cursor.moveToFirst()) {
            do {
                String month = cursor.getString(cursor.getColumnIndex("month"));
                int unit = cursor.getInt(cursor.getColumnIndex("unit"));
                double rebate = cursor.getDouble(cursor.getColumnIndex("rebate"));
                double total = cursor.getDouble(cursor.getColumnIndex("total"));
                double finalCost = cursor.getDouble(cursor.getColumnIndex("final"));

                list.add(new HistoryActivity.Record(month, unit, rebate, total, finalCost));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return list;
    }
}
