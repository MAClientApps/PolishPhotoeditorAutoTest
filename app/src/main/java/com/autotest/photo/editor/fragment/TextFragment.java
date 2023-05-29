package com.autotest.photo.editor.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.BitmapShader;
import android.graphics.Color;
import android.graphics.Shader;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.view.OnApplyWindowInsetsListener;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.autotest.photo.editor.R;
import com.autotest.photo.editor.adapters.FontAdapter;
import com.autotest.photo.editor.adapters.ShadowAdapter;
import com.autotest.photo.editor.adapters.TetxBackgroundAdapter;
import com.autotest.photo.editor.adapters.TetxColorAdapter;
import com.autotest.photo.editor.polish.PolishText;
import com.autotest.photo.editor.polish.PolishEditText;
import com.autotest.photo.editor.picker.PolishCarouselPicker;
import com.autotest.photo.editor.assets.FontFileAsset;
import com.autotest.photo.editor.preference.Preference;
import com.autotest.photo.editor.utils.SystemUtil;

import java.util.ArrayList;
import java.util.List;

public class TextFragment extends DialogFragment implements View.OnClickListener, FontAdapter.ItemClickListener,
        ShadowAdapter.ShadowItemClickListener, TetxColorAdapter.ColorListener, TetxBackgroundAdapter.BackgroundColorListener {
    public static final String EXTRA_COLOR_CODE = "extra_color_code";
    public static final String EXTRA_INPUT_TEXT = "extra_input_text";
    public static final String TAG = "TextFragment";
    LinearLayout linear_layout_edit_text_tools;

    public PolishText polishText;
    ImageView image_view_text_texture;
    SeekBar seekbar_radius;
    SeekBar seekbar_height;
    SeekBar seekbar_background_opacity;
    SeekBar seekbar_width;
    ImageView image_view_color;
    TextView textViewFont;
    TextView textViewShadow;
    ScrollView scroll_view_change_color_layout;
    ImageView image_view_fonts;
    ImageView image_view_adjust;
    ScrollView scroll_view_change_font_adjust;
    LinearLayout scroll_view_change_font_layout;
    ImageView image_view_align_left;
    ImageView image_view_align_center;
    ImageView image_view_align_right;
    public RecyclerView recycler_view_color;
    public RecyclerView recycler_view_background;
    public List<PolishCarouselPicker.PickerItem> colorItems;
    private FontAdapter fontAdapter;
    private ShadowAdapter shadowAdapter;
    View view_highlight_texture;
    LinearLayout linear_layout_preview;
    RecyclerView recycler_view_fonts;
    RecyclerView recycler_view_shadow;
    PolishEditText add_text_edit_text;
    private InputMethodManager inputMethodManager;
    TextView text_view_preview_effect;
    TextView textViewSeekBarSize;
    TextView textViewSeekBarColor;
    TextView textViewSeekBarBackground;
    TextView textViewSeekBarRadius;
    TextView textViewSeekBarWith;
    TextView textViewSeekBarHeight;
    ImageView image_view_save_change;
    ImageView image_view_keyboard;
    CheckBox checkbox_background;
    private TextEditor textEditor;
    private List<ImageView> textFunctions;
    SeekBar seekbar_text_size;
    public List<PolishCarouselPicker.PickerItem> textTextureItems;
    PolishCarouselPicker texture_carousel_picker;
    SeekBar textColorOpacity;

    public interface TextEditor {
        void onBackButton();
        void onDone(PolishText polishText);
    }

    public void onItemClick(View view, int i) {
        FontFileAsset.setFontByName(requireContext(), this.text_view_preview_effect, FontFileAsset.getListFonts().get(i));
        this.polishText.setFontName(FontFileAsset.getListFonts().get(i));
        this.polishText.setFontIndex(i);
    }

    public static TextFragment show(@NonNull AppCompatActivity appCompatActivity, @NonNull String str, @ColorInt int i) {
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_INPUT_TEXT, str);
        bundle.putInt(EXTRA_COLOR_CODE, i);
        TextFragment textFragment = new TextFragment();
        textFragment.setArguments(bundle);
        textFragment.show(appCompatActivity.getSupportFragmentManager(), TAG);
        return textFragment;
    }

    public static TextFragment show(@NonNull AppCompatActivity appCompatActivity, PolishText addTextProperties) {
        TextFragment addTextFragment = new TextFragment();
        addTextFragment.setPolishText(addTextProperties);
        addTextFragment.show(appCompatActivity.getSupportFragmentManager(), TAG);
        return addTextFragment;
    }

    public void onShadowItemClick(View view, int i) {
        PolishText.TextShadow textShadow = PolishText.getLstTextShadow().get(i);
        this.text_view_preview_effect.setShadowLayer((float) textShadow.getRadius(), (float) textShadow.getDx(), (float) textShadow.getDy(), textShadow.getColorShadow());
        this.text_view_preview_effect.invalidate();
        this.polishText.setTextShadow(textShadow);
        this.polishText.setTextShadowIndex(i);
    }

    public static TextFragment show(@NonNull AppCompatActivity appCompatActivity) {
        return show(appCompatActivity, "Test", ContextCompat.getColor(appCompatActivity, R.color.black));
    }

    public void setPolishText(PolishText polishText) {
        this.polishText = polishText;
    }

    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(-1, -1);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
    }

    @Nullable
    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        getDialog().getWindow().requestFeature(1);
        getDialog().getWindow().setFlags(1024, 1024);
        return layoutInflater.inflate(R.layout.fragment_add_text, viewGroup, false);
    }

    public void dismissAndShowSticker() {
        if (this.textEditor != null) {
            this.textEditor.onBackButton();
        }
        dismiss();
    }

    public void onActivityCreated(@Nullable Bundle bundle) {
        super.onActivityCreated(bundle);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle bundle) {
        super.onViewCreated(view, bundle);
        initView(view);
        if (this.polishText == null) {
            this.polishText = PolishText.getDefaultProperties();
        }
        this.add_text_edit_text.setTextFragment(this);
        initAddTextLayout();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(new DisplayMetrics());
        this.inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        setDefaultStyleForEdittext();
        this.inputMethodManager.toggleSoftInput(2, 0);
        highlightFunction(this.image_view_keyboard);
        this.recycler_view_fonts.setLayoutManager(new GridLayoutManager(getContext(), 5));
        this.fontAdapter = new FontAdapter(getContext(), FontFileAsset.getListFonts());
        this.fontAdapter.setClickListener(this);
        this.recycler_view_fonts.setAdapter(this.fontAdapter);
        this.recycler_view_shadow.setLayoutManager(new GridLayoutManager(getContext(), 5));
        this.shadowAdapter = new ShadowAdapter(getContext(), PolishText.getLstTextShadow());
        this.shadowAdapter.setClickListener(this);
        this.recycler_view_shadow.setAdapter(this.shadowAdapter);
        this.texture_carousel_picker.setAdapter(new PolishCarouselPicker.CarouselViewAdapter(getContext(), this.textTextureItems, 0));
        this.texture_carousel_picker.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int i) {
            }

            public void onPageSelected(int i) {
            }

            public void onPageScrolled(int i, float f, int i2) {
                if (f > 0.0f) {
                    if (TextFragment.this.image_view_text_texture.getVisibility() == View.INVISIBLE) {
                        TextFragment.this.image_view_text_texture.setVisibility(View.VISIBLE);
                        TextFragment.this.view_highlight_texture.setVisibility(View.VISIBLE);
                    }
                    float f2 = ((float) i) + f;
                    BitmapShader bitmapShader = new BitmapShader((TextFragment.this.textTextureItems.get(Math.round(f2))).getBitmap(), Shader.TileMode.MIRROR, Shader.TileMode.MIRROR);
                    TextFragment.this.text_view_preview_effect.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
                    TextFragment.this.text_view_preview_effect.getPaint().setShader(bitmapShader);
                    TextFragment.this.polishText.setTextShader(bitmapShader);
                    TextFragment.this.polishText.setTextShaderIndex(Math.round(f2));
                }
            }
        });

        this.textColorOpacity.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                String value = String.valueOf(i);
                textViewSeekBarColor.setText(value);
                int i2 = 255 - i;
                TextFragment.this.polishText.setTextAlpha(i2);
                TextFragment.this.text_view_preview_effect.setTextColor(Color.argb(i2, Color.red(TextFragment.this.polishText.getTextColor()), Color.green(TextFragment.this.polishText.getTextColor()), Color.blue(TextFragment.this.polishText.getTextColor())));
            }
        });
        this.add_text_edit_text.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable editable) {
            }

            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                TextFragment.this.text_view_preview_effect.setText(charSequence.toString());
                TextFragment.this.polishText.setText(charSequence.toString());
            }
        });
        this.checkbox_background.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                if (!z) {
                    TextFragment.this.polishText.setShowBackground(false);
                    TextFragment.this.text_view_preview_effect.setBackgroundResource(0);
                    TextFragment.this.text_view_preview_effect.setLayoutParams(new LinearLayout.LayoutParams(-2, -2));
                } else if (TextFragment.this.checkbox_background.isPressed() || TextFragment.this.polishText.isShowBackground()) {
                    TextFragment.this.polishText.setShowBackground(true);
                    TextFragment.this.initPreviewText();
                } else {
                    TextFragment.this.checkbox_background.setChecked(false);
                    TextFragment.this.polishText.setShowBackground(false);
                    TextFragment.this.initPreviewText();
                }
            }
        });
        this.seekbar_width.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                String value = String.valueOf(i);
                textViewSeekBarWith.setText(value);
                TextFragment.this.text_view_preview_effect.setPadding(SystemUtil.dpToPx(TextFragment.this.requireContext(), i), TextFragment.this.text_view_preview_effect.getPaddingTop(), SystemUtil.dpToPx(TextFragment.this.getContext(), i), TextFragment.this.text_view_preview_effect.getPaddingBottom());
                TextFragment.this.polishText.setPaddingWidth(i);
            }
        });
        this.seekbar_height.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                String value = String.valueOf(i);
                textViewSeekBarHeight.setText(value);
                TextFragment.this.text_view_preview_effect.setPadding(TextFragment.this.text_view_preview_effect.getPaddingLeft(), SystemUtil.dpToPx(TextFragment.this.requireContext(), i), TextFragment.this.text_view_preview_effect.getPaddingRight(), SystemUtil.dpToPx(TextFragment.this.getContext(), i));
                TextFragment.this.polishText.setPaddingHeight(i);
            }
        });
        this.seekbar_background_opacity.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                String value = String.valueOf(i);
                textViewSeekBarBackground.setText(value);
                TextFragment.this.polishText.setBackgroundAlpha(255 - i);
                    int red = Color.red(TextFragment.this.polishText.getBackgroundColor());
                    int green = Color.green(TextFragment.this.polishText.getBackgroundColor());
                    int blue = Color.blue(TextFragment.this.polishText.getBackgroundColor());
                    GradientDrawable gradientDrawable = new GradientDrawable();
                    gradientDrawable.setColor(Color.argb(TextFragment.this.polishText.getBackgroundAlpha(), red, green, blue));
                    gradientDrawable.setCornerRadius((float) SystemUtil.dpToPx(TextFragment.this.requireContext(), TextFragment.this.polishText.getBackgroundBorder()));
                    TextFragment.this.text_view_preview_effect.setBackground(gradientDrawable);

            }
        });
        this.seekbar_text_size.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                String value = String.valueOf(i);
                textViewSeekBarSize.setText(value);
                int i2 = 15;
                if (i >= 15) {
                    i2 = i;
                }
                TextFragment.this.text_view_preview_effect.setTextSize((float) i2);
                TextFragment.this.polishText.setTextSize(i2);
            }
        });
        this.seekbar_radius.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                String value = String.valueOf(i);
                textViewSeekBarRadius.setText(value);
                TextFragment.this.polishText.setBackgroundBorder(i);
                    GradientDrawable gradientDrawable = new GradientDrawable();
                    gradientDrawable.setCornerRadius((float) SystemUtil.dpToPx(TextFragment.this.requireContext(), i));
                    gradientDrawable.setColor(Color.argb(TextFragment.this.polishText.getBackgroundAlpha(), Color.red(TextFragment.this.polishText.getBackgroundColor()), Color.green(TextFragment.this.polishText.getBackgroundColor()), Color.blue(TextFragment.this.polishText.getBackgroundColor())));
                    TextFragment.this.text_view_preview_effect.setBackground(gradientDrawable);

            }
        });
        if (Preference.getKeyboard(requireContext()) > 0) {
            updateAddTextBottomToolbarHeight(Preference.getKeyboard(getContext()));
        }
        initPreviewText();
    }

    public void initView(View view) {
        this.add_text_edit_text = view.findViewById(R.id.add_text_edit_text);
        this.image_view_keyboard = view.findViewById(R.id.image_view_keyboard);
        this.image_view_fonts = view.findViewById(R.id.image_view_fonts);
        this.image_view_color = view.findViewById(R.id.image_view_color);
        this.image_view_align_left = view.findViewById(R.id.imageViewAlignLeft);
        this.image_view_align_center = view.findViewById(R.id.imageViewAlignCenter);
        this.image_view_align_right = view.findViewById(R.id.imageViewAlignRight);
        this.textViewFont = view.findViewById(R.id.textViewFont);
        this.textViewShadow = view.findViewById(R.id.textViewShadow);
        this.textViewSeekBarSize = view.findViewById(R.id.seekbarSize);
        this.textViewSeekBarColor = view.findViewById(R.id.seekbarColor);
        this.textViewSeekBarBackground = view.findViewById(R.id.seekbarBackground);
        this.textViewSeekBarRadius = view.findViewById(R.id.seekbarRadius);
        this.textViewSeekBarWith = view.findViewById(R.id.seekbarWith);
        this.textViewSeekBarHeight = view.findViewById(R.id.seekbarHeight);
        this.image_view_adjust = view.findViewById(R.id.image_view_adjust);
        this.image_view_save_change = view.findViewById(R.id.image_view_save_change);
        this.scroll_view_change_font_layout = view.findViewById(R.id.scroll_view_change_font_layout);
        this.scroll_view_change_font_adjust = view.findViewById(R.id.scroll_view_change_color_adjust);
        this.linear_layout_edit_text_tools = view.findViewById(R.id.linear_layout_edit_text_tools);
        this.recycler_view_fonts = view.findViewById(R.id.recycler_view_fonts);
        this.recycler_view_shadow = view.findViewById(R.id.recycler_view_shadow);
        this.scroll_view_change_color_layout = view.findViewById(R.id.scroll_view_change_color_layout);
        this.texture_carousel_picker = view.findViewById(R.id.texture_carousel_picker);
        this.image_view_text_texture = view.findViewById(R.id.image_view_text_texture);
        this.view_highlight_texture = view.findViewById(R.id.view_highlight_texture);
        this.textColorOpacity = view.findViewById(R.id.seekbar_text_opacity);
        this.text_view_preview_effect = view.findViewById(R.id.text_view_preview_effect);
        this.linear_layout_preview = view.findViewById(R.id.linear_layout_preview);
        this.checkbox_background = view.findViewById(R.id.checkbox_background);
        this.seekbar_width = view.findViewById(R.id.seekbar_width);
        this.seekbar_height = view.findViewById(R.id.seekbar_height);
        this.seekbar_background_opacity = view.findViewById(R.id.seekbar_background_opacity);
        this.seekbar_text_size = view.findViewById(R.id.seekbar_text_size);
        this.seekbar_radius = view.findViewById(R.id.seekbar_radius);

        this.recycler_view_color = view.findViewById(R.id.recyclerViewColor);
        this.recycler_view_color.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        this.recycler_view_color.setAdapter(new TetxColorAdapter(getContext(), this));
        this.recycler_view_color.setVisibility(View.VISIBLE);

        this.recycler_view_background = view.findViewById(R.id.recyclerViewBackground);
        this.recycler_view_background.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        this.recycler_view_background.setAdapter(new TetxBackgroundAdapter(getContext(), this));
        this.recycler_view_background.setVisibility(View.VISIBLE);
    }

    public void initPreviewText() {
        if (this.polishText.isShowBackground()) {
            if (this.polishText.getBackgroundColor() != 0) {
                this.text_view_preview_effect.setBackgroundColor(this.polishText.getBackgroundColor());
            }
            if (this.polishText.getBackgroundAlpha() < 255) {
                this.text_view_preview_effect.setBackgroundColor(Color.argb(this.polishText.getBackgroundAlpha(), Color.red(this.polishText.getBackgroundColor()), Color.green(this.polishText.getBackgroundColor()), Color.blue(this.polishText.getBackgroundColor())));
            }
            if (this.polishText.getBackgroundBorder() > 0) {
                GradientDrawable gradientDrawable = new GradientDrawable();
                gradientDrawable.setCornerRadius((float) SystemUtil.dpToPx(requireContext(), this.polishText.getBackgroundBorder()));
                gradientDrawable.setColor(Color.argb(this.polishText.getBackgroundAlpha(), Color.red(this.polishText.getBackgroundColor()), Color.green(this.polishText.getBackgroundColor()), Color.blue(this.polishText.getBackgroundColor())));
                this.text_view_preview_effect.setBackground(gradientDrawable);
            }
        }
        if (this.polishText.getPaddingHeight() > 0) {
            this.text_view_preview_effect.setPadding(this.text_view_preview_effect.getPaddingLeft(), this.polishText.getPaddingHeight(), this.text_view_preview_effect.getPaddingRight(), this.polishText.getPaddingHeight());
            this.seekbar_height.setProgress(this.polishText.getPaddingHeight());
        }
        if (this.polishText.getPaddingWidth() > 0) {
            this.text_view_preview_effect.setPadding(this.polishText.getPaddingWidth(), this.text_view_preview_effect.getPaddingTop(), this.polishText.getPaddingWidth(), this.text_view_preview_effect.getPaddingBottom());
            this.seekbar_width.setProgress(this.polishText.getPaddingWidth());
        }
        if (this.polishText.getText() != null) {
            this.text_view_preview_effect.setText(this.polishText.getText());
            this.add_text_edit_text.setText(this.polishText.getText());
        }
        if (this.polishText.getTextShader() != null) {
            this.text_view_preview_effect.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            this.text_view_preview_effect.getPaint().setShader(this.polishText.getTextShader());
        }
        if (this.polishText.getTextAlign() == 4) {
            this.image_view_align_center.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_format_align_center_select));
        } else if (this.polishText.getTextAlign() == 3) {
            this.image_view_align_right.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_format_align_right));
        } else if (this.polishText.getTextAlign() == 2) {
            this.image_view_align_left.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_format_align_left));
        }
        this.text_view_preview_effect.setPadding(SystemUtil.dpToPx(getContext(), this.polishText.getPaddingWidth()), this.text_view_preview_effect.getPaddingTop(), SystemUtil.dpToPx(getContext(), this.polishText.getPaddingWidth()), this.text_view_preview_effect.getPaddingBottom());
        this.text_view_preview_effect.setTextColor(this.polishText.getTextColor());
        this.text_view_preview_effect.setTextAlignment(this.polishText.getTextAlign());
        this.text_view_preview_effect.setTextSize((float) this.polishText.getTextSize());
        FontFileAsset.setFontByName(getContext(), this.text_view_preview_effect, this.polishText.getFontName());
        this.text_view_preview_effect.invalidate();
    }

    private void setDefaultStyleForEdittext() {
        this.add_text_edit_text.requestFocus();
        this.add_text_edit_text.setTextSize(20.0f);
        this.add_text_edit_text.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        this.add_text_edit_text.setTextColor(Color.parseColor("#FFFFFF"));
    }

    private void initAddTextLayout() {
        this.textFunctions = getTextFunctions();
        this.image_view_keyboard.setOnClickListener(this);
        this.image_view_fonts.setOnClickListener(this);
        this.textViewFont.setOnClickListener(this);
        this.textViewShadow.setOnClickListener(this);
        this.image_view_adjust.setOnClickListener(this);
        this.image_view_color.setOnClickListener(this);
        this.image_view_save_change.setOnClickListener(this);
        this.image_view_align_left.setOnClickListener(this);
        this.image_view_align_center.setOnClickListener(this);
        this.image_view_align_right.setOnClickListener(this);
        this.scroll_view_change_font_layout.setVisibility(View.GONE);
        this.scroll_view_change_font_adjust.setVisibility(View.GONE);
        this.scroll_view_change_color_layout.setVisibility(View.GONE);
        this.image_view_text_texture.setVisibility(View.INVISIBLE);
        this.view_highlight_texture.setVisibility(View.GONE);
        this.seekbar_width.setProgress(this.polishText.getPaddingWidth());
        this.colorItems = getColorItems();
        this.textTextureItems = getTextTextures();
    }

    @Override
    public void onColorSelected(TetxColorAdapter.SquareView squareView) {
        if (squareView.isColor) {
            TextFragment.this.text_view_preview_effect.setTextColor(squareView.drawableId);
            TextFragment.this.polishText.setTextColor(squareView.drawableId);
            TextFragment.this.text_view_preview_effect.getPaint().setShader(null);
            TextFragment.this.polishText.setTextShader(null);
        }  else {
            TextFragment.this.text_view_preview_effect.setTextColor(squareView.drawableId);
            TextFragment.this.polishText.setTextColor(squareView.drawableId);
            TextFragment.this.text_view_preview_effect.getPaint().setShader(null);
            TextFragment.this.polishText.setTextShader(null);
        }
    }

    @Override
    public void onBackgroundColorSelected(TetxBackgroundAdapter.SquareView squareView) {
        if (squareView.isColor) {
            TextFragment.this.text_view_preview_effect.setBackgroundColor(squareView.drawableId);
            TextFragment.this.polishText.setBackgroundColor(squareView.drawableId);
            TextFragment.this.seekbar_radius.setEnabled(true);
            TextFragment.this.polishText.setShowBackground(true);
            if (!TextFragment.this.checkbox_background.isChecked()) {
                TextFragment.this.checkbox_background.setChecked(true);
            }
            int red = Color.red(TextFragment.this.polishText.getBackgroundColor());
            int green = Color.green(TextFragment.this.polishText.getBackgroundColor());
            int blue = Color.blue(TextFragment.this.polishText.getBackgroundColor());
            GradientDrawable gradientDrawable = new GradientDrawable();
            gradientDrawable.setColor(Color.argb(TextFragment.this.polishText.getBackgroundAlpha(), red, green, blue));
            gradientDrawable.setCornerRadius((float) SystemUtil.dpToPx(TextFragment.this.requireContext(), TextFragment.this.polishText.getBackgroundBorder()));
            TextFragment.this.text_view_preview_effect.setBackground(gradientDrawable);

        }  else {
            TextFragment.this.text_view_preview_effect.setBackgroundColor(squareView.drawableId);
            TextFragment.this.polishText.setBackgroundColor(squareView.drawableId);
            TextFragment.this.seekbar_radius.setEnabled(true);
            TextFragment.this.polishText.setShowBackground(true);
            if (!TextFragment.this.checkbox_background.isChecked()) {
                TextFragment.this.checkbox_background.setChecked(true);
            }
            int red = Color.red(TextFragment.this.polishText.getBackgroundColor());
            int green = Color.green(TextFragment.this.polishText.getBackgroundColor());
            int blue = Color.blue(TextFragment.this.polishText.getBackgroundColor());
            GradientDrawable gradientDrawable = new GradientDrawable();
            gradientDrawable.setColor(Color.argb(TextFragment.this.polishText.getBackgroundAlpha(), red, green, blue));
            gradientDrawable.setCornerRadius((float) SystemUtil.dpToPx(TextFragment.this.requireContext(), TextFragment.this.polishText.getBackgroundBorder()));
            TextFragment.this.text_view_preview_effect.setBackground(gradientDrawable);
        }
    }

    public void onResume() {
        super.onResume();
        ViewCompat.setOnApplyWindowInsetsListener(getDialog().getWindow().getDecorView(), new OnApplyWindowInsetsListener() {
            public final WindowInsetsCompat onApplyWindowInsets(View view, WindowInsetsCompat windowInsetsCompat) {
                return ViewCompat.onApplyWindowInsets(
                        TextFragment.this.getDialog().getWindow().getDecorView(),
                        windowInsetsCompat.inset(windowInsetsCompat.getSystemWindowInsetLeft(), 0, windowInsetsCompat.getSystemWindowInsetRight(), windowInsetsCompat.getSystemWindowInsetBottom()));
            }
        });

    }

    public void updateAddTextBottomToolbarHeight(final int i) {
        new Handler().post(new Runnable() {
            public void run() {
                ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) TextFragment.this.linear_layout_edit_text_tools.getLayoutParams();
                layoutParams.bottomMargin = i;
                TextFragment.this.linear_layout_edit_text_tools.setLayoutParams(layoutParams);
                TextFragment.this.linear_layout_edit_text_tools.invalidate();
                TextFragment.this.scroll_view_change_font_layout.invalidate();
                TextFragment.this.scroll_view_change_font_adjust.invalidate();
                TextFragment.this.scroll_view_change_color_layout.invalidate();
                Log.i("HIHIH", i + "");
            }
        });
    }

    public void setOnTextEditorListener(TextEditor textEditor2) {
        this.textEditor = textEditor2;
    }

    private void highlightFunction(ImageView imageView) {
        for (ImageView next : this.textFunctions) {
            if (next == imageView) {
                imageView.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.line));
            } else {
                next.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.line_fake));
            }
        }
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.textViewFont:
                this.textViewShadow.setTextColor(getResources().getColor(R.color.white));
                this.textViewFont.setTextColor(getResources().getColor(R.color.mainColor));
                this.recycler_view_fonts.setVisibility(View.VISIBLE);
                this.recycler_view_shadow.setVisibility(View.GONE);
                return;
            case R.id.textViewShadow:
                this.textViewShadow.setTextColor(getResources().getColor(R.color.mainColor));
                this.textViewFont.setTextColor(getResources().getColor(R.color.white));
                this.recycler_view_fonts.setVisibility(View.GONE);
                this.recycler_view_shadow.setVisibility(View.VISIBLE);
                return;
            case R.id.imageViewAlignLeft:
                if (this.polishText.getTextAlign() == 3 || this.polishText.getTextAlign() == 4) {
                    this.polishText.setTextAlign(2);
                    this.image_view_align_left.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_format_align_left_select));
                    this.image_view_align_center.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_format_align_center));
                    this.image_view_align_right.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_format_align_right));
                }
                this.text_view_preview_effect.setTextAlignment(this.polishText.getTextAlign());
                TextView textView = this.text_view_preview_effect;
                textView.setText(this.text_view_preview_effect.getText().toString().trim() + " ");
                this.text_view_preview_effect.setText(this.text_view_preview_effect.getText().toString().trim());
                return;
            case R.id.imageViewAlignCenter:
                if (this.polishText.getTextAlign() == 2 || this.polishText.getTextAlign() == 3) {
                    this.polishText.setTextAlign(4);
                    this.image_view_align_center.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_format_align_center_select));
                    this.image_view_align_left.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_format_align_left));
                    this.image_view_align_right.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_format_align_right));
                }
                this.text_view_preview_effect.setTextAlignment(this.polishText.getTextAlign());
                TextView textViews = this.text_view_preview_effect;
                textViews.setText(this.text_view_preview_effect.getText().toString().trim() + " ");
                this.text_view_preview_effect.setText(this.text_view_preview_effect.getText().toString().trim());
                return;
            case R.id.imageViewAlignRight:
                if (this.polishText.getTextAlign() == 4 || this.polishText.getTextAlign() == 2) {
                    this.polishText.setTextAlign(3);
                    this.image_view_align_left.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_format_align_left));
                    this.image_view_align_center.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_format_align_center));
                    this.image_view_align_right.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_format_align_right_select));
                }
                this.text_view_preview_effect.setTextAlignment(this.polishText.getTextAlign());
                TextView textViewz = this.text_view_preview_effect;
                textViewz.setText(this.text_view_preview_effect.getText().toString().trim() + " ");
                this.text_view_preview_effect.setText(this.text_view_preview_effect.getText().toString().trim());
                return;
            case R.id.image_view_adjust:
                this.inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                this.scroll_view_change_color_layout.setVisibility(View.GONE);
                this.scroll_view_change_font_adjust.setVisibility(View.VISIBLE);
                this.scroll_view_change_font_layout.setVisibility(View.GONE);
                this.seekbar_background_opacity.setProgress(255 - this.polishText.getBackgroundAlpha());
                this.seekbar_text_size.setProgress(this.polishText.getTextSize());
                this.seekbar_radius.setProgress(this.polishText.getBackgroundBorder());
                this.seekbar_width.setProgress(this.polishText.getPaddingWidth());
                this.seekbar_height.setProgress(this.polishText.getPaddingHeight());
                this.textColorOpacity.setProgress(255 - this.polishText.getTextAlpha());
                toggleTextEditEditable(false);
                highlightFunction(this.image_view_adjust);
                return;
            case R.id.image_view_color:
                this.inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                this.scroll_view_change_color_layout.setVisibility(View.VISIBLE);
                this.scroll_view_change_font_adjust.setVisibility(View.GONE);
                toggleTextEditEditable(false);
                highlightFunction(this.image_view_color);
                this.scroll_view_change_font_layout.setVisibility(View.GONE);
                this.add_text_edit_text.setVisibility(View.GONE);
                this.texture_carousel_picker.setCurrentItem(this.polishText.getTextShaderIndex());
                this.checkbox_background.setChecked(this.polishText.isShowBackground());
                this.checkbox_background.setChecked(this.polishText.isShowBackground());
                if (this.polishText.getTextShader() != null && this.image_view_text_texture.getVisibility() == View.INVISIBLE) {
                    this.image_view_text_texture.setVisibility(View.VISIBLE);
                    this.view_highlight_texture.setVisibility(View.VISIBLE);
                    return;
                }
                return;
            case R.id.image_view_fonts:
                this.inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                this.scroll_view_change_font_layout.setVisibility(View.VISIBLE);
                this.scroll_view_change_color_layout.setVisibility(View.GONE);
                this.scroll_view_change_font_adjust.setVisibility(View.GONE);
                this.add_text_edit_text.setVisibility(View.GONE);
                toggleTextEditEditable(false);
                highlightFunction(this.image_view_fonts);
                this.shadowAdapter.setSelectedItem(this.polishText.getFontIndex());
                this.fontAdapter.setSelectedItem(this.polishText.getFontIndex());
                return;
            case R.id.image_view_save_change:
                if (this.polishText.getText() == null || this.polishText.getText().length() == 0) {
                    this.inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    this.textEditor.onBackButton();
                    dismiss();
                    return;
                }
                this.polishText.setTextWidth(this.text_view_preview_effect.getMeasuredWidth());
                this.polishText.setTextHeight(this.text_view_preview_effect.getMeasuredHeight());
                this.inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                this.textEditor.onDone(this.polishText);
                dismiss();
                return;
            case R.id.image_view_keyboard:
                toggleTextEditEditable(true);
                this.add_text_edit_text.setVisibility(View.VISIBLE);
                this.add_text_edit_text.requestFocus();
                highlightFunction(this.image_view_keyboard);
                this.scroll_view_change_font_layout.setVisibility(View.GONE);
                this.scroll_view_change_color_layout.setVisibility(View.GONE);
                this.scroll_view_change_font_adjust.setVisibility(View.GONE);
                this.linear_layout_edit_text_tools.invalidate();
                this.inputMethodManager.toggleSoftInput(2, 0);
                return;
            default:
        }
    }

    private void toggleTextEditEditable(boolean z) {
        this.add_text_edit_text.setFocusable(z);
        this.add_text_edit_text.setFocusableInTouchMode(z);
        this.add_text_edit_text.setClickable(z);

    }

    private List<ImageView> getTextFunctions() {
        ArrayList arrayList = new ArrayList();
        arrayList.add(this.image_view_keyboard);
        arrayList.add(this.image_view_fonts);
        arrayList.add(this.image_view_color);
        arrayList.add(this.image_view_adjust);
        arrayList.add(this.image_view_save_change);
        return arrayList;
    }

    public List<PolishCarouselPicker.PickerItem> getTextTextures() {
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < 42; i++) {
            try {
                AssetManager assets = getContext().getAssets();
                arrayList.add(new PolishCarouselPicker.DrawableItem(Drawable.createFromStream(assets.open("texture/texture_" + (i + 1) + ".webp"), (String) null)));
            } catch (Exception e) {
            }
        }
        return arrayList;
    }

    public List<PolishCarouselPicker.PickerItem> getColorItems() {
        ArrayList arrayList = new ArrayList();
        //FREE
        arrayList.add(new PolishCarouselPicker.ColorItem("#FFFFFF"));
        arrayList.add(new PolishCarouselPicker.ColorItem("#BBBBBB"));
        arrayList.add(new PolishCarouselPicker.ColorItem("#4E4E4E"));
        arrayList.add(new PolishCarouselPicker.ColorItem("#212121"));
        arrayList.add(new PolishCarouselPicker.ColorItem("#000000"));
        arrayList.add(new PolishCarouselPicker.ColorItem("#FFD7CD"));
        arrayList.add(new PolishCarouselPicker.ColorItem("#F9AB9D"));
        arrayList.add(new PolishCarouselPicker.ColorItem("#EC5C60"));
        arrayList.add(new PolishCarouselPicker.ColorItem("#CB3243"));
        arrayList.add(new PolishCarouselPicker.ColorItem("#CD181F"));
        arrayList.add(new PolishCarouselPicker.ColorItem("#FF0000"));
        arrayList.add(new PolishCarouselPicker.ColorItem("#FFF2CA"));
        arrayList.add(new PolishCarouselPicker.ColorItem("#FDE472"));
        arrayList.add(new PolishCarouselPicker.ColorItem("#F3AF59"));
        arrayList.add(new PolishCarouselPicker.ColorItem("#FC7F3D"));
        arrayList.add(new PolishCarouselPicker.ColorItem("#ED3F0F"));
        arrayList.add(new PolishCarouselPicker.ColorItem("#FFF1F1"));
        arrayList.add(new PolishCarouselPicker.ColorItem("#FFE1E4"));
        arrayList.add(new PolishCarouselPicker.ColorItem("#FFA4B9"));
        arrayList.add(new PolishCarouselPicker.ColorItem("#FF679F"));
        arrayList.add(new PolishCarouselPicker.ColorItem("#FB2C78"));
        arrayList.add(new PolishCarouselPicker.ColorItem("#E7D5E7"));
        arrayList.add(new PolishCarouselPicker.ColorItem("#D3A6D8"));
        arrayList.add(new PolishCarouselPicker.ColorItem("#BA66AF"));
        arrayList.add(new PolishCarouselPicker.ColorItem("#A53B8E"));
        arrayList.add(new PolishCarouselPicker.ColorItem("#65218C"));
        arrayList.add(new PolishCarouselPicker.ColorItem("#99D2F9"));
        arrayList.add(new PolishCarouselPicker.ColorItem("#81ADEA"));
        arrayList.add(new PolishCarouselPicker.ColorItem("#2961A9"));
        arrayList.add(new PolishCarouselPicker.ColorItem("#0E2E89"));
        arrayList.add(new PolishCarouselPicker.ColorItem("#171982"));
        arrayList.add(new PolishCarouselPicker.ColorItem("#A5E7F6"));
        arrayList.add(new PolishCarouselPicker.ColorItem("#7CE3FF"));
        arrayList.add(new PolishCarouselPicker.ColorItem("#00B0D0"));
        arrayList.add(new PolishCarouselPicker.ColorItem("#058BC0"));
        arrayList.add(new PolishCarouselPicker.ColorItem("#08447E"));
        arrayList.add(new PolishCarouselPicker.ColorItem("#DEEEE9"));
        arrayList.add(new PolishCarouselPicker.ColorItem("#B3D0C5"));
        arrayList.add(new PolishCarouselPicker.ColorItem("#4DAF9D"));
        arrayList.add(new PolishCarouselPicker.ColorItem("#21887C"));
        arrayList.add(new PolishCarouselPicker.ColorItem("#0F664E"));
        arrayList.add(new PolishCarouselPicker.ColorItem("#D3E5A6"));
        arrayList.add(new PolishCarouselPicker.ColorItem("#AACE87"));
        arrayList.add(new PolishCarouselPicker.ColorItem("#A3AF38"));
        arrayList.add(new PolishCarouselPicker.ColorItem("#6D822B"));
        arrayList.add(new PolishCarouselPicker.ColorItem("#366131"));
        arrayList.add(new PolishCarouselPicker.ColorItem("#E4D9C0"));
        arrayList.add(new PolishCarouselPicker.ColorItem("#D6C392"));
        arrayList.add(new PolishCarouselPicker.ColorItem("#A3815A"));
        arrayList.add(new PolishCarouselPicker.ColorItem("#72462F"));
        arrayList.add(new PolishCarouselPicker.ColorItem("#3E3129"));
        //PRO 1
        arrayList.add(new PolishCarouselPicker.ColorItem("#A7A7A7"));
        arrayList.add(new PolishCarouselPicker.ColorItem("#995452"));
        arrayList.add(new PolishCarouselPicker.ColorItem("#E1D6D7"));
        arrayList.add(new PolishCarouselPicker.ColorItem("#CCBFD4"));
        arrayList.add(new PolishCarouselPicker.ColorItem("#A67C80"));
        arrayList.add(new PolishCarouselPicker.ColorItem("#D4D3CF"));
        arrayList.add(new PolishCarouselPicker.ColorItem("#7A7185"));
        arrayList.add(new PolishCarouselPicker.ColorItem("#6E5055"));
        arrayList.add(new PolishCarouselPicker.ColorItem("#666563"));
        arrayList.add(new PolishCarouselPicker.ColorItem("#F0E4FB"));
        arrayList.add(new PolishCarouselPicker.ColorItem("#EED0D5"));
        arrayList.add(new PolishCarouselPicker.ColorItem("#E1CCD1"));
        arrayList.add(new PolishCarouselPicker.ColorItem("#C3C9DF"));
        arrayList.add(new PolishCarouselPicker.ColorItem("#929493"));
        arrayList.add(new PolishCarouselPicker.ColorItem("#807D5C"));
        arrayList.add(new PolishCarouselPicker.ColorItem("#E2E4E1"));
        arrayList.add(new PolishCarouselPicker.ColorItem("#8596A4"));
        arrayList.add(new PolishCarouselPicker.ColorItem("#ECECEC"));
        arrayList.add(new PolishCarouselPicker.ColorItem("#96A48C"));
        arrayList.add(new PolishCarouselPicker.ColorItem("#AFB0B2"));
        arrayList.add(new PolishCarouselPicker.ColorItem("#BEC0BF"));
        arrayList.add(new PolishCarouselPicker.ColorItem("#B7C5B3"));
        arrayList.add(new PolishCarouselPicker.ColorItem("#A0A7BA"));
        arrayList.add(new PolishCarouselPicker.ColorItem("#FFFAF7"));
        arrayList.add(new PolishCarouselPicker.ColorItem("#7D8970"));
        //PRO 2
        arrayList.add(new PolishCarouselPicker.ColorItem("#FF7063"));
        arrayList.add(new PolishCarouselPicker.ColorItem("#FF3274"));
        arrayList.add(new PolishCarouselPicker.ColorItem("#3EA2D7"));
        arrayList.add(new PolishCarouselPicker.ColorItem("#FFA258"));
        arrayList.add(new PolishCarouselPicker.ColorItem("#F22D52"));
        arrayList.add(new PolishCarouselPicker.ColorItem("#F2A0B6"));
        arrayList.add(new PolishCarouselPicker.ColorItem("#7285F2"));
        arrayList.add(new PolishCarouselPicker.ColorItem("#3ADB7C"));
        arrayList.add(new PolishCarouselPicker.ColorItem("#F2C4CD"));
        arrayList.add(new PolishCarouselPicker.ColorItem("#129AEF"));
        arrayList.add(new PolishCarouselPicker.ColorItem("#0278A6"));
        arrayList.add(new PolishCarouselPicker.ColorItem("#0D8C39"));
        arrayList.add(new PolishCarouselPicker.ColorItem("#5639A5"));
        arrayList.add(new PolishCarouselPicker.ColorItem("#A482F4"));
        arrayList.add(new PolishCarouselPicker.ColorItem("#4AE3D2"));
        arrayList.add(new PolishCarouselPicker.ColorItem("#00D28E"));
        arrayList.add(new PolishCarouselPicker.ColorItem("#FA4848"));
        arrayList.add(new PolishCarouselPicker.ColorItem("#434882"));
        arrayList.add(new PolishCarouselPicker.ColorItem("#730068"));
        arrayList.add(new PolishCarouselPicker.ColorItem("#729D38"));
        arrayList.add(new PolishCarouselPicker.ColorItem("#C6E377"));
        arrayList.add(new PolishCarouselPicker.ColorItem("#022D3C"));
        arrayList.add(new PolishCarouselPicker.ColorItem("#900C3F"));
        arrayList.add(new PolishCarouselPicker.ColorItem("#FF5733"));
        arrayList.add(new PolishCarouselPicker.ColorItem("#FFC300"));
        return arrayList;
    }
}
