package com.android.gjprojection.roveedoll;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.android.gjprojection.roveedoll.controls.DataBackground;

public class MainActivity extends AppCompatActivity {

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        final DataBackground dataBackground = findViewById(R.id.data_background);
        final Button dataScale = findViewById(R.id.scaleButton);
        dataScale.setText("x" + dataBackground.getxScale());
        dataScale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataBackground.pushScale();
                dataScale.setText("x" + dataBackground.getxScale());
            }
        });
    }
}
