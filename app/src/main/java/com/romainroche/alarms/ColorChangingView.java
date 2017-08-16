package com.romainroche.alarms;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Created by rroche on 11/08/2017.
 */

public class ColorChangingView extends LinearLayout {

    private int fromColor, fromRed, fromGreen, fromBlue;
    private int toColor, toRed, toGreen, toBlue;

    protected long timestamp, remainingTime;

    public long duration = (long)(1.0 * 60 * 1000.0); // in milliseconds
    public long targetTime = (long)0;

    public boolean isOn = false;


    public ColorChangingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setFromColorResource(R.color.background0);
        this.setToColorResource(R.color.background1);
        this.setBackgroundResource(this.fromColor);
        this.remainingTime = duration;
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

    public void play() {
        this.timestamp = System.currentTimeMillis();
        this.targetTime = this.timestamp + this.remainingTime;
        this.isOn = true;
        this.invalidate();
    }

    public void pause() {
        this.isOn = false;
        this.invalidate();
    }

    public void reset() {
        this.isOn = false;
        this.setBackgroundResource(this.fromColor);
        this.remainingTime = this.duration;
        this.invalidate();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        for(int i = 0 ; i < this.getChildCount() ; i++){
            this.getChildAt(i).layout(l, t, r, b);
        }
    }

    protected void resetTimeData() {
        this.timestamp = System.currentTimeMillis();
        if (this.timestamp >= this.targetTime) {
            this.targetTime = this.targetTime + this.duration;
            int tmpColor = this.fromColor;
            this.setFromColorResource(this.toColor);
            this.setToColorResource(tmpColor);
        }
        this.setRemainingTime();
    }

    protected void setRemainingTime() {
        this.remainingTime = this.targetTime - this.timestamp;
    }

    @Override
    public void draw(Canvas canvas) {

        if (this.isOn) {
            this.resetTimeData();
            float ratio = 1 - ((float)this.remainingTime / (float)this.duration);

            int resR = this.fromRed + (int)((this.toRed - this.fromRed) * ratio);
            int resG = this.fromGreen + (int)((this.toGreen - this.fromGreen) * ratio);
            int resB = this.fromBlue + (int)((this.toBlue - this.fromBlue) * ratio);
            int res = (255 & 0xff) << 24 | (resR & 0xff) << 16 | (resG & 0xff) << 8 | (resB & 0xff);

            this.setBackgroundColor(res);
            this.invalidate();
        }

        super.draw(canvas);
    }

}
