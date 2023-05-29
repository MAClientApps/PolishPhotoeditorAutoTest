package com.autotest.photo.editor.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.autotest.photo.editor.R;
import com.autotest.photo.editor.adapters.SplashSquareAdapter;
import com.autotest.photo.editor.assets.StickerFileAsset;
import com.autotest.photo.editor.polish.PolishSplashSticker;
import com.autotest.photo.editor.polish.PolishSplashSquareView;

public class SketchSquareFragment extends DialogFragment implements SplashSquareAdapter.SplashChangeListener {
    private static final String TAG = "SketchSquareFragment";
    private ImageView image_view_background;
    public Bitmap bitmap;
    private Bitmap SketchBitmap;
    private FrameLayout frame_layout;
    public boolean isSplashView;
    public RecyclerView recycler_view_splash;
    public TextView image_view_shape;
    public SketchListener sketchListener;
    private PolishSplashSticker polishSplashSticker;
    public PolishSplashSquareView polishSplashView;
    private ViewGroup viewGroup;

    public interface SketchListener {
        void onSaveSketch(Bitmap bitmap);
    }

    public void setPolishSplashView(boolean z) {
        this.isSplashView = z;
    }

    public void setBitmap(Bitmap bitmap2) {
        this.bitmap = bitmap2;
    }

    public static SketchSquareFragment show(@NonNull AppCompatActivity appCompatActivity, Bitmap bitmap2, Bitmap bitmap3, Bitmap bitmap4, SketchListener sketchListener, boolean z) {
        SketchSquareFragment splashDialog = new SketchSquareFragment();
        splashDialog.setBitmap(bitmap2);
        splashDialog.setSketchBitmap(bitmap4);
        splashDialog.setSketchListener(sketchListener);
        splashDialog.setPolishSplashView(z);
        splashDialog.show(appCompatActivity.getSupportFragmentManager(), TAG);
        return splashDialog;
    }

    public void setSketchBitmap(Bitmap bitmap2) {
        this.SketchBitmap = bitmap2;
    }

    public void setSketchListener(SketchListener sketchListener) {
        this.sketchListener = sketchListener;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setRetainInstance(true);
    }

    @SuppressLint("WrongConstant")
    @Nullable
    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup2, @Nullable Bundle bundle) {
        getDialog().getWindow().requestFeature(1);
        getDialog().getWindow().setFlags(1024, 1024);
        View inflate = layoutInflater.inflate(R.layout.fragment_square, viewGroup2, false);
        this.viewGroup = viewGroup2;
        this.image_view_background = inflate.findViewById(R.id.imageViewBackground);
        this.polishSplashView = inflate.findViewById(R.id.splashView);
        this.frame_layout = inflate.findViewById(R.id.frame_layout);
        this.image_view_background.setImageBitmap(this.SketchBitmap);
        this.image_view_shape = inflate.findViewById(R.id.textViewTitle);
        if (this.isSplashView) {
            this.polishSplashView.setImageBitmap(this.bitmap);
            this.image_view_shape.setText("SKETCH SQ");
        }
        this.recycler_view_splash = inflate.findViewById(R.id.recyclerViewSplashSquare);
        this.recycler_view_splash.setLayoutManager(new LinearLayoutManager(getContext(), 0, false));
        this.recycler_view_splash.setHasFixedSize(true);
        this.recycler_view_splash.setAdapter(new SplashSquareAdapter(getContext(), this, this.isSplashView));
        if (this.isSplashView) {
            this.polishSplashSticker = new PolishSplashSticker(StickerFileAsset.loadBitmapFromAssets(getContext(), "frame/image_mask_1.webp"), StickerFileAsset.loadBitmapFromAssets(getContext(), "frame/image_frame_1.webp"));
            this.polishSplashView.addSticker(this.polishSplashSticker);
        }

        this.polishSplashView.refreshDrawableState();
        this.polishSplashView.setLayerType(2, null);
        this.image_view_shape.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                SketchSquareFragment.this.polishSplashView.setcSplashMode(0);
                SketchSquareFragment.this.recycler_view_splash.setVisibility(View.VISIBLE);
                SketchSquareFragment.this.polishSplashView.refreshDrawableState();
                SketchSquareFragment.this.polishSplashView.invalidate();
            }
        });
        inflate.findViewById(R.id.imageViewSaveSplash).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                SketchSquareFragment.this.sketchListener.onSaveSketch(SketchSquareFragment.this.polishSplashView.getBitmap(SketchSquareFragment.this.SketchBitmap));
                SketchSquareFragment.this.dismiss();
            }
        });
        inflate.findViewById(R.id.imageViewCloseSplash).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                SketchSquareFragment.this.dismiss();
            }
        });
        return inflate;
    }

    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        getDialog().getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle bundle) {
        super.onViewCreated(view, bundle);
    }

    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(-1, -1);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(-16777216));
        }
    }

    public void onDestroy() {
        super.onDestroy();
        this.polishSplashView.getSticker().release();
        if (this.SketchBitmap != null) {
            this.SketchBitmap.recycle();
        }
        this.SketchBitmap = null;
        this.bitmap = null;
    }

    public void onSelected(PolishSplashSticker splashSticker2) {
        this.polishSplashView.addSticker(splashSticker2);
    }

}
