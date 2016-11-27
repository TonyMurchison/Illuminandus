package com.example.tonymurchison.illuminandus;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

class Wall extends GameObjects {
    private float opacity;
    private ImageView wallImage;
    int timeTouched=0;
    boolean hittable;
    int special=0;
    Context context;

    //constructor
    Wall(ImageView wall){

        wallImage = wall;

    }

    void setHittable(boolean x){
        hittable = x;
    }


    boolean getHittable(){
        return hittable;
    }

    void setSpecial(int i){
        special = i;
    }

    int getSpecial(){
        return special;
    }

    void setColor(Context c){
        context = c;
        if(special==1) {
            wallImage.setBackgroundColor(context.getResources().getColor(R.color.greenKey));
        }
        if(special==2) {
            wallImage.setBackgroundColor(context.getResources().getColor(R.color.blueKey));
        }
        if(special==3) {
            wallImage.setBackgroundColor(context.getResources().getColor(R.color.redKey));
        }
    }

    //This method sets the coordinates for the four corners
    void setCorners() {
        if(width>height){
            //top left corner
            topLeftX=(centerX-(width/2)+2);
            topLeftY=(centerY-(height/2));

            //top right corner
            topRightX=(centerX+(width/2)-2);
            topRightY=(centerY-(height/2));

            //bottom right corner
            bottomRightX=(centerX+(width/2)-2);
            bottomRightY=(centerY+(height/2));

            //bottom left corner
            bottomLeftX=(centerX-(width/2)+2);
            bottomLeftY=(centerY+(height/2));
        }

        else{
            //top left corner
            topLeftX=(centerX-(width/2));
            topLeftY=(centerY-(height/2)+2);

            //top right corner
            topRightX=(centerX+(width/2));
            topRightY=(centerY-(height/2)+2);

            //bottom right corner
            bottomRightX=(centerX+(width/2));
            bottomRightY=(centerY+(height/2)-2);

            //bottom left corner
            bottomLeftX=(centerX-(width/2)+2);
            bottomLeftY=(centerY+(height/2)-2);
        }
    }

    float getVisility(){
        return opacity;
    }

    void setVisibility(float i){
        opacity=i;
        if(i==0){
            wallImage.setVisibility(View.INVISIBLE  );
        }
        else {
            wallImage.setVisibility(View.VISIBLE);
        }
    }

    void setWidth(int widthInput) {
        width = widthInput;
        wallImage.requestLayout();
        wallImage.getLayoutParams().width = width;
    }

    void setHeight(int heightInput){
        height = heightInput;
        wallImage.requestLayout();
        wallImage.getLayoutParams().height = height;
    }

    void setCenter(int x, int y){
        centerX=x;
        centerY=y;
        RelativeLayout.LayoutParams alp = getLayoutParams();
        alp.leftMargin=centerX-width/2;
        alp.topMargin=centerY-height/2;
        setLayoutParams(alp);

    }

    void setTimeTouched(int timeInput){
        timeTouched=timeInput;
    }

    int getTimeTouched(){
        return timeTouched;
    }

    RelativeLayout.LayoutParams getLayoutParams(){
        return (RelativeLayout.LayoutParams) wallImage.getLayoutParams();
    }

    void setLayoutParams(RelativeLayout.LayoutParams alp){
        wallImage.setLayoutParams(alp);
    }
}