package my.illuminandus;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.ads.MobileAds;


public class MainActivity extends AppCompatActivity {



    //TODO echte advertenties laden
    //TODO ballen misschien beter zichtbaar maken
    //TODO icon ronde hoeken geven




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences prefs = this.getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("OffsetX", 0);
        editor.putInt("OffsetY", 0);

        editor.commit();

        MobileAds.initialize(getApplicationContext(), "ca-app-pub-9044449755774524~3454029098");



        Intent intent = new Intent(this, MainLevelSelect.class);
        startActivity(intent);
        finish();
    }



}