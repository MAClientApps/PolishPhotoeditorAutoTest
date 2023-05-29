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

public class BlurSquareBgFragment extends DialogFragment implements SplashSquareAdapter.SplashChangeListener {
    private static final String TAG = "BlurSquareBgFragment";
    private ImageView imageViewBackground;
    public Bitmap bitmap;
    private Bitmap BlurBitmap;
    private FrameLayout frameLayout;
    public boolean isSplashView;
    public RecyclerView recyclerViewBlur;
    public TextView textVewTitle;
    public BlurSquareBgListener blurSquareBgListener;
    private PolishSplashSticker polishSplashSticker;
    public PolishSplashSquareView polishSplashView;
    private ViewGroup viewGroup;

    public interface BlurSquareBgListener {
        void onSaveBlurBackground(Bitmap bitmap);
    }

    public void setPolishSplashView(boolean z) {
        this.isSplashView = z;
    }

    public void setBitmap(Bitmap bitmap2) {
        this.bitmap = bitmap2;
    }

    public static BlurSquareBgFragment show(@NonNull AppCompatActivity appCompatActivity, Bitmap bitmap2, Bitmap bitmap3, Bitmap bitmap4, BlurSquareBgListener blurSquareBgListener, boolean z) {
        BlurSquareBgFragment splashDialog = new BlurSquareBgFragment();
        splashDialog.setBitmap(bitmap2);
        splashDialog.setBlurBitmap(bitmap4);
        splashDialog.setBlurSquareBgListener(blurSquareBgListener);
        splashDialog.setPolishSplashView(z);
        splashDialog.show(appCompatActivity.getSupportFragmentManager(), TAG);
        return splashDialog;
    }

    public void setBlurBitmap(Bitmap bitmap2) {
        this.BlurBitmap = bitmap2;
    }


    public void setBlurSquareBgListener(BlurSquareBgListener blurSquareBgListener) {
        this.blurSquareBgListener = blurSquareBgListener;
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
        this.imageViewBackground = inflate.findViewById(R.id.imageViewBackground);
        this.polishSplashView = inflate.findViewById(R.id.splashView);
        this.frameLayout = inflate.findViewById(R.id.frame_layout);
        this.imageViewBackground.setImageBitmap(this.bitmap);
        this.textVewTitle = inflate.findViewById(R.id.textViewTitle);
        if (this.isSplashView) {
            this.polishSplashView.setImageBitmap(this.BlurBitmap);
            this.textVewTitle.setText("BLUR BG");
        }
        this.recyclerViewBlur = inflate.findViewById(R.id.recyclerViewSplashSquare);
        this.recyclerViewBlur.setLayoutManager(new LinearLayoutManager(getContext(), 0, false));
        this.recyclerViewBlur.setHasFixedSize(true);
        this.recyclerViewBlur.setAdapter(new SplashSquareAdapter(getContext(), this, this.isSplashView));
        if (this.isSplashView) {
            this.polishSplashSticker = new PolishSplashSticker(StickerFileAsset.loadBitmapFromAssets(getContext(), "frame/image_mask_1.webp"), StickerFileAsset.loadBitmapFromAssets(getContext(), "frame/image_frame_1.webp"));
            this.polishSplashView.addSticker(this.polishSplashSticker);
        }

        this.polishSplashView.refreshDrawableState();
        this.polishSplashView.setLayerType(2, null);
        this.textVewTitle.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                BlurSquareBgFragment.this.polishSplashView.setcSplashMode(0);
                BlurSquareBgFragment.this.recyclerViewBlur.setVisibility(View.VISIBLE);
                BlurSquareBgFragment.this.polishSplashView.refreshDrawableState();
                BlurSquareBgFragment.this.polishSplashView.invalidate();
            }
        });
        inflate.findViewById(R.id.imageViewSaveSplash).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                BlurSquareBgFragment.this.blurSquareBgListener.onSaveBlurBackground(BlurSquareBgFragment.this.polishSplashView.getBitmap(BlurSquareBgFragment.this.bitmap));
                BlurSquareBgFragment.this.dismiss();
            }
        });
        inflate.findViewById(R.id.imageViewCloseSplash).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                BlurSquareBgFragment.this.dismiss();
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
        if (this.BlurBitmap != null) {
            this.BlurBitmap.recycle();
        }
        this.BlurBitmap = null;
        this.bitmap = null;
    }

    public void onSelected(PolishSplashSticker splashSticker2) {
        this.polishSplashView.addSticker(splashSticker2);
    }

}
