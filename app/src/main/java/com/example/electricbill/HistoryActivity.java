package com.example.electricbill;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {
    ListView listViewResults;
    DBHelper dbHelper;

    ArrayList<Record> allRecords; // custom class to hold data

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        listViewResults = findViewById(R.id.listViewResults);
        dbHelper = new DBHelper(this);

        // Get records
        allRecords = dbHelper.getAllRecords(); // <-- Update your DBHelper to return this
        ArrayList<String> displayList = new ArrayList<>();

        for (Record r : allRecords) {
            displayList.add(r.month + " - RM " + String.format("%.2f", r.finalCost));
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, displayList);
        listViewResults.setAdapter(adapter);

        listViewResults.setOnItemClickListener((parent, view, position, id) -> {
            Record selected = allRecords.get(position);
            Intent intent = new Intent(HistoryActivity.this, DetailActivity.class);
            intent.putExtra("month", selected.month);
            intent.putExtra("unit", selected.unit);
            intent.putExtra("rebate", selected.rebate);
            intent.putExtra("total", selected.total);
            intent.putExtra("finalCost", selected.finalCost);
            startActivity(intent);
        });
    }

    // Create this helper class to hold record data
    static class Record {
        String month;
        int unit;
        double rebate;
        double total;
        double finalCost;

        Record(String month, int unit, double rebate, double total, double finalCost) {
            this.month = month;
            this.unit = unit;
            this.rebate = rebate;
            this.total = total;
            this.finalCost = finalCost;
        }
    }
}
