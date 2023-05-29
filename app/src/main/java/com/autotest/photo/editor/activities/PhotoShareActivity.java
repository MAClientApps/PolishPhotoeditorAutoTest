package com.autotest.photo.editor.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.nativead.MediaView;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.autotest.photo.editor.R;
import com.autotest.photo.editor.constants.Constants;
import com.autotest.photo.editor.dialog.RateDialog;
import com.autotest.photo.editor.picker.ImageCaptureManager;
import com.autotest.photo.editor.preference.Preference;

import java.io.File;
import java.io.IOException;

public class PhotoShareActivity extends PolishBaseActivity implements View.OnClickListener {
    private File file;
     NativeAd nativeAds;
    ImageCaptureManager createImageFile1;
    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        setFullScreen();
        setContentView((int) R.layout.activity_share_photo);
        createImageFile1=new ImageCaptureManager(this);

            loadNativeAd();


        String string = getIntent().getExtras().getString("path");
        this.file = new File(string);

        Glide.with(getApplicationContext()).load(this.file).into((ImageView) findViewById(R.id.image_view_preview));
        findViewById(R.id.image_view_preview).setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                PhotoShareActivity.this.onClick(view);
            }
        });

        findViewById(R.id.imageViewBack).setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                onBackPressed();
            }
        });

        findViewById(R.id.imageViewHome).setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            public final void onClick(View view) {
                Intent intent = new Intent(PhotoShareActivity.this, PolishHomeActivity.class);
                intent.setFlags(67108864);
                startActivity(intent);
                finish();
            }
        });
        if (!Preference.isRated(this)) {
            new RateDialog(this, false).show();
        }
    }

    private void loadNativeAd()
    {
        AdLoader.Builder builder = new AdLoader.Builder(this, getString(R.string.native_ads))
                .forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
                    @Override
                    public void onNativeAdLoaded(@NonNull NativeAd nativeAd)
                    {
                        if (nativeAds != null)
                            nativeAds = nativeAd;


                        FrameLayout frameLayout = findViewById(R.id.ad_container);
                        NativeAdView adView;
                        try {
                            adView = (NativeAdView ) getLayoutInflater().inflate(R.layout.native_ad_layout, null);
                            populateNativeAd(nativeAd, adView);
                            frameLayout.removeAllViews();
                            frameLayout.addView(adView);
                        }catch (Exception e)
                        {
                            Toast.makeText(PhotoShareActivity.this, "Failed to load Ad.", Toast.LENGTH_SHORT).show();
                        }


                    }

                });
        AdLoader adLoader = builder.withAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(LoadAdError adError)
            {
                // Handle the failure by logging, altering the UI, and so on.
                Toast.makeText(PhotoShareActivity.this, "Failed to load Ad.", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onAdClicked() {
                // Log the click event or other custom behavior.
            }
        }).build();
        adLoader.loadAd(new AdRequest.Builder().build());
    }


    public void onDestroy() {
        super.onDestroy();
    }


    @SuppressLint("WrongConstant")
    public void startAcitivity(PhotoShareActivity saveAndShareActivity, View view) {
        Intent intent = new Intent("android.intent.action.SEND");
        intent.setType("image/*");
        intent.putExtra("android.intent.extra.STREAM", FileProvider.getUriForFile(saveAndShareActivity.getApplicationContext(), getResources().getString(R.string.file_provider), saveAndShareActivity.file));
        intent.addFlags(3);
        saveAndShareActivity.startActivity(Intent.createChooser(intent, "Share"));
    }

    public void onResume() {
        super.onResume();
    }

    @SuppressLint("WrongConstant")
    public void onClick(View view) {
        if (view != null) {
            int id = view.getId();
            if (id != R.id.image_view_preview) {
                switch (id) {
                    case R.id.linearLayoutShareOne:
//                        startAcitivity(PhotoShareActivity.this, view);
                        return;
                    case R.id.linear_layout_facebook:
//                        sharePhoto(Constants.FACEBOOK);
                        sharephotosocialplateforms(Uri.parse(file.getAbsolutePath().toString()),Constants.FACEBOOK);
                        return;
                    case R.id.linear_layout_instagram:
//                        sharePhoto(Constants.INSTAGRAM);
                        sharephotosocialplateforms(Uri.parse(file.getAbsolutePath().toString()),Constants.INSTAGRAM);
                        return;
                    case R.id.linear_layout_messenger:
//                        sharePhoto(Constants.MESSEGER);
                        sharephotosocialplateforms(Uri.parse(file.getAbsolutePath().toString()),Constants.MESSEGER);
                        return;
                    case R.id.linear_layout_share_more:
                        Uri createCacheFile = createCacheFile();
                        if (createCacheFile != null) {
                            Intent intent = new Intent();
                            intent.setAction("android.intent.action.SEND");
                            intent.addFlags(1);
                            intent.setDataAndType(createCacheFile, getContentResolver().getType(createCacheFile));
                            intent.putExtra("android.intent.extra.STREAM", createCacheFile);
                            startActivity(Intent.createChooser(intent, "Choose an app"));
                            return;
                        }
                        Toast.makeText(this, "Fail to sharing", 0).show();
                        return;
                    default:
                        switch (id) {
                            case R.id.linear_layout_twitter:
//                                sharePhoto(Constants.TWITTER);
                                sharephotosocialplateforms(Uri.parse(file.getAbsolutePath().toString()),Constants.TWITTER);
                                return;
                            case R.id.linear_layout_whatsapp:
//                                sharePhoto(Constants.WHATSAPP);
                                sharephotosocialplateforms(Uri.parse(file.getAbsolutePath().toString()),Constants.WHATSAPP);
                                return;
                            default:
                                return;
                        }
                }
            } else {
                Intent intent4 = new Intent();
                intent4.setAction("android.intent.action.VIEW");
                intent4.setDataAndType(FileProvider.getUriForFile(getApplicationContext(), getResources().getString(R.string.file_provider), this.file), "image/*");
                intent4.addFlags(3);
                startActivity(intent4);
            }
        }
    }

    public void sharephotosocialplateforms(Uri contentUri,String packagestr){

        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        // temp permission for receiving app to read this file
        shareIntent.setDataAndType(contentUri, getContentResolver().getType(contentUri));
        shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
        shareIntent.setType("image/*");
        shareIntent.setPackage(packagestr);
        if(packagestr.equals("com.instagram.android")){
            shareIntent.setType("image/jpeg");

        }
        startActivity(shareIntent);
    }

    @SuppressLint("WrongConstant")
    public void sharePhoto(String str) {
        if (isPackageInstalled(this, str)) {
            Uri createCacheFile = createCacheFile();
            if (createCacheFile != null) {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.SEND");
                intent.addFlags(1);
                intent.setDataAndType(createCacheFile, getContentResolver().getType(createCacheFile));
                intent.putExtra("android.intent.extra.STREAM", createCacheFile);
                intent.setPackage(str);
                startActivity(intent);
                return;
            }
            Toast.makeText(this, "Fail to sharing", 0).show();
            return;
        }
        Toast.makeText(this, "Can't find this App, please download and try it again", 0).show();
        Intent intent2 = new Intent("android.intent.action.VIEW");
        intent2.setData(Uri.parse("market://details?id=" + str));
        startActivity(intent2);
    }

    @SuppressLint("WrongConstant")
    public static boolean isPackageInstalled(Context context, String str) {
        try {
            context.getPackageManager().getPackageInfo(str, 128);
            return true;
        } catch (PackageManager.NameNotFoundException unused) {
            return false;
        }
    }

    private Uri createCacheFile() {
//        return FileProvider.getUriForFile(getApplicationContext(), getResources().getString(R.string.file_provider), this.file);
        File createImageFile = null;
        try {
            createImageFile = createImageFile1.createImageFile();
            return Uri.fromFile(new File(createImageFile.getAbsolutePath()));
        } catch (IOException e) {
            e.printStackTrace();
        }
//        return FileProvider.getUriForFile(this.getApplicationContext().getApplicationContext(), this.getApplicationContext().getApplicationInfo().packageName + ".provider", this.file.getAbsoluteFile());
//   return FileProvider.getUriForFile(this.getApplicationContext().getApplicationContext(), this.getApplicationContext().getApplicationInfo().packageName + ".provider",createImageFile);
        return null;

    }
    private void populateNativeAd(NativeAd nativeAd, NativeAdView adView)
    {
        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
        adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));
        adView.setBodyView(adView.findViewById(R.id.ad_body_text));
        adView.setStarRatingView(adView.findViewById(R.id.star_rating));
        adView.setMediaView((MediaView) adView.findViewById(R.id.media_view));
        adView.setIconView(adView.findViewById(R.id.adv_icon));

        adView.getMediaView().setMediaContent(nativeAd.getMediaContent());
        ((TextView)adView.getHeadlineView()).setText(nativeAd.getHeadline());

        if (nativeAd.getBody() == null)
            adView.getBodyView().setVisibility(View.INVISIBLE);
        else
        {
            ((TextView)adView.getBodyView()).setText(nativeAd.getBody());
            adView.getBodyView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getAdvertiser() == null)
            adView.getAdvertiserView().setVisibility(View.INVISIBLE);
        else
        {
            ((TextView)adView.getAdvertiserView()).setText(nativeAd.getAdvertiser());
            adView.getAdvertiserView().setVisibility(View.VISIBLE);
        }
        if (nativeAd.getStarRating() == null)
            adView.getStarRatingView().setVisibility(View.INVISIBLE);
        else
        {
            ((RatingBar)adView.getStarRatingView()).setRating(nativeAd.getStarRating().floatValue());
            adView.getStarRatingView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getIcon() == null)
            adView.getIconView().setVisibility(View.INVISIBLE);
        else
        {
            ((ImageView)adView.getIconView()).setImageDrawable(nativeAd.getIcon().getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }
        adView.setNativeAd(nativeAd);

    }

}
