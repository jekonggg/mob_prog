package com.example.finals_sizcco;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.VideoView;
import androidx.appcompat.app.AppCompatActivity;

public class GetStartedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.get_started_activity_layout);

        // Setup the VideoView to play the video
        VideoView getStartedVideo = findViewById(R.id.getStartedVideo);
        Uri videoUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.getstartedvideo);
        getStartedVideo.setVideoURI(videoUri);
        getStartedVideo.start();
        getStartedVideo.setOnPreparedListener(mp -> mp.setLooping(true));

        // Setup "Get Started" button
        Button getStartedButton = findViewById(R.id.getStartedButton);
        getStartedButton.setOnClickListener(v -> {
            Intent intent = new Intent(GetStartedActivity.this, AllowanceActivity.class);
            startActivity(intent);
        });

        // Setup "Already have an account?" button
        Button alreadyHaveAccountButton = findViewById(R.id.alreadyHaveAccountButton);
        alreadyHaveAccountButton.setOnClickListener(v -> {
            // Redirect to the LoginActivity
            Intent intent = new Intent(GetStartedActivity.this, LoginActivity.class);
            startActivity(intent);
        });
    }
}
