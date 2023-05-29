package com.autotest.photo.editor.draw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path.Direction;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout.LayoutParams;

import com.autotest.photo.editor.polish.PolishSplashView;

public class SplashBrushView extends View {
    public BrushSize brushSize;
    public boolean isBrushSize = true;
    float opacity;
    float ratioRadius;

    public SplashBrushView(Context context) {
        super(context);
        initMyView();
    }

    public SplashBrushView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initMyView();
    }

    public SplashBrushView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        initMyView();
    }

    public void initMyView() {
        this.brushSize = new BrushSize();
    }


    public void onDraw(Canvas canvas) {
        float f;
        float f2;
        super.onDraw(canvas);
        int width = getWidth();
        int height = getHeight();
        if (width != 0 && height != 0) {
            float f3 = ((float) width) / 2.0f;
            float f4 = ((float) height) / 2.0f;
            if (width > height) {
                f2 = this.ratioRadius;
                f = PolishSplashView.resRatio;
            } else {
                f2 = this.ratioRadius;
                f = PolishSplashView.resRatio;
            }
            float f5 = (f2 * f) / 2.0f;
            if (((int) f5) * 2 > 150) {
                LayoutParams layoutParams = (LayoutParams) getLayoutParams();
                int i = ((int) (2.0f * f5)) + 40;
                layoutParams.height = i;
                layoutParams.width = i;
                layoutParams.alignWithParent = true;
                setLayoutParams(layoutParams);
            }
            this.brushSize.setCircle(f3, f4, f5, Direction.CCW);
            canvas.drawPath(this.brushSize.getPath(), this.brushSize.getPaint());
            if (!this.isBrushSize) {
                canvas.drawPath(this.brushSize.getPath(), this.brushSize.getInnerPaint());
            }
        }
    }

    public void setShapeRadiusRatio(float f) {
        this.ratioRadius = f;
    }

    public void setShapeOpacity(float f) {
        this.opacity = f;
    }
}
