package com.android.gjprojection.roveedoll.features.tutorial;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

import com.android.gjprojection.roveedoll.R;

public class VideoPlayer extends AppCompatActivity {
    /**
     * Video
     */
    private VideoView tutorialVideo;
    private int position = 0;
    private MediaController mediaController;
    int video;
    String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

        Intent intent = getIntent();
        video = intent.getIntExtra("path", -1);
        title = intent.getStringExtra("title");
        setTitle(title);

        //Video Settings
        tutorialVideo =  this.findViewById(R.id.videoTutorial);
        if(mediaController == null){
            mediaController = new MediaController(this);

            // Ser the tutorialVideo that acts as the anchor for the MediaController
            mediaController.setAnchorView(tutorialVideo);

            // Set mediaController for VideoView
            tutorialVideo.setMediaController(mediaController);
        }

        Uri myUri = Uri.parse("android.resource://" + getPackageName() + "/" + video); // initialize Uri here
        tutorialVideo.setVideoURI(myUri);
        tutorialVideo.requestFocus();

        // When the video file ready for playback
        tutorialVideo.setOnPreparedListener(mp -> {

            tutorialVideo.seekTo(position);
            if(position == 0){
                tutorialVideo.start();
            }

            // When video screen change size
            mp.setOnVideoSizeChangedListener((mp1, width, height) -> {
                // Re-set the videoView that acts as the anchor for the MediaController
                mediaController.setAnchorView(tutorialVideo);
            });
        });
        tutorialVideo.start();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // Get saved position.
        position = savedInstanceState.getInt("CurrentPosition");
        tutorialVideo.seekTo(position);
    }
}
