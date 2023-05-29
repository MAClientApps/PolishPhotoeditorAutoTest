package com.autotest.photo.editor.activities;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.autotest.photo.editor.R;
import com.autotest.photo.editor.dialog.DetailsDialog;
import com.autotest.photo.editor.dialog.RateDialog;
import com.autotest.photo.editor.picker.ImageCaptureManager;
import com.autotest.photo.editor.polish.PolishPickerView;
import com.autotest.photo.editor.preference.Preference;
import com.autotest.photo.editor.utils.AdsUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Objects;

public class PolishHomeActivity extends PolishBaseActivity {
    private ImageCaptureManager imageCaptureManager;
    private ImageView ImageViewSettings;
    private InterstitialAd mInterstitialAd;
    private AdRequest adRequest;
   // private final int MANAGE_FILE_ACCESS_REQ_CODE = 6237;
    // GDPR START //
//    ConsentForm form;
    // GDPR END //
    public static Context contextApp;
    //  Helper helper;

    public void onCreate(Bundle bundle) {
        setTheme(R.style.AppTheme_NoActionBar);
        super.onCreate(bundle);
        MobileAds.initialize(this, (OnInitializationCompleteListener) initializationStatus -> {
        });

        setFullScreen();
        setContentView(R.layout.activity_polish_home);
        contextApp = this.getApplicationContext();
        //    helper = new Helper(PolishHomeActivity.this);

        (findViewById(R.id.relative_layout_edit_photo)).setOnClickListener(this.onClickListener);
        (findViewById(R.id.relative_layout_edit_collage)).setOnClickListener(this.onClickListener);
        (findViewById(R.id.relative_layout_edit_camera)).setOnClickListener(this.onClickListener);
        this.imageCaptureManager = new ImageCaptureManager(this);
        this.ImageViewSettings = findViewById(R.id.imageViewSettings);
        this.ImageViewSettings.setOnClickListener(view -> {
            Intent intent = new Intent(PolishHomeActivity.this, PolishSettingsActivity.class);
            startActivity(intent);
        });
        initInterstitialAd();


        // GDPR START //
//        ConsentInformation consentInformation = ConsentInformation.getInstance(this);
 //       String[] publisherIds = {getResources().getString(R.string.Admob_ID)};
//        consentInformation.requestConsentInfoUpdate(publisherIds, new ConsentInfoUpdateListener() {
//            @Override
//            public void onConsentInfoUpdated(ConsentStatus consentStatus) {
//                // User's consent status successfully updated.
//            }
//
//            @Override
//            public void onFailedToUpdateConsentInfo(String errorDescription) {
//                // User's consent status failed to update.
//            }
//        });
        URL privacyUrl = null;
        try {
            // TODO: Replace with your app's privacy policy URL.
            privacyUrl = new URL(getResources().getString(R.string.policy_url));
        } catch (MalformedURLException e) {
            e.printStackTrace();
            // Handle error.
        }
//        form = new ConsentForm.Builder(this, privacyUrl)
//                .withListener(new ConsentFormListener() {
//                    @Override
//                    public void onConsentFormLoaded() {
//                        // Consent form loaded successfully.
//                        form.show();
//                    }
//
//                    @Override
//                    public void onConsentFormOpened() {
//                        // Consent form was displayed.
//                    }
//
//                    @Override
//                    public void onConsentFormClosed(
//                            ConsentStatus consentStatus, Boolean userPrefersAdFree) {
//                        // Consent form was closed.
//                    }
//
//                    @Override
//                    public void onConsentFormError(String errorDescription) {
//                        // Consent form error.
//                    }
//                })
//                .withPersonalizedAdsOption()
//                .withNonPersonalizedAdsOption()
//                .withAdFreeOption()
//                .build();
//        form.load();
        // GDPR END //

    }

    View.OnClickListener onClickListener = view -> {
        switch (view.getId()) {
            case R.id.relative_layout_edit_collage:
                if (mInterstitialAd != null) {
                    mInterstitialAd.show(Objects.requireNonNull(PolishHomeActivity.this));
                    mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                        @Override
                        public void onAdDismissedFullScreenContent() {
                            // Called when fullscreen content is dismissed.
                            goToCollage();
                        }

                        @Override
                        public void onAdFailedToShowFullScreenContent(AdError adError) {
                            // Called when fullscreen content failed to show.
                            goToCollage();
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
                    goToCollage();
                        initInterstitialAd();
                }
                return;
            case R.id.relative_layout_edit_camera:

                String[] arrPermissions = {"android.permission.CAMERA", "android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"};
                if (Build.VERSION.SDK_INT >= 29)arrPermissions=new String[]{"android.permission.CAMERA"};

                Dexter.withContext(PolishHomeActivity.this).withPermissions(arrPermissions).withListener(new MultiplePermissionsListener() {
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                        if (multiplePermissionsReport.areAllPermissionsGranted()) {
                            takePhotoFromCamera();
                        }
                        if (multiplePermissionsReport.isAnyPermissionPermanentlyDenied()) {
                            DetailsDialog.showDetailsDialog(PolishHomeActivity.this);
                        }
                    }

                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).withErrorListener(dexterError -> Toast.makeText(PolishHomeActivity.this, "Error occurred! ", Toast.LENGTH_SHORT).show()).onSameThread().check();

                return;
            case R.id.relative_layout_edit_photo:

                if (mInterstitialAd != null) {
                    mInterstitialAd.show(Objects.requireNonNull(PolishHomeActivity.this));
                    mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                        @Override
                        public void onAdDismissedFullScreenContent() {
                            // Called when fullscreen content is dismissed.
                            goToEditPhoto();
                        }

                        @Override
                        public void onAdFailedToShowFullScreenContent(AdError adError) {
                            // Called when fullscreen content failed to show.
                            goToEditPhoto();
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
                    goToEditPhoto();
                        initInterstitialAd();
                }
                return;
            default:
        }

    };


   /* private boolean checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            return Environment.isExternalStorageManager();
        } else {
            int result = ContextCompat.checkSelfPermission(PermissionActivity.this, READ_EXTERNAL_STORAGE);
            int result1 = ContextCompat.checkSelfPermission(PermissionActivity.this, WRITE_EXTERNAL_STORAGE);
            return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
        }
    }*/



    private void goToEditPhoto() {

        /*if (Build.VERSION.SDK_INT >= 30) {

            if (!Environment.isExternalStorageManager()) {
                try {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                    intent.addCategory("android.intent.category.DEFAULT");
                    intent.setData(Uri.parse(String.format("package:%s", getApplicationContext().getPackageName())));
                    startActivityForResult(intent, MANAGE_FILE_ACCESS_REQ_CODE);
                } catch (Exception e) {
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                    startActivityForResult(intent, MANAGE_FILE_ACCESS_REQ_CODE);
                }
            } else {
                PolishPickerView.builder().setPhotoCount(1).setPreviewEnabled(false).setShowCamera(false).start(PolishHomeActivity.this);
            }

        } else {*/
        String[] arrPermissions = {"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"};
        if (Build.VERSION.SDK_INT >= 29)arrPermissions=new String[]{"android.permission.READ_EXTERNAL_STORAGE"};


           /* if (Build.VERSION.SDK_INT < 30)
                arrPermissions = new String[]{"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"};
            else
                arrPermissions = new String[]{"android.permission.READ_EXTERNAL_STORAGE", "android.permission.MANAGE_EXTERNAL_STORAGE"};*/

            Dexter.withContext(PolishHomeActivity.this).withPermissions(arrPermissions).withListener(new MultiplePermissionsListener() {
                public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                    if (multiplePermissionsReport.areAllPermissionsGranted()) {
                        PolishPickerView.builder().setPhotoCount(1).setPreviewEnabled(false).setShowCamera(false).start(PolishHomeActivity.this);
                    }
                    if (multiplePermissionsReport.isAnyPermissionPermanentlyDenied()) {
                        DetailsDialog.showDetailsDialog(PolishHomeActivity.this);
                    }
                }

                public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                    permissionToken.continuePermissionRequest();
                }
            }).withErrorListener(dexterError -> Toast.makeText(PolishHomeActivity.this, "Error occurred! ", Toast.LENGTH_SHORT).show()).onSameThread().check();

      //  }
    }

    private void goToCollage() {

        /*if (Build.VERSION.SDK_INT >= 30) {

            if (!Environment.isExternalStorageManager()) {
                try {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                    intent.addCategory("android.intent.category.DEFAULT");
                    intent.setData(Uri.parse(String.format("package:%s", getApplicationContext().getPackageName())));
                    startActivityForResult(intent, MANAGE_FILE_ACCESS_REQ_CODE);
                } catch (Exception e) {
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                    startActivityForResult(intent, MANAGE_FILE_ACCESS_REQ_CODE);
                }
            } else {
                Intent intent = new Intent(PolishHomeActivity.this, GridPickerActivity.class);
                intent.putExtra(GridPickerActivity.KEY_LIMIT_MAX_IMAGE, 9);
                intent.putExtra(GridPickerActivity.KEY_LIMIT_MIN_IMAGE, 2);
                startActivityForResult(intent, 1001);
            }

        }else {*/

        String[] arrPermissions = {"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"};
        if (Build.VERSION.SDK_INT >= 29)arrPermissions=new String[]{"android.permission.READ_EXTERNAL_STORAGE"};
            ;

       /* if (Build.VERSION.SDK_INT < 30)
            arrPermissions = new String[]{"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"};
        else
            arrPermissions = new String[]{"android.permission.READ_EXTERNAL_STORAGE", "android.permission.MANAGE_EXTERNAL_STORAGE"};*/

            Dexter.withContext(PolishHomeActivity.this).withPermissions(arrPermissions).withListener(new MultiplePermissionsListener() {
                public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                    if (multiplePermissionsReport.areAllPermissionsGranted()) {
                        Intent intent = new Intent(PolishHomeActivity.this, GridPickerActivity.class);
                        intent.putExtra(GridPickerActivity.KEY_LIMIT_MAX_IMAGE, 9);
                        intent.putExtra(GridPickerActivity.KEY_LIMIT_MIN_IMAGE, 2);
                        startActivityForResult(intent, 1001);
                    }
                    if (multiplePermissionsReport.isAnyPermissionPermanentlyDenied()) {
                        DetailsDialog.showDetailsDialog(PolishHomeActivity.this);
                    }
                }

                public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                    permissionToken.continuePermissionRequest();
                }
            }).withErrorListener(dexterError -> Toast.makeText(PolishHomeActivity.this, "Error occurred! ", Toast.LENGTH_SHORT).show()).onSameThread().check();
       // }
    }

    public void onPostCreate(@Nullable Bundle bundle) {
        super.onPostCreate(bundle);
    }


    public void onActivityResult(int i, int i2, Intent intent) {
        if (i2 != -1) {
            super.onActivityResult(i, i2, intent);
        } else if (i == 1) {
            if (this.imageCaptureManager == null) {
                this.imageCaptureManager = new ImageCaptureManager(this);
            }
            new Handler(Looper.getMainLooper()).post(() -> PolishHomeActivity.this.imageCaptureManager.galleryAddPic());
            Intent intent2 = new Intent(getApplicationContext(), PolishEditorActivity.class);
            intent2.putExtra(PolishPickerView.KEY_SELECTED_PHOTOS, this.imageCaptureManager.getCurrentPhotoPath());
            startActivity(intent2);
        }
    }

    public void onResume() {
        super.onResume();
        if (AdsUtils.isNetworkAvailabel(getApplicationContext())) {
            if (!Preference.isRated(getApplicationContext()) && Preference.getCounter(getApplicationContext()) % 6 == 0) {
                new RateDialog(this, false).show();
            }
            Preference.increateCounter(getApplicationContext());
        }
    }

    public void takePhotoFromCamera() {
        try {
            startActivityForResult(this.imageCaptureManager.dispatchTakePictureIntent(), 1);
        } catch (IOException | ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void onDestroy() {
        super.onDestroy();
    }

    public void onBackPressed() {
        finish();
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


    public static Context getAppContext() {
        return contextApp;
    }
}
