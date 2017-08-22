package com.romainroche.alarms;

import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private CountdownView countdownView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.countdownView = (CountdownView)this.findViewById(R.id.countdownView);
        this.countdownView.setColors(new int[] {R.color.background1, R.color.background0, R.color.background3});
        this.countdownView.setDurations(new long[] {1 * 2 * 1000, 1 * 4 * 1000, 1 * 3 * 1000});
    }

}
