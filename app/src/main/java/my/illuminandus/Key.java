package my.illuminandus;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;


class Key extends GameObjects {
    private float opacity;
    private ImageView keyImage;
    private int type;
    private boolean hittable =false;

    Key(int typeInput, ImageView key){
        type = typeInput;
        keyImage = key;
    }

    void setHittable(boolean x){
        hittable = x;
    }


    boolean getHittable(){
        return hittable;
    }

    int returnType(){
        return type;
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
            keyImage.setVisibility(View.INVISIBLE  );
        }
        else {
            keyImage.setVisibility(View.VISIBLE);
        }
    }

    void setWidth(int widthInput) {
        width = widthInput;
        keyImage.requestLayout();
        keyImage.getLayoutParams().width = width;
    }

    void setHeight(int heightInput){
        height = heightInput;
        keyImage.requestLayout();
        keyImage.getLayoutParams().height = height;
    }

    void setCenter(int x, int y){
        centerX=x;
        centerY=y;
        RelativeLayout.LayoutParams alp = getLayoutParams();
        alp.leftMargin=centerX-width/2;
        alp.topMargin=centerY-height/2;
        setLayoutParams(alp);

    }



    RelativeLayout.LayoutParams getLayoutParams(){
        return (RelativeLayout.LayoutParams) keyImage.getLayoutParams();
    }

    void setLayoutParams(RelativeLayout.LayoutParams alp){
        keyImage.setLayoutParams(alp);
    }


}
