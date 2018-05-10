package com.example.nqh.thuvienbachkhoa.Admin;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

public class OverlayFrame extends FrameLayout {
    private boolean mIsOverlay;


    public OverlayFrame(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public OverlayFrame(@NonNull Context context) {
        super(context);
    }

    public void setOverlay(boolean isOverlay) {
        mIsOverlay = isOverlay;
        if (mIsOverlay)
            this.setBackgroundColor(Color.parseColor("#666666"));
        else
            this.setBackgroundColor(Color.parseColor("#ffffff"));
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (mIsOverlay) {
            invalidate();
            return true;
        }
        return super.onInterceptTouchEvent(ev);
    }
}
