package com.romainroche.alarms;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by rroche on 11/08/2017.
 */

public class ColorChangingView extends View {

    public int fromColor;
    public int toColor;
    public long fadeTime = (long)(3.0 * 60 * 1000.0); // in milliseconds
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
        if (timestamp > this.targetTime && this.targetTime != 0) {
            this.targetTime = 0;
            int tmpColor = this.fromColor;
            this.fromColor = toColor;
            this.toColor = tmpColor;
        }
        if (this.targetTime == 0) {
            this.targetTime = timestamp + this.fadeTime;
        }
        long deltaT = this.targetTime - timestamp;
        float ratio = 1 - ((float)deltaT / (float)this.fadeTime);

        int color0 = getResources().getInteger(this.fromColor);
        int color0R = (color0 >> 16) & 0xff;
        int color0G = (color0 >> 8) & 0xff;
        int color0B = (color0) & 0xff;

        int color1 = getResources().getInteger(this.toColor);
        int color1R = (color1 >> 16) & 0xff;
        int color1G = (color1 >> 8) & 0xff;
        int color1B = (color1) & 0xff;

        int resR = color0R + (int)((color1R - color0R) * ratio);
        int resG = color0G + (int)((color1G - color0G) * ratio);
        int resB = color0B + (int)((color1B - color0B) * ratio);
        int res = (255 & 0xff) << 24 | (resR & 0xff) << 16 | (resG & 0xff) << 8 | (resB & 0xff);

        this.setBackgroundColor(res);
        this.invalidate();
        super.draw(canvas);
    }

}
