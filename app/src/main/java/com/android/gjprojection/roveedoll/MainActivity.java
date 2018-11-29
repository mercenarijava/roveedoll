package com.android.gjprojection.roveedoll;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.android.gjprojection.roveedoll.features.free_line.FreeLineFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        getFragmentManager()
                .beginTransaction()
                .add(R.id.content_layout, FreeLineFragment.newInstance())
                .commit();

    }
}
