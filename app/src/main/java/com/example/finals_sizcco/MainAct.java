package com.example.finals_sizcco;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainAct extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_one);

        Button getStartedButton = findViewById(R.id.getStartedButton);
        Button navigateToAllowanceButton = findViewById(R.id.navigateToAllowanceButton);

        getStartedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainAct.this, GetStartedActivity.class);
                startActivity(intent);
            }
        });

        navigateToAllowanceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainAct.this, AllowanceActivity.class);
                startActivity(intent);
            }
        });
    }
}
