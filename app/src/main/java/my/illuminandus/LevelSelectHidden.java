package my.illuminandus;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;


public class LevelSelectHidden extends AppCompatActivity {
    private Button[] buttons = new Button[60];
    private int levelsAmount = 60;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hidden_level_select);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

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
        SharedPreferences prefs = this.getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        int unlockedLevel = prefs.getInt("hiddenLevelProgress", 0);

        if(unlockedLevel==0){
            for(int i=1;i<levelsAmount;i++){
                buttons[i].setBackground(getResources().getDrawable(R.drawable.layout_button_level_select_locked));
                buttons[i].setClickable(false);
            }
        }
        else{
            if(unlockedLevel!=levelsAmount-1){
                for(int i=unlockedLevel+1;i<levelsAmount;i++){
                    buttons[i].setBackground(getResources().getDrawable(R.drawable.layout_button_level_select_locked));
                    buttons[i].setClickable(false);
                }
            }
        }


        for(int i=0;i<levelsAmount;i++){
            buttons[i].setHeight(buttons[i].getWidth());
        }
    }

    public void onButtonClickHiddenLevels(View v){
        int buttonPressed = Integer.parseInt((String)v.getTag());
        Intent intent;
        if(buttonPressed<6){
            intent = new Intent(LevelSelectHidden.this, InfoScreenLevel.class);
        }
        else {
            intent = new Intent(LevelSelectHidden.this, LevelPlayHiddenWalls.class);
        }

        intent.putExtra("levelNumber", buttonPressed-1);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        Intent intent = new Intent(LevelSelectHidden.this, MainLevelSelect.class);
        startActivity(intent);
        finish();
    }





}
