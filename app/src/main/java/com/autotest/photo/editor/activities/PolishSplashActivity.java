 package com.autotest.photo.editor.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.appcompat.app.AppCompatActivity;

import com.autotest.photo.editor.R;

 public class PolishSplashActivity extends AppCompatActivity{


     public void onCreate(Bundle bundle) {
         super.onCreate(bundle);
         getWindow().setFlags(1024, 1024);
         setContentView(R.layout.activity_polish_splash);
         Animation animation = AnimationUtils.loadAnimation(this, R.anim.enter_from_bottom);
         Animation animation1 = AnimationUtils.loadAnimation(this, R.anim.enter_from_bottom_delay);
         findViewById(R.id.imageViewLogo).startAnimation(animation);
         findViewById(R.id.textViewTitle).startAnimation(animation1);
         new Handler(Looper.getMainLooper()).postDelayed(() -> {
             startToMainActivity();
         }, 2000);

     }

     public void onDestroy() {
         super.onDestroy();
         Runtime.getRuntime().gc();
     }

     public void startToMainActivity() {
         startActivity(new Intent(this, PolishHomeActivity.class));
         finish();
     }

 }
