package com.romainroche.alarms;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    private CountdownView countdownView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        this.countdownView = (CountdownView)this.findViewById(R.id.countdownView);
        this.countdownView.setColors(new int[] {R.color.background1, R.color.background0, R.color.background3});
        this.countdownView.setDurations(new long[] {1 * 2 * 1000, 1 * 4 * 1000, 1 * 3 * 1000});
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_add:
                this.countdownView.pause();
                this.startActivity(new Intent(this, CreateAlarmsActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
