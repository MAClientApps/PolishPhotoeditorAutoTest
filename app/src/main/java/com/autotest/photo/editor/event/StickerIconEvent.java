package com.autotest.photo.editor.event;

import android.view.MotionEvent;

import com.autotest.photo.editor.polish.PolishStickerView;

public interface StickerIconEvent {
    void onActionDown(PolishStickerView paramStickerView, MotionEvent paramMotionEvent);

    void onActionMove(PolishStickerView paramStickerView, MotionEvent paramMotionEvent);

    void onActionUp(PolishStickerView paramStickerView, MotionEvent paramMotionEvent);
}
