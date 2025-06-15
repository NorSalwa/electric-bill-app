package com.example.electricbill;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {
    ListView listViewResults;
    DBHelper dbHelper;
    ArrayList<BillRecord> billRecords;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        listViewResults = findViewById(R.id.listViewResults);
        dbHelper = new DBHelper(this);

        billRecords = dbHelper.getAllBillRecords(); // Use custom class
        ArrayList<String> displayList = new ArrayList<>();
        for (BillRecord record : billRecords) {
            displayList.add(record.month + " - RM " + String.format("%.2f", record.finalCost));
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, displayList);
        listViewResults.setAdapter(adapter);

        listViewResults.setOnItemClickListener((parent, view, position, id) -> {
            int recordId = billRecords.get(position).id; // get id by position
            Intent intent = new Intent(HistoryActivity.this, DetailActivity.class);
            intent.putExtra("record_id", recordId);
            startActivity(intent);
        });
    }

    // Inner class to hold ID and display data
    static class BillRecord {
        int id;
        String month;
        double finalCost;

        public BillRecord(int id, String month, double finalCost) {
            this.id = id;
            this.month = month;
            this.finalCost = finalCost;
        }
    }
}
