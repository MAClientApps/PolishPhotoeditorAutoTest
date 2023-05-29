package com.autotest.photo.editor.crop;

import android.graphics.Bitmap;


interface CropPaintTaskCompleted {
    void onTaskCompleted(Bitmap bitmap, double d);
}
