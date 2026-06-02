package com.example.electricitybillestimator;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class DetailActivity extends AppCompatActivity {

    TextView tvDetailMonth,
            tvDetailTotal,
            tvDetailFinal;

    EditText etDetailUnit,
            etDetailRebate;

    Button btnUpdate,
            btnDelete;

    String billId;
    String month;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        AppCompatDelegate.setDefaultNightMode(
                AppCompatDelegate.MODE_NIGHT_NO);

        setContentView(R.layout.activity_detail);

        // CONNECT XML

        tvDetailMonth =
                findViewById(R.id.tvDetailMonth);

        tvDetailTotal =
                findViewById(R.id.tvDetailTotal);

        tvDetailFinal =
                findViewById(R.id.tvDetailFinal);

        etDetailUnit =
                findViewById(R.id.etDetailUnit);

        etDetailRebate =
                findViewById(R.id.etDetailRebate);

        btnUpdate =
                findViewById(R.id.btnUpdate);

        btnDelete =
                findViewById(R.id.btnDelete);

        // GET INTENT DATA

        billId =
                getIntent().getStringExtra("id");

        month =
                getIntent().getStringExtra("month");

        int unit =
                getIntent().getIntExtra(
                        "unit",
                        0);

        int rebate =
                getIntent().getIntExtra(
                        "rebate",
                        0);

        double totalCharges =
                getIntent().getDoubleExtra(
                        "totalCharges",
                        0);

        double finalCost =
                getIntent().getDoubleExtra(
                        "finalCost",
                        0);

        // DISPLAY DATA

        tvDetailMonth.setText(
                "Month : " + month);

        etDetailUnit.setText(
                String.valueOf(unit));

        etDetailRebate.setText(
                String.valueOf(rebate));

        tvDetailTotal.setText(
                "Total Charges : RM " +
                        String.format("%.2f",
                                totalCharges));

        tvDetailFinal.setText(
                "Final Cost : RM " +
                        String.format("%.2f",
                                finalCost));

        // UPDATE BUTTON

        btnUpdate.setOnClickListener(v -> {

            updateRecord();
        });

        // DELETE BUTTON

        btnDelete.setOnClickListener(v -> {

            FirebaseDatabase.getInstance()
                    .getReference("Bills")
                    .child(billId)
                    .removeValue();

            Toast.makeText(
                    this,
                    "Record Deleted",
                    Toast.LENGTH_SHORT).show();

            finish();
        });
    }

    // UPDATE RECORD FUNCTION

    private void updateRecord() {

        String unitText =
                etDetailUnit.getText().toString();

        String rebateText =
                etDetailRebate.getText().toString();

        // VALIDATION

        if(unitText.isEmpty()) {

            etDetailUnit.setError(
                    "Enter electricity unit");

            return;
        }

        if(rebateText.isEmpty()) {

            etDetailRebate.setError(
                    "Enter rebate percentage");

            return;
        }

        int unit =
                Integer.parseInt(unitText);

        int rebate =
                Integer.parseInt(rebateText);

        if(unit < 1 || unit > 1000) {

            etDetailUnit.setError(
                    "Unit must be between 1 - 1000");

            return;
        }

        if(rebate < 0 || rebate > 5) {

            etDetailRebate.setError(
                    "Rebate must be between 0 - 5");

            return;
        }

        // CALCULATE TOTAL CHARGES

        double totalCharges;

        if(unit <= 200) {

            totalCharges =
                    unit * 0.218;
        }

        else if(unit <= 300) {

            totalCharges =
                    (200 * 0.218) +
                            ((unit - 200) * 0.334);
        }

        else if(unit <= 600) {

            totalCharges =
                    (200 * 0.218) +
                            (100 * 0.334) +
                            ((unit - 300) * 0.516);
        }

        else {

            totalCharges =
                    (200 * 0.218) +
                            (100 * 0.334) +
                            (300 * 0.516) +
                            ((unit - 600) * 0.546);
        }

        // FINAL COST

        double finalCost =
                totalCharges -
                        (totalCharges * rebate / 100.0);

        // UPDATE FIREBASE

        HashMap<String, Object> map =
                new HashMap<>();

        map.put("month", month);
        map.put("unit", unit);
        map.put("rebate", rebate);
        map.put("totalCharges", totalCharges);
        map.put("finalCost", finalCost);
        map.put("id", billId);

        FirebaseDatabase.getInstance()
                .getReference("Bills")
                .child(billId)
                .updateChildren(map);

        // UPDATE DISPLAY

        tvDetailTotal.setText(
                "Total Charges : RM " +
                        String.format("%.2f",
                                totalCharges));

        tvDetailFinal.setText(
                "Final Cost : RM " +
                        String.format("%.2f",
                                finalCost));

        Toast.makeText(
                this,
                "Record Updated Successfully",
                Toast.LENGTH_SHORT).show();
    }
}