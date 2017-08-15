package com.romainroche.alarms;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by rroche on 11/08/2017.
 */

public class ColorChangingView extends LinearLayout {

    private int fromColor, fromRed, fromGreen, fromBlue;
    private int toColor, toRed, toGreen, toBlue;
    public long duration = (long)(3.0 * 60 * 1000.0); // in milliseconds
    public long targetTime = (long)0;

    private TextView textView;

    public ColorChangingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setFromColorResource(R.color.background0);
        this.setToColorResource(R.color.background1);
        this.setBackgroundResource(this.fromColor);
        View.inflate(context, R.layout.countdown, this);
        this.textView = (TextView)findViewById(R.id.countdownTextView);
    }

    public int getFromColorResource() { return this.fromColor; }
    public void setFromColorResource(int resourceId) {
        this.fromColor = resourceId;
        int from = this.getResources().getInteger(this.fromColor);
        this.fromRed = (from >> 16) & 0xff;
        this.fromGreen = (from >> 8) & 0xff;
        this.fromBlue = (from) & 0xff;
    }

    public int getToColorResource() { return this.toColor; }
    public void setToColorResource(int resourceId) {
        this.toColor = resourceId;
        int to = this.getResources().getInteger(this.toColor);
        this.toRed = (to >> 16) & 0xff;
        this.toGreen = (to >> 8) & 0xff;
        this.toBlue = (to) & 0xff;
    }

    private String countdownText(long deltaT) {
        long remainingTime = deltaT;
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
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        for(int i = 0 ; i < this.getChildCount() ; i++){
            this.getChildAt(i).layout(l, t, r, b);
        }
    }

    @Override
    public void draw(Canvas canvas) {
        long timestamp = System.currentTimeMillis();
        if (timestamp > this.targetTime && this.targetTime != 0) {
            this.targetTime = 0;
            int tmpColor = this.fromColor;
            this.setFromColorResource(this.toColor);
            this.setToColorResource(tmpColor);
        }
        if (this.targetTime == 0) {
            this.targetTime = timestamp + this.duration;
        }
        long deltaT = this.targetTime - timestamp;
        float ratio = 1 - ((float)deltaT / (float)this.duration);

        int resR = this.fromRed + (int)((this.toRed - this.fromRed) * ratio);
        int resG = this.fromGreen + (int)((this.toGreen - this.fromGreen) * ratio);
        int resB = this.fromBlue + (int)((this.toBlue - this.fromBlue) * ratio);
        int res = (255 & 0xff) << 24 | (resR & 0xff) << 16 | (resG & 0xff) << 8 | (resB & 0xff);

        this.setBackgroundColor(res);
        this.textView.setText(this.countdownText(deltaT));
        this.invalidate();
        super.draw(canvas);
    }

}
