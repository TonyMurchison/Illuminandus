package com.example.tonymurchison.illuminandus;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class FinishedScreen extends AppCompatActivity {
    private AdView mAdView;
    private int levelNumber;
    private String gameType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.finished_screen);
        getSupportActionBar().hide();
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        Bundle extras = getIntent().getExtras();
        levelNumber = extras.getInt("levelNumber");
        gameType = extras.getString("GameType");

        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        unlockLevel();

    }

    private void unlockLevel(){
        if(gameType.equals("hidden")){
            SharedPreferences prefs = this.getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("hiddenLevelProgress", levelNumber+1);
            editor.commit();
        }
    }

    public void returnToLevelSelect(View v){
        Intent intent = new Intent();
        if(gameType.equals("hidden")){
            intent = new Intent(FinishedScreen.this, HiddenLevelSelect.class);
        }

        startActivity(intent);
        finish();
    }
}
