package com.example.tonymurchison.illuminandus;

/**
 contains completed state, best time so far, and the 'par' time
 */

public class Level{
    public boolean completed;
    float level_time;
    static float time_goal;
    static String image_source;

    public float getLevelTime(){

        return level_time;
    }

    public float getTimeGoal(){
        return time_goal;
    }

    public boolean getCompleted(){
        return completed;
    }

    public String getImageSource(){
        return image_source;
    }

    public void setLevel_time(float new_level_time){
        level_time = new_level_time;
        completed = true;
    }
}