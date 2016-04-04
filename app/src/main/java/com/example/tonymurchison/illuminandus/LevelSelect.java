package com.example.tonymurchison.illuminandus;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class LevelSelect extends AppCompatActivity {

    private TextView[] time_array = new TextView[4];    //Levels' corresponding time TextViews
    private ImageView[] level_array = new ImageView[4]; //Levels' corresponding ImageViews
    private ImageView loadingBackground;
    private ImageView loadingBar;
    private TextView time_goal;
    private TextView time_scored;
    private ImageView next_button;
    private ImageView previous_button;
    private int screenNumber;
    private boolean unlockedstate = false;
    private boolean backUnlocked = false;
    private boolean debug_timer = false;    //if true, always allows access to all levels
    private float x1, x2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_select);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        previous_button = (ImageView) findViewById(R.id.previous_button);
        next_button = (ImageView) findViewById(R.id.next_button);
        level_array[0] = (ImageView) findViewById(R.id.first_level_select);
        level_array[1] = (ImageView) findViewById(R.id.second_level_select);
        level_array[2] = (ImageView) findViewById(R.id.third_level_select);
        level_array[3] = (ImageView) findViewById(R.id.fourth_level_select);
        time_array[0] = (TextView) findViewById(R.id.first_level_time);
        time_array[1] = (TextView) findViewById(R.id.second_level_time);
        time_array[2] = (TextView) findViewById(R.id.third_level_time);
        time_array[3] = (TextView) findViewById(R.id.fourth_level_time);
        time_goal = (TextView) findViewById(R.id.time_needed);
        time_scored = (TextView) findViewById(R.id.time_scored);
        loadingBackground = (ImageView) findViewById(R.id.loadingBackground);
        loadingBar = (ImageView) findViewById(R.id.loadingBar);

        for(int i=0; i < 4; i++) {
            level_array[i].setTag(i);
            level_array[i].setClickable(true);
        }

        Bundle extras = getIntent().getExtras();                //Receives levelNumber from played level, and returns view to corresponding four levels.
        if(extras == null || extras.getInt("levelNumber") == 0){
            screenNumber = getPreviousScreenNumber();
        }
        else{
            screenNumber = extras.getInt("levelNumber") / 4;
        }

        updateLevels(screenNumber);
        setUnlock();
        }

    private void setUnlock(){       //checks whether next four are available
        HighScoreEditor highScoreEditor = new HighScoreEditor();
        if(screenNumber == 0){
            backUnlocked = false;
            previous_button.setVisibility(View.INVISIBLE);
        }
        else{
            backUnlocked = true;
            previous_button.setVisibility(View.VISIBLE);
        }
        unlockedstate = true;
        next_button.setImageResource(R.drawable.button_next);

        for(int i = 4 * screenNumber; i < (4 * screenNumber + 4); i++){
            int localHighScore = highScoreEditor.getValue(this, "HighScore_" + i);
            if(debug_timer) localHighScore = 1000;
            if (localHighScore == 0){
                time_array[i % 4].setText("");
            }
            else{
                time_array[i % 4].setText(secondsToMinutes(localHighScore));
            }
        }

        UnlockEditor unlockEditor = new UnlockEditor();
        if(unlockEditor.requestUnlock(this, screenNumber) == false){
            setNextButtonLocked();
        }

        if(unlockEditor.requestTotalTime(this, screenNumber) > 0){
            time_scored.setText(secondsToMinutes(unlockEditor.requestTotalTime(this, screenNumber)));
        }
        else{
            time_scored.setText("");
        }
    }

    public void onNextButtonClick(View view){
        if(unlockedstate) {
            screenNumber++;
            updateLevels(screenNumber);
            setUnlock();
        }
        else {
            Toast toast = Toast.makeText(getApplicationContext(), "Levels not yet unlocked", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public void onPreviousButtonClick(View view){
        if(backUnlocked) {
            screenNumber--;
            updateLevels(screenNumber);
            setUnlock();
        }
        return;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x1 = event.getX();
                return false;
            case MotionEvent.ACTION_UP:
                x2 = event.getX();
                float deltaX = x2 - x1;
                if (deltaX > 150){
                    onPreviousButtonClick(previous_button);
                    return false;
                }
                if (deltaX < -150){
                    onNextButtonClick(next_button);
                    return false;
                }
                else return true;
        }
        return super.onTouchEvent(event);
    }

    private void updateLevels(int screenNumber){
        HighScoreEditor highScoreEditor = new HighScoreEditor();
        for(int i = screenNumber * 4; i < screenNumber * 4 + 4; i++){ //Sets images to the correct source in a horrible way
            if(highScoreEditor.getValue(this, "HighScore_" + i) == 0) {
                int id = getResources().getIdentifier("locked_level_" + i, "drawable", getPackageName());
                level_array[i -screenNumber * 4].setImageResource(id);
            }
            else{
                int id = getResources().getIdentifier("unlocked_level_" + i, "drawable", getPackageName());
                level_array[i -screenNumber * 4].setImageResource(id);
                time_array[i -screenNumber * 4].setText(secondsToMinutes(highScoreEditor.getValue(this, "HighScore_" + i)));
            }
        }
        setGoalTime(screenNumber);
    }

    private int setGoalTime(int screenNumber) {      //sets goaltime for any screenNumber, then returns said time in milliseconds
        int tempArray[] = getResources().getIntArray(R.array.parTime);
        int totalGoalTime = 0;
        for(int i = screenNumber * 4; i < screenNumber * 4 + 4; i++){
            totalGoalTime = totalGoalTime + tempArray[i];
        }
        time_goal.setText(secondsToMinutes(totalGoalTime));
        return totalGoalTime;
    }

    public void onLevelClick(View v){
        for(int i=0;i<4;i++){
            level_array[i].setClickable(false);
        }

        //TODO fix glitching loading bar!
        loadingBar.setVisibility(View.VISIBLE);
        loadingBackground.setVisibility(View.VISIBLE);

        int viewNumber = (Integer) v.getTag();
        int levelNumber = viewNumber + screenNumber * 4;
        Intent intent = new Intent(LevelSelect.this, LevelPlay.class);
        intent.putExtra("levelNumber", levelNumber);
        startActivity(intent);
        finish();
    }

    private String secondsToMinutes(int numberOfSeconds){
        int timeMinutes = (numberOfSeconds / (1000 * 60)) % 60;
        String time_minutes;
        String time_seconds;
        if(timeMinutes < 10){
            time_minutes = "0" + timeMinutes;
        }
        else {
            time_minutes = "" + timeMinutes;
        }
        int timeSeconds = ((numberOfSeconds - (timeMinutes * 60 * 1000)) / 1000) % 60;
        if(timeSeconds < 10){
            time_seconds = "0" + timeSeconds;
        }
        else {
            time_seconds = "" + timeSeconds;
        }
        return time_minutes + " : " + time_seconds;
    }

    private void setNextButtonLocked(){
        next_button.setImageResource(R.drawable.button_next_locked);
        unlockedstate = false;
    }

    private int getPreviousScreenNumber(){
        HighScoreEditor highScoreEditor = new HighScoreEditor();
        int last_level = 0;
        while(true){
            if(highScoreEditor.getValue(this, "HighScore_" + last_level) == 0){
                return (last_level / 4);
            }
            last_level++;
        }
    }

    public void infoButtonClick(View v){
        Intent intent = new Intent(LevelSelect.this, InformationScreen.class);
        startActivity(intent);
        finish();
    }
}
