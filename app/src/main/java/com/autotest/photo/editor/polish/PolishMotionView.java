package com.autotest.photo.editor.polish;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ViewConfiguration;

public class PolishMotionView extends PolishMotionViewTouchBase {
    protected int mDoubleTapDirection;
    protected boolean mDoubleTapEnabled = true;
    public OnImageViewTouchDoubleTapListener mDoubleTapListener;
    private OnImageFlingListener mFlingListener;
    protected GestureDetector mGestureDetector;
    protected GestureDetector.OnGestureListener mGestureListener;
    protected ScaleGestureDetector mScaleDetector;
    protected boolean mScaleEnabled = true;
    protected float mScaleFactor;
    protected ScaleGestureDetector.OnScaleGestureListener mScaleListener;
    protected boolean mScrollEnabled = true;

    public OnImageViewTouchSingleTapListener mSingleTapListener;
    protected int mTouchSlop;

    public interface OnImageFlingListener {
        void onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2);
    }

    public interface OnImageViewTouchDoubleTapListener {
        void onDoubleTap();
    }

    public interface OnImageViewTouchSingleTapListener {
        void onSingleTapConfirmed();
    }

    public PolishMotionView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }


    public void init() {
        super.init();
        this.mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        this.mGestureListener = getGestureListener();
        this.mScaleListener = getScaleListener();
        this.mScaleDetector = new ScaleGestureDetector(getContext(), this.mScaleListener);
        this.mGestureDetector = new GestureDetector(getContext(), this.mGestureListener, (Handler) null, true);
        this.mDoubleTapDirection = 1;
    }

    public GestureDetector.OnGestureListener getGestureListener() {
        return new GestureListener();
    }


    public ScaleGestureDetector.OnScaleGestureListener getScaleListener() {
        return new ScaleListener();
    }


    public void _setImageDrawable(Drawable drawable, Matrix matrix, float f, float f2) {
        super._setImageDrawable(drawable, matrix, f, f2);
        this.mScaleFactor = getMaxScale() / 3.0f;
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        this.mScaleDetector.onTouchEvent(motionEvent);
        if (!this.mScaleDetector.isInProgress()) {
            this.mGestureDetector.onTouchEvent(motionEvent);
        }
        if ((motionEvent.getAction() & 255) == 1 && getScale() < getMinScale()) {
            zoomTo(getMinScale(), 500.0f);
        }
        return true;
    }


    public void onZoomAnimationCompleted(float f) {
        if (f < getMinScale()) {
            zoomTo(getMinScale(), 50.0f);
        }
    }


    public float onDoubleTapPost(float f, float f2) {
        if (this.mDoubleTapDirection != 1) {
            this.mDoubleTapDirection = 1;
            return 1.0f;
        } else if ((this.mScaleFactor * 2.0f) + f <= f2) {
            return f + this.mScaleFactor;
        } else {
            this.mDoubleTapDirection = -1;
            return f2;
        }
    }

    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
        if (!this.mScrollEnabled || motionEvent == null || motionEvent2 == null || motionEvent.getPointerCount() > 1 || motionEvent2.getPointerCount() > 1 || this.mScaleDetector.isInProgress() || getScale() == 1.0f) {
            return false;
        }
        this.mUserScaled = true;
        scrollBy(-f, -f2);
        invalidate();
        return true;
    }

    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
        if (!this.mScrollEnabled) {
            return false;
        }
        if (this.mFlingListener != null) {
            this.mFlingListener.onFling(motionEvent, motionEvent2, f, f2);
        }
        if (motionEvent.getPointerCount() > 1 || motionEvent2.getPointerCount() > 1 || this.mScaleDetector.isInProgress() || getScale() == 1.0f) {
            return false;
        }
        float x = motionEvent2.getX() - motionEvent.getX();
        float y = motionEvent2.getY() - motionEvent.getY();
        if (Math.abs(f) <= 800.0f && Math.abs(f2) <= 800.0f) {
            return false;
        }
        this.mUserScaled = true;
        scrollBy(x / 2.0f, y / 2.0f, 300.0d);
        invalidate();
        return true;
    }

    public class GestureListener extends GestureDetector.SimpleOnGestureListener {
        public GestureListener() {
        }

        public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
            if (PolishMotionView.this.mSingleTapListener != null) {
                PolishMotionView.this.mSingleTapListener.onSingleTapConfirmed();
            }
            return super.onSingleTapConfirmed(motionEvent);
        }

        public boolean onDoubleTap(MotionEvent motionEvent) {
            Log.i(PolishMotionViewTouchBase.LOG_TAG, "onDoubleTap. double tap enabled? " + PolishMotionView.this.mDoubleTapEnabled);
            if (PolishMotionView.this.mDoubleTapEnabled) {
                PolishMotionView.this.mUserScaled = true;
                PolishMotionView.this.zoomTo(Math.min(PolishMotionView.this.getMaxScale(), Math.max(PolishMotionView.this.onDoubleTapPost(PolishMotionView.this.getScale(), PolishMotionView.this.getMaxScale()), PolishMotionView.this.getMinScale())), motionEvent.getX(), motionEvent.getY(), 200.0f);
                PolishMotionView.this.invalidate();
            }
            if (PolishMotionView.this.mDoubleTapListener != null) {
                PolishMotionView.this.mDoubleTapListener.onDoubleTap();
            }
            return super.onDoubleTap(motionEvent);
        }

        public void onLongPress(MotionEvent motionEvent) {
            if (PolishMotionView.this.isLongClickable() && !PolishMotionView.this.mScaleDetector.isInProgress()) {
                PolishMotionView.this.setPressed(true);
                PolishMotionView.this.performLongClick();
            }
        }

        public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
            return PolishMotionView.this.onScroll(motionEvent, motionEvent2, f, f2);
        }

        public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
            return PolishMotionView.this.onFling(motionEvent, motionEvent2, f, f2);
        }
    }

    public class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        protected boolean mScaled = false;

        public ScaleListener() {
        }

        public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
            float currentSpan = scaleGestureDetector.getCurrentSpan() - scaleGestureDetector.getPreviousSpan();
            float scale = PolishMotionView.this.getScale() * scaleGestureDetector.getScaleFactor();
            if (PolishMotionView.this.mScaleEnabled) {
                if (this.mScaled && currentSpan != 0.0f) {
                    PolishMotionView.this.mUserScaled = true;
                    PolishMotionView.this.zoomTo(Math.min(PolishMotionView.this.getMaxScale(), Math.max(scale, PolishMotionView.this.getMinScale() - 0.1f)), scaleGestureDetector.getFocusX(), scaleGestureDetector.getFocusY());
                    PolishMotionView.this.mDoubleTapDirection = 1;
                    PolishMotionView.this.invalidate();
                    return true;
                } else if (!this.mScaled) {
                    this.mScaled = true;
                }
            }
            return true;
        }
    }

}
