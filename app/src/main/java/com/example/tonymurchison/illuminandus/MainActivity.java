package com.example.tonymurchison.illuminandus;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


public class MainActivity extends AppCompatActivity {


    //TODO zorgen dat muren niet uitsteken
    //TODO rode bal minder heftig maken
    //TODO timing in hidden analyseren of het goed is
    //TODO tekst vergroten
    //TODO changing mazes zorgen dat de muren niet problemen geven met de bal
    //TODO visuele indicatie van de tijd die een powerup nog actief is
    //TODO splashscreen logo
    //TODO nieuw icon
    //TODO google gaming ding toevoegen?
    //TODO social media toevoegen
    //TODO rating vragen
    //TODO zorgen dat je powerups niet kunt missen



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences prefs = this.getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("OffsetX", 0);
        editor.putInt("OffsetY", 0);

        editor.commit();



        Intent intent = new Intent(this, MainLevelSelect.class);
        startActivity(intent);
        finish();
    }



}