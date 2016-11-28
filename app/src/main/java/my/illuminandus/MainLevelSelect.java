package my.illuminandus;


import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.tjeannin.apprate.AppRate;

public class MainLevelSelect extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_level_select);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        new AppRate(this).setShowIfAppHasCrashed(false).setMinLaunchesUntilPrompt(2).init();
    }




    public void mainLevelSelectButtonClick(View v){
        String buttonPressed = (String)v.getTag();
        Intent intent = new Intent(MainLevelSelect.this, InfoScreenGameType.class);
        intent.putExtra("gameType", buttonPressed);
        startActivity(intent);
        finish();
    }






}
