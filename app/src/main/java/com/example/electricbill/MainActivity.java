package com.example.electricbill;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    Spinner spinnerMonth;
    EditText editTextUnit, editTextRebate;
    TextView textTotalCharges, textFinalCost;
    Button btnCalculate, btnViewHistory;
    ImageButton btnAbout;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Link UI
        spinnerMonth = findViewById(R.id.spinnerMonth);
        editTextUnit = findViewById(R.id.editTextUnit);
        editTextRebate = findViewById(R.id.editTextRebate);
        textTotalCharges = findViewById(R.id.textTotalCharges);
        textFinalCost = findViewById(R.id.textFinalCost);
        btnCalculate = findViewById(R.id.btnCalculate);
        ImageButton btnAbout = findViewById(R.id.btnAbout);
        btnViewHistory = findViewById(R.id.btnViewHistory);
        dbHelper = new DBHelper(this);

        // Setup Spinner with months
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.months, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMonth.setAdapter(adapter);

        // Buttons
        btnCalculate.setOnClickListener(v -> calculateAndSave());

        btnAbout.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AboutActivity.class);
            startActivity(intent);
        });

        btnViewHistory.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Clear input fields
        editTextUnit.setText("");
        editTextRebate.setText("");
        textTotalCharges.setText("Total Charges: RM 0.00");
        textFinalCost.setText("Final Cost: RM 0.00");

        // Reset Spinner to first month
        if (spinnerMonth != null) {
            spinnerMonth.setSelection(0);
        }
    }


    private void calculateAndSave() {
        // Validation
        if (editTextUnit.getText().toString().isEmpty() || editTextRebate.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please enter unit and rebate.", Toast.LENGTH_SHORT).show();
            return;
        }

        String month = spinnerMonth.getSelectedItem().toString();
        int unit = Integer.parseInt(editTextUnit.getText().toString());
        double rebate = Double.parseDouble(editTextRebate.getText().toString());

        if (rebate < 0 || rebate > 5) {
            Toast.makeText(this, "Rebate must be between 0% and 5%", Toast.LENGTH_SHORT).show();
            return;
        }

        // Tariff calculation
        double total = 0;
        if (unit <= 200) total = unit * 0.218;
        else if (unit <= 300) total = 200 * 0.218 + (unit - 200) * 0.334;
        else if (unit <= 600) total = 200 * 0.218 + 100 * 0.334 + (unit - 300) * 0.516;
        else total = 200 * 0.218 + 100 * 0.334 + 300 * 0.516 + (unit - 600) * 0.546;

        double finalCost = total - (total * rebate / 100);

        // Show results
        textTotalCharges.setText(String.format("Total Charges: RM %.2f", total));
        textFinalCost.setText(String.format("Final Cost: RM %.2f", finalCost));

        // Save to DB
        dbHelper.insertData(month, unit, rebate, total, finalCost);


        editTextUnit.setText("");
        editTextRebate.setText("");
        spinnerMonth.setSelection(0);
    }

}
