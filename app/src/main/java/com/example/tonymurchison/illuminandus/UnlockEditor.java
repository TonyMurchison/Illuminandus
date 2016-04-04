package com.example.tonymurchison.illuminandus;

import android.content.Context;

import java.util.ArrayList;

/**
 * An editor that allows LevelPlay and LevelSelect access to a list of available levels
 */

public class UnlockEditor{

    private Context c;

    public boolean requestUnlock(Context c, int screenNumber){
        this.c = c;
        HighScoreEditor highScoreEditor = new HighScoreEditor();
        int total_time = 0;
        int total_goal_time = 0;

        for(int i = 4 * screenNumber; i < (screenNumber * 4 + 4); i++){
            int local_value = highScoreEditor.getValue(this.c, "HighScore_" + i);
            if(local_value == 0){
                return false;
            }
            else {
                total_time = total_time + local_value;
            }
        }
        int tempArray[] = this.c.getResources().getIntArray(R.array.parTime);
        for(int i = 4 * screenNumber; i < (screenNumber * 4 + 4); i++){
            total_goal_time = total_goal_time + tempArray[i];
        }
        if(total_time <= total_goal_time){
            return true;
        }
        else return false;
    }

    public int requestTotalTime(Context c, int screenNumber){
        this.c = c;
        HighScoreEditor highScoreEditor = new HighScoreEditor();
        int total_time = 0;

        for(int i = 4 * screenNumber; i < (screenNumber * 4 + 4); i++) {
            int local_value = highScoreEditor.getValue(this.c, "HighScore_" + i);
            total_time = total_time + local_value;
        }

        return total_time;
    }
}
