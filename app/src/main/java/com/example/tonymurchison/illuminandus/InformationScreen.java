package com.example.tonymurchison.illuminandus;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

public class InformationScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_screen);

        //getSupportActionBar().hide();
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


    }



    public void onBackPressed(){
        super.onBackPressed();
        Intent intent = new Intent(InformationScreen.this, LevelSelect.class);
        startActivity(intent);
        finish();
    }


    public void returnClick(View view) {
        Intent intent = new Intent(InformationScreen.this, LevelSelect.class);
        startActivity(intent);
        finish();
    }
}
