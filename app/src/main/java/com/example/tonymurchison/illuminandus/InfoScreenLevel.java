package com.example.tonymurchison.illuminandus;


import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

public class InfoScreenLevel extends AppCompatActivity {
    int levelNumber;
    String levelString[];
    String infoString[];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        levelNumber = extras.getInt("levelNumber");



        setContentView(R.layout.info_screen_level);
        TextView levelText = (TextView) findViewById(R.id.info_screen_level_level_string);
        TextView infoText = (TextView) findViewById(R.id.info_screen_level_info_string);

        if(levelNumber<7) {
            levelString = getResources().getStringArray(R.array.levelNames);
            infoString = getResources().getStringArray(R.array.infoStrings);

            levelText.setText(levelString[levelNumber]);
            infoText.setText(infoString[levelNumber]);
        }

        getSupportActionBar().hide();
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        Intent intent = new Intent(InfoScreenLevel.this, HiddenLevelSelect.class);
        startActivity(intent);
        finish();
    }

    public void infoScreenLevelClicked(View v){
        Intent intent = new Intent(InfoScreenLevel.this, LevelPlayHiddenWalls.class);
        intent.putExtra("levelNumber", levelNumber);
        startActivity(intent);
        finish();
    }






}
