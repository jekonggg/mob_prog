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

        VideoView getStartedVideo = findViewById(R.id.getStartedVideo);

        Uri videoUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.getstartedvideo);
        getStartedVideo.setVideoURI(videoUri);

        getStartedVideo.start();

        getStartedVideo.setOnPreparedListener(mp -> mp.setLooping(true));

        Button getStartedButton = findViewById(R.id.getStartedButton);

        getStartedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GetStartedActivity.this, AllowanceActivity.class);
                startActivity(intent);
            }
        });
    }
}

