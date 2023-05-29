package com.autotest.photo.editor.polish;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.ColorInt;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresPermission;
import androidx.annotation.UiThread;

import com.autotest.photo.editor.R;
import com.autotest.photo.editor.listener.BrushColorChangeListener;
import com.autotest.photo.editor.draw.BrushDrawingView;
import com.autotest.photo.editor.draw.DrawModel;
import com.autotest.photo.editor.draw.Drawing;
import com.autotest.photo.editor.draw.OnSaveBitmap;
import com.autotest.photo.editor.listener.OnPolishEditorListener;

import org.wysaid.view.ImageGLSurfaceView;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class PolishEditor implements BrushColorChangeListener {

    private static final String TAG = "PolishEditor";
    private List<View> viewList;
    private List<String> filterlist;
    private BrushDrawingView brushDrawingView;
    private Context context;
    private View deleteView;
    private ImageGLSurfaceView glSurfaceView;
    private boolean isTextPinchZoomable;
    private Typeface mDefaultEmojiTypeface;
    private Typeface mDefaultTextTypeface;
    private final LayoutInflater mLayoutInflater;
    private OnPolishEditorListener mOnPhotoEditorListener;
    public PolishView parentView;
    private List<View> redoViews;
    private int fliterindex=-1;

    public interface OnSaveListener {
        void onFailure(@NonNull Exception exc);

        void onSuccess(@NonNull String str);
    }

    private PolishEditor(Builder builder) {
        this.context = builder.context;
        this.parentView = builder.parentView;
        this.deleteView = builder.deleteView;
        this.brushDrawingView = builder.brushDrawingView;
        this.glSurfaceView = builder.glSurfaceView;
        this.isTextPinchZoomable = builder.isTextPinchZoomable;
        this.mDefaultTextTypeface = builder.textTypeface;
        this.mDefaultEmojiTypeface = builder.emojiTypeface;
        this.mLayoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.brushDrawingView.setBrushViewChangeListener(this);
        this.viewList = new ArrayList();
        this.redoViews = new ArrayList();
        this.filterlist = new ArrayList();

    }

    public BrushDrawingView getBrushDrawingView() {
        return this.brushDrawingView;
    }


    public void setBrushDrawingMode(boolean z) {
        if (this.brushDrawingView != null) {
            this.brushDrawingView.setBrushDrawingMode(z);
        }
    }

    public void setAdjustFilter(String str) {
        this.glSurfaceView.setFilterWithConfig(str);
    }

    public void setFilterIntensityForIndex(float f, int i, boolean z) {
        this.glSurfaceView.setFilterIntensityForIndex(f, i, z);
    }

    public void setBrushMode(int i) {
        this.brushDrawingView.setDrawMode(i);
    }

    public void setBrushMagic(DrawModel drawBitmapModel) {
        this.brushDrawingView.setCurrentMagicBrush(drawBitmapModel);
    }


    public void setBrushSize(float f) {
        if (this.brushDrawingView != null) {
            this.brushDrawingView.setBrushSize(f);
        }
    }


    public void setBrushColor(@ColorInt int i) {
        if (this.brushDrawingView != null) {
            this.brushDrawingView.setBrushColor(i);
        }
    }

    public void setBrushEraserSize(float f) {
        if (this.brushDrawingView != null) {
            this.brushDrawingView.setBrushEraserSize(f);
        }
    }


    public void brushEraser() {
        if (this.brushDrawingView != null) {
            this.brushDrawingView.brushEraser();
        }
    }


    public boolean undo() {
        if (fliterindex > 0) {
            Log.d(TAG, "undo: "+fliterindex);
            this.parentView.setFilterEffect(filterlist.get(--fliterindex));
            return true;

        }

        return false;
    }

    public boolean redo() {
        if (fliterindex<filterlist.size()) {
            Log.d(TAG, "redo: "+fliterindex);

            this.parentView.setFilterEffect(filterlist.get(++fliterindex));
            return true;


        }

        return false;
    }

    public void redoBrush() {
        if (this.brushDrawingView != null) {
            this.brushDrawingView.redo();
        }
    }

    public void undoBrush() {
        if (this.brushDrawingView != null) {
            this.brushDrawingView.undo();
        }
    }

    public void setPaintOpacity(@IntRange(from = 0, to = 100) int i) {
        if (this.brushDrawingView != null) {
            this.brushDrawingView.setPaintOpacity((int) ((((double) i) / 100.0d) * 255.0d));
        }
    }

    public void setMagicOpacity(@IntRange(from = 0, to = 100) int i) {
        if (this.brushDrawingView != null) {
            this.brushDrawingView.setMagicOpacity((int) ((((double) i) / 100.0d) * 255.0d));
        }
    }

    public void clearBrushAllViews() {
        if (this.brushDrawingView != null) {
            this.brushDrawingView.clearAll();
        }
    }

    public void clearAllViews() {
        for (int i = 0; i < this.viewList.size(); i++) {
            this.parentView.removeView(this.viewList.get(i));
        }
        if (this.viewList.contains(this.brushDrawingView)) {
            this.parentView.addView(this.brushDrawingView);
        }
        this.viewList.clear();
        this.redoViews.clear();
        clearBrushAllViews();
    }

    @UiThread
    public void clearHelperBox() {
        for (int i = 0; i < this.parentView.getChildCount(); i++) {
            View childAt = this.parentView.getChildAt(i);
            FrameLayout frameLayout = (FrameLayout) childAt.findViewById(R.id.frmBorder);
            if (frameLayout != null) {
                frameLayout.setBackgroundResource(0);
            }
            ImageView imageView = (ImageView) childAt.findViewById(R.id.imgPhotoEditorClose);
            if (imageView != null) {
                imageView.setVisibility(View.GONE);
            }
        }
    }

    public void setFilterEffect(String str) {
        this.parentView.setFilterEffect(str);

        this.filterlist.add(str);
//      if (!str.equals(""))
        fliterindex++;

    }

    @RequiresPermission(allOf = {"android.permission.WRITE_EXTERNAL_STORAGE"})
    @SuppressLint({"StaticFieldLeak"})
    @Deprecated
    public void saveImage(@NonNull String str, @NonNull OnSaveListener onSaveListener) {
        saveAsFile(str, onSaveListener);
    }

    @RequiresPermission(allOf = {"android.permission.WRITE_EXTERNAL_STORAGE"})
    public void saveAsFile(@NonNull String str, @NonNull OnSaveListener onSaveListener) {
        saveAsFile(str, new PolishSettings.Builder().build(), onSaveListener);
    }

    @RequiresPermission(allOf = {"android.permission.WRITE_EXTERNAL_STORAGE"})
    @SuppressLint({"StaticFieldLeak"})
    public void saveAsFile(@NonNull final String str, @NonNull final PolishSettings saveSettings, @NonNull final OnSaveListener onSaveListener) {
        this.parentView.saveGLSurfaceViewAsBitmap(new OnSaveBitmap() {
            public void onBitmapReady(Bitmap bitmap) {
                new AsyncTask<String, String, Exception>() {

                    public void onPreExecute() {
                        super.onPreExecute();
                        PolishEditor.this.clearHelperBox();
                    }


                    @SuppressLint({"MissingPermission"})
                    public Exception doInBackground(String... strArr) {
                        Bitmap bitmap;
                        try {
                            FileOutputStream fileOutputStream = new FileOutputStream(new File(str), false);
                            if (PolishEditor.this.parentView != null) {
                                if (saveSettings.isTransparencyEnabled()) {
                                    bitmap = PolishBitmapUtils.removeTransparency(getBitmapFromView(PolishEditor.this.parentView));
                                } else {
                                    bitmap = getBitmapFromView(PolishEditor.this.parentView);
                                }
                                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
                            }
                            fileOutputStream.flush();
                            fileOutputStream.close();
                            Log.d(PolishEditor.TAG, "Filed Saved Successfully");
                            return null;
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.d(PolishEditor.TAG, "Failed to save File");
                            return e;
                        }
                    }


                    public void onPostExecute(Exception exc) {
                        super.onPostExecute(exc);
                        if (exc == null) {
                            if (saveSettings.isClearViewsEnabled()) {
                                PolishEditor.this.clearAllViews();
                            }
                            onSaveListener.onSuccess(str);
                            return;
                        }
                        onSaveListener.onFailure(exc);
                    }
                }.execute(new String[0]);
            }

            public void onFailure(Exception exc) {
                onSaveListener.onFailure(exc);
            }
        });
    }

    @SuppressLint({"StaticFieldLeak"})
    public void saveStickerAsBitmap(@NonNull OnSaveBitmap onSaveBitmap) {
        saveStickerAsBitmap(new PolishSettings.Builder().build(), onSaveBitmap);
    }

    @SuppressLint({"StaticFieldLeak"})
    public void saveStickerAsBitmap(@NonNull PolishSettings saveSettings, @NonNull OnSaveBitmap onSaveBitmap) {
        Bitmap bitmap;

        if (saveSettings.isTransparencyEnabled()) {
            bitmap = PolishBitmapUtils.removeTransparency(getBitmapFromView(this.parentView));
        } else {
            bitmap = getBitmapFromView(this.parentView);
        }
        onSaveBitmap.onBitmapReady(bitmap);
    }

    private static String convertEmoji(String str) {
        try {
            return new String(Character.toChars(Integer.parseInt(str.substring(2), 16)));
        } catch (NumberFormatException e) {
            return "";
        }
    }

    public void setOnPhotoEditorListener(@NonNull OnPolishEditorListener onPhotoEditorListener) {
        this.mOnPhotoEditorListener = onPhotoEditorListener;
    }

    public boolean isCacheEmpty() {
        return this.viewList.size() == 0 && this.redoViews.size() == 0;
    }

    public void onViewAdd(BrushDrawingView brushDrawingView2) {
        if (this.redoViews.size() > 0) {
            this.redoViews.remove(this.redoViews.size() - 1);
        }
        this.viewList.add(brushDrawingView2);
        if (this.mOnPhotoEditorListener != null) {
            this.mOnPhotoEditorListener.onAddViewListener(Drawing.BRUSH_DRAWING, this.viewList.size());
        }
    }

    public void onViewRemoved(BrushDrawingView brushDrawingView2) {
        if (this.viewList.size() > 0) {
            View remove = this.viewList.remove(this.viewList.size() - 1);
            if (!(remove instanceof BrushDrawingView)) {
                this.parentView.removeView(remove);
            }
            this.redoViews.add(remove);
        }
        if (this.mOnPhotoEditorListener != null) {
            this.mOnPhotoEditorListener.onRemoveViewListener(this.viewList.size());
            this.mOnPhotoEditorListener.onRemoveViewListener(Drawing.BRUSH_DRAWING, this.viewList.size());
        }
    }

    public void onStartDrawing() {
        if (this.mOnPhotoEditorListener != null) {
            this.mOnPhotoEditorListener.onStartViewChangeListener(Drawing.BRUSH_DRAWING);
        }
    }

    public void onStopDrawing() {
        if (this.mOnPhotoEditorListener != null) {
            this.mOnPhotoEditorListener.onStopViewChangeListener(Drawing.BRUSH_DRAWING);
        }
    }

    public static class Builder {

        public BrushDrawingView brushDrawingView;

        public Context context;

        public View deleteView;

        public Typeface emojiTypeface;

        public ImageGLSurfaceView glSurfaceView;

        public boolean isTextPinchZoomable = true;

        public PolishView parentView;

        public Typeface textTypeface;

        public Builder(Context context2, PolishView polishView) {
            this.context = context2;
            this.parentView = polishView;
            this.brushDrawingView = polishView.getBrushDrawingView();
            this.glSurfaceView = polishView.getGLSurfaceView();
        }


        public Builder setDeleteView(View view) {
            this.deleteView = view;
            return this;
        }

        public Builder setDefaultTextTypeface(Typeface typeface) {
            this.textTypeface = typeface;
            return this;
        }

        public Builder setDefaultEmojiTypeface(Typeface typeface) {
            this.emojiTypeface = typeface;
            return this;
        }

        public Builder setPinchTextScalable(boolean z) {
            this.isTextPinchZoomable = z;
            return this;
        }

        public PolishEditor build() {
            return new PolishEditor(this);
        }
    }


    public Bitmap getBitmapFromView(View view)
    {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }
}