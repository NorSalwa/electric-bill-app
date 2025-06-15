package com.example.electricbill;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;

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

        String month = getIntent().getStringExtra("month");
        Cursor c = dbHelper.getDetailsByMonth(month);

        if (c.moveToFirst()) {
            String result = "Month: " + c.getString(0) + "\nUnits: " + c.getInt(1) + "kWh\n" +
                    "Rebate: " + c.getDouble(2) + "%\nTotal Charges: RM " +
                    String.format("%.2f", c.getDouble(3)) + "\nFinal Cost: RM " +
                    String.format("%.2f", c.getDouble(4));
            tvDetails.setText(result);
        } else {
            tvDetails.setText("No data found.");
        }

        c.close();
    }
}
