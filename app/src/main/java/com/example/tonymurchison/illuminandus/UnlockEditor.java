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
        if(total_time <= tempArray[screenNumber]){
            return true;
        }
        else return false;
    }
}
