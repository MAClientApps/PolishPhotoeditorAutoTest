package com.autotest.photo.editor.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.autotest.photo.editor.R;
import com.autotest.photo.editor.entity.Photo;
import com.autotest.photo.editor.event.OnItemCheckListener;
import com.autotest.photo.editor.fragment.ImagePagerFragment;
import com.autotest.photo.editor.fragment.PhotoPickerFragment;
import com.autotest.photo.editor.polish.PolishPickerView;

import java.util.ArrayList;

public class PhotoPickerActivity extends AppCompatActivity {
    private boolean forwardMain;
    private ImagePagerFragment imagePagerFragment;
    private int maxCount = 9;
    private ArrayList<String> originalPhotos = null;
    private PhotoPickerFragment pickerFragment;
    private boolean showGif = false;
    public PhotoPickerActivity getActivity() {
        return this;
    }
    private AdView adView;
    private AdRequest adRequest;
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        requestWindowFeature(1);
        getWindow().setFlags(1024, 1024);
        boolean booleanExtra = getIntent().getBooleanExtra(PolishPickerView.EXTRA_SHOW_CAMERA, true);
        boolean booleanExtra2 = getIntent().getBooleanExtra(PolishPickerView.EXTRA_SHOW_GIF, false);
        boolean booleanExtra3 = getIntent().getBooleanExtra(PolishPickerView.EXTRA_PREVIEW_ENABLED, true);
        this.forwardMain = getIntent().getBooleanExtra(PolishPickerView.MAIN_ACTIVITY, false);
        setShowGif(booleanExtra2);

        setContentView(R.layout.activity_photo_picker);
        setSupportActionBar(findViewById(R.id.toolbar));
        setTitle("");
        ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.setDisplayHomeAsUpEnabled(true);
        if (Build.VERSION.SDK_INT >= 21) {
            supportActionBar.setElevation(25.0f);
        }
        //init Ads
        MobileAds.initialize(PhotoPickerActivity.this, (OnInitializationCompleteListener) initializationStatus -> { });

       /* adView = findViewById(R.id.adView);
        adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);*/

        adView = findViewById(R.id.adView);

            adView.setVisibility(View.VISIBLE);
            adRequest = new AdRequest.Builder().build();
            adView.loadAd(adRequest);


        this.maxCount = getIntent().getIntExtra(PolishPickerView.EXTRA_MAX_COUNT, 9);
        int intExtra = getIntent().getIntExtra(PolishPickerView.EXTRA_GRID_COLUMN, 4);
        this.originalPhotos = getIntent().getStringArrayListExtra(PolishPickerView.EXTRA_ORIGINAL_PHOTOS);
        this.pickerFragment = (PhotoPickerFragment) getSupportFragmentManager().findFragmentByTag("tag");
        if (this.pickerFragment == null) {
            this.pickerFragment = PhotoPickerFragment.newInstance(booleanExtra, booleanExtra2, booleanExtra3, intExtra, this.maxCount, this.originalPhotos);
            getSupportFragmentManager().beginTransaction().replace(R.id.container, this.pickerFragment, "tag").commit();
            getSupportFragmentManager().executePendingTransactions();
        }
        this.pickerFragment.getPhotoGridAdapter().setOnItemCheckListener(new OnItemCheckListener() {
            public final boolean onItemCheck(int i, Photo photo, int i2) {
                if (!forwardMain) {
                    Intent intent = new Intent(PhotoPickerActivity.this, PolishEditorActivity.class);
                    intent.putExtra(PolishPickerView.KEY_SELECTED_PHOTOS, photo.getPath());
                    startActivity(intent);
                    finish();
                    return true;
                }
                PolishCollageActivity.getQueShotGridActivityInstance().replaceCurrentPiece(photo.getPath());
                finish();
                return true;
            }
        });
    }

    public void onBackPressed() {
        if (this.imagePagerFragment == null || !this.imagePagerFragment.isVisible()) {
            super.onBackPressed();
        } else if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        }
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == 16908332) {
            super.onBackPressed();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void setShowGif(boolean z) {
        this.showGif = z;
    }
}
