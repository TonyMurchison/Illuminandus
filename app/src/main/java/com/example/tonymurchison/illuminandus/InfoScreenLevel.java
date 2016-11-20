package com.example.tonymurchison.illuminandus;


import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
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

        levelString = getResources().getStringArray(R.array.levelNames);
        infoString = getResources().getStringArray(R.array.infoStrings);

        setContentView(R.layout.info_screen_level);
        TextView levelText = (TextView) findViewById(R.id.info_screen_level_level_string);
        TextView infoText = (TextView) findViewById(R.id.info_screen_level_info_string);

        levelText.setText(levelString[levelNumber]);
        infoText.setText(infoString[levelNumber]);

        getSupportActionBar().hide();
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


    }



    public void infoScreenLevelClicked(View v){
        Intent intent = new Intent(InfoScreenLevel.this, LevelPlay2.class);
        intent.putExtra("levelNumber", levelNumber);
        startActivity(intent);
        finish();
    }






}
