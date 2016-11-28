package my.illuminandus;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

class Exit extends GameObjects{
    private ImageView exitImage;
    private boolean hittable = true;

    //constructor
    Exit(ImageView exit){
        exitImage = exit;
    }

    //This method sets the coordinates for the four corners
    void setCorners() {
        //top left corner
        topLeftX=(centerX-(width/2));
        topLeftY=(centerY-(height/2));

        //top right corner
        topRightX=(centerX+(width/2));
        topRightY=(centerY-(height/2));

        //bottom right corner
        bottomRightX=(centerX+(width/2));
        bottomRightY=(centerY+(height/2));

        //bottom left corner
        bottomLeftX=(centerX-(width/2));
        bottomLeftY=(centerY+(height/2));
    }

    void setVisibility(float x){
        if(x==1){
            exitImage.setVisibility(View.VISIBLE);
        }
        if(x==0){
            exitImage.setVisibility(View.INVISIBLE);
        }
    }

    void setWidth(int widthInput){
        width = widthInput;
        exitImage.requestLayout();
        exitImage.getLayoutParams().width = width;
    }

    void setHeight(int heightInput){
        height = heightInput;
        exitImage.requestLayout();
        exitImage.getLayoutParams().height = height;
    }

    void setCenter(int x, int y){
        centerX=x;
        centerY=y;
        RelativeLayout.LayoutParams alp = getLayoutParams();
        alp.leftMargin=centerX-width/2;
        alp.topMargin=centerY-height/2;
        setLayoutParams(alp);
    }

    void setHittable(boolean x){
        hittable = x;
    }

    boolean getHittable(){
        return hittable;
    }

    private RelativeLayout.LayoutParams getLayoutParams(){
        return (RelativeLayout.LayoutParams) exitImage.getLayoutParams();
    }

    private void setLayoutParams(RelativeLayout.LayoutParams alp){
        exitImage.setLayoutParams(alp);
    }
}