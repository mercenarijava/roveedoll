package com.android.gjprojection.roveedoll.features.tutorial;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.android.gjprojection.roveedoll.R;
import com.android.gjprojection.roveedoll.UIBase;

public class TutorialActivity extends AppCompatActivity implements UIBase {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);
        connectViews();
        connectListeners();
    }

    @Override
    public void connectViews() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        setTitle(R.string.tutorial_activity_title);
    }

    @Override
    public void connectListeners() {
        MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.vid_1);
        mediaPlayer.start();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

}
