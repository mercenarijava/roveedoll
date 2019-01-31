package com.android.gjprojection.roveedoll.features.free_line;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.android.gjprojection.roveedoll.R;
import com.android.gjprojection.roveedoll.UIBase;

public class FreeLineActivity extends AppCompatActivity implements UIBase {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_free_line);
        connectViews();
        connectListeners();
    }

    @Override
    public void connectViews() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        this.setTitle(R.string.v_mode_title);
    }

    @Override
    public void connectListeners() {
        addFragment();
    }

    private void addFragment() {
        final FragmentManager fm = getSupportFragmentManager();
        final FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.content_layout, FreeLineFragment.newInstance());
        ft.commit();
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
