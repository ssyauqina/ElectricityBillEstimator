package com.example.electricitybillestimator;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // DISABLE DARK MODE

        AppCompatDelegate.setDefaultNightMode(
                AppCompatDelegate.MODE_NIGHT_NO);

        // CONNECT XML

        setContentView(R.layout.activity_about);

        // GITHUB LINK

        TextView tvGithub = findViewById(R.id.tvGithub);

        tvGithub.setOnClickListener(v -> {

            String githubUrl =
                    "https://github.com/ssyauqina/ElectricityBillEstimator";

            Intent intent = new Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(githubUrl)
            );

            startActivity(intent);
        });
    }
}