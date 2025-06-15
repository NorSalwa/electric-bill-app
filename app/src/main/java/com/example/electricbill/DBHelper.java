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
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "month TEXT, unit INTEGER, rebate REAL, total REAL, final REAL)";
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

    public ArrayList<String> getAllDisplayRows() {
        ArrayList<String> list = new ArrayList<>();
        Cursor cursor = getReadableDatabase().rawQuery("SELECT id, month, final FROM " + TABLE_NAME, null);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String display = "ID " + id + " | " + cursor.getString(1) + " - RM " + String.format("%.2f", cursor.getDouble(2));
            list.add(display);
        }
        cursor.close();
        return list;
    }

    public Cursor getDetailsById(int id) {
        return getReadableDatabase().rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE id=?", new String[]{String.valueOf(id)});
    }

    public ArrayList<HistoryActivity.BillRecord> getAllBillRecords() {
        ArrayList<HistoryActivity.BillRecord> list = new ArrayList<>();
        Cursor cursor = getReadableDatabase().rawQuery("SELECT id, month, final FROM bills", null);

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            String month = cursor.getString(cursor.getColumnIndex("month"));
            double finalCost = cursor.getDouble(cursor.getColumnIndex("final"));
            list.add(new HistoryActivity.BillRecord(id, month, finalCost));
        }

        cursor.close();
        return list;
    }

}
