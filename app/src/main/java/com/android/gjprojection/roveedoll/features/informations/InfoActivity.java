package com.android.gjprojection.roveedoll.features.informations;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.android.gjprojection.roveedoll.R;
import com.android.gjprojection.roveedoll.UIBase;

public class InfoActivity extends AppCompatActivity implements UIBase{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        connectViews();
        connectListeners();
    }

    @Override
    public void connectViews() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        setTitle(R.string.info_activity_title);
    }

    @Override
    public void connectListeners() {

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
