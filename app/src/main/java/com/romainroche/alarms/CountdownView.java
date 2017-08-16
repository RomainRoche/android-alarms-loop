package com.romainroche.alarms;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by rroche on 16/08/2017.
 */

public class CountdownView extends ColorChangingView {

    private TextView textView;
    private Button playButton;
    private Button clearButton;

    public CountdownView(Context context, AttributeSet attrs) {
        super(context, attrs);
        View.inflate(context, R.layout.countdown, this);
        this.textView = (TextView)findViewById(R.id.countdownTextView);
        this.textView.setText(this.countdownText());

        final CountdownView countdown = this;
        this.playButton = (Button)findViewById(R.id.playButton);
        this.playButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (countdown.isOn) {
                    countdown.pause();
                } else {
                    countdown.play();
                }
            }
        });

        this.clearButton = (Button)findViewById(R.id.clearButton);
        this.clearButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                countdown.reset();
            }
        });
    }

    @Override
    public void play() {
        super.play();
        this.playButton.setText(this.getResources().getString(R.string.pause));
    }

    @Override
    public void pause() {
        super.pause();
        this.playButton.setText(this.getResources().getString(R.string.start));
    }

    @Override
    public void reset() {
        super.reset();
        this.textView.setText(this.countdownText());
    }

    private String countdownText() {
        long minutes = this.remainingTime / (long)(60 * 1000);
        long milliseconds = this.remainingTime % (long)(60 * 1000);
        long seconds = milliseconds / 1000;
        milliseconds = milliseconds - (seconds * 1000);
        String minutesString = minutes >= 10 ? "" : "0";
        String secondsString = seconds >= 10 ? "" : "0";
        String milliString = milliseconds >= 100 ? "" : milliseconds >= 10 ? "0" : "00";
        return minutesString + minutes + ":" + secondsString + seconds + "." + milliString + milliseconds;
    }

    @Override
    public void draw(Canvas canvas) {
        if (this.isOn) {
            this.setRemainingTime();
            this.textView.setText(this.countdownText());
            this.invalidate();
        }
        super.draw(canvas);
    }

}
