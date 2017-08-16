package com.romainroche.alarms;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

/**
 * Created by rroche on 16/08/2017.
 */

public class CountdownView extends ColorChangingView {

    private TextView textView;

    public CountdownView(Context context, AttributeSet attrs) {
        super(context, attrs);
        View.inflate(context, R.layout.countdown, this);
        this.textView = (TextView)findViewById(R.id.countdownTextView);
        this.textView.setText(this.countdownText());
    }

    private String countdownText() {
        long remainingTime = this.deltaT;
        long minutes = remainingTime / (long)(60 * 1000);
        long milliseconds = remainingTime % (long)(60 * 1000);
        long seconds = milliseconds / 1000;
        milliseconds = milliseconds - (seconds * 1000);
        String minutesString = minutes >= 10 ? "" : "0";
        String secondsString = seconds >= 10 ? "" : "0";
        String milliString = milliseconds >= 100 ? "" : milliseconds >= 10 ? "0" : "000";
        return minutesString + minutes + ":" + secondsString + seconds + "." + milliString + milliseconds;
    }

    @Override
    public void draw(Canvas canvas) {
        if (this.isOn) {
            this.getTimeData();
            this.textView.setText(this.countdownText());
            this.invalidate();
        }
        super.draw(canvas);
    }

}
