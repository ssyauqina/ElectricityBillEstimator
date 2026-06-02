package com.example.electricitybillestimator;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    Spinner spMonth;
    EditText etUnit;
    SeekBar seekRebate;

    TextView tvRebate, tvTotalCharges, tvFinalCost;

    Button btnCalculate,
            btnReset,
            btnSave,
            btnHistory,
            btnAbout;

    DatabaseReference databaseReference;

    double totalCharges = 0;
    double finalCost = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        AppCompatDelegate.setDefaultNightMode(
                AppCompatDelegate.MODE_NIGHT_NO);

        setContentView(R.layout.activity_main);

        // CONNECT XML

        spMonth = findViewById(R.id.spMonth);

        etUnit = findViewById(R.id.etUnit);

        seekRebate = findViewById(R.id.seekRebate);

        tvRebate = findViewById(R.id.tvRebate);

        tvTotalCharges =
                findViewById(R.id.tvTotalCharges);

        tvFinalCost =
                findViewById(R.id.tvFinalCost);

        btnCalculate =
                findViewById(R.id.btnCalculate);

        btnReset =
                findViewById(R.id.btnReset);

        btnSave =
                findViewById(R.id.btnSave);

        btnHistory =
                findViewById(R.id.btnHistory);

        btnAbout =
                findViewById(R.id.btnAbout);

        // FIREBASE DATABASE

        databaseReference =
                FirebaseDatabase.getInstance()
                        .getReference("Bills");

        // MONTH SPINNER

        String[] months = {

                "January",
                "February",
                "March",
                "April",
                "May",
                "June",
                "July",
                "August",
                "September",
                "October",
                "November",
                "December"
        };

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(

                        this,
                        android.R.layout.simple_spinner_dropdown_item,
                        months
                );

        spMonth.setAdapter(adapter);

        // SEEK BAR

        seekRebate.setOnSeekBarChangeListener(

                new SeekBar.OnSeekBarChangeListener() {

                    @Override
                    public void onProgressChanged(
                            SeekBar seekBar,
                            int progress,
                            boolean fromUser) {

                        tvRebate.setText(progress + "%");
                    }

                    @Override
                    public void onStartTrackingTouch(
                            SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(
                            SeekBar seekBar) {

                    }
                });

        // CALCULATE BUTTON

        btnCalculate.setOnClickListener(

                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        calculateBill();
                    }
                });

        // RESET BUTTON

        btnReset.setOnClickListener(

                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        // RESET MONTH

                        spMonth.setSelection(0);

                        // RESET UNIT

                        etUnit.setText("");

                        // RESET REBATE

                        seekRebate.setProgress(0);

                        tvRebate.setText("0%");

                        // RESET RESULT

                        tvTotalCharges.setText(
                                "Total Charges : RM 0.00");

                        tvFinalCost.setText(
                                "Final Cost : RM 0.00");

                        Toast.makeText(
                                MainActivity.this,
                                "Form Reset Successfully",
                                Toast.LENGTH_SHORT).show();
                    }
                });

        // SAVE BUTTON

        btnSave.setOnClickListener(

                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        saveData();
                    }
                });

        // HISTORY BUTTON

        btnHistory.setOnClickListener(

                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        Intent intent =
                                new Intent(
                                        MainActivity.this,
                                        HistoryActivity.class);

                        startActivity(intent);
                    }
                });

        // ABOUT BUTTON

        btnAbout.setOnClickListener(

                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        Intent intent =
                                new Intent(
                                        MainActivity.this,
                                        AboutActivity.class);

                        startActivity(intent);
                    }
                });
    }

    // CALCULATE FUNCTION

    private void calculateBill() {

        String unitText =
                etUnit.getText().toString();

        // VALIDATION

        if(unitText.isEmpty()) {

            etUnit.setError(
                    "Please enter electricity unit");

            return;
        }

        int unit =
                Integer.parseInt(unitText);

        if(unit < 1 || unit > 1000) {

            etUnit.setError(
                    "Unit must be between 1 and 1000");

            return;
        }

        // GET REBATE

        int rebate =
                seekRebate.getProgress();

        // CALCULATE TOTAL CHARGES

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

        finalCost =

                totalCharges -

                        (totalCharges *
                                rebate / 100.0);

        // DISPLAY RESULT

        tvTotalCharges.setText(

                "Total Charges : RM " +

                        String.format("%.2f",
                                totalCharges));

        tvFinalCost.setText(

                "Final Cost : RM " +

                        String.format("%.2f",
                                finalCost));

        Toast.makeText(

                this,

                "Calculation Successful",

                Toast.LENGTH_SHORT).show();
    }

    // SAVE DATA FUNCTION

    private void saveData() {

        String unitText =
                etUnit.getText().toString();

        // VALIDATION

        if(unitText.isEmpty()) {

            etUnit.setError(
                    "Please calculate first");

            return;
        }

        // FIREBASE ID

        String id =
                databaseReference.push().getKey();

        // GET VALUES

        String month =
                spMonth.getSelectedItem().toString();

        int unit =
                Integer.parseInt(unitText);

        int rebate =
                seekRebate.getProgress();

        // CREATE MODEL

        BillModel model =

                new BillModel(

                        id,

                        month,

                        unit,

                        rebate,

                        totalCharges,

                        finalCost
                );

        // SAVE TO FIREBASE

        databaseReference
                .child(id)
                .setValue(model);

        Toast.makeText(

                this,

                "Record Saved Successfully",

                Toast.LENGTH_SHORT).show();
    }
}