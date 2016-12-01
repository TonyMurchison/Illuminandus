package my.illuminandus;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {


    //TODO splashscreen logo
    //TODO nieuw icon
    //TODO levels maken
    //TODO muren goed uitlijnen




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