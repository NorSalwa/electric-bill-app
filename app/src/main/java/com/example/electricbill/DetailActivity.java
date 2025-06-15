package com.example.electricbill;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class DetailActivity extends AppCompatActivity {
    TextView tvDetails;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        tvDetails = findViewById(R.id.tvDetails);
        dbHelper = new DBHelper(this);

        int recordId = getIntent().getIntExtra("record_id", -1);

        if (recordId != -1) {
            Cursor cursor = dbHelper.getDetailsById(recordId);
            if (cursor.moveToFirst()) {
                String month = cursor.getString(cursor.getColumnIndex("month"));
                int unit = cursor.getInt(cursor.getColumnIndex("unit"));
                double rebate = cursor.getDouble(cursor.getColumnIndex("rebate"));
                double total = cursor.getDouble(cursor.getColumnIndex("total"));
                double finalCost = cursor.getDouble(cursor.getColumnIndex("final"));

                String detail = "Month: " + month + "\nUnit: " + unit + "kWh\nRebate: " + rebate +
                        "%\nTotal Charges: RM " + String.format("%.2f", total) +
                        "\nFinal Cost: RM " + String.format("%.2f", finalCost);

                tvDetails.setText(detail);
            } else {
                Toast.makeText(this, "No data found.", Toast.LENGTH_SHORT).show();
            }
            cursor.close();
        } else {
            Toast.makeText(this, "Invalid record selected.", Toast.LENGTH_SHORT).show();
        }
    }
}
