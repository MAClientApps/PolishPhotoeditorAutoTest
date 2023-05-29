package com.autotest.photo.editor.crop;

import android.graphics.Bitmap;


public interface CropTaskCompleted {
    void onTaskCompleted(Bitmap bitmap, Bitmap bitmap2, int i, int i2);
}
