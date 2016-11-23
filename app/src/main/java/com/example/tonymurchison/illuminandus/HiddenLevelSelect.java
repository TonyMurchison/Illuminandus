package com.example.tonymurchison.illuminandus;


import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;

public class HiddenLevelSelect extends AppCompatActivity {
    private AdView mAdView;

    private Button[] buttons = new Button[30];
    private int levelsAmount = 30;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hidden_level_select);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        final LinearLayout layout = (LinearLayout)findViewById(R.id.hiddenWallsLayout);
        ViewTreeObserver vto = layout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                layout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                setButtons();

            }
        });

        for(int i=0;i<levelsAmount;i++){
            int id = getResources().getIdentifier("Button_" + (i+1), "id", getPackageName());
            buttons[i] = (Button)findViewById(id);
        }


    }

    private void setButtons(){
        for(int i=0;i<levelsAmount;i++){
            buttons[i].setHeight(buttons[i].getWidth());
        }
    }

    public void onButtonClick(View v){
        int buttonPressed = Integer.parseInt((String)v.getTag());
        Intent intent = new Intent(HiddenLevelSelect.this, LevelPlayHiddenWalls.class);
        intent.putExtra("levelNumber", buttonPressed-1);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        Intent intent = new Intent(HiddenLevelSelect.this, MainLevelSelect.class);
        startActivity(intent);
        finish();
    }





}
