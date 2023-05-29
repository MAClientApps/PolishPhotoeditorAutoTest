package com.autotest.photo.editor.event;

import android.view.MotionEvent;

import com.autotest.photo.editor.polish.PolishStickerView;

public abstract class AbstractFlipEvent implements StickerIconEvent {
    protected abstract int getFlipDirection();

    public void onActionDown(PolishStickerView paramStickerView, MotionEvent paramMotionEvent) {
    }

    public void onActionMove(PolishStickerView paramStickerView, MotionEvent paramMotionEvent) {
    }

    public void onActionUp(PolishStickerView paramStickerView, MotionEvent paramMotionEvent) {
        paramStickerView.flipCurrentSticker(getFlipDirection());
    }
}
