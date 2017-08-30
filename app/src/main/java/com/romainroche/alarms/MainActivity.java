package com.romainroche.alarms;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.romainroche.alarms.data.Alarm;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private CountdownView countdownView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        ArrayList<Alarm> alarms = new ArrayList<Alarm>();
        alarms.add(new Alarm(2, "Foo", R.color.background1));
        alarms.add(new Alarm(4, "Bar", R.color.background0));
        alarms.add(new Alarm(3, "Baz", R.color.background3));

        this.countdownView = (CountdownView)this.findViewById(R.id.countdownView);
        this.countdownView.setAlarms(alarms);
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
