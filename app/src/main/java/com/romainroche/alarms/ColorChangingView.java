package com.romainroche.alarms;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * Created by rroche on 11/08/2017.
 */

public class ColorChangingView extends FrameLayout {

    private int index = 0;
    private int[] colors = new int[] {R.color.background0, R.color.background1};
    private long[] durations = new long[] {(long)(1.0 * 60 * 1000.0)};

    private int fromRed, fromGreen, fromBlue;
    private int toRed, toGreen, toBlue;

    protected long timestamp, remainingTime;
    protected long targetTime = (long)0;

    public boolean isOn = false;

    private View circle;

    public ColorChangingView(Context context, AttributeSet attrs) {

        super(context, attrs);

        this.colors = new int[] {R.color.background0, R.color.background1};
        this.setFromColorResource(this.colors[0]);
        this.setToColorResource(this.colors[1]);
        this.setBackgroundResource(this.colors[0]);
        this.remainingTime = this.durations[0];

    }

    private void setFromColorResource(int resourceId) {
        int from = this.getResources().getInteger(resourceId);
        this.fromRed = (from >> 16) & 0xff;
        this.fromGreen = (from >> 8) & 0xff;
        this.fromBlue = (from) & 0xff;
    }

    private void setToColorResource(int resourceId) {
        int to = this.getResources().getInteger(resourceId);
        this.toRed = (to >> 16) & 0xff;
        this.toGreen = (to >> 8) & 0xff;
        this.toBlue = (to) & 0xff;
    }

    private int getCurrentColorResource() {
        return this.colors[this.index % this.colors.length];
    }

    private int getNextColorResource() {
        return this.colors[(this.index + 1) % this.colors.length];
    }

    private long getCurrentDuration() {
        return this.durations[this.index % this.durations.length];
    }

    public void setColors(int[] colors) {
        if (colors == null || colors.length == 0) {
            colors = new int[] {R.color.background0, R.color.background1};
        } else if (colors.length == 1) {
            colors = new int[] {colors[0], R.color.background1};
        }
        this.colors = colors;
        this.setBackgroundResource(this.colors[0]);
        this.setFromColorResource(this.colors[0]);
        this.setToColorResource(this.colors[1]);
    }

    public void setDurations(long[] durations) {
        if (durations == null || durations.length == 0) {
            durations = new long[] {1 * 60 * 1000};
        }
        this.durations = durations;
        this.remainingTime = this.durations[0];
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
        this.setBackgroundResource(this.colors[0]);
        this.remainingTime = this.durations[0];
        this.invalidate();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        for(int i = 0 ; i < this.getChildCount() ; i++){
            this.getChildAt(i).layout(l, t, r, b);
        }
    }

    protected void doCircle() {

        if (this.circle == null) {
            this.circle = this.findViewById(R.id.circle);
        }

        int width = this.getWidth() * 2;
        int height = this.getHeight() * 2;
        if (height > width) {
            width = height;
        } else {
            height = width;
        }

        FrameLayout.LayoutParams layout = new FrameLayout.LayoutParams(width, height);
        layout.leftMargin = (this.getWidth() - width) / 2;
        layout.topMargin = (this.getHeight() - height) / 2;
        this.circle.setLayoutParams(layout);

        Drawable bg = this.circle.getBackground();
        if (bg instanceof ShapeDrawable) {
            ((ShapeDrawable)bg).getPaint().setColor(this.getResources().getInteger(this.getCurrentColorResource()));
        } else if (bg instanceof GradientDrawable) {
            ((GradientDrawable)bg).setColor(this.getResources().getInteger(this.getCurrentColorResource()));
        }

        final ColorChangingView self = this;

        AnimatorSet set = (AnimatorSet) AnimatorInflater.loadAnimator(this.getContext(),
                R.animator.scale_up);
        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {}

            @Override
            public void onAnimationEnd(Animator animation) {
                self.setBackgroundResource(self.getCurrentColorResource());
                self.circle.setAlpha(0.f);
            }

            @Override
            public void onAnimationCancel(Animator animation) {}

            @Override
            public void onAnimationRepeat(Animator animation) {}
        });

        set.setTarget(self.circle);
        self.circle.setAlpha(1.f);
        set.start();

    }

    protected void wave() {

        int width = this.getWidth() * 2;
        int height = this.getHeight() * 2;
        float x = 0.f; // width / -4.f;
        float y = 0.f; // height / -4.f;

        RelativeLayout.LayoutParams layout = new RelativeLayout.LayoutParams(width, height);
        layout.leftMargin = (int)x;
        layout.topMargin = (int)y;
        final View wave = new View(this.getContext());
        wave.setBackgroundResource(this.getCurrentColorResource());
        wave.setLayoutParams(layout);
        wave.setX(x);
        wave.setY(y);

        final ColorChangingView self = this;

        AnimatorSet set = (AnimatorSet) AnimatorInflater.loadAnimator(this.getContext(),
                R.animator.scale_up);
        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {}

            @Override
            public void onAnimationEnd(Animator animation) {
                self.setBackgroundResource(self.getCurrentColorResource());
                self.removeView(wave);
            }

            @Override
            public void onAnimationCancel(Animator animation) {}

            @Override
            public void onAnimationRepeat(Animator animation) {}
        });

        this.addView(wave, 0);
        this.invalidate();

        set.setTarget(wave);
        set.start();


    }

    protected void resetTimeData() {
        this.timestamp = System.currentTimeMillis();
        if (this.timestamp >= this.targetTime) {
            this.index++;
            this.targetTime = this.targetTime + this.getCurrentDuration();
            this.doCircle();
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
            float ratio = .5f * (1 - ((float)this.remainingTime / (float)this.getCurrentDuration()));

            int resR = this.fromRed + (int)((this.toRed - this.fromRed) * ratio);
            int resG = this.fromGreen + (int)((this.toGreen - this.fromGreen) * ratio);
            int resB = this.fromBlue + (int)((this.toBlue - this.fromBlue) * ratio);
            int res = (255 & 0xff) << 24 | (resR & 0xff) << 16 | (resG & 0xff) << 8 | (resB & 0xff);

            //this.setBackgroundColor(res);
            //this.invalidate();
        }

        super.draw(canvas);
    }

}
