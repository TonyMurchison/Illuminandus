package com.example.tonymurchison.illuminandus;


import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class MainLevelSelect extends AppCompatActivity {
    private AdView mAdView;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_level_select);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    public void mainLevelSelectButtonClick(View v){
        String buttonPressed = (String)v.getTag();
        Intent intent = new Intent();

        if(buttonPressed.equals("hidden")){
            intent = new Intent(MainLevelSelect.this, HiddenLevelSelect.class);
        }
        if(buttonPressed.equals("changing")){
            intent = new Intent(MainLevelSelect.this, LevelPlayChangingWalls.class);
        }
        if(buttonPressed.equals("locked")){
            intent = new Intent(MainLevelSelect.this, LevelPlayKeys.class);
        }

        startActivity(intent);
        finish();
    }






}
