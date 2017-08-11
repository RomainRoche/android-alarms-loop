package com.romainroche.alarms;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by rroche on 11/08/2017.
 */

public class ColorChangingView extends View {

    public int fromColor;
    public int toColor;
    public long fadeTime = (long)(3.0 * 60 * 100.0); // in milliseconds
    public long targetTime = (long)0;

    public ColorChangingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.fromColor = R.color.background0;
        this.toColor = R.color.background1;
        this.setBackgroundResource(this.fromColor);
    }

    @Override
    public void draw(Canvas canvas) {
        long timestamp = System.currentTimeMillis();
        if (timestamp > this.targetTime) {
            this.targetTime = 0;
            int tmpColor = this.fromColor;
            this.fromColor = toColor;
            this.toColor = tmpColor;
        }
        if (this.targetTime == 0) {
            this.targetTime = timestamp + this.fadeTime;
        }
        long deltaT = this.targetTime - timestamp;
        float ratio = (float)(deltaT / this.fadeTime);
        int color0 = (int)Long.parseLong(getResources().getString(this.fromColor));
        int color1 = (int)Long.parseLong(getResources().getString(this.toColor));
        int bgColor = color0 + (int)((color1 - color0) * ratio);
        this.setBackgroundColor(bgColor);
        this.invalidate();
        super.draw(canvas);
    }

}
