package com.autotest.photo.editor.activities;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.constraintlayout.widget.Guideline;
import androidx.core.content.ContextCompat;
import androidx.exifinterface.media.ExifInterface;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.hold1.keyboardheightprovider.KeyboardHeightProvider;
import com.autotest.photo.editor.R;
import com.autotest.photo.editor.adapters.AspectAdapter;
import com.autotest.photo.editor.adapters.CollageBackgroundAdapter;
import com.autotest.photo.editor.adapters.CollageColorAdapter;
import com.autotest.photo.editor.adapters.FilterAdapter;
import com.autotest.photo.editor.adapters.GridAdapter;
import com.autotest.photo.editor.adapters.GridItemToolsAdapter;
import com.autotest.photo.editor.adapters.GridToolsAdapter;
import com.autotest.photo.editor.adapters.RecyclerTabLayout;
import com.autotest.photo.editor.adapters.StickerAdapter;
import com.autotest.photo.editor.adapters.StickersTabAdapter;
import com.autotest.photo.editor.assets.FilterFileAsset;
import com.autotest.photo.editor.assets.StickerFileAsset;
import com.autotest.photo.editor.event.AlignHorizontallyEvent;
import com.autotest.photo.editor.event.DeleteIconEvent;
import com.autotest.photo.editor.event.EditTextIconEvent;
import com.autotest.photo.editor.event.FlipHorizontallyEvent;
import com.autotest.photo.editor.event.ZoomIconEvent;
import com.autotest.photo.editor.fragment.CropFragment;
import com.autotest.photo.editor.fragment.FilterFragment;
import com.autotest.photo.editor.fragment.TextFragment;
import com.autotest.photo.editor.listener.FilterListener;
import com.autotest.photo.editor.model.General;
import com.autotest.photo.editor.module.Module;
import com.autotest.photo.editor.picker.PermissionsUtils;
import com.autotest.photo.editor.polish.PolishGridView;
import com.autotest.photo.editor.polish.PolishPickerView;
import com.autotest.photo.editor.polish.PolishStickerIcons;
import com.autotest.photo.editor.polish.PolishStickerView;
import com.autotest.photo.editor.polish.PolishText;
import com.autotest.photo.editor.polish.PolishTextView;
import com.autotest.photo.editor.polish.grid.PolishGrid;
import com.autotest.photo.editor.polish.grid.PolishLayout;
import com.autotest.photo.editor.polish.grid.PolishLayoutParser;
import com.autotest.photo.editor.preference.Preference;
import com.autotest.photo.editor.sticker.DrawableSticker;
import com.autotest.photo.editor.sticker.Sticker;
import com.autotest.photo.editor.utils.CollageUtils;
import com.autotest.photo.editor.utils.FilterUtils;
import com.autotest.photo.editor.utils.SaveFileUtils;
import com.autotest.photo.editor.utils.SystemUtil;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.steelkiwi.cropiwa.AspectRatio;

import org.jetbrains.annotations.NotNull;
import org.wysaid.nativePort.CGENativeLibrary;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

@SuppressLint("StaticFieldLeak")
public class PolishCollageActivity extends PolishBaseActivity implements GridToolsAdapter.OnItemSelected,
        AspectAdapter.OnNewSelectedListener, StickerAdapter.OnClickSplashListener,
        CollageBackgroundAdapter.BackgroundGridListener, FilterListener,
        CropFragment.OnCropPhoto, CollageColorAdapter.BackgroundColorListener,
        FilterFragment.OnFilterSavePhoto, GridItemToolsAdapter.OnPieceFuncItemSelected,
        GridAdapter.OnItemClickListener {
    private static PolishCollageActivity QueShotGridActivityInstance;
    public static PolishCollageActivity QueShotGridActivityCollage;
    public PolishLayout queShotLayout;
    public PolishGridView queShotGridView;
    public AspectRatio aspectRatio;
    public CollageBackgroundAdapter.SquareView currentBackgroundState;
    private RelativeLayout relativeLayoutLoading;
    public GridToolsAdapter gridToolsAdapter = new GridToolsAdapter(this, true);
    private GridItemToolsAdapter gridItemToolsAdapter = new GridItemToolsAdapter(this);
    public LinearLayout linear_layout_wrapper_sticker_list;
    public Module moduleToolsId;
    public ImageView imageViewAddSticker;
    public float BorderRadius;
    public float Padding;
    private int deviceHeight = 0;
    public int deviceWidth = 0;
    // Guideline
    private Guideline guidelineTools;
    private Guideline guideline;
    public TextFragment.TextEditor textEditor;
    public TextFragment addTextFragment;
    private KeyboardHeightProvider keyboardHeightProvider;
    // ConstraintLayout
    public ConstraintLayout constraint_layout_change_background;
    public ConstraintLayout constrant_layout_change_Layout;
    public ConstraintLayout constraint_layout_filter_layout;
    private ConstraintLayout constraint_layout_collage_layout;
    private ConstraintLayout constraint_save_control;
    private ConstraintLayout constraint_layout_wrapper_collage_view;
    public ConstraintLayout constraint_layout_sticker;
    private ConstraintLayout constraintLayoutSaveText;
    private ConstraintLayout constraintLayoutSaveSticker;
    private ConstraintLayout constraintLayoutAddText;
    // RecyclerView
    public RecyclerView recyclerViewTools;
    public RecyclerView recyclerViewFilter;
    private RecyclerView recycler_view_collage;
    private RecyclerView recycler_view_ratio;
    private RecyclerView recycler_view_blur;
    private RecyclerView recycler_view_color;
    private RecyclerView recycler_view_gradient;
    public RecyclerView recyclerViewToolsCollage;
    // ArrayList
    public ArrayList listFilterAll = new ArrayList<>();
    public List<Drawable> drawableList = new ArrayList<>();
    public List<String> stringList;
    public List<Target> targets = new ArrayList();
    // TextView
    private LinearLayout linearLayoutBorder;
    private RelativeLayout relativeLayoutAddText;
    private TextView text_view_save;
    private TextView textViewTitle;
    private TextView textViewSeekBarPadding;
    private TextView textViewSeekBarRadius;
    public TextView textViewCancel;
    public TextView textViewDiscard;
    // SeekBar
    private SeekBar seekBarRadius;
    private SeekBar seekBarPadding;
    public SeekBar seekbarSticker;
    // Ads
    private LinearLayout bannerContainer;

    private Animation slideUpAnimation, slideDownAnimation;

    private LinearLayout linearLayoutLayer;
    private LinearLayout linearLayoutRatio;
    private LinearLayout linearLayoutBorde;
    private TextView textViewListLayer;
    private TextView textViewListRatio;
    private TextView textViewListBorder;
    private View viewCollage;
    private View viewBorder;
    private View viewRatio;

    private LinearLayout linearLayoutColor;
    private LinearLayout linearLayoutGradient;
    private LinearLayout linearLayoutBlur;
    private TextView textViewListColor;
    private TextView textViewListGradient;
    private TextView textViewListBlur;
    private View viewColor;
    private View viewGradient;
    private View viewBlur;
    private InterstitialAd mInterstitialAd;
    private AdRequest adRequest;
    private AdView adView;
    private General generalModel;

    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        setFullScreen();
        setContentView(R.layout.activity_polish_collage);
        if (Build.VERSION.SDK_INT < 30) {
            getWindow().setSoftInputMode(72);
        }
        this.deviceWidth = getResources().getDisplayMetrics().widthPixels;
        this.deviceHeight = getResources().getDisplayMetrics().heightPixels;

        findViewById(R.id.image_view_exit).setOnClickListener(view -> PolishCollageActivity.this.onBackPressed());
        this.queShotGridView = findViewById(R.id.collage_view);
        this.bannerContainer = findViewById(R.id.bannerContainer);
        this.constraintLayoutSaveText = findViewById(R.id.constraint_layout_confirm_save_text);
        this.constraintLayoutSaveSticker = findViewById(R.id.constraint_layout_confirm_save_sticker);
        this.constraint_layout_wrapper_collage_view = findViewById(R.id.constraint_layout_wrapper_collage_view);
        this.constraint_layout_filter_layout = findViewById(R.id.constraint_layout_filter_layout);
        this.recyclerViewTools = findViewById(R.id.recycler_view_tools);
        this.recyclerViewTools.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        this.recyclerViewTools.setAdapter(this.gridToolsAdapter);
        this.recyclerViewToolsCollage = findViewById(R.id.recycler_view_tools_collage);
        this.recyclerViewToolsCollage.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        this.recyclerViewToolsCollage.setAdapter(this.gridItemToolsAdapter);
        this.seekBarPadding = findViewById(R.id.seekbar_border);
        this.seekBarPadding.setOnSeekBarChangeListener(this.onSeekBarChangeListener);
        this.seekBarRadius = findViewById(R.id.seekbar_radius);
        this.seekBarRadius.setOnSeekBarChangeListener(this.onSeekBarChangeListener);
        this.stringList = getIntent().getStringArrayListExtra(GridPickerActivity.KEY_DATA_RESULT);
        this.relativeLayoutLoading = findViewById(R.id.relative_layout_loading);
        this.recyclerViewFilter = findViewById(R.id.recycler_view_filter);
        this.linearLayoutBorder = findViewById(R.id.linearLayoutPadding);
        this.guidelineTools = findViewById(R.id.guidelineTools);
        this.guideline = findViewById(R.id.guideline);
        this.relativeLayoutAddText = findViewById(R.id.relative_layout_add_text);
        this.relativeLayoutAddText.setVisibility(View.GONE);
        this.constraintLayoutAddText = findViewById(R.id.constraint_layout_confirm_text);
        this.queShotLayout = CollageUtils.getCollageLayouts(this.stringList.size()).get(0);
        this.queShotGridView.setQueShotLayout(this.queShotLayout);
        this.queShotGridView.setTouchEnable(true);
        this.queShotGridView.setNeedDrawLine(false);
        this.queShotGridView.setNeedDrawOuterLine(false);
        this.queShotGridView.setLineSize(4);
        this.queShotGridView.setCollagePadding(6.0f);
        this.queShotGridView.setCollageRadian(15.0f);
        this.queShotGridView.setLineColor(ContextCompat.getColor(this, R.color.white));
        this.queShotGridView.setSelectedLineColor(ContextCompat.getColor(this, R.color.mainColor));
        this.queShotGridView.setHandleBarColor(ContextCompat.getColor(this, R.color.mainColor));
        this.queShotGridView.setAnimateDuration(300);
        this.queShotGridView.setOnQueShotSelectedListener((collage, i) -> {
            PolishCollageActivity.this.recyclerViewTools.setVisibility(View.GONE);
            PolishCollageActivity.this.recyclerViewToolsCollage.setVisibility(View.VISIBLE);
            PolishCollageActivity.this.slideUp(PolishCollageActivity.this.recyclerViewToolsCollage);
            PolishCollageActivity.this.setGoneSave();
            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) PolishCollageActivity.this.recyclerViewToolsCollage.getLayoutParams();
            layoutParams.bottomMargin = SystemUtil.dpToPx(PolishCollageActivity.this.getApplicationContext(), 10);
            PolishCollageActivity.this.recyclerViewToolsCollage.setLayoutParams(layoutParams);
            PolishCollageActivity.this.moduleToolsId = Module.COLLAGE;
        });
        this.queShotGridView.setOnQueShotUnSelectedListener(() -> {
            PolishCollageActivity.this.recyclerViewToolsCollage.setVisibility(View.GONE);
            PolishCollageActivity.this.recyclerViewTools.setVisibility(View.VISIBLE);
            setVisibleSave();
            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) recyclerViewToolsCollage.getLayoutParams();
            layoutParams.bottomMargin = 0;
            recyclerViewToolsCollage.setLayoutParams(layoutParams);
            moduleToolsId = Module.NONE;
        });

        this.constraint_save_control = findViewById(R.id.constraint_save_control);
        this.queShotGridView.post(() -> PolishCollageActivity.this.loadPhoto());
        findViewById(R.id.imageViewSaveLayer).setOnClickListener(this.onClickListener);
        findViewById(R.id.imageViewCloseLayer).setOnClickListener(this.onClickListener);
        findViewById(R.id.imageViewSaveText).setOnClickListener(this.onClickListener);
        findViewById(R.id.imageViewCloseText).setOnClickListener(this.onClickListener);
        findViewById(R.id.imageViewClosebackground).setOnClickListener(this.onClickListener);
        findViewById(R.id.image_view_close_sticker).setOnClickListener(this.onClickListener);
        findViewById(R.id.imageViewSaveFilter).setOnClickListener(this.onClickListener);
        findViewById(R.id.imageViewSavebackground).setOnClickListener(this.onClickListener);
        findViewById(R.id.image_view_save_sticker).setOnClickListener(this.onClickListener);
        findViewById(R.id.imageViewCloseFilter).setOnClickListener(this.onClickListener);
        this.linearLayoutLayer = findViewById(R.id.linearLayoutCollage);
        this.linearLayoutBorde = findViewById(R.id.linearLayoutBorder);
        this.linearLayoutRatio = findViewById(R.id.linearLayoutRatio);
        this.textViewListLayer = findViewById(R.id.text_view_collage);
        this.textViewListBorder = findViewById(R.id.text_view_border);
        this.textViewListRatio = findViewById(R.id.text_view_ratio);
        this.viewCollage = findViewById(R.id.view_collage);
        this.viewBorder = findViewById(R.id.view_border);
        this.viewRatio = findViewById(R.id.view_ratio);
        this.linearLayoutLayer.setOnClickListener(view -> PolishCollageActivity.this.setLayer());
        this.linearLayoutBorde.setOnClickListener(view -> PolishCollageActivity.this.setBorder());
        this.linearLayoutRatio.setOnClickListener(view -> PolishCollageActivity.this.setRatio());

        this.linearLayoutColor = findViewById(R.id.linearLayoutColor);
        this.linearLayoutGradient = findViewById(R.id.linearLayoutGradient);
        this.linearLayoutBlur = findViewById(R.id.linearLayoutBlur);
        this.textViewListColor = findViewById(R.id.text_view_color);
        this.textViewListGradient = findViewById(R.id.text_view_gradient);
        this.textViewListBlur = findViewById(R.id.text_view_blur);
        this.viewGradient = findViewById(R.id.view_gradient);
        this.viewBlur = findViewById(R.id.view_blur);
        this.viewColor = findViewById(R.id.view_color);
        this.linearLayoutColor.setOnClickListener(view -> PolishCollageActivity.this.setBackgroundColor());
        this.linearLayoutGradient.setOnClickListener(view -> PolishCollageActivity.this.setBackgroundGradient());
        this.linearLayoutBlur.setOnClickListener(view -> PolishCollageActivity.this.selectBackgroundBlur());

        this.constrant_layout_change_Layout = findViewById(R.id.constrant_layout_change_Layout);
        this.textViewTitle = findViewById(R.id.textViewTitle);
        this.textViewSeekBarPadding = findViewById(R.id.seekbarPadding);
        this.textViewSeekBarRadius = findViewById(R.id.seekbarRadius);
        GridAdapter collageAdapter = new GridAdapter();
        this.recycler_view_collage = findViewById(R.id.recycler_view_collage);
        this.recycler_view_collage.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        this.recycler_view_collage.setAdapter(collageAdapter);
        collageAdapter.refreshData(CollageUtils.getCollageLayouts(this.stringList.size()), null);
        collageAdapter.setOnItemClickListener(this);
        AspectAdapter aspectRatioPreviewAdapter = new AspectAdapter(true);
        aspectRatioPreviewAdapter.setListener(this);
        this.recycler_view_ratio = findViewById(R.id.recycler_view_ratio);
        this.recycler_view_ratio.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        this.recycler_view_ratio.setAdapter(aspectRatioPreviewAdapter);
        this.linear_layout_wrapper_sticker_list = findViewById(R.id.linear_layout_wrapper_sticker_list);
        ViewPager stickerViewPager = findViewById(R.id.stickerViewpaper);
        this.constraint_layout_sticker = findViewById(R.id.constraint_layout_sticker);
        this.seekbarSticker = findViewById(R.id.seekbarStickerAlpha);
        this.seekbarSticker.setVisibility(View.GONE);
        this.seekbarSticker.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                Sticker currentSticker = PolishCollageActivity.this.queShotGridView.getCurrentSticker();
                if (currentSticker != null) {
                    currentSticker.setAlpha(i);
                }
            }
        });

        this.relativeLayoutAddText.setOnClickListener(view -> {
            PolishCollageActivity.this.queShotGridView.setHandlingSticker(null);
            PolishCollageActivity.this.openTextFragment();
        });

            //init Ads
            initInterstitialAd();
            adView = findViewById(R.id.adView);
            adRequest = new AdRequest.Builder().build();
            adView.loadAd(adRequest);


        this.text_view_save = findViewById(R.id.text_view_save);
        this.text_view_save.setOnClickListener(view -> {

            if (mInterstitialAd != null) {
                mInterstitialAd.show(Objects.requireNonNull(PolishCollageActivity.this));
                mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                    @Override
                    public void onAdDismissedFullScreenContent() {
                        // Called when fullscreen content is dismissed.
                        SaveView();
                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(AdError adError) {
                        // Called when fullscreen content failed to show.
                        SaveView();
                    }

                    @Override
                    public void onAdShowedFullScreenContent() {
                        // Called when fullscreen content is shown.
                        // Make sure to set your reference to null so you don't
                        // show it a second time.
                        mInterstitialAd = null;
                    }

                });
            } else {
                SaveView();
                    initInterstitialAd();
            }
        });
        this.imageViewAddSticker = findViewById(R.id.imageViewAddSticker);
        this.imageViewAddSticker.setVisibility(View.GONE);
        this.imageViewAddSticker.setOnClickListener(view -> {
            imageViewAddSticker.setVisibility(View.GONE);
            linear_layout_wrapper_sticker_list.setVisibility(View.VISIBLE);
        });

        PolishStickerIcons quShotStickerIconClose = new PolishStickerIcons(ContextCompat.getDrawable(this, R.drawable.ic_outline_close), 0, PolishStickerIcons.DELETE);
        quShotStickerIconClose.setIconEvent(new DeleteIconEvent());
        PolishStickerIcons quShotStickerIconScale = new PolishStickerIcons(ContextCompat.getDrawable(this, R.drawable.ic_outline_scale), 3, PolishStickerIcons.SCALE);
        quShotStickerIconScale.setIconEvent(new ZoomIconEvent());
        PolishStickerIcons quShotStickerIconFlip = new PolishStickerIcons(ContextCompat.getDrawable(this, R.drawable.ic_outline_flip), 1, PolishStickerIcons.FLIP);
        quShotStickerIconFlip.setIconEvent(new FlipHorizontallyEvent());
        PolishStickerIcons quShotStickerIconCenter = new PolishStickerIcons(ContextCompat.getDrawable(this, R.drawable.ic_outline_center), 2, PolishStickerIcons.ALIGN);
        quShotStickerIconCenter.setIconEvent(new AlignHorizontallyEvent());
        PolishStickerIcons quShotStickerIconRotate = new PolishStickerIcons(ContextCompat.getDrawable(this, R.drawable.ic_outline_rotate), 3, PolishStickerIcons.ROTATE);
        quShotStickerIconRotate.setIconEvent(new ZoomIconEvent());
        PolishStickerIcons quShotStickerIconEdit = new PolishStickerIcons(ContextCompat.getDrawable(this, R.drawable.ic_outline_edit), 1, PolishStickerIcons.EDIT);
        quShotStickerIconEdit.setIconEvent(new EditTextIconEvent());
        this.queShotGridView.setIcons(Arrays.asList(new PolishStickerIcons[]{quShotStickerIconClose, quShotStickerIconScale, quShotStickerIconFlip, quShotStickerIconEdit, quShotStickerIconRotate, quShotStickerIconCenter}));
        this.queShotGridView.setConstrained(true);
        this.queShotGridView.setOnStickerOperationListener(this.onStickerOperationListener);
        stickerViewPager.setAdapter(new PagerAdapter() {
            public int getCount() {
                return 15;
            }

            public boolean isViewFromObject(@NonNull View view, @NonNull Object obj) {
                return view.equals(obj);
            }

            @Override
            public void destroyItem(@NonNull @NotNull ViewGroup container, int position, @NonNull @NotNull Object object) {
                (container).removeView((View) object);

            }

            @NonNull
            public Object instantiateItem(@NonNull ViewGroup viewGroup, int i) {
                View inflate = LayoutInflater.from(PolishCollageActivity.this.getBaseContext()).inflate(R.layout.list_sticker, null, false);
                RecyclerView recycler_view_sticker = inflate.findViewById(R.id.recyclerViewSticker);
                recycler_view_sticker.setHasFixedSize(true);
                recycler_view_sticker.setLayoutManager(new GridLayoutManager(PolishCollageActivity.this.getApplicationContext(), 7));
                switch (i) {
                    case 0:
                        recycler_view_sticker.setAdapter(new StickerAdapter(PolishCollageActivity.this.getApplicationContext(), StickerFileAsset.bubbleList(), i, PolishCollageActivity.this));
                        break;
                    case 1:
                        recycler_view_sticker.setAdapter(new StickerAdapter(PolishCollageActivity.this.getApplicationContext(), StickerFileAsset.rainbowList(), i, PolishCollageActivity.this));
                        break;
                    case 2:
                        recycler_view_sticker.setAdapter(new StickerAdapter(PolishCollageActivity.this.getApplicationContext(), StickerFileAsset.cartoonList(), i, PolishCollageActivity.this));
                        break;
                    case 3:
                        recycler_view_sticker.setAdapter(new StickerAdapter(PolishCollageActivity.this.getApplicationContext(), StickerFileAsset.childList(), i, PolishCollageActivity.this));
                        break;
                    case 4:
                        recycler_view_sticker.setAdapter(new StickerAdapter(PolishCollageActivity.this.getApplicationContext(), StickerFileAsset.flowerList(), i, PolishCollageActivity.this));
                        break;
                    case 5:
                        recycler_view_sticker.setAdapter(new StickerAdapter(PolishCollageActivity.this.getApplicationContext(), StickerFileAsset.amojiList(), i, PolishCollageActivity.this));
                        break;
                    case 6:
                        recycler_view_sticker.setAdapter(new StickerAdapter(PolishCollageActivity.this.getApplicationContext(), StickerFileAsset.deliciousList(), i, PolishCollageActivity.this));
                        break;
                    case 7:
                        recycler_view_sticker.setAdapter(new StickerAdapter(PolishCollageActivity.this.getApplicationContext(), StickerFileAsset.handList(), i, PolishCollageActivity.this));
                        break;
                    case 8:
                        recycler_view_sticker.setAdapter(new StickerAdapter(PolishCollageActivity.this.getApplicationContext(), StickerFileAsset.popularList(), i, PolishCollageActivity.this));
                        break;
                    case 9:
                        recycler_view_sticker.setAdapter(new StickerAdapter(PolishCollageActivity.this.getApplicationContext(), StickerFileAsset.valentineList(), i, PolishCollageActivity.this));
                        break;
                    case 10:
                        recycler_view_sticker.setAdapter(new StickerAdapter(PolishCollageActivity.this.getApplicationContext(), StickerFileAsset.emojList(), i, PolishCollageActivity.this));
                        break;
                    case 11:
                        recycler_view_sticker.setAdapter(new StickerAdapter(PolishCollageActivity.this.getApplicationContext(), StickerFileAsset.rageList(), i, PolishCollageActivity.this));
                        break;
                    case 12:
                        recycler_view_sticker.setAdapter(new StickerAdapter(PolishCollageActivity.this.getApplicationContext(), StickerFileAsset.christmasList(), i, PolishCollageActivity.this));
                        break;
                    case 13:
                        recycler_view_sticker.setAdapter(new StickerAdapter(PolishCollageActivity.this.getApplicationContext(), StickerFileAsset.unicornList(), i, PolishCollageActivity.this));
                        break;
                    case 14:
                        recycler_view_sticker.setAdapter(new StickerAdapter(PolishCollageActivity.this.getApplicationContext(), StickerFileAsset.stickerList(), i, PolishCollageActivity.this));
                        break;


                }
                viewGroup.addView(inflate);
                return inflate;
            }
        });
        RecyclerTabLayout recycler_tab_layout = findViewById(R.id.recycler_tab_layout);
        recycler_tab_layout.setUpWithAdapter(new StickersTabAdapter(stickerViewPager, getApplicationContext()));
        recycler_tab_layout.setPositionThreshold(0.5f);
        recycler_tab_layout.setBackgroundColor(ContextCompat.getColor(this, R.color.TabColor));

        Preference.setKeyboard(getApplicationContext(), 0);
        this.keyboardHeightProvider = new KeyboardHeightProvider(this);
        this.keyboardHeightProvider.addKeyboardListener(i -> {
            if (i <= 0) {
                Preference.setHeightOfNotch(getApplicationContext(), -i);
            } else if (addTextFragment != null) {
                addTextFragment.updateAddTextBottomToolbarHeight(Preference.getHeightOfNotch(getApplicationContext()) + i);
                Preference.setKeyboard(getApplicationContext(), i + Preference.getHeightOfNotch(getApplicationContext()));
            }
        });

        setLoading(false);
        this.constraint_layout_change_background = findViewById(R.id.constrant_layout_change_background);
        this.constraint_layout_collage_layout = findViewById(R.id.constraint_layout_collage_layout);
        this.currentBackgroundState = new CollageBackgroundAdapter.SquareView(Color.parseColor("#ffffff"), "", true);
        this.recycler_view_color = findViewById(R.id.recycler_view_color);
        this.recycler_view_color.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL, false));
        this.recycler_view_color.setHasFixedSize(true);
        this.recycler_view_color.setAdapter(new CollageColorAdapter(getApplicationContext(), this));
        this.recycler_view_gradient = findViewById(R.id.recycler_view_gradient);
        this.recycler_view_gradient.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL, false));
        this.recycler_view_gradient.setHasFixedSize(true);
        this.recycler_view_gradient.setAdapter(new CollageBackgroundAdapter(getApplicationContext(), (CollageBackgroundAdapter.BackgroundGridListener) this, true));
        this.recycler_view_blur = findViewById(R.id.recycler_view_blur);
        this.recycler_view_blur.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL, false));
        this.recycler_view_blur.setHasFixedSize(true);
        this.recycler_view_blur.setAdapter(new CollageBackgroundAdapter(getApplicationContext(), (CollageBackgroundAdapter.BackgroundGridListener) this, true));
        Display defaultDisplay = getWindowManager().getDefaultDisplay();
        Point point = new Point();
        defaultDisplay.getSize(point);
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) this.queShotGridView.getLayoutParams();
        layoutParams.height = point.x;
        layoutParams.width = point.x;
        this.queShotGridView.setLayoutParams(layoutParams);
        this.aspectRatio = new AspectRatio(1, 1);
        this.queShotGridView.setAspectRatio(new AspectRatio(1, 1));
        QueShotGridActivityCollage = this;
        this.moduleToolsId = Module.NONE;
        CGENativeLibrary.setLoadImageCallback(this.loadImageCallback, (Object) null);
        QueShotGridActivityInstance = this;

        this.recyclerViewToolsCollage.setAlpha(0.0f);
        this.constraint_layout_collage_layout.post(() -> {
            slideDown(recyclerViewToolsCollage);
        });
        new Handler().postDelayed(() -> {
            recyclerViewToolsCollage.setAlpha(1.0f);
        }, 1000);

    }

    ActivityResultLauncher<Intent> paymentResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    // There are no request codes
                    /*if(recyclerViewFilter!=null)recyclerViewFilter.getAdapter().notifyDataSetChanged();
                    if(recycler_view_color!=null)recycler_view_color.getAdapter().notifyDataSetChanged();
                    if(recycler_view_collage!=null)recycler_view_collage.getAdapter().notifyDataSetChanged();*/

                    recyclerViewTools.setVisibility(View.VISIBLE);
                    adView = findViewById(R.id.adView);
                    adView.setVisibility(View.GONE);
                    mInterstitialAd=null;
                }
            });

    private void SaveView() {
        if (PermissionsUtils.checkWriteStoragePermission(PolishCollageActivity.this)) {
            Bitmap createBitmap = SaveFileUtils.createBitmap(PolishCollageActivity.this.queShotGridView, 1920);
            Bitmap createBitmap2 = PolishCollageActivity.this.queShotGridView.createBitmap();
            new SaveCollageAsFile().execute(new Bitmap[]{createBitmap, createBitmap2});
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            this.queShotGridView.reset();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onPause() {
        super.onPause();
        this.keyboardHeightProvider.onPause();
    }

    public void onResume() {
        super.onResume();
        this.keyboardHeightProvider.onResume();
    }

    public void slideDown(View view) {
        ObjectAnimator.ofFloat(view, "translationY", 0.0f, (float) view.getHeight()).start();
    }

    public void slideUp(View view) {
        ObjectAnimator.ofFloat(view, "translationY", new float[]{(float) view.getHeight(), 0.0f}).start();
    }

    private void openTextFragment() {
        this.addTextFragment = TextFragment.show(this);
        this.textEditor = new TextFragment.TextEditor() {
            public void onDone(PolishText addTextProperties) {
                PolishCollageActivity.this.queShotGridView.addSticker(new PolishTextView(PolishCollageActivity.this.getApplicationContext(), addTextProperties));
            }

            public void onBackButton() {
                if (PolishCollageActivity.this.queShotGridView.getStickers().isEmpty()) {
                    PolishCollageActivity.this.onBackPressed();
                }
            }
        };
        this.addTextFragment.setOnTextEditorListener(this.textEditor);
    }


    @SuppressLint("NonConstantResourceId")
    public View.OnClickListener onClickListener = view -> {
        switch (view.getId()) {
            case R.id.imageViewClosebackground:
            case R.id.imageViewCloseFilter:
            case R.id.imageViewCloseLayer:
            case R.id.image_view_close_sticker:
            case R.id.imageViewCloseText:
                PolishCollageActivity.this.setVisibleSave();
                PolishCollageActivity.this.onBackPressed();
                return;
            case R.id.imageViewSavebackground:
                setGuideLineTools();
                this.recyclerViewTools.setVisibility(View.VISIBLE);
                this.constraint_layout_change_background.setVisibility(View.GONE);
                this.recyclerViewToolsCollage.setVisibility(View.VISIBLE);
                PolishCollageActivity.this.setVisibleSave();
                PolishCollageActivity.this.queShotGridView.setLocked(true);
                PolishCollageActivity.this.queShotGridView.setTouchEnable(true);
                if (PolishCollageActivity.this.queShotGridView.getBackgroundResourceMode() == 0) {
                    PolishCollageActivity.this.currentBackgroundState.isColor = true;
                    PolishCollageActivity.this.currentBackgroundState.isBitmap = false;
                    PolishCollageActivity.this.currentBackgroundState.drawableId = ((ColorDrawable) PolishCollageActivity.this.queShotGridView.getBackground()).getColor();
                    PolishCollageActivity.this.currentBackgroundState.drawable = null;
                } else if (PolishCollageActivity.this.queShotGridView.getBackgroundResourceMode() == 1) {
                    PolishCollageActivity.this.currentBackgroundState.isColor = false;
                    PolishCollageActivity.this.currentBackgroundState.isBitmap = false;
                    PolishCollageActivity.this.currentBackgroundState.drawable = PolishCollageActivity.this.queShotGridView.getBackground();
                } else {
                    PolishCollageActivity.this.currentBackgroundState.isColor = false;
                    PolishCollageActivity.this.currentBackgroundState.isBitmap = true;
                    PolishCollageActivity.this.currentBackgroundState.drawable = PolishCollageActivity.this.queShotGridView.getBackground();
                }
                PolishCollageActivity.this.moduleToolsId = Module.NONE;
                return;
            case R.id.imageViewSaveFilter:
                setGuideLineTools();
                this.recyclerViewTools.setVisibility(View.VISIBLE);
                this.constraint_layout_filter_layout.setVisibility(View.GONE);
                this.recyclerViewToolsCollage.setVisibility(View.VISIBLE);
                PolishCollageActivity.this.moduleToolsId = Module.NONE;
                return;
            case R.id.imageViewSaveText:
                setGuideLineTools();
                this.recyclerViewTools.setVisibility(View.VISIBLE);
                this.constraintLayoutAddText.setVisibility(View.GONE);
                this.constraintLayoutSaveText.setVisibility(View.GONE);
                this.queShotGridView.setHandlingSticker(null);
                this.queShotGridView.setLocked(true);
                this.relativeLayoutAddText.setVisibility(View.GONE);
                setVisibleSave();
                this.moduleToolsId = Module.NONE;
                return;
            case R.id.imageViewSaveLayer:
                setGuideLineTools();
                this.recyclerViewTools.setVisibility(View.VISIBLE);
                this.constrant_layout_change_Layout.setVisibility(View.GONE);
                PolishCollageActivity.this.setVisibleSave();
                this.recyclerViewToolsCollage.setVisibility(View.VISIBLE);
                PolishCollageActivity.this.queShotLayout = PolishCollageActivity.this.queShotGridView.getQueShotLayout();
                PolishCollageActivity.this.BorderRadius = PolishCollageActivity.this.queShotGridView.getCollageRadian();
                PolishCollageActivity.this.Padding = PolishCollageActivity.this.queShotGridView.getCollagePadding();
                PolishCollageActivity.this.queShotGridView.setLocked(true);
                PolishCollageActivity.this.queShotGridView.setTouchEnable(true);
                PolishCollageActivity.this.aspectRatio = PolishCollageActivity.this.queShotGridView.getAspectRatio();
                PolishCollageActivity.this.moduleToolsId = Module.NONE;
                return;
            case R.id.image_view_save_sticker:
                setGuideLineTools();
                this.recyclerViewTools.setVisibility(View.VISIBLE);
                this.constraint_layout_sticker.setVisibility(View.GONE);
                this.constraintLayoutSaveSticker.setVisibility(View.GONE);
                PolishCollageActivity.this.queShotGridView.setHandlingSticker(null);
                PolishCollageActivity.this.seekbarSticker.setVisibility(View.GONE);
                PolishCollageActivity.this.imageViewAddSticker.setVisibility(View.GONE);
                PolishCollageActivity.this.linear_layout_wrapper_sticker_list.setVisibility(View.VISIBLE);
                PolishCollageActivity.this.setVisibleSave();
                PolishCollageActivity.this.queShotGridView.setLocked(true);
                PolishCollageActivity.this.queShotGridView.setTouchEnable(true);
                PolishCollageActivity.this.moduleToolsId = Module.NONE;

                this.linear_layout_wrapper_sticker_list.setVisibility(View.VISIBLE);
                this.recyclerViewToolsCollage.setVisibility(View.VISIBLE);
                setVisibleSave();
                return;
            default:
        }
    };


    public SeekBar.OnSeekBarChangeListener onSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        public void onStopTrackingTouch(SeekBar seekBar) {
        }

        public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
            switch (seekBar.getId()) {
                case R.id.seekbar_border:
                    PolishCollageActivity.this.queShotGridView.setCollagePadding((float) i);
                    String valuePadding = String.valueOf(i);
                    textViewSeekBarPadding.setText(valuePadding);
                    break;
                case R.id.seekbar_radius:
                    PolishCollageActivity.this.queShotGridView.setCollageRadian((float) i);
                    String valueRadius = String.valueOf(i);
                    textViewSeekBarRadius.setText(valueRadius);
                    break;
            }
            PolishCollageActivity.this.queShotGridView.invalidate();
        }
    };
    PolishStickerView.OnStickerOperationListener onStickerOperationListener = new PolishStickerView.OnStickerOperationListener() {
        public void onStickerDrag(@NonNull Sticker sticker) {
        }

        public void onStickerFlip(@NonNull Sticker sticker) {
        }

        public void onStickerTouchedDown(@NonNull Sticker sticker) {
        }

        public void onStickerZoom(@NonNull Sticker sticker) {
        }

        public void onTouchDownBeauty(float f, float f2) {
        }

        public void onTouchDragBeauty(float f, float f2) {
        }

        public void onTouchUpBeauty(float f, float f2) {
        }

        public void onAddSticker(@NonNull Sticker sticker) {
            PolishCollageActivity.this.seekbarSticker.setVisibility(View.VISIBLE);
            PolishCollageActivity.this.seekbarSticker.setProgress(sticker.getAlpha());
        }

        public void onStickerSelected(@NonNull Sticker sticker) {
            PolishCollageActivity.this.seekbarSticker.setVisibility(View.VISIBLE);
            PolishCollageActivity.this.seekbarSticker.setProgress(sticker.getAlpha());
        }

        public void onStickerDeleted(@NonNull Sticker sticker) {
            PolishCollageActivity.this.seekbarSticker.setVisibility(View.GONE);
        }

        public void onStickerTouchOutside() {
            PolishCollageActivity.this.seekbarSticker.setVisibility(View.GONE);
        }

        public void onStickerDoubleTap(@NonNull Sticker sticker) {
            if (sticker instanceof PolishTextView) {
                sticker.setShow(false);
                PolishCollageActivity.this.queShotGridView.setHandlingSticker(null);
                PolishCollageActivity.this.addTextFragment = TextFragment.show(PolishCollageActivity.this, ((PolishTextView) sticker).getPolishText());
                PolishCollageActivity.this.textEditor = new TextFragment.TextEditor() {
                    public void onDone(PolishText addTextProperties) {
                        PolishCollageActivity.this.queShotGridView.getStickers().remove(PolishCollageActivity.this.queShotGridView.getLastHandlingSticker());
                        PolishCollageActivity.this.queShotGridView.addSticker(new PolishTextView(PolishCollageActivity.this, addTextProperties));
                    }

                    public void onBackButton() {
                        PolishCollageActivity.this.queShotGridView.showLastHandlingSticker();
                    }
                };
                PolishCollageActivity.this.addTextFragment.setOnTextEditorListener(PolishCollageActivity.this.textEditor);
            }
        }
    };


    public static PolishCollageActivity getQueShotGridActivityInstance() {
        return QueShotGridActivityInstance;
    }

    public void isPermissionGranted(boolean z, String str) {
        if (z) {
            Bitmap createBitmap = SaveFileUtils.createBitmap(this.queShotGridView, 1920);
            Bitmap createBitmap2 = this.queShotGridView.createBitmap();
            new SaveCollageAsFile().execute(createBitmap, createBitmap2);
        }
    }

    public CGENativeLibrary.LoadImageCallback loadImageCallback = new CGENativeLibrary.LoadImageCallback() {
        public Bitmap loadImage(String string, Object object) {
            try {
                return BitmapFactory.decodeStream(PolishCollageActivity.this.getAssets().open(string));
            } catch (IOException ioException) {
                return null;
            }
        }

        public void loadImageOK(Bitmap bitmap, Object object) {
            bitmap.recycle();
        }
    };

    public void setBackgroundColor() {
        this.recycler_view_color.scrollToPosition(0);
        ((CollageColorAdapter) this.recycler_view_color.getAdapter()).setSelectedIndex(-1);
        this.recycler_view_color.getAdapter().notifyDataSetChanged();
        this.recycler_view_color.setVisibility(View.VISIBLE);
        this.recycler_view_gradient.setVisibility(View.GONE);
        this.recycler_view_blur.setVisibility(View.GONE);
        this.textViewListColor.setTextColor(ContextCompat.getColor(this, R.color.white));
        this.textViewListGradient.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        this.textViewListBlur.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        this.viewColor.setVisibility(View.VISIBLE);
        this.viewGradient.setVisibility(View.INVISIBLE);
        this.viewBlur.setVisibility(View.INVISIBLE);
    }

    public void setBackgroundGradient() {
        this.recycler_view_gradient.scrollToPosition(0);
        ((CollageBackgroundAdapter) this.recycler_view_gradient.getAdapter()).setSelectedIndex(-1);
        this.recycler_view_gradient.getAdapter().notifyDataSetChanged();
        this.recycler_view_color.setVisibility(View.GONE);
        this.recycler_view_gradient.setVisibility(View.VISIBLE);
        this.recycler_view_blur.setVisibility(View.GONE);
        this.textViewListColor.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        this.textViewListGradient.setTextColor(ContextCompat.getColor(this, R.color.white));
        this.textViewListBlur.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        this.viewColor.setVisibility(View.INVISIBLE);
        this.viewGradient.setVisibility(View.VISIBLE);
        this.viewBlur.setVisibility(View.INVISIBLE);
    }

    public void selectBackgroundBlur() {
        ArrayList arrayList = new ArrayList();
        for (PolishGrid drawable : this.queShotGridView.getQueShotGrids()) {
            arrayList.add(drawable.getDrawable());
        }
        CollageBackgroundAdapter backgroundGridAdapter = new CollageBackgroundAdapter(getApplicationContext(), this, (List<Drawable>) arrayList);
        backgroundGridAdapter.setSelectedIndex(-1);
        this.recycler_view_blur.setAdapter(backgroundGridAdapter);
        this.recycler_view_color.setVisibility(View.GONE);
        this.recycler_view_gradient.setVisibility(View.GONE);
        this.recycler_view_blur.setVisibility(View.VISIBLE);
        this.textViewListColor.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        this.textViewListGradient.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        this.textViewListBlur.setTextColor(ContextCompat.getColor(this, R.color.white));
        this.viewColor.setVisibility(View.INVISIBLE);
        this.viewGradient.setVisibility(View.INVISIBLE);
        this.viewBlur.setVisibility(View.VISIBLE);
    }

    public void setLayer() {
        this.recycler_view_collage.setVisibility(View.VISIBLE);
        this.recycler_view_ratio.setVisibility(View.GONE);
        this.linearLayoutBorder.setVisibility(View.GONE);
        this.textViewListLayer.setTextColor(ContextCompat.getColor(this, R.color.white));
        this.textViewListBorder.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        this.textViewListRatio.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        this.viewCollage.setVisibility(View.VISIBLE);
        this.viewBorder.setVisibility(View.INVISIBLE);
        this.viewRatio.setVisibility(View.INVISIBLE);
    }

    public void setBorder() {
        this.recycler_view_collage.setVisibility(View.GONE);
        this.recycler_view_ratio.setVisibility(View.GONE);
        this.linearLayoutBorder.setVisibility(View.VISIBLE);
        this.textViewListLayer.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        this.textViewListBorder.setTextColor(ContextCompat.getColor(this, R.color.white));
        this.textViewListRatio.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        this.viewCollage.setVisibility(View.INVISIBLE);
        this.viewBorder.setVisibility(View.VISIBLE);
        this.viewRatio.setVisibility(View.INVISIBLE);
        this.seekBarPadding.setProgress((int) this.queShotGridView.getCollagePadding());
        this.seekBarRadius.setProgress((int) this.queShotGridView.getCollageRadian());

    }

    public void setRatio() {
        this.recycler_view_collage.setVisibility(View.GONE);
        this.recycler_view_ratio.setVisibility(View.VISIBLE);
        this.linearLayoutBorder.setVisibility(View.GONE);
        this.textViewListLayer.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        this.textViewListBorder.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        this.textViewListRatio.setTextColor(ContextCompat.getColor(this, R.color.white));
        this.viewCollage.setVisibility(View.INVISIBLE);
        this.viewBorder.setVisibility(View.INVISIBLE);
        this.viewRatio.setVisibility(View.VISIBLE);

    }

    public void onToolSelected(Module module) {
        this.moduleToolsId = module;
        switch (module) {
            case LAYER:
                setLayer();
                setGuideLine();
                this.constrant_layout_change_Layout.setVisibility(View.VISIBLE);
                this.recyclerViewTools.setVisibility(View.GONE);
                this.recyclerViewToolsCollage.setVisibility(View.GONE);
                this.queShotLayout = this.queShotGridView.getQueShotLayout();
                this.aspectRatio = this.queShotGridView.getAspectRatio();
                this.BorderRadius = this.queShotGridView.getCollageRadian();
                this.Padding = this.queShotGridView.getCollagePadding();
                this.recycler_view_collage.scrollToPosition(0);
                ((GridAdapter) this.recycler_view_collage.getAdapter()).setSelectedIndex(-1);
                this.recycler_view_collage.getAdapter().notifyDataSetChanged();
                this.recycler_view_ratio.scrollToPosition(0);
                ((AspectAdapter) this.recycler_view_ratio.getAdapter()).setLastSelectedView(-1);
                this.recycler_view_ratio.getAdapter().notifyDataSetChanged();
                this.queShotGridView.setLocked(false);
                this.queShotGridView.setTouchEnable(false);
                setGoneSave();
                return;
            case PADDING:
                setBorder();
                setGuideLine();
                this.constrant_layout_change_Layout.setVisibility(View.VISIBLE);
                this.recyclerViewTools.setVisibility(View.GONE);
                this.recyclerViewToolsCollage.setVisibility(View.GONE);
                this.queShotLayout = this.queShotGridView.getQueShotLayout();
                this.aspectRatio = this.queShotGridView.getAspectRatio();
                this.BorderRadius = this.queShotGridView.getCollageRadian();
                this.Padding = this.queShotGridView.getCollagePadding();
                this.recycler_view_collage.scrollToPosition(0);
                ((GridAdapter) this.recycler_view_collage.getAdapter()).setSelectedIndex(-1);
                this.recycler_view_collage.getAdapter().notifyDataSetChanged();
                this.recycler_view_ratio.scrollToPosition(0);
                ((AspectAdapter) this.recycler_view_ratio.getAdapter()).setLastSelectedView(-1);
                this.recycler_view_ratio.getAdapter().notifyDataSetChanged();
                this.queShotGridView.setLocked(false);
                this.queShotGridView.setTouchEnable(false);
                setGoneSave();
                return;
            case RATIO:
                setRatio();
                setGuideLine();
                this.constrant_layout_change_Layout.setVisibility(View.VISIBLE);
                this.recyclerViewTools.setVisibility(View.GONE);
                this.recyclerViewToolsCollage.setVisibility(View.GONE);
                this.queShotLayout = this.queShotGridView.getQueShotLayout();
                this.aspectRatio = this.queShotGridView.getAspectRatio();
                this.BorderRadius = this.queShotGridView.getCollageRadian();
                this.Padding = this.queShotGridView.getCollagePadding();
                this.recycler_view_collage.scrollToPosition(0);
                ((GridAdapter) this.recycler_view_collage.getAdapter()).setSelectedIndex(-1);
                this.recycler_view_collage.getAdapter().notifyDataSetChanged();
                this.recycler_view_ratio.scrollToPosition(0);
                ((AspectAdapter) this.recycler_view_ratio.getAdapter()).setLastSelectedView(-1);
                this.recycler_view_ratio.getAdapter().notifyDataSetChanged();
                this.queShotGridView.setLocked(false);
                this.queShotGridView.setTouchEnable(false);
                setGoneSave();
                return;
            case FILTER:
                if (this.drawableList.isEmpty()) {
                    for (PolishGrid drawable : this.queShotGridView.getQueShotGrids()) {
                        this.drawableList.add(drawable.getDrawable());
                    }
                }
                new allFilters().execute();
                setGoneSave();
                return;
            case TEXT:
                this.queShotGridView.setTouchEnable(false);
                setGoneSave();
                setGuideLine();
                this.queShotGridView.setLocked(false);
                openTextFragment();
                this.constraintLayoutAddText.setVisibility(View.VISIBLE);
                this.recyclerViewTools.setVisibility(View.GONE);
                this.constraintLayoutSaveText.setVisibility(View.VISIBLE);
                this.relativeLayoutAddText.setVisibility(View.VISIBLE);
                return;
            case STICKER:
                setGuideLine();
                this.constraint_layout_sticker.setVisibility(View.VISIBLE);
                this.recyclerViewTools.setVisibility(View.GONE);
                this.constraintLayoutSaveSticker.setVisibility(View.VISIBLE);
                this.recyclerViewToolsCollage.setVisibility(View.GONE);
                this.queShotGridView.updateLayout(this.queShotLayout);
                this.queShotGridView.setCollagePadding(this.Padding);
                this.queShotGridView.setCollageRadian(this.BorderRadius);
                getWindowManager().getDefaultDisplay().getSize(new Point());
                onNewAspectRatioSelected(this.aspectRatio);
                this.queShotGridView.setAspectRatio(this.aspectRatio);
                for (int i = 0; i < this.drawableList.size(); i++) {
                    this.queShotGridView.getQueShotGrids().get(i).setDrawable(this.drawableList.get(i));
                }
                this.queShotGridView.invalidate();
                if (this.currentBackgroundState.isColor) {
                    this.queShotGridView.setBackgroundResourceMode(0);
                    this.queShotGridView.setBackgroundColor(this.currentBackgroundState.drawableId);
                } else {
                    this.queShotGridView.setBackgroundResourceMode(1);
                    if (this.currentBackgroundState.drawable != null) {
                        this.queShotGridView.setBackground(this.currentBackgroundState.drawable);
                    } else {
                        this.queShotGridView.setBackgroundResource(this.currentBackgroundState.drawableId);
                    }
                }
                setGoneSave();
                this.queShotGridView.setLocked(false);
                this.queShotGridView.setTouchEnable(false);
                this.linear_layout_wrapper_sticker_list.setVisibility(View.VISIBLE);

                return;
            case GRADIENT:
                setGuideLine();
                this.constraint_layout_change_background.setVisibility(View.VISIBLE);
                this.recyclerViewTools.setVisibility(View.GONE);
                this.recyclerViewToolsCollage.setVisibility(View.GONE);
                this.queShotGridView.setLocked(false);
                this.queShotGridView.setTouchEnable(false);
                setGoneSave();
                setBackgroundColor();
                if (this.queShotGridView.getBackgroundResourceMode() == 0) {
                    this.currentBackgroundState.isColor = true;
                    this.currentBackgroundState.isBitmap = false;
                    this.currentBackgroundState.drawableId = ((ColorDrawable) this.queShotGridView.getBackground()).getColor();
                    return;
                } else if (this.queShotGridView.getBackgroundResourceMode() == 2 || (this.queShotGridView.getBackground() instanceof ColorDrawable)) {
                    this.currentBackgroundState.isBitmap = true;
                    this.currentBackgroundState.isColor = false;
                    this.currentBackgroundState.drawable = this.queShotGridView.getBackground();
                    return;
                } else if (this.queShotGridView.getBackground() instanceof GradientDrawable) {
                    this.currentBackgroundState.isBitmap = false;
                    this.currentBackgroundState.isColor = false;
                    this.currentBackgroundState.drawable = this.queShotGridView.getBackground();
                    return;
                } else {
                    return;
                }
            default:
        }
    }

    public void loadPhoto() {
        final int i;
        final ArrayList arrayList = new ArrayList();
        if (this.stringList.size() > this.queShotLayout.getAreaCount()) {
            i = this.queShotLayout.getAreaCount();
        } else {
            i = this.stringList.size();
        }
        for (int i2 = 0; i2 < i; i2++) {
            Target r4 = new Target() {
                public void onBitmapFailed(Exception exc, Drawable drawable) {
                }

                public void onPrepareLoad(Drawable drawable) {
                }

                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom loadedFrom) {
                    int width = bitmap.getWidth();
                    float f = (float) width;
                    float height = (float) bitmap.getHeight();
                    float max = Math.max(f / f, height / f);
                    if (max > 1.0f) {
                        bitmap = Bitmap.createScaledBitmap(bitmap, (int) (f / max), (int) (height / max), false);
                    }
                    arrayList.add(bitmap);
                    if (arrayList.size() == i) {
                        if (PolishCollageActivity.this.stringList.size() < PolishCollageActivity.this.queShotLayout.getAreaCount()) {
                            for (int i = 0; i < PolishCollageActivity.this.queShotLayout.getAreaCount(); i++) {
                                PolishCollageActivity.this.queShotGridView.addQuShotCollage((Bitmap) arrayList.get(i % i));
                            }
                        } else {
                            PolishCollageActivity.this.queShotGridView.addPieces(arrayList);
                        }
                    }
                    PolishCollageActivity.this.targets.remove(this);
                }
            };
            Picasso picasso = Picasso.get();
            picasso.load("file:///" + this.stringList.get(i2)).resize(this.deviceWidth, this.deviceWidth).centerInside().config(Bitmap.Config.RGB_565).into((Target) r4);
            this.targets.add(r4);
        }
    }

    private void setOnBackPressDialog() {
        final Dialog dialogOnBackPressed = new Dialog(PolishCollageActivity.this, R.style.UploadDialog);
        dialogOnBackPressed.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogOnBackPressed.setContentView(R.layout.dialog_exit);
        dialogOnBackPressed.setCancelable(true);
        dialogOnBackPressed.show();
        this.textViewCancel = dialogOnBackPressed.findViewById(R.id.textViewCancel);
        this.textViewDiscard = dialogOnBackPressed.findViewById(R.id.textViewDiscard);

        this.textViewDiscard.setOnClickListener(view -> {
            dialogOnBackPressed.dismiss();
            PolishCollageActivity.this.moduleToolsId = null;
            PolishCollageActivity.this.finish();
            finish();
        });

        this.textViewCancel.setOnClickListener(view -> {
            dialogOnBackPressed.dismiss();
        });
    }

    public void setGuideLineTools() {
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(this.constraint_layout_collage_layout);
        constraintSet.connect(this.constraint_layout_wrapper_collage_view.getId(), 1, this.constraint_layout_collage_layout.getId(), 1, 0);
        constraintSet.connect(this.constraint_layout_wrapper_collage_view.getId(), 4, this.guidelineTools.getId(), 3, 0);
        constraintSet.connect(this.constraint_layout_wrapper_collage_view.getId(), 2, this.constraint_layout_collage_layout.getId(), 2, 0);
        constraintSet.applyTo(this.constraint_layout_collage_layout);
    }

    public void setGuideLine() {
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(this.constraint_layout_collage_layout);
        constraintSet.connect(this.constraint_layout_wrapper_collage_view.getId(), 1, this.constraint_layout_collage_layout.getId(), 1, 0);
        constraintSet.connect(this.constraint_layout_wrapper_collage_view.getId(), 4, this.guideline.getId(), 3, 0);
        constraintSet.connect(this.constraint_layout_wrapper_collage_view.getId(), 2, this.constraint_layout_collage_layout.getId(), 2, 0);
        constraintSet.applyTo(this.constraint_layout_collage_layout);
    }

    public void setGoneSave() {
        this.constraint_save_control.setVisibility(View.GONE);
    }

    public void setVisibleSave() {
        this.constraint_save_control.setVisibility(View.VISIBLE);
    }

    public void onBackPressed() {
        if (this.moduleToolsId == null) {
            super.onBackPressed();
            return;
        }
        try {
            switch (this.moduleToolsId) {
                case PADDING:
                case RATIO:
                case LAYER:
                    setGuideLineTools();
                    this.recyclerViewTools.setVisibility(View.VISIBLE);
                    this.constrant_layout_change_Layout.setVisibility(View.GONE);
                    this.recyclerViewToolsCollage.setVisibility(View.VISIBLE);
                    setVisibleSave();
                    this.queShotGridView.updateLayout(this.queShotLayout);
                    this.queShotGridView.setCollagePadding(this.Padding);
                    this.queShotGridView.setCollageRadian(this.BorderRadius);
                    this.moduleToolsId = Module.NONE;
                    getWindowManager().getDefaultDisplay().getSize(new Point());
                    onNewAspectRatioSelected(this.aspectRatio);
                    this.queShotGridView.setAspectRatio(this.aspectRatio);
                    this.queShotGridView.setLocked(true);
                    this.queShotGridView.setTouchEnable(true);
                    return;
                case TEXT:
                    setGuideLineTools();
                    this.recyclerViewTools.setVisibility(View.VISIBLE);
                    this.constraintLayoutAddText.setVisibility(View.GONE);
                    this.constraintLayoutSaveText.setVisibility(View.GONE);
                    if (!this.queShotGridView.getStickers().isEmpty()) {
                        this.queShotGridView.getStickers().clear();
                        this.queShotGridView.setHandlingSticker(null);
                    }
                    this.moduleToolsId = Module.NONE;
                    this.relativeLayoutAddText.setVisibility(View.GONE);
                    this.queShotGridView.setHandlingSticker(null);
                    setVisibleSave();
                    this.queShotGridView.setLocked(true);
                    this.queShotGridView.setTouchEnable(true);
                    return;
                case FILTER:
                    setGuideLineTools();
                    this.recyclerViewTools.setVisibility(View.VISIBLE);
                    this.constraint_layout_filter_layout.setVisibility(View.GONE);
                    this.recyclerViewToolsCollage.setVisibility(View.VISIBLE);
                    this.queShotGridView.setLocked(true);
                    this.queShotGridView.setTouchEnable(true);
                    for (int i = 0; i < this.drawableList.size(); i++) {
                        this.queShotGridView.getQueShotGrids().get(i).setDrawable(this.drawableList.get(i));
                    }
                    this.queShotGridView.invalidate();
                    setVisibleSave();
                    this.moduleToolsId = Module.NONE;
                    return;
                case STICKER:
                    setGuideLineTools();
                    this.constraintLayoutSaveSticker.setVisibility(View.GONE);
                    if (this.queShotGridView.getStickers().size() <= 0) {
                        this.linear_layout_wrapper_sticker_list.setVisibility(View.VISIBLE);
                        this.imageViewAddSticker.setVisibility(View.GONE);
                        this.queShotGridView.setHandlingSticker((Sticker) null);
                        this.recyclerViewTools.setVisibility(View.VISIBLE);
                        this.constraint_layout_sticker.setVisibility(View.GONE);
                        this.recyclerViewToolsCollage.setVisibility(View.VISIBLE);
                        this.queShotGridView.setLocked(true);
                        this.moduleToolsId = Module.NONE;
                    } else if (this.imageViewAddSticker.getVisibility() == View.VISIBLE) {
                        this.queShotGridView.getStickers().clear();
                        this.imageViewAddSticker.setVisibility(View.GONE);
                        this.queShotGridView.setHandlingSticker(null);
                        this.linear_layout_wrapper_sticker_list.setVisibility(View.VISIBLE);
                        this.recyclerViewToolsCollage.setVisibility(View.VISIBLE);
                        this.recyclerViewTools.setVisibility(View.VISIBLE);
                        this.constraint_layout_sticker.setVisibility(View.GONE);
                        this.queShotGridView.setLocked(true);
                        this.queShotGridView.setTouchEnable(true);
                        this.moduleToolsId = Module.NONE;
                    } else {
                        this.linear_layout_wrapper_sticker_list.setVisibility(View.GONE);
                        this.imageViewAddSticker.setVisibility(View.VISIBLE);
                        this.recyclerViewToolsCollage.setVisibility(View.VISIBLE);
                        this.recyclerViewTools.setVisibility(View.VISIBLE);
                    }
                    this.linear_layout_wrapper_sticker_list.setVisibility(View.VISIBLE);
                    this.recyclerViewTools.setVisibility(View.VISIBLE);
                    this.constraint_layout_sticker.setVisibility(View.GONE);
                    setVisibleSave();
                    return;
                case GRADIENT:
                    setGuideLineTools();
                    this.recyclerViewTools.setVisibility(View.VISIBLE);
                    this.constraint_layout_change_background.setVisibility(View.GONE);
                    this.recyclerViewToolsCollage.setVisibility(View.VISIBLE);
                    this.queShotGridView.setLocked(true);
                    this.queShotGridView.setTouchEnable(true);
                    if (this.currentBackgroundState.isColor) {
                        this.queShotGridView.setBackgroundResourceMode(0);
                        this.queShotGridView.setBackgroundColor(this.currentBackgroundState.drawableId);
                    } else if (this.currentBackgroundState.isBitmap) {
                        this.queShotGridView.setBackgroundResourceMode(2);
                        this.queShotGridView.setBackground(this.currentBackgroundState.drawable);
                    } else {
                        this.queShotGridView.setBackgroundResourceMode(1);
                        if (this.currentBackgroundState.drawable != null) {
                            this.queShotGridView.setBackground(this.currentBackgroundState.drawable);
                        } else {
                            this.queShotGridView.setBackgroundResource(this.currentBackgroundState.drawableId);
                        }
                    }
                    setVisibleSave();
                    this.moduleToolsId = Module.NONE;
                    return;
                case COLLAGE:
                    setVisibleSave();
                    setGuideLineTools();
                    this.recyclerViewTools.setVisibility(View.VISIBLE);
                    this.recyclerViewToolsCollage.setVisibility(View.GONE);
                    this.moduleToolsId = Module.NONE;
                    this.queShotGridView.setQueShotGrid(null);
                    this.queShotGridView.setPrevHandlingQueShotGrid(null);
                    this.queShotGridView.invalidate();
                    this.moduleToolsId = Module.NONE;
                    return;
                case NONE:
                    setOnBackPressDialog();
                    return;
                default:
                    super.onBackPressed();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onItemClick(PolishLayout puzzleLayout2, int i) {
        PolishLayout parse = PolishLayoutParser.parse(puzzleLayout2.generateInfo());
        puzzleLayout2.setRadian(this.queShotGridView.getCollageRadian());
        puzzleLayout2.setPadding(this.queShotGridView.getCollagePadding());
        this.queShotGridView.updateLayout(parse);
    }

    public void onNewAspectRatioSelected(AspectRatio aspectRatio) {
        Display defaultDisplay = getWindowManager().getDefaultDisplay();
        Point point = new Point();
        defaultDisplay.getSize(point);
        int[] calculateWidthAndHeight = calculateWidthAndHeight(aspectRatio, point);
        this.queShotGridView.setLayoutParams(new ConstraintLayout.LayoutParams(calculateWidthAndHeight[0], calculateWidthAndHeight[1]));
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(this.constraint_layout_wrapper_collage_view);
        constraintSet.connect(this.queShotGridView.getId(), 3, this.constraint_layout_wrapper_collage_view.getId(), 3, 0);
        constraintSet.connect(this.queShotGridView.getId(), 1, this.constraint_layout_wrapper_collage_view.getId(), 1, 0);
        constraintSet.connect(this.queShotGridView.getId(), 4, this.constraint_layout_wrapper_collage_view.getId(), 4, 0);
        constraintSet.connect(this.queShotGridView.getId(), 2, this.constraint_layout_wrapper_collage_view.getId(), 2, 0);
        constraintSet.applyTo(this.constraint_layout_wrapper_collage_view);
        this.queShotGridView.setAspectRatio(aspectRatio);
    }

    public void replaceCurrentPiece(String str) {
        new OnLoadBitmapFromUri().execute(str);
    }

    private int[] calculateWidthAndHeight(AspectRatio aspectRatio, Point point) {
        int height = this.constraint_layout_wrapper_collage_view.getHeight();
        if (aspectRatio.getHeight() > aspectRatio.getWidth()) {
            int ratio = (int) (aspectRatio.getRatio() * ((float) height));
            if (ratio < point.x) {
                return new int[]{ratio, height};
            }
            return new int[]{point.x, (int) (((float) point.x) / aspectRatio.getRatio())};
        }
        int ratio2 = (int) (((float) point.x) / aspectRatio.getRatio());
        if (ratio2 > height) {
            return new int[]{(int) (((float) height) * aspectRatio.getRatio()), height};
        }
        return new int[]{point.x, ratio2};
    }

    public void addSticker(int item, Bitmap bitmap) {
        this.queShotGridView.addSticker(new DrawableSticker(new BitmapDrawable(getResources(), bitmap)));
        this.linear_layout_wrapper_sticker_list.setVisibility(View.GONE);
        this.imageViewAddSticker.setVisibility(View.VISIBLE);
    }

    public void onBackgroundSelected(int item, final CollageBackgroundAdapter.SquareView squareView) {
        if (squareView.isColor) {
            this.queShotGridView.setBackgroundColor(squareView.drawableId);
            this.queShotGridView.setBackgroundResourceMode(0);
        } else if (squareView.drawable != null) {
            this.queShotGridView.setBackgroundResourceMode(2);
            new AsyncTask<Void, Bitmap, Bitmap>() {

                public Bitmap doInBackground(Void... voidArr) {
                    return FilterUtils.getBlurImageFromBitmap(((BitmapDrawable) squareView.drawable).getBitmap(), 5.0f);
                }


                public void onPostExecute(Bitmap bitmap) {
                    PolishCollageActivity.this.queShotGridView.setBackground(new BitmapDrawable(PolishCollageActivity.this.getResources(), bitmap));
                }
            }.execute();
        } else {
            this.queShotGridView.setBackgroundResource(squareView.drawableId);
            this.queShotGridView.setBackgroundResourceMode(1);
        }
    }

    public void onBackgroundColorSelected(int item, CollageColorAdapter.SquareView squareView) {
       if (squareView.isColor) {
            this.queShotGridView.setBackgroundColor(squareView.drawableId);
            this.queShotGridView.setBackgroundResourceMode(0);
        }
    }

    public void onFilterSelected(int item, String str) {
        new LoadBitmapWithFilter().execute(new String[]{str});

    }

    public void finishCrop(Bitmap bitmap) {
        this.queShotGridView.replace(bitmap, "");
    }

    public void onSaveFilter(Bitmap bitmap) {
        this.queShotGridView.replace(bitmap, "");
    }

    @Override
    public void onPieceFuncSelected(Module toolType) {
        switch (toolType) {
            case REPLACE:
                PolishPickerView.builder().setPhotoCount(1).setPreviewEnabled(false).setShowCamera(false).setForwardMain(true).start(this);
                return;
            case H_FLIP:
                this.queShotGridView.flipHorizontally();
                return;
            case V_FLIP:
                this.queShotGridView.flipVertically();
                return;
            case ROTATE:
                this.queShotGridView.rotate(90.0f);
                return;
            case CROP:
                CropFragment.show(this, this, ((BitmapDrawable) this.queShotGridView.getQueShotGrid().getDrawable()).getBitmap());
                return;
            case FILTER:
                new LoadFilterBitmapForCurrentPiece().execute();
                return;
        }
    }

    class allFilters extends AsyncTask<Void, Void, Void> {
        allFilters() {
        }

        public void onPreExecute() {
            PolishCollageActivity.this.setLoading(true);
        }

        @SuppressLint("WrongThread")
        public Void doInBackground(Void... voidArr) {
            PolishCollageActivity.this.listFilterAll.clear();
            PolishCollageActivity.this.listFilterAll.addAll(FilterFileAsset.getListBitmapFilter(ThumbnailUtils.extractThumbnail(((BitmapDrawable) PolishCollageActivity.this.queShotGridView.getQueShotGrids().get(0).getDrawable()).getBitmap(), 100, 100)));
            return null;
        }

        public void onPostExecute(Void voidR) {
            PolishCollageActivity.this.recyclerViewFilter.setAdapter(new FilterAdapter(PolishCollageActivity.this.listFilterAll, PolishCollageActivity.this, PolishCollageActivity.this.getApplicationContext(), Arrays.asList(FilterFileAsset.FILTERS)));
            PolishCollageActivity.this.recyclerViewToolsCollage.setVisibility(View.GONE);
            PolishCollageActivity.this.queShotGridView.setLocked(false);
            PolishCollageActivity.this.queShotGridView.setTouchEnable(false);
            setGuideLine();
            PolishCollageActivity.this.constraint_layout_filter_layout.setVisibility(View.VISIBLE);
            PolishCollageActivity.this.recyclerViewTools.setVisibility(View.GONE);
            PolishCollageActivity.this.setLoading(false);
        }
    }

    class LoadFilterBitmapForCurrentPiece extends AsyncTask<Void, List<Bitmap>, List<Bitmap>> {
        LoadFilterBitmapForCurrentPiece() {
        }

        public void onPreExecute() {
            PolishCollageActivity.this.setLoading(true);
        }

        @SuppressLint("WrongThread")
        public List<Bitmap> doInBackground(Void... voidArr) {
            return FilterFileAsset.getListBitmapFilter(ThumbnailUtils.extractThumbnail(((BitmapDrawable) PolishCollageActivity.this.queShotGridView.getQueShotGrid().getDrawable()).getBitmap(), 100, 100));
        }

        public void onPostExecute(List<Bitmap> list) {
            PolishCollageActivity.this.setLoading(false);
            if (PolishCollageActivity.this.queShotGridView.getQueShotGrid() != null) {
                FilterFragment.show(PolishCollageActivity.this, PolishCollageActivity.this, ((BitmapDrawable) PolishCollageActivity.this.queShotGridView.getQueShotGrid().getDrawable()).getBitmap(), list);
            }
        }
    }

    class LoadBitmapWithFilter extends AsyncTask<String, List<Bitmap>, List<Bitmap>> {
        LoadBitmapWithFilter() {
        }

        public void onPreExecute() {
            PolishCollageActivity.this.setLoading(true);
        }

        public List<Bitmap> doInBackground(String... strArr) {
            ArrayList arrayList = new ArrayList();
            for (Drawable drawable : PolishCollageActivity.this.drawableList) {
                arrayList.add(FilterUtils.getBitmapWithFilter(((BitmapDrawable) drawable).getBitmap(), strArr[0]));
            }
            return arrayList;
        }


        public void onPostExecute(List<Bitmap> list) {
            for (int i = 0; i < list.size(); i++) {
                BitmapDrawable bitmapDrawable = new BitmapDrawable(PolishCollageActivity.this.getResources(), list.get(i));
                bitmapDrawable.setAntiAlias(true);
                bitmapDrawable.setFilterBitmap(true);
                PolishCollageActivity.this.queShotGridView.getQueShotGrids().get(i).setDrawable(bitmapDrawable);
            }
            PolishCollageActivity.this.queShotGridView.invalidate();
            PolishCollageActivity.this.setLoading(false);
        }
    }

    class OnLoadBitmapFromUri extends AsyncTask<String, Bitmap, Bitmap> {
        OnLoadBitmapFromUri() {
        }

        public void onPreExecute() {
            PolishCollageActivity.this.setLoading(true);
        }

        public Bitmap doInBackground(String... strArr) {
            try {
                Uri fromFile = Uri.fromFile(new File(strArr[0]));

                Bitmap rotateBitmap = SystemUtil.rotateBitmap(MediaStore.Images.Media.getBitmap(PolishCollageActivity.this.getContentResolver(), fromFile), new ExifInterface(PolishCollageActivity.this.getContentResolver().openInputStream(fromFile)).getAttributeInt(ExifInterface.TAG_ORIENTATION, 1));

                float width = (float) rotateBitmap.getWidth();
                float height = (float) rotateBitmap.getHeight();
                float max = Math.max(width / 1280.0f, height / 1280.0f);
                return max > 1.0f ? Bitmap.createScaledBitmap(rotateBitmap, (int) (width / max), (int) (height / max), false) : rotateBitmap;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        public void onPostExecute(Bitmap bitmap) {
            PolishCollageActivity.this.setLoading(false);
            PolishCollageActivity.this.queShotGridView.replace(bitmap, "");
        }
    }


    class SaveCollageAsFile extends AsyncTask<Bitmap, String, String> {
        SaveCollageAsFile() {
        }

        public void onPreExecute() {
            PolishCollageActivity.this.setLoading(true);
        }

        public String doInBackground(Bitmap... bitmapArr) {
            Bitmap bitmap = bitmapArr[0];
            Bitmap bitmap2 = bitmapArr[1];
            Bitmap createBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(createBitmap);
            Paint paint = null;
            canvas.drawBitmap(bitmap, (Rect) null, new RectF(0.0f, 0.0f, (float) bitmap.getWidth(), (float) bitmap.getHeight()), paint);
            canvas.drawBitmap(bitmap2, (Rect) null, new RectF(0.0f, 0.0f, (float) bitmap.getWidth(), (float) bitmap.getHeight()), paint);
            bitmap.recycle();
            bitmap2.recycle();
            try {
                File image = SaveFileUtils.saveBitmapFileCollage(PolishCollageActivity.this, createBitmap, new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(new Date()),null);
                createBitmap.recycle();
                return image.getAbsolutePath();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        public void onPostExecute(String str) {
            PolishCollageActivity.this.setLoading(false);
            Intent intent = new Intent(PolishCollageActivity.this, PhotoShareActivity.class);
            intent.putExtra("path", str);
            PolishCollageActivity.this.startActivity(intent);
        }
    }

    public void setLoading(boolean z) {
        if (z) {
            getWindow().setFlags(16, 16);
            this.relativeLayoutLoading.setVisibility(View.VISIBLE);
            return;
        }
        getWindow().clearFlags(16);
        this.relativeLayoutLoading.setVisibility(View.GONE);
    }

    private void initInterstitialAd() {
        adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(Objects.requireNonNull(this), getResources().getString(R.string.interstitial), adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                // The mInterstitialAd reference will be null until
                // an ad is loaded.
                mInterstitialAd = interstitialAd;

            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                // Handle the error

                mInterstitialAd = null;
            }
        });
    }

}
