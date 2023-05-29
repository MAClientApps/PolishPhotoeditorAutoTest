package com.autotest.photo.editor.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.autotest.photo.editor.R;
import com.autotest.photo.editor.dialog.RateDialog;

public class PolishSettingsActivity extends AppCompatActivity {

    public LinearLayout relativeLayoutShare;
    public LinearLayout relativeLayoutRate;
    public LinearLayout relativeLayoutFeedBack;
    public LinearLayout relativeLayoutPrivacy;
    public LinearLayout relativeLayoutApps;
    private AdView adView;
    private AdRequest adRequest;



    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getWindow().setFlags(1024, 1024);
        setContentView(R.layout.activity_polish_settings);
        setSupportActionBar(findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Settings");

            //init Ads
           // MobileAds.initialize(PolishSettingsActivity.this, (OnInitializationCompleteListener) initializationStatus -> { });
            adView = findViewById(R.id.adView);
            adRequest = new AdRequest.Builder().build();
            adView.loadAd(adRequest);

        this.relativeLayoutShare = findViewById(R.id.linearLayoutShare);
        this.relativeLayoutRate = findViewById(R.id.linearLayoutRate);
        this.relativeLayoutFeedBack = findViewById(R.id.linearLayoutFeedback);
        this.relativeLayoutPrivacy = findViewById(R.id.linearLayoutPrivacy);
        this.relativeLayoutApps = findViewById(R.id.linearLayoutApps);

        this.relativeLayoutShare.setOnClickListener(view -> {
            Intent myapp = new Intent(Intent.ACTION_SEND);
            myapp.setType("text/plain");
            myapp.putExtra(Intent.EXTRA_TEXT, "Download this awesome app\n https://play.google.com/store/apps/details?id=" + getPackageName() + " \n");
            startActivity(myapp);
        });

        this.relativeLayoutRate.setOnClickListener(view -> {
            new RateDialog(PolishSettingsActivity.this, false).show();
        });

        this.relativeLayoutFeedBack.setOnClickListener(view -> {
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
            emailIntent.setData(Uri.parse("mailto:"));
            emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{getResources().getString(R.string.email_feedback)});
            emailIntent.putExtra(Intent.EXTRA_TEXT, "");
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));

            PackageManager packageManager = getPackageManager();
            boolean isIntentSafe = emailIntent.resolveActivity(packageManager) != null;
            if (isIntentSafe) {
                startActivity(emailIntent);
            } else {
                Toast.makeText(this, R.string.email_app_not_installed, Toast.LENGTH_SHORT).show();
            }
        });

        this.relativeLayoutPrivacy.setOnClickListener(view -> {

            try {
                PolishSettingsActivity.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(getString(R.string.policy_url))));
            } catch (Exception ignored) {
            }

/*
            Intent privacy = new Intent(PolishSettingsActivity.this, PolicyActivity.class);
            startActivity(privacy);

 */
        });

        this.relativeLayoutApps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("qq","moreApp");
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://search?q=pub:" + getString(R.string.developer_account_link))));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/developer?id=" + getString(R.string.developer_account_link))));
                }
            }
        });

    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == 16908332) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(menuItem);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
