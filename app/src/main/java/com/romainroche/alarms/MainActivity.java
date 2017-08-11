package com.romainroche.alarms;

import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private double countdown = 3 * 60 * 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ConstraintLayout root = (ConstraintLayout)findViewById(R.id.root);
        root.setBackgroundResource(R.color.background0);
    }

}
