package com.example.finals_sizcco;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.VideoView;
import androidx.appcompat.app.AppCompatActivity;

public class GetStartedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.get_started_activity_layout);

        // Initialize VideoView and Button
        VideoView getStartedVideo = findViewById(R.id.getStartedVideo);
        Uri videoUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.getstartedvideo);
        getStartedVideo.setVideoURI(videoUri);
        getStartedVideo.start();

        // Loop the video
        getStartedVideo.setOnPreparedListener(mp -> mp.setLooping(true));

        // Navigate to AllowanceActivity on button click
        Button getStartedButton = findViewById(R.id.getStartedButton);
        getStartedButton.setOnClickListener(v -> {
            Intent intent = new Intent(GetStartedActivity.this, AllowanceActivity.class);
            startActivity(intent);
        });

        // Navigate to LoginActivity when "Already have an account?" is clicked
        TextView alreadyHaveAccount = findViewById(R.id.alreadyHaveAccount);
        alreadyHaveAccount.setOnClickListener(v -> {
            Intent intent = new Intent(GetStartedActivity.this, LoginActivity.class);
            startActivity(intent);
        });
    }
}
