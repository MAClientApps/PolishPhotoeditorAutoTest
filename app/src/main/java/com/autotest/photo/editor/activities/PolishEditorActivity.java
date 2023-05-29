package com.autotest.photo.editor.activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.constraintlayout.widget.Guideline;
import androidx.core.content.ContextCompat;
import androidx.core.internal.view.SupportMenu;
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
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.hold1.keyboardheightprovider.KeyboardHeightProvider;
import com.autotest.photo.editor.R;
import com.autotest.photo.editor.adapters.AdjustAdapter;
import com.autotest.photo.editor.adapters.ColorAdapter;
import com.autotest.photo.editor.adapters.FilterAdapter;
import com.autotest.photo.editor.adapters.MagicBrushAdapter;
import com.autotest.photo.editor.adapters.OverlayAdapter;
import com.autotest.photo.editor.adapters.PolishDrawToolsAdapter;
import com.autotest.photo.editor.adapters.PolishEffectToolsAdapter;
import com.autotest.photo.editor.adapters.PolishSQToolsAdapter;
import com.autotest.photo.editor.adapters.PolishToolsAdapter;
import com.autotest.photo.editor.adapters.RecyclerTabLayout;
import com.autotest.photo.editor.adapters.StickerAdapter;
import com.autotest.photo.editor.adapters.StickersTabAdapter;
import com.autotest.photo.editor.assets.FilterFileAsset;
import com.autotest.photo.editor.assets.OverlayFileAsset;
import com.autotest.photo.editor.assets.StickerFileAsset;
import com.autotest.photo.editor.constants.StoreManager;
import com.autotest.photo.editor.draw.DrawModel;
import com.autotest.photo.editor.draw.Drawing;
import com.autotest.photo.editor.event.AlignHorizontallyEvent;
import com.autotest.photo.editor.event.DeleteIconEvent;
import com.autotest.photo.editor.event.EditTextIconEvent;
import com.autotest.photo.editor.event.FlipHorizontallyEvent;
import com.autotest.photo.editor.event.ZoomIconEvent;
import com.autotest.photo.editor.fragment.BlurSquareBgFragment;
import com.autotest.photo.editor.fragment.ColoredFragment;
import com.autotest.photo.editor.fragment.CropFragment;
import com.autotest.photo.editor.fragment.FrameFragment;
import com.autotest.photo.editor.fragment.HSlFragment;
import com.autotest.photo.editor.fragment.MirrorFragment;
import com.autotest.photo.editor.fragment.MosaicFragment;
import com.autotest.photo.editor.fragment.RatioFragment;
import com.autotest.photo.editor.fragment.SaturationSquareBackgroundFragment;
import com.autotest.photo.editor.fragment.SaturationSquareFragment;
import com.autotest.photo.editor.fragment.SketchSquareBackgroundFragment;
import com.autotest.photo.editor.fragment.SketchSquareFragment;
import com.autotest.photo.editor.fragment.TextFragment;
import com.autotest.photo.editor.layout.ArtLayout;
import com.autotest.photo.editor.layout.BlurLayout;
import com.autotest.photo.editor.layout.DripLayout;
import com.autotest.photo.editor.layout.MotionLayout;
import com.autotest.photo.editor.layout.NeonLayout;
import com.autotest.photo.editor.layout.PixLabLayout;
import com.autotest.photo.editor.layout.SplashLayout;
import com.autotest.photo.editor.layout.WingLayout;
import com.autotest.photo.editor.listener.AdjustListener;
import com.autotest.photo.editor.listener.BrushColorListener;
import com.autotest.photo.editor.listener.BrushMagicListener;
import com.autotest.photo.editor.listener.FilterListener;
import com.autotest.photo.editor.listener.OnPolishEditorListener;
import com.autotest.photo.editor.listener.OverlayListener;
import com.autotest.photo.editor.model.General;
import com.autotest.photo.editor.module.Module;
import com.autotest.photo.editor.picker.PermissionsUtils;
import com.autotest.photo.editor.polish.PolishEditor;
import com.autotest.photo.editor.polish.PolishPickerView;
import com.autotest.photo.editor.polish.PolishStickerIcons;
import com.autotest.photo.editor.polish.PolishStickerView;
import com.autotest.photo.editor.polish.PolishText;
import com.autotest.photo.editor.polish.PolishTextView;
import com.autotest.photo.editor.polish.PolishView;
import com.autotest.photo.editor.preference.Preference;
import com.autotest.photo.editor.sticker.DrawableSticker;
import com.autotest.photo.editor.sticker.Sticker;
import com.autotest.photo.editor.utils.BitmapTransfer;
import com.autotest.photo.editor.utils.DegreeSeekBar;
import com.autotest.photo.editor.utils.FilterUtils;
import com.autotest.photo.editor.utils.SaveFileUtils;
import com.autotest.photo.editor.utils.SystemUtil;

import org.jetbrains.annotations.NotNull;
import org.wysaid.myUtils.MsgUtil;
import org.wysaid.nativePort.CGENativeLibrary;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

@SuppressLint("StaticFieldLeak")
public class PolishEditorActivity extends PolishBaseActivity implements OnPolishEditorListener,
        View.OnClickListener, StickerAdapter.OnClickSplashListener, BlurSquareBgFragment.BlurSquareBgListener,
        CropFragment.OnCropPhoto, BrushColorListener, SaturationSquareBackgroundFragment.SplashSaturationBackgrundListener,
        RatioFragment.RatioSaveListener, FrameFragment.RatioSaveListener, SketchSquareBackgroundFragment.SketchBackgroundListener,
        SaturationSquareFragment.SplashSaturationListener,
        MosaicFragment.MosaicListener, ColoredFragment.ColoredListener, SketchSquareFragment.SketchListener,
        PolishToolsAdapter.OnQuShotItemSelected, PolishDrawToolsAdapter.OnQuShotDrawItemSelected,
        PolishEffectToolsAdapter.OnQuShotEffectItemSelected,
        PolishSQToolsAdapter.OnQuShotSQItemSelected, FilterListener, AdjustListener, OverlayListener,
        BrushMagicListener, HSlFragment.OnFilterSavePhoto {

    private static final String TAG = "PolishEditorActivity";
    // Tools
    public Module moduleToolsId = Module.NONE;
    // Keyboard
    private KeyboardHeightProvider keyboardProvider;
    private Animation slideUpAnimation, slideDownAnimation;
    // Guideline
    private Guideline guidelinePaint;
    private Guideline guideline;
    // Adapter
    public AdjustAdapter mAdjustAdapter;
    public ColorAdapter colorAdapter;
    private final PolishToolsAdapter mEditingToolsAdapter = new PolishToolsAdapter(this);
    private final PolishDrawToolsAdapter mEditingDrawToolsAdapter = new PolishDrawToolsAdapter(this);
    private final PolishEffectToolsAdapter mEditingEffectToolsAdapter = new PolishEffectToolsAdapter(this);
    private final PolishSQToolsAdapter mEditingSplashToolsAdapter = new PolishSQToolsAdapter(this);
    // QuShot
    public PolishEditor polishEditor;
    public PolishView polishView;

    // BitmapStickerIcon
    PolishStickerIcons polishStickerIconClose;
    PolishStickerIcons polishStickerIconScale;
    PolishStickerIcons polishStickerIconFlip;
    PolishStickerIcons polishStickerIconRotate;
    PolishStickerIcons polishStickerIconEdit;
    PolishStickerIcons polishStickerIconAlign;
    // Guideline
    private DegreeSeekBar adjustSeekBar;
    private DegreeSeekBar adjustFilter;
    private SeekBar seekBarOverlay;
    // Fragment
    public TextFragment.TextEditor textEditor;
    public TextFragment textFragment;
    // ViewPager
    public ViewPager viewPagerStickers;
    // ConstraintLayout
    private ConstraintLayout constraintLayoutSave;
    private ConstraintLayout constraintLayoutDraw;
    private ConstraintLayout constraintLayoutSplash;
    private ConstraintLayout constraintLayoutEffects;
    private ConstraintLayout constraintLayoutAdjust;
    private ConstraintLayout constraintLayoutOverlay;
    private ConstraintLayout constraintLayoutConfirmCompareOverlay;
    private ConstraintLayout constraintLayoutSaveOverlay;
    private ConstraintLayout constraintLayoutSaveText;
    private ConstraintLayout constraintLayoutSaveSticker;
    private ConstraintLayout constraintLayoutConfirmCompareHardmix;
    private ConstraintLayout constraintLayoutDodge;
    private ConstraintLayout constraintLayoutConfirmCompareDodge;
    private ConstraintLayout constraintLayoutDivide;
    private ConstraintLayout constraintLayoutConfirmCompareDivide;
    private ConstraintLayout constraintLayoutBurn;
    private ConstraintLayout constraintLayoutConfirmCompareBurn;
    private ConstraintLayout constraintLayoutPaint;
    private ConstraintLayout constraintLayoutPaintTool;
    private ConstraintLayout constraintLayoutNeon;
    private ConstraintLayout constraintLayoutNeonTool;
    private ConstraintLayout constraintLayoutMagic;
    private ConstraintLayout constraintLayoutMagicTool;
    public ConstraintLayout constraintLayoutFilter;
    private ConstraintLayout constraintLayoutSticker;
    private ConstraintLayout constraintLayoutAddText;
    private ConstraintLayout constraintLayoutView;
    // RelativeLayout
    private RelativeLayout relativeLayoutAddText;
    private RelativeLayout relativeLayoutWrapper;
    private RelativeLayout relativeLayoutLoading;
    public LinearLayout linear_layout_wrapper_sticker_list;

    // RecyclerView
    public RecyclerView recyclerViewTools;
    public RecyclerView recyclerViewDraw;
    public RecyclerView recyclerViewSpalsh;
    public RecyclerView recyclerViewEffect;
    public RecyclerView recyclerViewFilter;
    private RecyclerView recyclerViewPaintListColor;
    private RecyclerView recyclerViewMagicListColor;
    private RecyclerView recyclerViewNeonListColor;
    private RecyclerView recyclerViewAdjust;
    // ImageView
    private ImageView imageViewCompareAdjust;
    public ImageView imageViewCompareFilter;
    public ImageView imageViewCompareOverlay;
    private ImageView imageViewRedoPaint;
    private ImageView imageViewCleanPaint;
    private ImageView imageViewCleanNeon;
    private ImageView imageViewRedoNeon;
    private ImageView imageViewUndoPaint;
    private ImageView imageViewUndoNeon;
    private ImageView imageViewCleanMagic;
    private ImageView imageViewRedoMagic;
    private ImageView imageViewUndoMagic;
    public ImageView undo;
    public ImageView redo;
    public ImageView imageViewAddSticker;

    // TextView
    public TextView textViewCancel;
    public TextView textViewDiscard;
    public TextView textViewSaveEditing;
    public ImageView image_view_exit;

    // Seekbar
    public SeekBar seekbarSticker;
    // ArrayList & List
    public ArrayList listDodgeEffect = new ArrayList<>();
    public ArrayList listColorEffect = new ArrayList<>();
    public ArrayList listHardMixEffect = new ArrayList<>();
    public ArrayList listHueEffect = new ArrayList<>();
    public ArrayList listOverlayEffect = new ArrayList<>();
    public ArrayList listBurnEffect = new ArrayList<>();
    public ArrayList listFilter = new ArrayList<>();
    // Admob Ads

    private TextView textViewTitleBrush;
    private ImageView imageViewBrushSize;
    private ImageView imageViewBrushOpacity;
    private ImageView imageViewBrushEraser;
    private TextView textViewValueBrush;
    private TextView textViewValueOpacity;
    private TextView textViewValueEraser;
    private SeekBar seekBarBrush;
    private SeekBar seekBarOpacity;
    private SeekBar seekBarEraser;

    private TextView textViewTitleNeon;
    private ImageView imageViewNeonSize;
    private ImageView imageViewNeonEraser;
    private TextView textViewNeonBrush;
    private TextView textViewNeonEraser;
    private SeekBar seekBarNeonBrush;
    private SeekBar seekBarNeonEraser;

    private TextView textViewTitleMagic;
    private ImageView imageViewMagicSize;
    private ImageView imageViewMagicOpacity;
    private ImageView imageViewMagicEraser;
    private TextView textViewMagicBrush;
    private TextView textViewMagicOpacity;
    private TextView textViewMagicEraser;
    private SeekBar seekBarMagicBrush;
    private SeekBar seekBarMagicOpacity;
    private SeekBar seekBarMagicEraser;


    private RelativeLayout relativeLayoutPaint;
    private RelativeLayout relativeLayoutNeon;
    private RelativeLayout relativeLayoutMagic;

    public RecyclerView recycler_view_overlay_effect;
    public RecyclerView recycler_view_color_effect;
    public RecyclerView recycler_view_dodge_effect;
    public RecyclerView recycler_view_hardmix_effet;
    public RecyclerView recycler_view_hue_effect;
    public RecyclerView recycler_view_burn_effect;

    private View view_overlay;
    private View view_hue;
    private View view_color;
    private View view_dodge;
    private View view_hardmix;
    private View view_burn;

    private LinearLayout linearLayoutOverlay;
    private LinearLayout linearLayoutHue;
    private LinearLayout linearLayoutColor;
    private LinearLayout linearLayoutDodge;
    private LinearLayout linearLayoutBurn;
    private LinearLayout linearLayoutHardmix;

    private TextView text_view_overlay;
    private TextView text_view_color;
    private TextView text_view_dodge;
    private TextView text_view_hardmix;
    private TextView text_view_hue;
    private TextView text_view_burn;

    private AdView adView;
    private AdRequest adRequest;
    private InterstitialAd mInterstitialAd;
    private General generalModel;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        requestWindowFeature(1);
        getWindow().setFlags(1024, 1024);
        setContentView(R.layout.activity_polish_editor);
        CGENativeLibrary.setLoadImageCallback(this.loadImageCallback, null);
        slideDownAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_down);
        slideUpAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);
        if (Build.VERSION.SDK_INT < 26) {
            getWindow().setSoftInputMode(48);
        }


        initView();
        onClickListener();
        setView();
        setBottomToolbar(false);
         //init Ads
            MobileAds.initialize(PolishEditorActivity.this, (OnInitializationCompleteListener) initializationStatus -> {
            });
            initInterstitialAd();
            adView = findViewById(R.id.adView);
            adRequest = new AdRequest.Builder().build();
            adView.loadAd(adRequest);

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void onPause() {
        super.onPause();
        this.keyboardProvider.onPause();
    }

    public void onResume() {
        super.onResume();
        this.keyboardProvider.onResume();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initView() {
        this.relativeLayoutLoading = findViewById(R.id.relative_layout_loading);
        this.relativeLayoutLoading.setVisibility(View.VISIBLE);
        this.polishView = findViewById(R.id.photo_editor_view);
        this.polishView.setVisibility(View.INVISIBLE);
        this.recyclerViewTools = findViewById(R.id.recyclerViewTools);
        this.recyclerViewDraw = findViewById(R.id.recyclerViewDraw);
        this.recyclerViewSpalsh = findViewById(R.id.recyclerViewBlurSqaure);
        this.constraintLayoutEffects = findViewById(R.id.constraint_layout_effects);
        this.recyclerViewEffect = findViewById(R.id.recyclerViewEffect);
        this.recyclerViewFilter = findViewById(R.id.recycler_view_filter);
        this.recyclerViewAdjust = findViewById(R.id.recyclerViewAdjust);
        this.constraintLayoutView = findViewById(R.id.constraint_layout_root_view);
        this.constraintLayoutFilter = findViewById(R.id.constraint_layout_filter);
        this.constraintLayoutAdjust = findViewById(R.id.constraintLayoutAdjust);
        this.constraintLayoutOverlay = findViewById(R.id.constraint_layout_overlay);
        this.constraintLayoutConfirmCompareOverlay = findViewById(R.id.constraintLayoutConfirmCompareOverlay);
        this.constraintLayoutSaveOverlay = findViewById(R.id.constraint_layout_confirm_save_overlay);
        this.linearLayoutOverlay = findViewById(R.id.linearLayoutOverlay);
        this.linearLayoutHue = findViewById(R.id.linearLayoutHue);
        this.linearLayoutColor = findViewById(R.id.linearLayoutColor);
        this.linearLayoutDodge = findViewById(R.id.linearLayoutDodge);
        this.linearLayoutBurn = findViewById(R.id.linearLayoutBurn);
        this.linearLayoutHardmix = findViewById(R.id.linearLayoutHardmix);
        this.text_view_overlay = findViewById(R.id.text_view_overlay);
        this.text_view_color = findViewById(R.id.text_view_color);
        this.text_view_dodge = findViewById(R.id.text_view_dodge);
        this.text_view_hardmix = findViewById(R.id.text_view_hardmix);
        this.text_view_hue = findViewById(R.id.text_view_hue);
        this.text_view_burn = findViewById(R.id.text_view_burn);
        this.recycler_view_overlay_effect = findViewById(R.id.recycler_view_overlay_effect);
        this.recycler_view_color_effect = findViewById(R.id.recycler_view_color_effect);
        this.recycler_view_dodge_effect = findViewById(R.id.recycler_view_dodge_effect);
        this.recycler_view_hardmix_effet = findViewById(R.id.recycler_view_hardmix_effet);
        this.recycler_view_hue_effect = findViewById(R.id.recycler_view_hue_effect);
        this.recycler_view_burn_effect = findViewById(R.id.recycler_view_burn_effect);
        this.recycler_view_color_effect.setVisibility(View.GONE);
        this.recycler_view_dodge_effect.setVisibility(View.GONE);
        this.recycler_view_hardmix_effet.setVisibility(View.GONE);
        this.recycler_view_hue_effect.setVisibility(View.GONE);
        this.recycler_view_burn_effect.setVisibility(View.GONE);
        this.view_overlay = findViewById(R.id.view_overlay);
        this.view_hue = findViewById(R.id.view_hue);
        this.view_color = findViewById(R.id.view_color);
        this.view_dodge = findViewById(R.id.view_dodge);
        this.view_hardmix = findViewById(R.id.view_hardmix);
        this.view_burn = findViewById(R.id.view_burn);
        this.view_color.setVisibility(View.INVISIBLE);
        this.view_hue.setVisibility(View.INVISIBLE);
        this.view_dodge.setVisibility(View.INVISIBLE);
        this.view_hardmix.setVisibility(View.INVISIBLE);
        this.view_burn.setVisibility(View.INVISIBLE);
        this.constraintLayoutSticker = findViewById(R.id.constraint_layout_sticker);
        this.constraintLayoutAddText = findViewById(R.id.constraint_layout_confirm_text);
        this.viewPagerStickers = findViewById(R.id.stickerViewpaper);
        this.linear_layout_wrapper_sticker_list = findViewById(R.id.linear_layout_wrapper_sticker_list);
        this.guidelinePaint = findViewById(R.id.guidelinePaint);
        this.guideline = findViewById(R.id.guideline);
        this.seekbarSticker = findViewById(R.id.seekbarStickerAlpha);
        this.seekbarSticker.setVisibility(View.GONE);
        this.textViewTitleBrush = findViewById(R.id.textViewTitleBrush);
        this.imageViewBrushSize = findViewById(R.id.imageViewSizePaint);
        this.constraintLayoutSaveText = findViewById(R.id.constraint_layout_confirm_save_text);
        this.constraintLayoutSaveSticker = findViewById(R.id.constraint_layout_confirm_save_sticker);
        this.imageViewBrushOpacity = findViewById(R.id.imageViewOpacityPaint);
        this.imageViewBrushEraser = findViewById(R.id.imageViewEraserPaint);
        this.textViewValueBrush = findViewById(R.id.seekbarBrushValue);
        this.textViewValueOpacity = findViewById(R.id.seekbarOpacityValue);
        this.textViewValueEraser = findViewById(R.id.seekbarEraserValue);
        this.seekBarBrush = findViewById(R.id.seekbarBrushSize);
        this.seekBarOpacity = findViewById(R.id.seekbarOpacitySize);
        this.seekBarEraser = findViewById(R.id.seekbarEraserSize);
        this.textViewTitleMagic = findViewById(R.id.textViewTitleMagic);
        this.imageViewMagicSize = findViewById(R.id.imageViewSizeMagic);
        this.imageViewMagicOpacity = findViewById(R.id.imageViewOpacityMagic);
        this.imageViewMagicEraser = findViewById(R.id.imageViewEraserMagic);
        this.textViewMagicBrush = findViewById(R.id.seekbarBrushMagicValue);
        this.textViewMagicOpacity = findViewById(R.id.seekbarOpacityMagicValue);
        this.textViewMagicEraser = findViewById(R.id.seekbarEraserMagicValue);
        this.seekBarMagicBrush = findViewById(R.id.seekbarMagicSize);
        this.seekBarMagicOpacity = findViewById(R.id.seekbarOpacityMagic);
        this.seekBarMagicEraser = findViewById(R.id.seekbarEraserMagic);
        this.textViewTitleNeon = findViewById(R.id.textViewTitleNeon);
        this.imageViewNeonSize = findViewById(R.id.imageViewSizeNeon);
        this.imageViewNeonEraser = findViewById(R.id.imageViewEraserNeon);
        this.textViewNeonBrush = findViewById(R.id.seekbarNeonValue);
        this.textViewNeonEraser = findViewById(R.id.seekbarEraserNeon);
        this.seekBarNeonBrush = findViewById(R.id.seekbarNeonSize);
        this.seekBarNeonEraser = findViewById(R.id.seekbarNeonEraser);
        this.relativeLayoutPaint = findViewById(R.id.viewPaint);
        this.relativeLayoutNeon = findViewById(R.id.viewNeon);
        this.relativeLayoutMagic = findViewById(R.id.viewMagic);
        this.redo = findViewById(R.id.redo);
        this.undo = findViewById(R.id.undo);
        this.constraintLayoutPaint = findViewById(R.id.constraintLayoutPaint);
        this.constraintLayoutPaintTool = findViewById(R.id.constraintLayoutPaintTool);
        this.recyclerViewPaintListColor = findViewById(R.id.recyclerViewColorPaint);
        this.recyclerViewMagicListColor = findViewById(R.id.recyclerViewColorMagic);
        this.constraintLayoutMagic = findViewById(R.id.constraintLayoutMagic);
        this.constraintLayoutMagicTool = findViewById(R.id.constraintLayoutMagicTool);
        this.constraintLayoutNeon = findViewById(R.id.constraintLayoutNeon);
        this.constraintLayoutNeonTool = findViewById(R.id.constraintLayoutNeonTool);
        this.recyclerViewNeonListColor = findViewById(R.id.recyclerViewColorNeon);
        this.imageViewUndoPaint = findViewById(R.id.image_view_undo);
        this.imageViewUndoPaint.setVisibility(View.GONE);
        this.imageViewUndoMagic = findViewById(R.id.image_view_undo_Magic);
        this.imageViewUndoMagic.setVisibility(View.GONE);
        this.imageViewRedoPaint = findViewById(R.id.image_view_redo);
        this.imageViewRedoPaint.setVisibility(View.GONE);
        this.imageViewRedoMagic = findViewById(R.id.image_view_redo_Magic);
        this.imageViewRedoMagic.setVisibility(View.GONE);
        this.imageViewCleanPaint = findViewById(R.id.image_view_clean);
        this.imageViewCleanPaint.setVisibility(View.GONE);
        this.imageViewCleanMagic = findViewById(R.id.image_view_clean_Magic);
        this.imageViewCleanMagic.setVisibility(View.GONE);
        this.imageViewCleanNeon = findViewById(R.id.image_view_clean_neon);
        this.imageViewCleanNeon.setVisibility(View.GONE);
        this.imageViewUndoNeon = findViewById(R.id.image_view_undo_neon);
        this.imageViewUndoNeon.setVisibility(View.GONE);
        this.imageViewRedoNeon = findViewById(R.id.image_view_redo_neon);
        this.imageViewRedoNeon.setVisibility(View.GONE);
        this.relativeLayoutWrapper = findViewById(R.id.relative_layout_wrapper_photo);
        this.textViewSaveEditing = findViewById(R.id.text_view_save);
        this.image_view_exit = findViewById(R.id.image_view_exit);
        this.constraintLayoutSave = findViewById(R.id.constraintLayoutSave);
        this.constraintLayoutDraw = findViewById(R.id.constraint_layout_draw);
        this.constraintLayoutSplash = findViewById(R.id.constraint_layout_blur_sqaure);
        this.imageViewCompareAdjust = findViewById(R.id.imageViewCompareAdjust);
        this.imageViewCompareAdjust.setOnTouchListener(this.onTouchListener);
        this.imageViewCompareFilter = findViewById(R.id.image_view_compare_filter);
        this.imageViewCompareFilter.setOnTouchListener(this.onTouchListener);
        this.imageViewCompareFilter.setVisibility(View.GONE);
        this.imageViewCompareOverlay = findViewById(R.id.image_view_compare_overlay);
        this.imageViewCompareOverlay.setOnTouchListener(this.onTouchListener);
        this.imageViewCompareOverlay.setVisibility(View.GONE);
        this.relativeLayoutAddText = findViewById(R.id.relative_layout_add_text);
    }

    private void setOnBackPressDialog() {
        final Dialog dialogOnBackPressed = new Dialog(PolishEditorActivity.this, R.style.UploadDialog);
        dialogOnBackPressed.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogOnBackPressed.setContentView(R.layout.dialog_exit);
        dialogOnBackPressed.setCancelable(true);
        dialogOnBackPressed.show();
        this.textViewCancel = dialogOnBackPressed.findViewById(R.id.textViewCancel);
        this.textViewDiscard = dialogOnBackPressed.findViewById(R.id.textViewDiscard);
        this.textViewCancel.setOnClickListener(view -> {
            dialogOnBackPressed.dismiss();
        });

        this.textViewDiscard.setOnClickListener(view -> {
            dialogOnBackPressed.dismiss();
            PolishEditorActivity.this.moduleToolsId = null;
            PolishEditorActivity.this.finish();
            finish();
        });

    }

    private void setView() {
        this.recyclerViewTools.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        this.recyclerViewTools.setAdapter(this.mEditingToolsAdapter);
        this.recyclerViewTools.setHasFixedSize(true);
        this.recyclerViewDraw.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        this.recyclerViewDraw.setAdapter(this.mEditingDrawToolsAdapter);
        this.recyclerViewDraw.setHasFixedSize(true);
        this.recyclerViewSpalsh.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        this.recyclerViewSpalsh.setAdapter(this.mEditingSplashToolsAdapter);
        this.recyclerViewSpalsh.setHasFixedSize(true);
        this.recyclerViewEffect.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        this.recyclerViewEffect.setAdapter(this.mEditingEffectToolsAdapter);
        this.recyclerViewFilter.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        this.recyclerViewFilter.setHasFixedSize(true);
        this.recycler_view_overlay_effect.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        this.recycler_view_overlay_effect.setHasFixedSize(true);
        this.recycler_view_color_effect.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        this.recycler_view_color_effect.setHasFixedSize(true);
        this.recycler_view_dodge_effect.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        this.recycler_view_dodge_effect.setHasFixedSize(true);
        this.recycler_view_hardmix_effet.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        this.recycler_view_hardmix_effet.setHasFixedSize(true);
        this.recycler_view_hue_effect.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        this.recycler_view_hue_effect.setHasFixedSize(true);
        this.recycler_view_burn_effect.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        this.recycler_view_burn_effect.setHasFixedSize(true);
        new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        this.recyclerViewAdjust.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        this.recyclerViewAdjust.setHasFixedSize(true);
        this.mAdjustAdapter = new AdjustAdapter(getApplicationContext(), this);
        this.recyclerViewAdjust.setAdapter(this.mAdjustAdapter);
        this.recyclerViewPaintListColor.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        this.recyclerViewPaintListColor.setHasFixedSize(true);
        this.recyclerViewPaintListColor.setAdapter(new ColorAdapter(getApplicationContext(), this));
        this.recyclerViewNeonListColor.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        this.recyclerViewNeonListColor.setHasFixedSize(true);
        this.recyclerViewNeonListColor.setAdapter(new ColorAdapter(getApplicationContext(), this));
        this.recyclerViewMagicListColor.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        this.recyclerViewMagicListColor.setHasFixedSize(true);
        this.recyclerViewMagicListColor.setAdapter(new MagicBrushAdapter(getApplicationContext(), this));

        // new Handler().postDelayed(this::checkData, 3000);

        viewPagerStickers.setAdapter(new PagerAdapter() {
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
                View inflate = LayoutInflater.from(PolishEditorActivity.this.getBaseContext()).inflate(R.layout.list_sticker, null, false);
                RecyclerView recycler_view_sticker = inflate.findViewById(R.id.recyclerViewSticker);
                recycler_view_sticker.setHasFixedSize(true);
                recycler_view_sticker.setLayoutManager(new GridLayoutManager(PolishEditorActivity.this.getApplicationContext(), 7));
                switch (i) {
                    case 0:
                        recycler_view_sticker.setAdapter(new StickerAdapter(PolishEditorActivity.this.getApplicationContext(), StickerFileAsset.bubbleList(), i, PolishEditorActivity.this));
                        break;
                    case 1:
                        recycler_view_sticker.setAdapter(new StickerAdapter(PolishEditorActivity.this.getApplicationContext(), StickerFileAsset.rainbowList(), i, PolishEditorActivity.this));
                        break;
                    case 2:
                        recycler_view_sticker.setAdapter(new StickerAdapter(PolishEditorActivity.this.getApplicationContext(), StickerFileAsset.cartoonList(), i, PolishEditorActivity.this));
                        break;
                    case 3:
                        recycler_view_sticker.setAdapter(new StickerAdapter(PolishEditorActivity.this.getApplicationContext(), StickerFileAsset.childList(), i, PolishEditorActivity.this));
                        break;
                    case 4:
                        recycler_view_sticker.setAdapter(new StickerAdapter(PolishEditorActivity.this.getApplicationContext(), StickerFileAsset.flowerList(), i, PolishEditorActivity.this));
                        break;
                    case 5:
                        recycler_view_sticker.setAdapter(new StickerAdapter(PolishEditorActivity.this.getApplicationContext(), StickerFileAsset.amojiList(), i, PolishEditorActivity.this));
                        break;
                    case 6:
                        recycler_view_sticker.setAdapter(new StickerAdapter(PolishEditorActivity.this.getApplicationContext(), StickerFileAsset.deliciousList(), i, PolishEditorActivity.this));
                        break;
                    case 7:
                        recycler_view_sticker.setAdapter(new StickerAdapter(PolishEditorActivity.this.getApplicationContext(), StickerFileAsset.handList(), i, PolishEditorActivity.this));
                        break;
                    case 8:
                        recycler_view_sticker.setAdapter(new StickerAdapter(PolishEditorActivity.this.getApplicationContext(), StickerFileAsset.popularList(), i, PolishEditorActivity.this));
                        break;
                    case 9:
                        recycler_view_sticker.setAdapter(new StickerAdapter(PolishEditorActivity.this.getApplicationContext(), StickerFileAsset.valentineList(), i, PolishEditorActivity.this));
                        break;
                    case 10:
                        recycler_view_sticker.setAdapter(new StickerAdapter(PolishEditorActivity.this.getApplicationContext(), StickerFileAsset.emojList(), i, PolishEditorActivity.this));
                        break;
                    case 11:
                        recycler_view_sticker.setAdapter(new StickerAdapter(PolishEditorActivity.this.getApplicationContext(), StickerFileAsset.rageList(), i, PolishEditorActivity.this));
                        break;
                    case 12:
                        recycler_view_sticker.setAdapter(new StickerAdapter(PolishEditorActivity.this.getApplicationContext(), StickerFileAsset.christmasList(), i, PolishEditorActivity.this));
                        break;
                    case 13:
                        recycler_view_sticker.setAdapter(new StickerAdapter(PolishEditorActivity.this.getApplicationContext(), StickerFileAsset.unicornList(), i, PolishEditorActivity.this));
                        break;
                    case 14:
                        recycler_view_sticker.setAdapter(new StickerAdapter(PolishEditorActivity.this.getApplicationContext(), StickerFileAsset.stickerList(), i, PolishEditorActivity.this));
                        break;

                }

                viewGroup.addView(inflate);
                return inflate;
            }
        });
        RecyclerTabLayout recycler_tab_layout = findViewById(R.id.recycler_tab_layout);
        recycler_tab_layout.setUpWithAdapter(new StickersTabAdapter(viewPagerStickers, getApplicationContext()));
        recycler_tab_layout.setPositionThreshold(0.5f);
        recycler_tab_layout.setBackgroundColor(ContextCompat.getColor(this, R.color.TabColor));

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            new loadBitmapUri().execute(bundle.getString(PolishPickerView.KEY_SELECTED_PHOTOS));
        }
        Display display = getWindowManager().getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        CGENativeLibrary.setLoadImageCallback(this.loadImageCallback, null);
        if (Build.VERSION.SDK_INT < 26) {
            getWindow().setSoftInputMode(48);
        }
        this.polishEditor = new PolishEditor.Builder(this, this.polishView).setPinchTextScalable(true).build();
        this.polishEditor.setOnPhotoEditorListener(this);

        Preference.setKeyboard(getApplicationContext(), 0);
        this.keyboardProvider = new KeyboardHeightProvider(this);
        this.keyboardProvider.addKeyboardListener(i -> {
            if (i <= 0) {
                Preference.setHeightOfNotch(getApplicationContext(), -i);
            } else if (textFragment != null) {
                textFragment.updateAddTextBottomToolbarHeight(Preference.getHeightOfNotch(getApplicationContext()) + i);
                Preference.setKeyboard(getApplicationContext(), i + Preference.getHeightOfNotch(getApplicationContext()));
            }
        });
    }

    private void onClickListener() {
        this.textViewSaveEditing.setOnClickListener(view -> {
            if (mInterstitialAd != null) {
                mInterstitialAd.show(Objects.requireNonNull(PolishEditorActivity.this));
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

        this.undo.setOnClickListener(view -> PolishEditorActivity.this.setUndo());
        this.redo.setOnClickListener(view -> PolishEditorActivity.this.setRedo());
        this.linearLayoutOverlay.setOnClickListener(view -> PolishEditorActivity.this.setOverlayEffect());
        this.linearLayoutColor.setOnClickListener(view -> PolishEditorActivity.this.setColorEffect());
        this.linearLayoutDodge.setOnClickListener(view -> PolishEditorActivity.this.setDodgeEffect());
        this.linearLayoutHardmix.setOnClickListener(view -> PolishEditorActivity.this.setHardMixEffect());
        this.linearLayoutHue.setOnClickListener(view -> PolishEditorActivity.this.setHueEffect());
        this.linearLayoutBurn.setOnClickListener(view -> PolishEditorActivity.this.setBurnEffect());

        this.image_view_exit.setOnClickListener(view -> PolishEditorActivity.this.onBackPressed());
        this.imageViewBrushEraser.setOnClickListener(view -> PolishEditorActivity.this.setErasePaint());
        this.imageViewBrushSize.setOnClickListener(view -> PolishEditorActivity.this.setColorPaint());
        this.imageViewBrushOpacity.setOnClickListener(view -> PolishEditorActivity.this.setPaintOpacity());

        this.imageViewNeonEraser.setOnClickListener(view -> PolishEditorActivity.this.setEraseNeon());
        this.imageViewNeonSize.setOnClickListener(view -> PolishEditorActivity.this.setColorNeon());

        this.imageViewMagicEraser.setOnClickListener(view -> PolishEditorActivity.this.setEraseMagic());
        this.imageViewMagicSize.setOnClickListener(view -> PolishEditorActivity.this.setMagicBrush());
        this.imageViewMagicOpacity.setOnClickListener(view -> PolishEditorActivity.this.setMagicOpacity());

        this.seekBarBrush.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                PolishEditorActivity.this.polishEditor.setBrushSize((float) (i + 5));
                String brshValue = String.valueOf(i);
                textViewValueBrush.setText(brshValue);
            }
        });
        this.seekBarOpacity.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            public void onStopTrackingTouch(SeekBar seekBar) {

            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                PolishEditorActivity.this.polishEditor.setPaintOpacity(i);
                String brshValue = String.valueOf(i);
                textViewValueOpacity.setText(brshValue);
            }
        });
        this.seekBarEraser.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                PolishEditorActivity.this.polishEditor.setBrushEraserSize((float) i);
                PolishEditorActivity.this.polishEditor.brushEraser();
                String brshValue = String.valueOf(i);
                textViewValueEraser.setText(brshValue);
            }

            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        this.seekBarMagicBrush.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                PolishEditorActivity.this.polishEditor.setBrushSize((float) (i + 5));
                String brshValue = String.valueOf(i);
                textViewMagicBrush.setText(brshValue);
            }
        });
        this.seekBarMagicOpacity.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            public void onStopTrackingTouch(SeekBar seekBar) {

            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                PolishEditorActivity.this.polishEditor.setMagicOpacity(i);
                String brshValue = String.valueOf(i);
                textViewMagicOpacity.setText(brshValue);
            }
        });
        this.seekBarMagicEraser.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                PolishEditorActivity.this.polishEditor.setBrushEraserSize((float) i);
                PolishEditorActivity.this.polishEditor.brushEraser();
                String brshValue = String.valueOf(i);
                textViewMagicEraser.setText(brshValue);
            }

            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        this.seekBarNeonBrush.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                PolishEditorActivity.this.polishEditor.setBrushSize((float) (i + 5));
                String brshValue = String.valueOf(i);
                textViewNeonBrush.setText(brshValue);
            }
        });
        this.seekBarNeonEraser.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                PolishEditorActivity.this.polishEditor.setBrushEraserSize((float) i);
                PolishEditorActivity.this.polishEditor.brushEraser();
                String brshValue = String.valueOf(i);
                textViewNeonEraser.setText(brshValue);
            }

            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        this.seekbarSticker.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                Sticker currentSticker = PolishEditorActivity.this.polishView.getCurrentSticker();
                if (currentSticker != null) {
                    currentSticker.setAlpha(i);
                }
            }
        });

        this.imageViewAddSticker = findViewById(R.id.imageViewAddSticker);
        this.imageViewAddSticker.setVisibility(View.GONE);
        this.imageViewAddSticker.setOnClickListener(view -> {
            PolishEditorActivity.this.imageViewAddSticker.setVisibility(View.GONE);
            PolishEditorActivity.this.linear_layout_wrapper_sticker_list.setVisibility(View.VISIBLE);
        });

        this.relativeLayoutAddText.setOnClickListener(view -> {
            PolishEditorActivity.this.polishView.setHandlingSticker(null);
            PolishEditorActivity.this.textFragment();
        });
        this.adjustSeekBar = (DegreeSeekBar) findViewById(R.id.seekbarAdjust);
        this.adjustSeekBar.setCenterTextColor(getResources().getColor(R.color.mainColor));
        this.adjustSeekBar.setTextColor(getResources().getColor(R.color.white));
        this.adjustSeekBar.setPointColor(getResources().getColor(R.color.white));
        this.adjustSeekBar.setDegreeRange(-50, 50);
        this.adjustSeekBar.setScrollingListener(new DegreeSeekBar.ScrollingListener() {
            public void onScrollEnd() {
            }

            public void onScrollStart() {
            }

            public void onScroll(int i) {
                AdjustAdapter.AdjustModel currentAdjustModel = PolishEditorActivity.this.mAdjustAdapter.getCurrentAdjustModel();
                currentAdjustModel.originValue = (((float) Math.abs(i + 50)) * ((currentAdjustModel.maxValue - ((currentAdjustModel.maxValue + currentAdjustModel.minValue) / 2.0f)) / 50.0f)) + currentAdjustModel.minValue;
                PolishEditorActivity.this.polishEditor.setAdjustFilter(PolishEditorActivity.this.mAdjustAdapter.getFilterConfig());
            }
        });
        this.adjustFilter = (DegreeSeekBar) findViewById(R.id.seekbarFilter);
        this.adjustFilter.setCenterTextColor(getResources().getColor(R.color.mainColor));
        this.adjustFilter.setTextColor(getResources().getColor(R.color.white));
        this.adjustFilter.setPointColor(getResources().getColor(R.color.white));
        this.adjustFilter.setDegreeRange(0, 100);
        this.adjustFilter.setScrollingListener(new DegreeSeekBar.ScrollingListener() {
            public void onScrollEnd() {
            }

            public void onScrollStart() {
            }

            public void onScroll(int i) {
                PolishEditorActivity.this.polishView.setFilterIntensity(((float) i) / 100.0f);
            }
        });

        this.seekBarOverlay = findViewById(R.id.seekbarOverlay);
        this.seekBarOverlay.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                PolishEditorActivity.this.polishView.setFilterIntensity(((float) i) / 100.0f);
            }
        });


        polishStickerIconClose = new PolishStickerIcons(ContextCompat.getDrawable(this, R.drawable.ic_outline_close), 0, PolishStickerIcons.DELETE);
        polishStickerIconClose.setIconEvent(new DeleteIconEvent());
        polishStickerIconScale = new PolishStickerIcons(ContextCompat.getDrawable(this, R.drawable.ic_outline_scale), 3, PolishStickerIcons.SCALE);
        polishStickerIconScale.setIconEvent(new ZoomIconEvent());
        polishStickerIconFlip = new PolishStickerIcons(ContextCompat.getDrawable(this, R.drawable.ic_outline_flip), 1, PolishStickerIcons.FLIP);
        polishStickerIconFlip.setIconEvent(new FlipHorizontallyEvent());
        polishStickerIconRotate = new PolishStickerIcons(ContextCompat.getDrawable(this, R.drawable.ic_outline_rotate), 3, PolishStickerIcons.ROTATE);
        polishStickerIconRotate.setIconEvent(new ZoomIconEvent());
        polishStickerIconEdit = new PolishStickerIcons(ContextCompat.getDrawable(this, R.drawable.ic_outline_edit), 1, PolishStickerIcons.EDIT);
        polishStickerIconEdit.setIconEvent(new EditTextIconEvent());
        polishStickerIconAlign = new PolishStickerIcons(ContextCompat.getDrawable(this, R.drawable.ic_outline_center), 2, PolishStickerIcons.ALIGN);
        polishStickerIconAlign.setIconEvent(new AlignHorizontallyEvent());
        this.polishView.setIcons(Arrays.asList(polishStickerIconClose, polishStickerIconScale,
                polishStickerIconFlip, polishStickerIconEdit, polishStickerIconRotate, polishStickerIconAlign));
        this.polishView.setBackgroundColor(-16777216);
        this.polishView.setLocked(false);
        this.polishView.setConstrained(true);
        this.polishView.setOnStickerOperationListener(new PolishStickerView.OnStickerOperationListener() {
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
                PolishEditorActivity.this.seekbarSticker.setVisibility(View.VISIBLE);
                PolishEditorActivity.this.seekbarSticker.setProgress(sticker.getAlpha());
            }

            public void onStickerSelected(@NonNull Sticker sticker) {
                if (sticker instanceof PolishTextView) {
                    ((PolishTextView) sticker).setTextColor(SupportMenu.CATEGORY_MASK);
                    PolishEditorActivity.this.polishView.replace(sticker);
                    PolishEditorActivity.this.polishView.invalidate();
                }
                PolishEditorActivity.this.seekbarSticker.setVisibility(View.VISIBLE);
                PolishEditorActivity.this.seekbarSticker.setProgress(sticker.getAlpha());
            }

            public void onStickerDeleted(@NonNull Sticker sticker) {
                PolishEditorActivity.this.seekbarSticker.setVisibility(View.GONE);
            }

            public void onStickerTouchOutside() {
                PolishEditorActivity.this.seekbarSticker.setVisibility(View.GONE);
            }

            public void onStickerDoubleTap(@NonNull Sticker sticker) {
                if (sticker instanceof PolishTextView) {
                    sticker.setShow(false);
                    PolishEditorActivity.this.polishView.setHandlingSticker((Sticker) null);
                    PolishEditorActivity.this.textFragment = TextFragment.show(PolishEditorActivity.this, ((PolishTextView) sticker).getPolishText());
                    PolishEditorActivity.this.textEditor = new TextFragment.TextEditor() {
                        public void onDone(PolishText polishText) {
                            PolishEditorActivity.this.polishView.getStickers().remove(PolishEditorActivity.this.polishView.getLastHandlingSticker());
                            PolishEditorActivity.this.polishView.addSticker(new PolishTextView(PolishEditorActivity.this, polishText));
                        }

                        public void onBackButton() {
                            PolishEditorActivity.this.polishView.showLastHandlingSticker();
                        }
                    };
                    PolishEditorActivity.this.textFragment.setOnTextEditorListener(PolishEditorActivity.this.textEditor);
                }
            }
        });
    }


    public void setOverlayEffect() {
        this.recycler_view_overlay_effect.setVisibility(View.VISIBLE);
        this.recycler_view_color_effect.setVisibility(View.GONE);
        this.recycler_view_dodge_effect.setVisibility(View.GONE);
        this.recycler_view_hardmix_effet.setVisibility(View.GONE);
        this.recycler_view_hue_effect.setVisibility(View.GONE);
        this.recycler_view_burn_effect.setVisibility(View.GONE);
        this.text_view_overlay.setTextColor(ContextCompat.getColor(this, R.color.white));
        this.text_view_hue.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        this.text_view_color.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        this.text_view_dodge.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        this.text_view_hardmix.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        this.text_view_burn.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        this.view_overlay.setVisibility(View.VISIBLE);
        this.view_hue.setVisibility(View.INVISIBLE);
        this.view_color.setVisibility(View.INVISIBLE);
        this.view_dodge.setVisibility(View.INVISIBLE);
        this.view_hardmix.setVisibility(View.INVISIBLE);
        this.view_burn.setVisibility(View.INVISIBLE);
    }

    public void setColorEffect() {
        this.recycler_view_overlay_effect.setVisibility(View.GONE);
        this.recycler_view_color_effect.setVisibility(View.VISIBLE);
        this.recycler_view_dodge_effect.setVisibility(View.GONE);
        this.recycler_view_hardmix_effet.setVisibility(View.GONE);
        this.recycler_view_hue_effect.setVisibility(View.GONE);
        this.recycler_view_burn_effect.setVisibility(View.GONE);
        this.text_view_overlay.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        this.text_view_hue.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        this.text_view_color.setTextColor(ContextCompat.getColor(this, R.color.white));
        this.text_view_dodge.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        this.text_view_hardmix.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        this.text_view_burn.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        this.view_overlay.setVisibility(View.INVISIBLE);
        this.view_hue.setVisibility(View.INVISIBLE);
        this.view_color.setVisibility(View.VISIBLE);
        this.view_dodge.setVisibility(View.INVISIBLE);
        this.view_hardmix.setVisibility(View.INVISIBLE);
        this.view_burn.setVisibility(View.INVISIBLE);
    }

    public void setDodgeEffect() {
        this.recycler_view_overlay_effect.setVisibility(View.GONE);
        this.recycler_view_color_effect.setVisibility(View.GONE);
        this.recycler_view_dodge_effect.setVisibility(View.VISIBLE);
        this.recycler_view_hardmix_effet.setVisibility(View.GONE);
        this.recycler_view_hue_effect.setVisibility(View.GONE);
        this.recycler_view_burn_effect.setVisibility(View.GONE);
        this.text_view_overlay.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        this.text_view_hue.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        this.text_view_color.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        this.text_view_dodge.setTextColor(ContextCompat.getColor(this, R.color.white));
        this.text_view_hardmix.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        this.text_view_burn.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        this.view_overlay.setVisibility(View.INVISIBLE);
        this.view_hue.setVisibility(View.INVISIBLE);
        this.view_color.setVisibility(View.INVISIBLE);
        this.view_dodge.setVisibility(View.VISIBLE);
        this.view_hardmix.setVisibility(View.INVISIBLE);
        this.view_burn.setVisibility(View.INVISIBLE);
    }

    public void setHardMixEffect() {
        this.recycler_view_overlay_effect.setVisibility(View.GONE);
        this.recycler_view_color_effect.setVisibility(View.GONE);
        this.recycler_view_dodge_effect.setVisibility(View.GONE);
        this.recycler_view_hardmix_effet.setVisibility(View.VISIBLE);
        this.recycler_view_hue_effect.setVisibility(View.GONE);
        this.recycler_view_burn_effect.setVisibility(View.GONE);
        this.text_view_overlay.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        this.text_view_hue.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        this.text_view_color.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        this.text_view_dodge.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        this.text_view_hardmix.setTextColor(ContextCompat.getColor(this, R.color.white));
        this.text_view_burn.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        this.view_overlay.setVisibility(View.INVISIBLE);
        this.view_hue.setVisibility(View.INVISIBLE);
        this.view_color.setVisibility(View.INVISIBLE);
        this.view_dodge.setVisibility(View.INVISIBLE);
        this.view_hardmix.setVisibility(View.VISIBLE);
        this.view_burn.setVisibility(View.INVISIBLE);
    }

    public void setHueEffect() {
        this.recycler_view_overlay_effect.setVisibility(View.GONE);
        this.recycler_view_color_effect.setVisibility(View.GONE);
        this.recycler_view_dodge_effect.setVisibility(View.GONE);
        this.recycler_view_hardmix_effet.setVisibility(View.GONE);
        this.recycler_view_hue_effect.setVisibility(View.VISIBLE);
        this.recycler_view_burn_effect.setVisibility(View.GONE);
        this.text_view_overlay.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        this.text_view_hue.setTextColor(ContextCompat.getColor(this, R.color.white));
        this.text_view_color.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        this.text_view_dodge.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        this.text_view_hardmix.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        this.text_view_burn.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        this.view_overlay.setVisibility(View.INVISIBLE);
        this.view_hue.setVisibility(View.VISIBLE);
        this.view_color.setVisibility(View.INVISIBLE);
        this.view_dodge.setVisibility(View.INVISIBLE);
        this.view_hardmix.setVisibility(View.INVISIBLE);
        this.view_burn.setVisibility(View.INVISIBLE);
    }

    public void setBurnEffect() {
        this.recycler_view_overlay_effect.setVisibility(View.GONE);
        this.recycler_view_color_effect.setVisibility(View.GONE);
        this.recycler_view_dodge_effect.setVisibility(View.GONE);
        this.recycler_view_hardmix_effet.setVisibility(View.GONE);
        this.recycler_view_hue_effect.setVisibility(View.GONE);
        this.recycler_view_burn_effect.setVisibility(View.VISIBLE);
        this.text_view_overlay.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        this.text_view_hue.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        this.text_view_color.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        this.text_view_dodge.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        this.text_view_hardmix.setTextColor(ContextCompat.getColor(this, R.color.grayText));
        this.text_view_burn.setTextColor(ContextCompat.getColor(this, R.color.white));
        this.view_overlay.setVisibility(View.INVISIBLE);
        this.view_hue.setVisibility(View.INVISIBLE);
        this.view_color.setVisibility(View.INVISIBLE);
        this.view_dodge.setVisibility(View.INVISIBLE);
        this.view_hardmix.setVisibility(View.INVISIBLE);
        this.view_burn.setVisibility(View.VISIBLE);
    }

    private void setBottomToolbar(boolean z) {
        int mVisibility = !z ? View.GONE : View.VISIBLE;
        this.imageViewUndoPaint.setVisibility(mVisibility);
        this.imageViewRedoPaint.setVisibility(mVisibility);
        this.imageViewCleanPaint.setVisibility(mVisibility);
        this.imageViewCleanNeon.setVisibility(mVisibility);
        this.imageViewUndoNeon.setVisibility(mVisibility);
        this.imageViewRedoNeon.setVisibility(mVisibility);
        this.imageViewCleanMagic.setVisibility(mVisibility);
        this.imageViewUndoMagic.setVisibility(mVisibility);
        this.imageViewRedoMagic.setVisibility(mVisibility);
    }

    public void setErasePaint() {
        relativeLayoutPaint.setVisibility(View.VISIBLE);
        this.recyclerViewPaintListColor.setVisibility(View.VISIBLE);
        this.polishEditor.brushEraser();
        this.seekBarEraser.setProgress(20);
        this.seekBarEraser.setVisibility(View.VISIBLE);
        this.seekBarOpacity.setVisibility(View.GONE);
        this.seekBarBrush.setVisibility(View.GONE);
        this.textViewValueEraser.setVisibility(View.VISIBLE);
        this.textViewValueBrush.setVisibility(View.GONE);
        this.textViewValueOpacity.setVisibility(View.GONE);
        this.textViewTitleBrush.setText("Eraser");
        this.imageViewBrushSize.setImageResource(R.drawable.ic_brush);
        this.imageViewBrushOpacity.setImageResource(R.drawable.ic_opacity);
        this.imageViewBrushEraser.setImageResource(R.drawable.ic_eraser_select);
    }

    public void setColorPaint() {
        this.relativeLayoutPaint.setVisibility(View.GONE);
        this.recyclerViewPaintListColor.setVisibility(View.VISIBLE);
        this.recyclerViewPaintListColor.scrollToPosition(0);
        colorAdapter = (ColorAdapter) this.recyclerViewPaintListColor.getAdapter();
        if (colorAdapter != null) {
            colorAdapter.setSelectedColorIndex(0);
        }
        if (colorAdapter != null) {
            colorAdapter.notifyDataSetChanged();
        }
        this.polishEditor.setBrushMode(1);
        this.polishEditor.setBrushDrawingMode(true);
        this.seekBarBrush.setProgress(20);
        this.seekBarEraser.setVisibility(View.GONE);
        this.seekBarOpacity.setVisibility(View.GONE);
        this.seekBarBrush.setVisibility(View.VISIBLE);
        this.textViewValueEraser.setVisibility(View.GONE);
        this.textViewValueBrush.setVisibility(View.VISIBLE);
        this.textViewValueOpacity.setVisibility(View.GONE);
        this.textViewTitleBrush.setText("Brush");
        this.imageViewBrushSize.setImageResource(R.drawable.ic_brush_select);
        this.imageViewBrushOpacity.setImageResource(R.drawable.ic_opacity);
        this.imageViewBrushEraser.setImageResource(R.drawable.ic_eraser);
    }

    public void setPaintOpacity() {
        this.relativeLayoutPaint.setVisibility(View.GONE);
        this.seekBarOpacity.setProgress(100);
        this.seekBarEraser.setVisibility(View.GONE);
        this.seekBarOpacity.setVisibility(View.VISIBLE);
        this.seekBarBrush.setVisibility(View.GONE);
        this.textViewValueEraser.setVisibility(View.GONE);
        this.textViewValueBrush.setVisibility(View.GONE);
        this.textViewValueOpacity.setVisibility(View.VISIBLE);
        this.textViewTitleBrush.setText("Opacity");
        this.imageViewBrushSize.setImageResource(R.drawable.ic_brush);
        this.imageViewBrushOpacity.setImageResource(R.drawable.ic_opacity_select);
        this.imageViewBrushEraser.setImageResource(R.drawable.ic_eraser);
    }

    public void setEraseNeon() {
        relativeLayoutNeon.setVisibility(View.VISIBLE);
        this.recyclerViewNeonListColor.setVisibility(View.VISIBLE);
        this.polishEditor.brushEraser();
        this.seekBarNeonEraser.setProgress(20);
        this.seekBarNeonEraser.setVisibility(View.VISIBLE);
        this.seekBarNeonBrush.setVisibility(View.GONE);
        this.textViewNeonEraser.setVisibility(View.VISIBLE);
        this.textViewNeonBrush.setVisibility(View.GONE);
        this.textViewTitleNeon.setText("Eraser");
        this.imageViewNeonSize.setImageResource(R.drawable.ic_brush);
        this.imageViewNeonEraser.setImageResource(R.drawable.ic_eraser_select);
    }

    public void setColorNeon() {
        this.relativeLayoutNeon.setVisibility(View.GONE);
        this.recyclerViewNeonListColor.setVisibility(View.VISIBLE);
        this.recyclerViewNeonListColor.scrollToPosition(0);
        colorAdapter = (ColorAdapter) this.recyclerViewNeonListColor.getAdapter();
        if (colorAdapter != null) {
            colorAdapter.setSelectedColorIndex(0);
        }
        if (colorAdapter != null) {
            colorAdapter.notifyDataSetChanged();
        }
        this.polishEditor.setBrushMode(2);
        this.polishEditor.setBrushDrawingMode(true);
        this.seekBarNeonBrush.setProgress(20);
        this.seekBarNeonEraser.setVisibility(View.GONE);
        this.seekBarNeonBrush.setVisibility(View.VISIBLE);
        this.textViewNeonEraser.setVisibility(View.GONE);
        this.textViewNeonBrush.setVisibility(View.VISIBLE);
        this.textViewTitleNeon.setText("Brush");
        this.imageViewNeonSize.setImageResource(R.drawable.ic_brush_select);
        this.imageViewNeonEraser.setImageResource(R.drawable.ic_eraser);
    }

    public void setEraseMagic() {
        this.relativeLayoutMagic.setVisibility(View.VISIBLE);
        this.recyclerViewMagicListColor.setVisibility(View.VISIBLE);
        this.polishEditor.brushEraser();
        this.seekBarMagicEraser.setProgress(20);
        this.seekBarMagicEraser.setVisibility(View.VISIBLE);
        this.seekBarMagicOpacity.setVisibility(View.GONE);
        this.seekBarMagicBrush.setVisibility(View.GONE);
        this.textViewMagicEraser.setVisibility(View.VISIBLE);
        this.textViewMagicBrush.setVisibility(View.GONE);
        this.textViewMagicOpacity.setVisibility(View.GONE);
        this.textViewTitleMagic.setText("Eraser");
        this.imageViewMagicSize.setImageResource(R.drawable.ic_brush);
        this.imageViewMagicOpacity.setImageResource(R.drawable.ic_opacity);
        this.imageViewMagicEraser.setImageResource(R.drawable.ic_eraser_select);
    }

    public void setMagicBrush() {
        this.relativeLayoutMagic.setVisibility(View.GONE);
        this.recyclerViewMagicListColor.setVisibility(View.VISIBLE);
        this.recyclerViewMagicListColor.scrollToPosition(0);
        this.polishEditor.setBrushMagic(MagicBrushAdapter.lstDrawBitmapModel(getApplicationContext()).get(0));
        MagicBrushAdapter magicBrushAdapter = (MagicBrushAdapter) this.recyclerViewMagicListColor.getAdapter();
        if (magicBrushAdapter != null) {
            magicBrushAdapter.setSelectedColorIndex(0);
        }
        this.recyclerViewMagicListColor.scrollToPosition(0);
        if (magicBrushAdapter != null) {
            magicBrushAdapter.notifyDataSetChanged();
        }
        this.polishEditor.setBrushMode(3);
        this.polishEditor.setBrushDrawingMode(true);
        this.seekBarMagicBrush.setProgress(20);
        this.seekBarMagicEraser.setVisibility(View.GONE);
        this.seekBarMagicOpacity.setVisibility(View.GONE);
        this.seekBarMagicBrush.setVisibility(View.VISIBLE);
        this.textViewMagicEraser.setVisibility(View.GONE);
        this.textViewMagicBrush.setVisibility(View.VISIBLE);
        this.textViewMagicOpacity.setVisibility(View.GONE);
        this.textViewTitleMagic.setText("Brush");
        this.imageViewMagicSize.setImageResource(R.drawable.ic_brush_select);
        this.imageViewMagicOpacity.setImageResource(R.drawable.ic_opacity);
        this.imageViewMagicEraser.setImageResource(R.drawable.ic_eraser);
    }

    public void setMagicOpacity() {
        this.relativeLayoutMagic.setVisibility(View.GONE);
        this.polishEditor.setBrushMode(3);
        this.seekBarMagicOpacity.setProgress(100);
        this.polishEditor.setBrushDrawingMode(true);
        this.seekBarMagicEraser.setVisibility(View.GONE);
        this.seekBarMagicOpacity.setVisibility(View.VISIBLE);
        this.seekBarMagicBrush.setVisibility(View.GONE);
        this.textViewMagicEraser.setVisibility(View.GONE);
        this.textViewMagicBrush.setVisibility(View.GONE);
        this.textViewMagicOpacity.setVisibility(View.VISIBLE);
        this.textViewTitleMagic.setText("Opacity");
        this.imageViewMagicSize.setImageResource(R.drawable.ic_brush);
        this.imageViewMagicOpacity.setImageResource(R.drawable.ic_opacity_select);
        this.imageViewMagicEraser.setImageResource(R.drawable.ic_eraser);
    }

    public void viewSlideUp(final View showLayout) {
        showLayout.setVisibility(View.VISIBLE);
        slideUpAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);
        showLayout.startAnimation(slideUpAnimation);
        slideUpAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }

    public void viewSlideDown(final View hideLayout) {
        hideLayout.setVisibility(View.GONE);
        hideLayout.startAnimation(slideDownAnimation);
        slideDownAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }


    public CGENativeLibrary.LoadImageCallback loadImageCallback = new CGENativeLibrary.LoadImageCallback() {
        public Bitmap loadImage(String string, Object object) {
            try {
                return BitmapFactory.decodeStream(PolishEditorActivity.this.getAssets().open(string));
            } catch (IOException ioException) {
                return null;
            }
        }

        public void loadImageOK(Bitmap bitmap, Object object) {
            bitmap.recycle();
        }
    };

    @SuppressLint("ClickableViewAccessibility")
    View.OnTouchListener onTouchListener = (view, motionEvent) -> {
        switch (motionEvent.getAction()) {
            case 0:
                PolishEditorActivity.this.polishView.getGLSurfaceView().setAlpha(0.0f);
                return true;
            case 1:
                PolishEditorActivity.this.polishView.getGLSurfaceView().setAlpha(1.0f);
                return false;
            default:
                return true;
        }
    };

    public void onRequestPermissionsResult(int i, @NonNull String[] string, @NonNull int[] i2) {
        super.onRequestPermissionsResult(i, string, i2);
    }

    public void onAddViewListener(Drawing viewType, int i) {
        Log.d(TAG, "onAddViewListener() called with: viewType = [" + viewType + "], numberOfAddedViews = [" + i + "]");
    }

    public void onRemoveViewListener(int i) {
        Log.d(TAG, "onRemoveViewListener() called with: numberOfAddedViews = [" + i + "]");
    }

    public void onRemoveViewListener(Drawing viewType, int i) {
        Log.d(TAG, "onRemoveViewListener() called with: viewType = [" + viewType + "], numberOfAddedViews = [" + i + "]");
    }

    public void onStartViewChangeListener(Drawing viewType) {
        Log.d(TAG, "onStartViewChangeListener() called with: viewType = [" + viewType + "]");
    }

    public void onStopViewChangeListener(Drawing viewType) {
        Log.d(TAG, "onStopViewChangeListener() called with: viewType = [" + viewType + "]");
    }

    private void setUndo() {
        polishView.undo();
    }

    private void setRedo() {
        polishView.redo();
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imageViewCloseAdjust:
            case R.id.image_view_close_sticker:
            case R.id.imageViewCloseText:
            case R.id.imageViewCloseFilter:
            case R.id.imageViewCloseOverlay:
                setVisibleSave();
                onBackPressed();
                return;
            case R.id.imageViewSaveAdjust:
                new SaveFilter().execute();
                this.constraintLayoutAdjust.setVisibility(View.GONE);
                this.recyclerViewTools.setVisibility(View.VISIBLE);
                this.constraintLayoutSave.setVisibility(View.VISIBLE);
                setGuideLine();
                setVisibleSave();
                this.moduleToolsId = Module.NONE;
                return;
            case R.id.imageViewSavePaint:
                showLoading(true);
                runOnUiThread(() -> {
                    polishEditor.setBrushDrawingMode(false);
                    imageViewUndoPaint.setVisibility(View.GONE);
                    imageViewRedoPaint.setVisibility(View.GONE);
                    imageViewCleanPaint.setVisibility(View.GONE);
                    this.constraintLayoutPaintTool.setVisibility(View.GONE);
                    this.constraintLayoutSave.setVisibility(View.VISIBLE);
                    viewSlideUp(recyclerViewTools);
                    viewSlideDown(constraintLayoutPaint);
                    setGuideLine();
                    polishView.setImageSource(polishEditor.getBrushDrawingView().getDrawBitmap(polishView.getCurrentBitmap()));
                    polishEditor.clearBrushAllViews();
                    showLoading(false);
                    reloadingLayout();
                });
                setVisibleSave();
                this.moduleToolsId = Module.NONE;
                return;
            case R.id.imageViewSaveMagic:
                showLoading(true);
                runOnUiThread(() -> {
                    polishEditor.setBrushDrawingMode(false);
                    imageViewUndoMagic.setVisibility(View.GONE);
                    imageViewRedoMagic.setVisibility(View.GONE);
                    imageViewCleanMagic.setVisibility(View.GONE);
                    viewSlideUp(recyclerViewTools);
                    viewSlideDown(constraintLayoutMagic);
                    this.constraintLayoutMagicTool.setVisibility(View.GONE);
                    this.constraintLayoutSave.setVisibility(View.VISIBLE);
                    setGuideLine();
                    polishView.setImageSource(polishEditor.getBrushDrawingView().getDrawBitmap(polishView.getCurrentBitmap()));
                    polishEditor.clearBrushAllViews();
                    showLoading(false);
                    reloadingLayout();
                });
                setVisibleSave();
                this.moduleToolsId = Module.NONE;
                return;
            case R.id.imageViewSaveNeon:
                showLoading(true);
                runOnUiThread(() -> {
                    polishEditor.setBrushDrawingMode(false);
                    imageViewUndoNeon.setVisibility(View.GONE);
                    imageViewRedoNeon.setVisibility(View.GONE);
                    this.constraintLayoutNeonTool.setVisibility(View.GONE);
                    this.constraintLayoutSave.setVisibility(View.VISIBLE);
                    viewSlideUp(recyclerViewTools);
                    viewSlideDown(constraintLayoutNeon);
                    setGuideLine();
                    polishView.setImageSource(polishEditor.getBrushDrawingView().getDrawBitmap(polishView.getCurrentBitmap()));
                    polishEditor.clearBrushAllViews();
                    showLoading(false);
                    reloadingLayout();
                });
                setVisibleSave();
                this.moduleToolsId = Module.NONE;
                return;
            case R.id.imageViewSaveFilter:
                new SaveFilter().execute();
                this.imageViewCompareFilter.setVisibility(View.GONE);
                viewSlideUp(recyclerViewTools);
                viewSlideDown(constraintLayoutFilter);
                setGuideLine();
                this.moduleToolsId = Module.NONE;
                setVisibleSave();
                return;
            case R.id.imageViewSaveOverlay:
                new SaveFilter().execute();
                this.imageViewCompareOverlay.setVisibility(View.GONE);
                this.constraintLayoutSaveOverlay.setVisibility(View.GONE);
                seekBarOverlay.setVisibility(View.GONE);
                viewSlideUp(recyclerViewTools);
                viewSlideDown(constraintLayoutOverlay);
                this.moduleToolsId = Module.NONE;
                setGuideLine();
                setVisibleSave();
                return;
            case R.id.image_view_save_sticker:
                this.polishView.setHandlingSticker(null);
                this.polishView.setLocked(true);
                this.seekbarSticker.setVisibility(View.GONE);
                this.imageViewAddSticker.setVisibility(View.GONE);
                this.constraintLayoutSaveSticker.setVisibility(View.GONE);
                if (!this.polishView.getStickers().isEmpty()) {
                    new SaveSticker().execute();
                }
                this.linear_layout_wrapper_sticker_list.setVisibility(View.VISIBLE);
                viewSlideUp(recyclerViewTools);
                viewSlideDown(constraintLayoutSticker);
                setVisibleSave();
                this.moduleToolsId = Module.NONE;
                setGuideLine();
                return;
            case R.id.imageViewSaveText:
                this.polishView.setHandlingSticker(null);
                this.polishView.setLocked(true);
                this.constraintLayoutSaveText.setVisibility(View.GONE);
                setGuideLine();
                if (!this.polishView.getStickers().isEmpty()) {
                    new SaveSticker().execute();
                }
                viewSlideUp(recyclerViewTools);
                viewSlideDown(constraintLayoutAddText);
                setVisibleSave();
                this.moduleToolsId = Module.NONE;
                return;
            case R.id.image_view_redo_neon:
            case R.id.image_view_redo_Magic:
            case R.id.image_view_redo:
                this.polishEditor.redoBrush();
                return;
            case R.id.image_view_undo_neon:
            case R.id.image_view_undo:
            case R.id.image_view_undo_Magic:
                this.polishEditor.undoBrush();
                return;
            case R.id.image_view_clean_neon:
            case R.id.image_view_clean_Magic:
            case R.id.image_view_clean:
                this.polishEditor.clearBrushAllViews();
                return;
            default:
        }
    }

    public void isPermissionGranted(boolean z, String string) {
        if (z) {
            new SaveEditingBitmap().execute();
        }
    }


    public void textFragment() {
        this.textFragment = TextFragment.show(this);
        this.textEditor = new TextFragment.TextEditor() {
            public void onDone(PolishText polishText) {
                PolishEditorActivity.this.polishView.addSticker(new PolishTextView(PolishEditorActivity.this.getApplicationContext(), polishText));
            }

            public void onBackButton() {
                if (PolishEditorActivity.this.polishView.getStickers().isEmpty()) {
                    PolishEditorActivity.this.onBackPressed();
                }
            }
        };
        this.textFragment.setOnTextEditorListener(this.textEditor);
    }

    public void onQuShotToolSelected(Module module) {
        this.moduleToolsId = module;
        switch (module) {
            case TEXT:
                setGoneSave();
                setGuideLine();
                this.polishView.setLocked(false);
                textFragment();
                viewSlideDown(recyclerViewTools);
                viewSlideUp(constraintLayoutAddText);
                this.constraintLayoutSplash.setVisibility(View.GONE);
                this.constraintLayoutEffects.setVisibility(View.GONE);
                this.constraintLayoutDraw.setVisibility(View.GONE);
                this.constraintLayoutSaveText.setVisibility(View.VISIBLE);
                break;
            case STICKER:
                setGoneSave();
                setGuideLine();
                this.polishView.setLocked(false);
                this.constraintLayoutSplash.setVisibility(View.GONE);
                this.constraintLayoutEffects.setVisibility(View.GONE);
                this.constraintLayoutDraw.setVisibility(View.GONE);
                this.constraintLayoutSaveSticker.setVisibility(View.VISIBLE);
                this.linear_layout_wrapper_sticker_list.setVisibility(View.VISIBLE);
                if (!this.polishView.getStickers().isEmpty()) {
                    this.polishView.getStickers().clear();
                    this.polishView.setHandlingSticker(null);
                }
                viewSlideDown(recyclerViewTools);
                viewSlideUp(constraintLayoutSticker);
                break;
            case ADJUST:
                setGoneSave();
                setGuideLinePaint();
                this.constraintLayoutSplash.setVisibility(View.GONE);
                this.constraintLayoutEffects.setVisibility(View.GONE);
                this.constraintLayoutDraw.setVisibility(View.GONE);
                viewSlideDown(recyclerViewTools);
                viewSlideUp(constraintLayoutAdjust);
                this.adjustSeekBar.setCurrentDegrees(0);
                this.mAdjustAdapter = new AdjustAdapter(getApplicationContext(), this);
                this.recyclerViewAdjust.setAdapter(this.mAdjustAdapter);
                this.polishEditor.setAdjustFilter(this.mAdjustAdapter.getFilterConfig());
                break;
            case FILTER:
                this.constraintLayoutSplash.setVisibility(View.GONE);
                this.constraintLayoutEffects.setVisibility(View.GONE);
                this.constraintLayoutDraw.setVisibility(View.GONE);
                setGoneSave();
                new openFilters().execute();
                break;
            case DRAW:
                this.constraintLayoutSplash.setVisibility(View.GONE);
                this.constraintLayoutEffects.setVisibility(View.GONE);
                viewSlideUp(constraintLayoutDraw);
                break;
            case EFFECT:
                this.constraintLayoutSplash.setVisibility(View.GONE);
                this.constraintLayoutDraw.setVisibility(View.GONE);
                viewSlideUp(constraintLayoutEffects);
                break;
            case RATIO:
                this.constraintLayoutSplash.setVisibility(View.GONE);
                this.constraintLayoutEffects.setVisibility(View.GONE);
                this.constraintLayoutDraw.setVisibility(View.GONE);
                new openBlurFragment().execute();
                goneLayout();
                break;
            case BACKGROUND:
                this.constraintLayoutSplash.setVisibility(View.GONE);
                this.constraintLayoutEffects.setVisibility(View.GONE);
                this.constraintLayoutDraw.setVisibility(View.GONE);
                new openFrameFragment().execute();
                goneLayout();
                break;
            case HSL:
                this.constraintLayoutSplash.setVisibility(View.GONE);
                this.constraintLayoutEffects.setVisibility(View.GONE);
                this.constraintLayoutDraw.setVisibility(View.GONE);
                HSlFragment.show(this, this, this.polishView.getCurrentBitmap());
                goneLayout();
                break;
            case MIRROR:
                this.constraintLayoutSplash.setVisibility(View.GONE);
                this.constraintLayoutEffects.setVisibility(View.GONE);
                this.constraintLayoutDraw.setVisibility(View.GONE);
                new PolishEditorActivity.openMirrorFragment().execute(new Void[0]);
                goneLayout();
                break;
            case SQ_BG:
                viewSlideUp(constraintLayoutSplash);
                this.constraintLayoutEffects.setVisibility(View.GONE);
                this.constraintLayoutDraw.setVisibility(View.GONE);
                break;
            case CROP:
                this.constraintLayoutSplash.setVisibility(View.GONE);
                this.constraintLayoutEffects.setVisibility(View.GONE);
                this.constraintLayoutDraw.setVisibility(View.GONE);
                CropFragment.show(this, this, this.polishView.getCurrentBitmap());
                goneLayout();
                break;
            case BLURE:
                this.constraintLayoutSplash.setVisibility(View.GONE);
                this.constraintLayoutEffects.setVisibility(View.GONE);
                this.constraintLayoutDraw.setVisibility(View.GONE);
                goneLayout();
                BitmapTransfer.bitmap = this.polishView.getCurrentBitmap();
                Intent intent1 = new Intent(PolishEditorActivity.this, BlurLayout.class);
                startActivityForResult(intent1, 900);
                overridePendingTransition(R.anim.enter, R.anim.exit);
                break;

        }
        this.polishView.setHandlingSticker(null);
    }

    public void onQuShotEffectToolSelected(Module module) {
        this.moduleToolsId = module;
        switch (module) {
            case OVERLAY:
                setGoneSave();
                this.constraintLayoutSaveOverlay.setVisibility(View.VISIBLE);
                new effectOvarlay().execute();
                new effectColor().execute();
                new effectHardmix().execute();
                new effectDodge().execute();
                new effectDivide().execute();
                new effectBurn().execute();
                seekBarOverlay.setVisibility(View.VISIBLE);
                break;
            case NEON:
                this.constraintLayoutEffects.setVisibility(View.GONE);
                goneLayout();
                new neonEffect().execute();
                break;
            case PIX:
                this.constraintLayoutEffects.setVisibility(View.GONE);
                goneLayout();
                new pixEffect().execute();
                break;
            case ART:
                this.constraintLayoutEffects.setVisibility(View.GONE);
                goneLayout();
                new artEffect().execute();
                break;
            case WINGS:
                this.constraintLayoutEffects.setVisibility(View.GONE);
                goneLayout();
                new wingEffect().execute();
                break;
            case MOTION:
                this.constraintLayoutEffects.setVisibility(View.GONE);
                new motionEffect().execute();
                goneLayout();
                break;
            case SPLASH:
                this.constraintLayoutEffects.setVisibility(View.GONE);
                goneLayout();
                BitmapTransfer.bitmap = this.polishView.getCurrentBitmap();
                Intent intent = new Intent(PolishEditorActivity.this, SplashLayout.class);
                startActivityForResult(intent, 900);
                overridePendingTransition(R.anim.enter, R.anim.exit);
                break;
            case DRIP:
                this.constraintLayoutEffects.setVisibility(View.GONE);
                goneLayout();
                new dripEffect().execute();
                break;
        }
        this.polishView.setHandlingSticker(null);
    }

    public void onQuShotSQToolSelected(Module module) {
        this.moduleToolsId = module;
        switch (module) {
            case SPLASH_BG:
                this.constraintLayoutSplash.setVisibility(View.GONE);
                new openSplashSquareBackgroundFragment(true).execute();
                break;
            case SPLASH_SQ:
                this.constraintLayoutSplash.setVisibility(View.GONE);
                new openSplashFragment(true).execute();
                break;
            case SKETCH_SQ:
                this.constraintLayoutSplash.setVisibility(View.GONE);
                new openSketchFragment(true).execute();
                break;
            case SKETCH_BG:
                this.constraintLayoutSplash.setVisibility(View.GONE);
                new openSketchBackgroundFragment(true).execute();
                break;
            case BLUR_BG:
                this.constraintLayoutSplash.setVisibility(View.GONE);
                new openBlurSquareBackgroundFragment(true).execute();
                break;
        }
        this.polishView.setHandlingSticker(null);
    }

    public void onQuShotDrawToolSelected(Module module) {
        this.moduleToolsId = module;
        ConstraintSet constraintSet;
        switch (module) {
            case PAINT:
                setColorPaint();
                this.polishEditor.setBrushDrawingMode(true);
                this.constraintLayoutPaint.setVisibility(View.VISIBLE);
                this.recyclerViewTools.setVisibility(View.GONE);
                this.constraintLayoutDraw.setVisibility(View.GONE);
                this.constraintLayoutSave.setVisibility(View.GONE);
                PolishEditorActivity.this.constraintLayoutPaint.setVisibility(View.VISIBLE);
                this.constraintLayoutPaintTool.setVisibility(View.VISIBLE);
                this.polishEditor.clearBrushAllViews();
                this.polishEditor.setBrushDrawingMode(false);
                setGoneSave();
                setBottomToolbar(true);
                constraintSet = new ConstraintSet();
                constraintSet.clone(this.constraintLayoutView);
                constraintSet.connect(this.relativeLayoutWrapper.getId(), 1, this.constraintLayoutView.getId(), 1, 0);
                constraintSet.connect(this.relativeLayoutWrapper.getId(), 4, this.constraintLayoutPaint.getId(), 3, 0);
                constraintSet.connect(this.relativeLayoutWrapper.getId(), 2, this.constraintLayoutView.getId(), 2, 0);
                constraintSet.applyTo(this.constraintLayoutView);
                this.polishEditor.setBrushMode(1);
                reloadingLayout();
                break;
            case COLORED:
                new openColoredFragment().execute();
                this.constraintLayoutDraw.setVisibility(View.GONE);
                goneLayout();
                break;
            case NEON:
                setColorNeon();
                this.polishEditor.setBrushDrawingMode(true);
                this.recyclerViewTools.setVisibility(View.GONE);
                this.constraintLayoutNeonTool.setVisibility(View.VISIBLE);
                this.constraintLayoutDraw.setVisibility(View.GONE);
                PolishEditorActivity.this.constraintLayoutSave.setVisibility(View.GONE);
                PolishEditorActivity.this.constraintLayoutNeon.setVisibility(View.VISIBLE);
                this.polishEditor.clearBrushAllViews();
                this.polishEditor.setBrushDrawingMode(false);
                setGoneSave();
                setBottomToolbar(true);
                constraintSet = new ConstraintSet();
                constraintSet.clone(this.constraintLayoutView);
                constraintSet.connect(this.relativeLayoutWrapper.getId(), 1, this.constraintLayoutView.getId(), 1, 0);
                constraintSet.connect(this.relativeLayoutWrapper.getId(), 4, this.constraintLayoutNeon.getId(), 3, 0);
                constraintSet.connect(this.relativeLayoutWrapper.getId(), 2, this.constraintLayoutView.getId(), 2, 0);
                constraintSet.applyTo(this.constraintLayoutView);
                this.polishEditor.setBrushMode(2);
                reloadingLayout();
                break;
            case MAGIC:
                setMagicBrush();
                this.polishEditor.setBrushDrawingMode(true);
                this.recyclerViewTools.setVisibility(View.GONE);
                this.constraintLayoutDraw.setVisibility(View.GONE);
                this.constraintLayoutMagic.setVisibility(View.VISIBLE);
                this.constraintLayoutMagicTool.setVisibility(View.VISIBLE);
                this.polishEditor.clearBrushAllViews();
                this.polishEditor.setBrushDrawingMode(false);
                setGoneSave();
                setBottomToolbar(true);
                constraintSet = new ConstraintSet();
                constraintSet.clone(this.constraintLayoutView);
                constraintSet.connect(this.relativeLayoutWrapper.getId(), 1, this.constraintLayoutView.getId(), 1, 0);
                constraintSet.connect(this.relativeLayoutWrapper.getId(), 4, this.constraintLayoutMagic.getId(), 3, 0);
                constraintSet.connect(this.relativeLayoutWrapper.getId(), 2, this.constraintLayoutView.getId(), 2, 0);
                constraintSet.applyTo(this.constraintLayoutView);
                this.polishEditor.setBrushMode(3);
                reloadingLayout();
                break;
            case MOSAIC:
                new openShapeFragment().execute();
                this.constraintLayoutDraw.setVisibility(View.GONE);
                goneLayout();
                break;
        }
        this.polishView.setHandlingSticker(null);
    }

    private void goneLayout() {
        setVisibleSave();
    }

    public void setGoneSave() {
        this.constraintLayoutSave.setVisibility(View.GONE);
    }

    public void setGuideLine() {
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(this.constraintLayoutView);
        constraintSet.connect(this.relativeLayoutWrapper.getId(), 1, this.constraintLayoutView.getId(), 1, 0);
        constraintSet.connect(this.relativeLayoutWrapper.getId(), 4, this.guideline.getId(), 3, 0);
        constraintSet.connect(this.relativeLayoutWrapper.getId(), 2, this.constraintLayoutView.getId(), 2, 0);
        constraintSet.applyTo(this.constraintLayoutView);
    }

    public void setGuideLinePaint() {
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(this.constraintLayoutView);
        constraintSet.connect(this.relativeLayoutWrapper.getId(), 1, this.constraintLayoutView.getId(), 1, 0);
        constraintSet.connect(this.relativeLayoutWrapper.getId(), 4, this.guidelinePaint.getId(), 3, 0);
        constraintSet.connect(this.relativeLayoutWrapper.getId(), 2, this.constraintLayoutView.getId(), 2, 0);
        constraintSet.applyTo(this.constraintLayoutView);
    }

    public void setVisibleSave() {
        this.constraintLayoutSave.setVisibility(View.VISIBLE);
    }

    public void onBackPressed() {
        if (this.moduleToolsId != null) {
            try {
                switch (this.moduleToolsId) {
                    case PAINT:
                        setVisibleSave();
                        viewSlideUp(recyclerViewTools);
                        viewSlideDown(constraintLayoutPaint);
                        this.imageViewUndoPaint.setVisibility(View.GONE);
                        this.imageViewRedoPaint.setVisibility(View.GONE);
                        this.imageViewCleanPaint.setVisibility(View.GONE);
                        this.constraintLayoutSave.setVisibility(View.VISIBLE);
                        this.constraintLayoutPaintTool.setVisibility(View.GONE);
                        this.polishEditor.setBrushDrawingMode(false);
                        setGuideLine();
                        this.polishEditor.clearBrushAllViews();
                        setVisibleSave();
                        this.moduleToolsId = Module.NONE;
                        reloadingLayout();
                        return;
                    case MAGIC:
                        setVisibleSave();
                        viewSlideUp(recyclerViewTools);
                        viewSlideDown(constraintLayoutMagic);
                        this.imageViewUndoMagic.setVisibility(View.GONE);
                        this.imageViewRedoMagic.setVisibility(View.GONE);
                        this.imageViewCleanMagic.setVisibility(View.GONE);
                        this.constraintLayoutMagicTool.setVisibility(View.GONE);
                        this.constraintLayoutSave.setVisibility(View.VISIBLE);
                        this.polishEditor.setBrushDrawingMode(false);
                        setGuideLine();
                        this.polishEditor.clearBrushAllViews();
                        setVisibleSave();
                        this.moduleToolsId = Module.NONE;
                        reloadingLayout();
                        return;
                    case NEON:
                        setVisibleSave();
                        viewSlideUp(recyclerViewTools);
                        viewSlideDown(constraintLayoutNeon);
                        this.imageViewUndoNeon.setVisibility(View.GONE);
                        this.imageViewRedoNeon.setVisibility(View.GONE);
                        this.constraintLayoutNeonTool.setVisibility(View.GONE);
                        this.constraintLayoutSave.setVisibility(View.VISIBLE);
                        this.polishEditor.setBrushDrawingMode(false);
                        setGuideLine();
                        this.polishEditor.clearBrushAllViews();
                        setVisibleSave();
                        this.moduleToolsId = Module.NONE;
                        reloadingLayout();
                        return;
                    case TEXT:
                        if (!this.polishView.getStickers().isEmpty()) {
                            this.polishView.getStickers().clear();
                            this.polishView.setHandlingSticker(null);
                        }
                        viewSlideUp(recyclerViewTools);
                        viewSlideDown(constraintLayoutAddText);
                        this.constraintLayoutSaveText.setVisibility(View.GONE);
                        this.polishView.setHandlingSticker(null);
                        this.polishView.setLocked(true);
                        setVisibleSave();
                        this.moduleToolsId = Module.NONE;
                        return;
                    case ADJUST:
                        this.polishEditor.setFilterEffect("");
                        viewSlideUp(recyclerViewTools);
                        viewSlideDown(constraintLayoutAdjust);
                        setGuideLine();
                        this.constraintLayoutSave.setVisibility(View.VISIBLE);
                        setVisibleSave();
                        this.moduleToolsId = Module.NONE;
                        return;
                    case FILTER:
                        viewSlideUp(recyclerViewTools);
                        viewSlideDown(constraintLayoutFilter);
                        this.constraintLayoutSave.setVisibility(View.VISIBLE);
                        setGuideLine();
                        setVisibleSave();
                        this.polishEditor.setFilterEffect("");
                        this.imageViewCompareFilter.setVisibility(View.GONE);
                        this.listFilter.clear();
                        if (this.recyclerViewFilter.getAdapter() != null) {
                            this.recyclerViewFilter.getAdapter().notifyDataSetChanged();
                        }
                        this.moduleToolsId = Module.NONE;
                        return;
                    case STICKER:
                        if (this.polishView.getStickers().size() <= 0) {
                            this.linear_layout_wrapper_sticker_list.setVisibility(View.VISIBLE);
                            viewSlideUp(recyclerViewTools);
                            viewSlideDown(constraintLayoutSticker);
                            this.imageViewAddSticker.setVisibility(View.GONE);
                            this.polishView.setHandlingSticker(null);
                            this.polishView.setLocked(true);
                            this.moduleToolsId = Module.NONE;
                        } else if (this.imageViewAddSticker.getVisibility() == View.VISIBLE) {
                            this.polishView.getStickers().clear();
                            this.imageViewAddSticker.setVisibility(View.GONE);
                            viewSlideUp(recyclerViewTools);
                            viewSlideDown(constraintLayoutSticker);
                            this.polishView.setHandlingSticker(null);
                            this.linear_layout_wrapper_sticker_list.setVisibility(View.VISIBLE);
                            this.moduleToolsId = Module.NONE;
                        } else {
                            this.linear_layout_wrapper_sticker_list.setVisibility(View.GONE);
                            this.imageViewAddSticker.setVisibility(View.VISIBLE);
                        }
                        this.linear_layout_wrapper_sticker_list.setVisibility(View.VISIBLE);
                        this.moduleToolsId = Module.NONE;
                        setVisibleSave();
                        this.constraintLayoutSaveSticker.setVisibility(View.GONE);
                        setGuideLine();
                        return;
                    case OVERLAY:
                        this.polishEditor.setFilterEffect("");
                        viewSlideUp(recyclerViewTools);
                        viewSlideDown(constraintLayoutOverlay);
                        this.imageViewCompareOverlay.setVisibility(View.GONE);
                        PolishEditorActivity.this.constraintLayoutSaveOverlay.setVisibility(View.GONE);
                        PolishEditorActivity.this.constraintLayoutConfirmCompareOverlay.setVisibility(View.GONE);
                        this.listOverlayEffect.clear();
                        if (this.recycler_view_overlay_effect.getAdapter() != null) {
                            this.recycler_view_overlay_effect.getAdapter().notifyDataSetChanged();
                        }
                        setGuideLine();
                        setVisibleSave();
                        this.moduleToolsId = Module.NONE;
                        return;
                    case SPLASH_DRAW:
                    case SKETCH_DRAW:
                    case BLUR:
                    case SPLASH_BG:
                    case SKETCH_SQ:
                    case SKETCH_BG:
                    case SPLASH_SQ:
                    case BLUR_BG:
                    case MOSAIC:
                    case COLORED:
                    case CROP:
                    case MIRROR:
                    case WOMEN:
                    case NONE:
                    case HSL:
                    case ART:
                    case PIX:
                    case DRIP:
                    case BLURE:
                    case SPLASH:
                    case WINGS:
                    case MOTION:
                        setOnBackPressDialog();
                        return;
                    default:
                        super.onBackPressed();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onAdjustSelected(AdjustAdapter.AdjustModel adjustModel) {
        this.adjustSeekBar.setCurrentDegrees(((int) ((adjustModel.originValue - adjustModel.minValue) / ((adjustModel.maxValue - ((adjustModel.maxValue + adjustModel.minValue) / 2.0f)) / 50.0f))) - 50);
    }

    public void addSticker(int item, Bitmap bitmap) {
        this.polishView.addSticker(new DrawableSticker(new BitmapDrawable(getResources(), bitmap)));
        this.linear_layout_wrapper_sticker_list.setVisibility(View.GONE);
        this.imageViewAddSticker.setVisibility(View.VISIBLE);
    }

    public void finishCrop(Bitmap bitmap) {
        this.polishView.setImageSource(bitmap);
        this.moduleToolsId = Module.NONE;
        reloadingLayout();
    }

    public void onColorChanged(String string) {
        this.polishEditor.setBrushColor(Color.parseColor(string));
    }

    public void ratioSavedBitmap(Bitmap bitmap) {
        this.polishView.setImageSource(bitmap);
        this.moduleToolsId = Module.NONE;
        reloadingLayout();
    }

    @Override
    public void onSaveFilter(Bitmap bitmap) {
        this.polishView.setImageSource(bitmap);
        this.moduleToolsId = Module.NONE;
    }

    public void onSaveSplashBackground(Bitmap bitmap) {
        this.polishView.setImageSource(bitmap);
        this.moduleToolsId = Module.NONE;
    }

    public void onSaveBlurBackground(Bitmap bitmap) {
        this.polishView.setImageSource(bitmap);
        this.moduleToolsId = Module.NONE;
    }

    public void onSaveSketchBackground(Bitmap bitmap) {
        this.polishView.setImageSource(bitmap);
        this.moduleToolsId = Module.NONE;
    }

    public void onSaveSketch(Bitmap bitmap) {
        this.polishView.setImageSource(bitmap);
        this.moduleToolsId = Module.NONE;
    }

    public void onSaveMosaic(Bitmap bitmap) {
        this.polishView.setImageSource(bitmap);
        this.moduleToolsId = Module.NONE;
    }

    @Override
    public void onMagicChanged(DrawModel drawBitmapModel) {
        this.polishEditor.setBrushMagic(drawBitmapModel);
    }

    @Override
    public void onSaveSplash(Bitmap bitmap) {
        this.polishView.setImageSource(bitmap);
        this.moduleToolsId = Module.NONE;
    }

    public void onFilterSelected(int itemCurrent, String string) {
        this.polishEditor.setFilterEffect(string);
        this.adjustFilter.setCurrentDegrees(50);
        if (this.moduleToolsId == Module.FILTER) {
            this.polishView.getGLSurfaceView().setFilterIntensity(0.5f);
        }
    }


    public void onOverlaySelected(int itemCurrent, String string) {
        this.polishEditor.setFilterEffect(string);
        this.seekBarOverlay.setProgress(50);
        if (this.moduleToolsId == Module.OVERLAY) {
            this.polishView.getGLSurfaceView().setFilterIntensity(0.5f);
        }
    }

    /**
     * Called when pointer capture is enabled or disabled for the current window.
     *
     * @param hasCapture True if the window has pointer capture.
     */
    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }


    class openFilters extends AsyncTask<Void, Void, Void> {
        openFilters() {
        }

        public void onPreExecute() {
            PolishEditorActivity.this.showLoading(true);
        }

        public Void doInBackground(Void... voids) {
            PolishEditorActivity.this.listFilter.clear();
            PolishEditorActivity.this.listFilter.addAll(FilterFileAsset.getListBitmapFilter(ThumbnailUtils.extractThumbnail(PolishEditorActivity.this.polishView.getCurrentBitmap(), 100, 100)));
            Log.d("XXXXXXXX", "allFilters " + PolishEditorActivity.this.listFilter.size());
            return null;
        }

        public void onPostExecute(Void voids) {
            PolishEditorActivity.this.recyclerViewFilter.setAdapter(new FilterAdapter(PolishEditorActivity.this.listFilter, PolishEditorActivity.this, PolishEditorActivity.this.getApplicationContext(), Arrays.asList(FilterFileAsset.FILTERS)));
            PolishEditorActivity.this.constraintLayoutSave.setVisibility(View.GONE);
            PolishEditorActivity.this.adjustFilter.setCurrentDegrees(50);
            PolishEditorActivity.this.showLoading(false);
            viewSlideDown(recyclerViewTools);
            viewSlideUp(constraintLayoutFilter);
            viewSlideUp(imageViewCompareFilter);
            setGuideLinePaint();
        }
    }

    class openBlurFragment extends AsyncTask<Void, Bitmap, Bitmap> {
        openBlurFragment() {
        }

        public void onPreExecute() {
            PolishEditorActivity.this.showLoading(true);
        }

        public Bitmap doInBackground(Void... voids) {
            return FilterUtils.getBlurImageFromBitmap(PolishEditorActivity.this.polishView.getCurrentBitmap(), 5.0f);
        }

        public void onPostExecute(Bitmap bitmap) {
            PolishEditorActivity.this.showLoading(false);
            RatioFragment.show(PolishEditorActivity.this, PolishEditorActivity.this, PolishEditorActivity.this.polishView.getCurrentBitmap(), bitmap);
        }
    }

    class openMirrorFragment extends AsyncTask<Void, Bitmap, Bitmap> {
        openMirrorFragment() {
        }

        public void onPreExecute() {
            PolishEditorActivity.this.showLoading(true);
        }

        public Bitmap doInBackground(Void... voids) {
            return FilterUtils.getBlurImageFromBitmap(PolishEditorActivity.this.polishView.getCurrentBitmap(), 5.0f);
        }

        public void onPostExecute(Bitmap bitmap) {
            PolishEditorActivity.this.showLoading(false);
            PolishEditorActivity queShotEditorActivity = PolishEditorActivity.this;
            MirrorFragment.show(queShotEditorActivity, queShotEditorActivity, queShotEditorActivity.polishView.getCurrentBitmap(), bitmap);
        }
    }

    class dripEffect extends AsyncTask<Void, Void, Void> {
        dripEffect() {
        }

        public void onPreExecute() {
            PolishEditorActivity.this.showLoading(true);
        }

        public Void doInBackground(Void... voids) {
            return null;
        }

        public void onPostExecute(Void voids) {
            StoreManager.setCurrentCropedBitmap(PolishEditorActivity.this, (Bitmap) null);
            StoreManager.setCurrentCroppedMaskBitmap(PolishEditorActivity.this, (Bitmap) null);
            DripLayout.setFaceBitmap(PolishEditorActivity.this.polishView.getCurrentBitmap());
            StoreManager.setCurrentOriginalBitmap(PolishEditorActivity.this, PolishEditorActivity.this.polishView.getCurrentBitmap());
            Intent dripIntent = new Intent(PolishEditorActivity.this, DripLayout.class);
            startActivityForResult(dripIntent, 900);
            overridePendingTransition(R.anim.enter, R.anim.exit);
            PolishEditorActivity.this.showLoading(false);
        }
    }

    class motionEffect extends AsyncTask<Void, Void, Void> {
        motionEffect() {
        }

        public void onPreExecute() {
            PolishEditorActivity.this.showLoading(true);
        }

        public Void doInBackground(Void... voids) {
            return null;
        }

        public void onPostExecute(Void voids) {
            StoreManager.setCurrentCropedBitmap(PolishEditorActivity.this, (Bitmap) null);
            StoreManager.setCurrentCroppedMaskBitmap(PolishEditorActivity.this, (Bitmap) null);
            MotionLayout.setFaceBitmap(PolishEditorActivity.this.polishView.getCurrentBitmap());
            StoreManager.setCurrentOriginalBitmap(PolishEditorActivity.this, PolishEditorActivity.this.polishView.getCurrentBitmap());
            Intent motionIntent = new Intent(PolishEditorActivity.this, MotionLayout.class);
            startActivityForResult(motionIntent, 900);
            overridePendingTransition(R.anim.enter, R.anim.exit);
            PolishEditorActivity.this.showLoading(false);
        }
    }

    class wingEffect extends AsyncTask<Void, Void, Void> {
        wingEffect() {
        }

        public void onPreExecute() {
            PolishEditorActivity.this.showLoading(true);
        }

        public Void doInBackground(Void... voids) {
            return null;
        }

        public void onPostExecute(Void voids) {
            StoreManager.setCurrentCropedBitmap(PolishEditorActivity.this, (Bitmap) null);
            StoreManager.setCurrentCroppedMaskBitmap(PolishEditorActivity.this, (Bitmap) null);
            WingLayout.setFaceBitmap(PolishEditorActivity.this.polishView.getCurrentBitmap());
            StoreManager.setCurrentOriginalBitmap(PolishEditorActivity.this, PolishEditorActivity.this.polishView.getCurrentBitmap());
            Intent neonIntent2 = new Intent(PolishEditorActivity.this, WingLayout.class);
            startActivityForResult(neonIntent2, 900);
            overridePendingTransition(R.anim.enter, R.anim.exit);
            PolishEditorActivity.this.showLoading(false);
        }
    }

    class neonEffect extends AsyncTask<Void, Void, Void> {
        neonEffect() {
        }

        public void onPreExecute() {
            PolishEditorActivity.this.showLoading(true);
        }

        public Void doInBackground(Void... voids) {
            return null;
        }

        public void onPostExecute(Void voids) {
            StoreManager.setCurrentCropedBitmap(PolishEditorActivity.this, (Bitmap) null);
            StoreManager.setCurrentCroppedMaskBitmap(PolishEditorActivity.this, (Bitmap) null);
            NeonLayout.setFaceBitmap(PolishEditorActivity.this.polishView.getCurrentBitmap());
            StoreManager.setCurrentOriginalBitmap(PolishEditorActivity.this, PolishEditorActivity.this.polishView.getCurrentBitmap());
            Intent neonIntent = new Intent(PolishEditorActivity.this, NeonLayout.class);
            startActivityForResult(neonIntent, 900);
            overridePendingTransition(R.anim.enter, R.anim.exit);
            PolishEditorActivity.this.showLoading(false);
        }
    }

    class artEffect extends AsyncTask<Void, Void, Void> {
        artEffect() {
        }

        public void onPreExecute() {
            PolishEditorActivity.this.showLoading(true);
        }

        public Void doInBackground(Void... voids) {
            return null;
        }

        public void onPostExecute(Void voids) {
            StoreManager.setCurrentCropedBitmap(PolishEditorActivity.this, (Bitmap) null);
            StoreManager.setCurrentCroppedMaskBitmap(PolishEditorActivity.this, (Bitmap) null);
            ArtLayout.setFaceBitmap(PolishEditorActivity.this.polishView.getCurrentBitmap());
            StoreManager.setCurrentOriginalBitmap(PolishEditorActivity.this, PolishEditorActivity.this.polishView.getCurrentBitmap());
            Intent art = new Intent(PolishEditorActivity.this, ArtLayout.class);
            startActivityForResult(art, 900);
            overridePendingTransition(R.anim.enter, R.anim.exit);
            PolishEditorActivity.this.showLoading(false);
        }
    }

    class pixEffect extends AsyncTask<Void, Void, Void> {
        pixEffect() {
        }

        public void onPreExecute() {
            PolishEditorActivity.this.showLoading(true);
        }

        public Void doInBackground(Void... voids) {
            return null;
        }

        public void onPostExecute(Void voids) {
            StoreManager.setCurrentCropedBitmap(PolishEditorActivity.this, (Bitmap) null);
            StoreManager.setCurrentCroppedMaskBitmap(PolishEditorActivity.this, (Bitmap) null);
            PixLabLayout.setFaceBitmap(PolishEditorActivity.this.polishView.getCurrentBitmap());
            StoreManager.setCurrentOriginalBitmap(PolishEditorActivity.this, PolishEditorActivity.this.polishView.getCurrentBitmap());
            Intent dripIntent = new Intent(PolishEditorActivity.this, PixLabLayout.class);
            startActivityForResult(dripIntent, 900);
            overridePendingTransition(R.anim.enter, R.anim.exit);
            PolishEditorActivity.this.showLoading(false);
        }
    }

    class openFrameFragment extends AsyncTask<Void, Bitmap, Bitmap> {
        openFrameFragment() {
        }

        public void onPreExecute() {
            PolishEditorActivity.this.showLoading(true);
        }

        public Bitmap doInBackground(Void... voids) {
            return FilterUtils.getBlurImageFromBitmap(PolishEditorActivity.this.polishView.getCurrentBitmap(), 5.0f);
        }

        public void onPostExecute(Bitmap bitmap) {
            PolishEditorActivity.this.showLoading(false);
            FrameFragment.show(PolishEditorActivity.this, PolishEditorActivity.this, PolishEditorActivity.this.polishView.getCurrentBitmap(), bitmap);
        }
    }

    class openShapeFragment extends AsyncTask<Void, List<Bitmap>, List<Bitmap>> {
        openShapeFragment() {
        }

        public void onPreExecute() {
            PolishEditorActivity.this.showLoading(true);
        }

        public List<Bitmap> doInBackground(Void... voids) {
            List<Bitmap> arrayList = new ArrayList<>();
            arrayList.add(FilterUtils.cloneBitmap(PolishEditorActivity.this.polishView.getCurrentBitmap()));
            arrayList.add(FilterUtils.getBlurImageFromBitmap(PolishEditorActivity.this.polishView.getCurrentBitmap(), 8.0f));
            return arrayList;
        }

        public void onPostExecute(List<Bitmap> list) {
            PolishEditorActivity.this.showLoading(false);
            MosaicFragment.show(PolishEditorActivity.this, list.get(0), list.get(1), PolishEditorActivity.this);
        }
    }

    class openColoredFragment extends AsyncTask<Void, List<Bitmap>, List<Bitmap>> {
        openColoredFragment() {
        }

        public void onPreExecute() {
            PolishEditorActivity.this.showLoading(true);
        }

        public List<Bitmap> doInBackground(Void... voids) {
            List<Bitmap> arrayList = new ArrayList<>();
            arrayList.add(FilterUtils.cloneBitmap(PolishEditorActivity.this.polishView.getCurrentBitmap()));
            arrayList.add(FilterUtils.getBlurImageFromBitmap(PolishEditorActivity.this.polishView.getCurrentBitmap(), 8.0f));
            return arrayList;
        }

        public void onPostExecute(List<Bitmap> list) {
            PolishEditorActivity.this.showLoading(false);
            ColoredFragment.show(PolishEditorActivity.this, list.get(0), list.get(1), PolishEditorActivity.this);
        }
    }

    class effectDodge extends AsyncTask<Void, Void, Void> {
        effectDodge() {
        }

        public void onPreExecute() {
            PolishEditorActivity.this.showLoading(true);
        }

        public Void doInBackground(Void... voids) {
            PolishEditorActivity.this.listDodgeEffect.clear();
            PolishEditorActivity.this.listDodgeEffect.addAll(OverlayFileAsset.getListBitmapDodgeEffect(ThumbnailUtils.extractThumbnail(PolishEditorActivity.this.polishView.getCurrentBitmap(), 100, 100)));
            return null;
        }

        public void onPostExecute(Void voids) {
            PolishEditorActivity.this.recycler_view_dodge_effect.setAdapter(new OverlayAdapter(PolishEditorActivity.this.listDodgeEffect, PolishEditorActivity.this, PolishEditorActivity.this.getApplicationContext(), Arrays.asList(OverlayFileAsset.DODGE_EFFECTS)));
            PolishEditorActivity.this.seekBarOverlay.setProgress(100);
        }
    }

    class effectColor extends AsyncTask<Void, Void, Void> {
        effectColor() {
        }

        public void onPreExecute() {
            PolishEditorActivity.this.showLoading(true);
        }

        public Void doInBackground(Void... voids) {
            PolishEditorActivity.this.listColorEffect.clear();
            PolishEditorActivity.this.listColorEffect.addAll(OverlayFileAsset.getListBitmapColorEffect(ThumbnailUtils.extractThumbnail(PolishEditorActivity.this.polishView.getCurrentBitmap(), 100, 100)));
            return null;
        }

        public void onPostExecute(Void voids) {
            PolishEditorActivity.this.recycler_view_color_effect.setAdapter(new OverlayAdapter(PolishEditorActivity.this.listColorEffect, PolishEditorActivity.this, PolishEditorActivity.this.getApplicationContext(), Arrays.asList(OverlayFileAsset.COLOR_EFFECTS)));
            PolishEditorActivity.this.seekBarOverlay.setProgress(100);
        }
    }

    class effectDivide extends AsyncTask<Void, Void, Void> {
        effectDivide() {
        }

        public void onPreExecute() {
            PolishEditorActivity.this.showLoading(true);
        }

        public Void doInBackground(Void... voids) {
            PolishEditorActivity.this.listHueEffect.clear();
            PolishEditorActivity.this.listHueEffect.addAll(OverlayFileAsset.getListBitmapHueEffect(ThumbnailUtils.extractThumbnail(PolishEditorActivity.this.polishView.getCurrentBitmap(), 100, 100)));
            return null;
        }

        public void onPostExecute(Void voids) {
            PolishEditorActivity.this.recycler_view_hue_effect.setAdapter(new OverlayAdapter(PolishEditorActivity.this.listHueEffect, PolishEditorActivity.this, PolishEditorActivity.this.getApplicationContext(), Arrays.asList(OverlayFileAsset.HUE_EFFECTS)));
            PolishEditorActivity.this.seekBarOverlay.setProgress(100);
        }
    }

    class effectHardmix extends AsyncTask<Void, Void, Void> {
        effectHardmix() {
        }

        public void onPreExecute() {
            PolishEditorActivity.this.showLoading(true);
        }

        public Void doInBackground(Void... voids) {
            PolishEditorActivity.this.listHardMixEffect.clear();
            PolishEditorActivity.this.listHardMixEffect.addAll(OverlayFileAsset.getListBitmapHardmixEffect(ThumbnailUtils.extractThumbnail(PolishEditorActivity.this.polishView.getCurrentBitmap(), 100, 100)));
            return null;
        }

        public void onPostExecute(Void voids) {
            PolishEditorActivity.this.recycler_view_hardmix_effet.setAdapter(new OverlayAdapter(PolishEditorActivity.this.listHardMixEffect, PolishEditorActivity.this, PolishEditorActivity.this.getApplicationContext(), Arrays.asList(OverlayFileAsset.HARDMIX_EFFECTS)));
            PolishEditorActivity.this.seekBarOverlay.setProgress(100);
        }
    }

    class effectOvarlay extends AsyncTask<Void, Void, Void> {
        effectOvarlay() {
        }

        public void onPreExecute() {
            PolishEditorActivity.this.showLoading(true);
        }

        public Void doInBackground(Void... voids) {
            PolishEditorActivity.this.listOverlayEffect.clear();
            PolishEditorActivity.this.listOverlayEffect.addAll(OverlayFileAsset.getListBitmapOverlayEffect(ThumbnailUtils.extractThumbnail(PolishEditorActivity.this.polishView.getCurrentBitmap(), 100, 100)));
            return null;
        }

        public void onPostExecute(Void voids) {
            PolishEditorActivity.this.recycler_view_overlay_effect.setAdapter(new OverlayAdapter(PolishEditorActivity.this.listOverlayEffect, PolishEditorActivity.this, PolishEditorActivity.this.getApplicationContext(), Arrays.asList(OverlayFileAsset.OVERLAY_EFFECTS)));
            PolishEditorActivity.this.imageViewCompareOverlay.setVisibility(View.VISIBLE);
            PolishEditorActivity.this.constraintLayoutSave.setVisibility(View.GONE);
            PolishEditorActivity.this.seekBarOverlay.setProgress(100);
            PolishEditorActivity.this.showLoading(false);
            viewSlideDown(recyclerViewTools);
            viewSlideDown(constraintLayoutEffects);
            viewSlideUp(constraintLayoutConfirmCompareOverlay);
            viewSlideUp(constraintLayoutOverlay);
            setGuideLinePaint();
        }
    }

    class effectBurn extends AsyncTask<Void, Void, Void> {
        effectBurn() {
        }

        public void onPreExecute() {
            PolishEditorActivity.this.showLoading(true);
        }

        public Void doInBackground(Void... voids) {
            PolishEditorActivity.this.listBurnEffect.clear();
            PolishEditorActivity.this.listBurnEffect.addAll(OverlayFileAsset.getListBitmapBurnEffect(ThumbnailUtils.extractThumbnail(PolishEditorActivity.this.polishView.getCurrentBitmap(), 100, 100)));
            return null;
        }

        public void onPostExecute(Void voids) {
            PolishEditorActivity.this.recycler_view_burn_effect.setAdapter(new OverlayAdapter(PolishEditorActivity.this.listBurnEffect, PolishEditorActivity.this, PolishEditorActivity.this.getApplicationContext(), Arrays.asList(OverlayFileAsset.BURN_EFFECTS)));
            PolishEditorActivity.this.seekBarOverlay.setProgress(100);
        }
    }

    class openSplashSquareBackgroundFragment extends AsyncTask<Void, List<Bitmap>, List<Bitmap>> {
        boolean isSplashSquared;

        public openSplashSquareBackgroundFragment(boolean z) {
            this.isSplashSquared = z;
        }

        public void onPreExecute() {
            PolishEditorActivity.this.showLoading(true);
        }

        public List<Bitmap> doInBackground(Void... voids) {
            Bitmap currentBitmap = PolishEditorActivity.this.polishView.getCurrentBitmap();
            List<Bitmap> arrayList = new ArrayList<>();
            arrayList.add(currentBitmap);
            if (this.isSplashSquared) {
                arrayList.add(FilterUtils.getBlackAndWhiteImageFromBitmap(currentBitmap));
            }
            return arrayList;
        }

        public void onPostExecute(List<Bitmap> list) {
            if (this.isSplashSquared) {
                PolishEditorActivity.this.constraintLayoutSplash.setVisibility(View.GONE);
                SaturationSquareBackgroundFragment.show(PolishEditorActivity.this, list.get(0), null, list.get(1), PolishEditorActivity.this, true);
            }
            PolishEditorActivity.this.showLoading(false);
        }
    }

    class openBlurSquareBackgroundFragment extends AsyncTask<Void, List<Bitmap>, List<Bitmap>> {
        boolean isSplashSquared;

        public openBlurSquareBackgroundFragment(boolean z) {
            this.isSplashSquared = z;
        }

        public void onPreExecute() {
            PolishEditorActivity.this.showLoading(true);
        }

        public List<Bitmap> doInBackground(Void... voids) {
            Bitmap currentBitmap = PolishEditorActivity.this.polishView.getCurrentBitmap();
            List<Bitmap> arrayList = new ArrayList<>();
            arrayList.add(currentBitmap);
            if (this.isSplashSquared) {
                arrayList.add(FilterUtils.getBlurImageFromBitmap(currentBitmap, 2.5f));
            }
            return arrayList;
        }

        public void onPostExecute(List<Bitmap> list) {
            if (this.isSplashSquared) {
                PolishEditorActivity.this.constraintLayoutSplash.setVisibility(View.GONE);
                BlurSquareBgFragment.show(PolishEditorActivity.this, list.get(0), null, list.get(1), PolishEditorActivity.this, true);
            }
            PolishEditorActivity.this.showLoading(false);
        }
    }

    class openSplashFragment extends AsyncTask<Void, List<Bitmap>, List<Bitmap>> {
        boolean isSplashSquared;

        public openSplashFragment(boolean z) {
            this.isSplashSquared = z;
        }

        public void onPreExecute() {
            PolishEditorActivity.this.showLoading(true);
        }

        public List<Bitmap> doInBackground(Void... voids) {
            Bitmap currentBitmap = PolishEditorActivity.this.polishView.getCurrentBitmap();
            List<Bitmap> arrayList = new ArrayList<>();
            arrayList.add(currentBitmap);
            if (this.isSplashSquared) {
                arrayList.add(FilterUtils.getBlackAndWhiteImageFromBitmap(currentBitmap));
            }
            return arrayList;
        }

        public void onPostExecute(List<Bitmap> list) {
            if (this.isSplashSquared) {
                PolishEditorActivity.this.constraintLayoutSplash.setVisibility(View.GONE);
                SaturationSquareFragment.show(PolishEditorActivity.this, list.get(0), null, list.get(1), PolishEditorActivity.this, true);
            }
            PolishEditorActivity.this.showLoading(false);
        }
    }

    class openSketchFragment extends AsyncTask<Void, List<Bitmap>, List<Bitmap>> {
        boolean isSplashSquared;

        public openSketchFragment(boolean z) {
            this.isSplashSquared = z;
        }

        public void onPreExecute() {
            PolishEditorActivity.this.showLoading(true);
        }

        public List<Bitmap> doInBackground(Void... voids) {
            Bitmap currentBitmap = PolishEditorActivity.this.polishView.getCurrentBitmap();
            List<Bitmap> arrayList = new ArrayList<>();
            arrayList.add(currentBitmap);
            if (this.isSplashSquared) {
                arrayList.add(FilterUtils.getSketchImageFromBitmap(currentBitmap, 0.8f));
            }
            return arrayList;
        }

        public void onPostExecute(List<Bitmap> list) {
            if (this.isSplashSquared) {
                PolishEditorActivity.this.constraintLayoutSplash.setVisibility(View.GONE);
                SketchSquareFragment.show(PolishEditorActivity.this, list.get(0), null, list.get(1), PolishEditorActivity.this, true);
            }
            PolishEditorActivity.this.showLoading(false);
        }
    }

    class openSketchBackgroundFragment extends AsyncTask<Void, List<Bitmap>, List<Bitmap>> {
        boolean isSketchBackgroundSquared;

        public openSketchBackgroundFragment(boolean z) {
            this.isSketchBackgroundSquared = z;
        }

        public void onPreExecute() {
            PolishEditorActivity.this.showLoading(true);
        }

        public List<Bitmap> doInBackground(Void... voids) {
            Bitmap currentBitmap = PolishEditorActivity.this.polishView.getCurrentBitmap();
            List<Bitmap> arrayList = new ArrayList<>();
            arrayList.add(currentBitmap);
            if (this.isSketchBackgroundSquared) {
                arrayList.add(FilterUtils.getSketchImageFromBitmap(currentBitmap, 0.8f));
            }
            return arrayList;
        }

        public void onPostExecute(List<Bitmap> list) {
            if (this.isSketchBackgroundSquared) {
                PolishEditorActivity.this.constraintLayoutSplash.setVisibility(View.GONE);
                SketchSquareBackgroundFragment.show(PolishEditorActivity.this, list.get(0), null, list.get(1), PolishEditorActivity.this, true);
            }
            PolishEditorActivity.this.showLoading(false);
        }
    }

    class SaveFilter extends AsyncTask<Void, Void, Bitmap> {
        SaveFilter() {
        }

        public void onPreExecute() {
            PolishEditorActivity.this.showLoading(true);
        }

        public Bitmap doInBackground(Void... voids) {
            final Bitmap[] bitmaps = {null};
            PolishEditorActivity.this.polishView.saveGLSurfaceViewAsBitmap(bitmap -> bitmaps[0] = bitmap);
            while (bitmaps[0] == null) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return bitmaps[0];
        }

        public void onPostExecute(Bitmap bitmap) {
            PolishEditorActivity.this.polishView.setImageSource(bitmap);
            PolishEditorActivity.this.polishView.setFilterEffect("");
            PolishEditorActivity.this.showLoading(false);
        }
    }

    class SaveSticker extends AsyncTask<Void, Void, Bitmap> {
        SaveSticker() {
        }

        public void onPreExecute() {
            PolishEditorActivity.this.polishView.getGLSurfaceView().setAlpha(0.0f);
            PolishEditorActivity.this.showLoading(true);
        }

        public Bitmap doInBackground(Void... voids) {
            final Bitmap[] bitmaps = {null};
            while (bitmaps[0] == null) {
                try {
                    PolishEditorActivity.this.polishEditor.saveStickerAsBitmap(bitmap -> bitmaps[0] = bitmap);
                    while (bitmaps[0] == null) {
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (Exception e) {
                }
            }
            return bitmaps[0];
        }

        public void onPostExecute(Bitmap bitmap) {
            PolishEditorActivity.this.polishView.setImageSource(bitmap);
            PolishEditorActivity.this.polishView.getStickers().clear();
            PolishEditorActivity.this.polishView.getGLSurfaceView().setAlpha(1.0f);
            PolishEditorActivity.this.showLoading(false);
            PolishEditorActivity.this.reloadingLayout();
        }
    }

    public void onActivityResult(int i, int i2, @Nullable Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 123) {
            if (i2 == -1) {
                try {
                    InputStream inputStream = getContentResolver().openInputStream(intent.getData());
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    float width = (float) bitmap.getWidth();
                    float height = (float) bitmap.getHeight();
                    float max = Math.max(width / 1280.0f, height / 1280.0f);
                    if (max > 1.0f) {
                        bitmap = Bitmap.createScaledBitmap(bitmap, (int) (width / max), (int) (height / max), false);
                    }
                    if (SystemUtil.rotateBitmap(bitmap, new ExifInterface(inputStream).getAttributeInt(ExifInterface.TAG_ORIENTATION, 1)) != bitmap) {
                        bitmap.recycle();
                        bitmap = null;
                    }
                    this.polishView.setImageSource(bitmap);
                    reloadingLayout();
                } catch (Exception e) {
                    e.printStackTrace();
                    MsgUtil.toastMsg(this, "Error: Can not open image");
                }
            } else {
                finish();
            }
        } else if (i == 900) {
            if (intent != null && intent.getStringExtra("MESSAGE").equals("done")) {
                if (BitmapTransfer.bitmap != null) {

                    new loadBitmap().execute(BitmapTransfer.bitmap);
                }
            }

        }
    }

    class loadBitmap extends AsyncTask<Bitmap, Bitmap, Bitmap> {
        loadBitmap() {
        }

        public void onPreExecute() {
            PolishEditorActivity.this.showLoading(true);
        }

        public Bitmap doInBackground(Bitmap... bitmaps) {
            try {
                Bitmap bitmap = bitmaps[0];//MediaStore.Images.Media.getBitmap(QueShotEditorActivity.this.getContentResolver(), fromFile);
                float width = (float) bitmap.getWidth();
                float height = (float) bitmap.getHeight();
                float max = Math.max(width / 1280.0f, height / 1280.0f);
                if (max > 1.0f) {
                    bitmap = Bitmap.createScaledBitmap(bitmap, (int) (width / max), (int) (height / max), false);
                }
//                Bitmap bitmap1 = SystemUtil.rotateBitmap(bitmap, new ExifInterface(QueShotEditorActivity.this.getContentResolver().openInputStream(fromFile)).getAttributeInt(ExifInterface.TAG_ORIENTATION, 1));
//                if (bitmap1 != bitmap) {
//                    bitmap.recycle();
//                }
                return bitmap;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }


        public void onPostExecute(Bitmap bitmap) {
            PolishEditorActivity.this.polishView.setImageSource(bitmap);
            PolishEditorActivity.this.reloadingLayout();
        }
    }

    class loadBitmapUri extends AsyncTask<String, Bitmap, Bitmap> {
        loadBitmapUri() {
        }

        public void onPreExecute() {
            PolishEditorActivity.this.showLoading(true);
        }

        public Bitmap doInBackground(String... string) {
            try {
                File file = new File(string[0]);
                Uri fromFile = Uri.fromFile(file);
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(PolishEditorActivity.this.getContentResolver(), fromFile);
                float width = (float) bitmap.getWidth();
                float height = (float) bitmap.getHeight();
                float max = Math.max(width / 1280.0f, height / 1280.0f);
                if (max > 1.0f) {
                    bitmap = Bitmap.createScaledBitmap(bitmap, (int) (width / max), (int) (height / max), false);
                }
                Bitmap bitmap1 = SystemUtil.rotateBitmap(bitmap, new ExifInterface(PolishEditorActivity.this.getContentResolver().openInputStream(fromFile)).getAttributeInt(ExifInterface.TAG_ORIENTATION, 1));
                if (bitmap1 != bitmap) {
                    bitmap.recycle();
                }

                return bitmap1;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }


        public void onPostExecute(Bitmap bitmap) {
            PolishEditorActivity.this.polishView.setImageSource(bitmap);
            PolishEditorActivity.this.reloadingLayout();
        }
    }


    public void reloadingLayout() {
        this.polishView.postDelayed(() -> {
            try {
                Display display = PolishEditorActivity.this.getWindowManager().getDefaultDisplay();
                Point point = new Point();
                display.getSize(point);
                int i = point.x;
                int height = PolishEditorActivity.this.relativeLayoutWrapper.getHeight();
                int i2 = PolishEditorActivity.this.polishView.getGLSurfaceView().getRenderViewport().width;
                float f = (float) PolishEditorActivity.this.polishView.getGLSurfaceView().getRenderViewport().height;
                float f2 = (float) i2;
                if (((int) ((((float) i) * f) / f2)) <= height) {
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(-1, -2);
                    params.addRule(13);
                    PolishEditorActivity.this.polishView.setLayoutParams(params);
                    PolishEditorActivity.this.polishView.setVisibility(View.VISIBLE);
                } else {
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams((int) ((((float) height) * f2) / f), -1);
                    params.addRule(13);
                    PolishEditorActivity.this.polishView.setLayoutParams(params);
                    PolishEditorActivity.this.polishView.setVisibility(View.VISIBLE);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            PolishEditorActivity.this.showLoading(false);
        }, 300);
    }

    class SaveEditingBitmap extends AsyncTask<Void, String, String> {
        SaveEditingBitmap() {
        }

        public void onPreExecute() {
            PolishEditorActivity.this.showLoading(true);
        }

        public String doInBackground(Void... voids) {
            try {
                return SaveFileUtils.saveBitmapFileEditor(PolishEditorActivity.this, PolishEditorActivity.this.polishView.getCurrentBitmap(), new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(new Date()), null).getAbsolutePath();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        public void onPostExecute(String string) {
            PolishEditorActivity.this.showLoading(false);
            if (string == null) {
                Toast.makeText(PolishEditorActivity.this.getApplicationContext(), "Oop! Something went wrong", Toast.LENGTH_LONG).show();
                return;
            }
            Intent i = new Intent(PolishEditorActivity.this, PhotoShareActivity.class);
            i.putExtra("path", string);
            PolishEditorActivity.this.startActivity(i);
        }

    }

    public void showLoading(boolean z) {
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

    private void SaveView() {
        if (PermissionsUtils.checkWriteStoragePermission(PolishEditorActivity.this)) {
            new SaveEditingBitmap().execute();
        }
    }

}
