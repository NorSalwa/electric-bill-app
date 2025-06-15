package com.example.electricbill;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {
    ListView listViewResults;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        listViewResults = findViewById(R.id.listViewResults);
        dbHelper = new DBHelper(this);

        ArrayList<String> records = dbHelper.getAllMonthsAndFinalCosts();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, records);
        listViewResults.setAdapter(adapter);

        listViewResults.setOnItemClickListener((parent, view, position, id) -> {
            String month = dbHelper.getMonthByPosition(position);
            Intent intent = new Intent(HistoryActivity.this, DetailActivity.class);
            intent.putExtra("month", month);
            startActivity(intent);
        });
    }
}
