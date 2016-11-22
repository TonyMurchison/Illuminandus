package com.example.tonymurchison.illuminandus;


import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class LevelSelectV2 extends AppCompatActivity {
    private AdView mAdView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.level_select_v2);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    public void onButtonClick(View v){
        int buttonNumber = Integer.parseInt((String)v.getTag());

        Intent intent = new Intent(LevelSelectV2.this, InfoScreenLevel.class);
        intent.putExtra("levelNumber", buttonNumber-1);
        startActivity(intent);
        finish();
    }

    public void onNewModeClick(View v){
        int buttonNumber = 1;
        Intent intent = new Intent(LevelSelectV2.this, LevelPlayChangingWalls.class);
        intent.putExtra("levelNumber", buttonNumber-1);
        startActivity(intent);
        finish();
    }

    public void onNewMode2Click(View v){
        int buttonNumber = 1;
        Intent intent = new Intent(LevelSelectV2.this, LevelPlayKeys.class);
        intent.putExtra("levelNumber", buttonNumber-1);
        startActivity(intent);
        finish();
    }




}
