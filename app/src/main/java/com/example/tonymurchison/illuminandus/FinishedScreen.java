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
        SharedPreferences prefs = this.getSharedPreferences("myPrefs", Context.MODE_PRIVATE);

        if(gameType.equals("hidden")){
            if(prefs.getInt("hiddenLevelProgress",0)<levelNumber+1){
                SharedPreferences.Editor editor = prefs.edit();
                editor.putInt("hiddenLevelProgress", levelNumber+1);
                editor.commit();
            }
        }

        if(gameType.equals("changing")){
            if(prefs.getInt("changingLevelProgress",0)<levelNumber+1){
                SharedPreferences.Editor editor = prefs.edit();
                editor.putInt("changingLevelProgress", levelNumber+1);
                editor.commit();
            }
        }

        if(gameType.equals("locked")){
            if(prefs.getInt("lockedLevelProgress",0)<levelNumber+1){
                SharedPreferences.Editor editor = prefs.edit();
                editor.putInt("lockedLevelProgress", levelNumber+1);
                editor.commit();
            }
        }

        if(gameType.equals("normal")){
            if(prefs.getInt("normalLevelProgress",0)<levelNumber+1){
                SharedPreferences.Editor editor = prefs.edit();
                editor.putInt("normalLevelProgress", levelNumber+1);
                editor.commit();
            }
        }





    }

    public void nextLevelButton(View v){
        Intent intent = new Intent();
        if(gameType.equals("hidden")){
            if(levelNumber<6){
                intent = new Intent(FinishedScreen.this, InfoScreenLevel.class);
            }
            else {
                intent = new Intent(FinishedScreen.this, LevelPlayHiddenWalls.class);
            }
        }

        if(gameType.equals("changing")){
            intent = new Intent(FinishedScreen.this, LevelPlayChangingWalls.class);
        }

        if(gameType.equals("locked")){
            intent = new Intent(FinishedScreen.this, LevelPlayKeys.class);
        }

        if(gameType.equals("normal")){
            intent = new Intent(FinishedScreen.this, LevelPlayNormalWalls.class);
        }

        intent.putExtra("levelNumber", levelNumber+1);
        startActivity(intent);
        finish();

    }

    public void restartLevelButton(View v){
        Intent intent = new Intent();
        if(gameType.equals("hidden")){
            if(levelNumber<7){
                intent = new Intent(FinishedScreen.this, InfoScreenLevel.class);
            }
            else {
                intent = new Intent(FinishedScreen.this, LevelPlayHiddenWalls.class);
            }
        }

        if(gameType.equals("changing")){
            intent = new Intent(FinishedScreen.this, LevelPlayChangingWalls.class);
        }

        if(gameType.equals("locked")){
            intent = new Intent(FinishedScreen.this, LevelPlayKeys.class);
        }

        if(gameType.equals("normal")){
            intent = new Intent(FinishedScreen.this, LevelPlayNormalWalls.class);
        }

        intent.putExtra("levelNumber", levelNumber);
        startActivity(intent);
        finish();
    }

    public void returnToLevelSelect(View v){
        Intent intent = new Intent();
        if(gameType.equals("hidden")){
            intent = new Intent(FinishedScreen.this, HiddenLevelSelect.class);
        }

        if(gameType.equals("changing")){
            intent = new Intent(FinishedScreen.this, ChangingLevelSelect.class);
        }

        if(gameType.equals("locked")){
            intent = new Intent(FinishedScreen.this, LockedLevelSelect.class);
        }

        if(gameType.equals("normal")){
            intent = new Intent(FinishedScreen.this, NormalLevelSelect.class);
        }

        startActivity(intent);
        finish();
    }
}