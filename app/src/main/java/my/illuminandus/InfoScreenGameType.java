package my.illuminandus;


import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;


public class InfoScreenGameType extends AppCompatActivity {
    int levelNumber;
    String gameType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        gameType = extras.getString("gameType");



        setContentView(R.layout.info_gametype);
        TextView gameTypeText = (TextView) findViewById(R.id.gameTypeTextView);
        TextView explainText = (TextView) findViewById(R.id.explenationTextView);

        String[] explenation = getResources().getStringArray(R.array.explenation);

        if(gameType.equals("hidden")){
            gameTypeText.setBackground(getResources().getDrawable(R.drawable.button_hidden));
            gameTypeText.setText("HIDDEN MAZE");
            explainText.setText(explenation[0]);
        }

        if(gameType.equals("changing")){
            gameTypeText.setBackground(getResources().getDrawable(R.drawable.button_changing));
            gameTypeText.setText("CHANGING MAZE");
            explainText.setText(explenation[1]);
        }

        if(gameType.equals("locked")){
            gameTypeText.setBackground(getResources().getDrawable(R.drawable.button_locked));
            gameTypeText.setText("LOCKED MAZE");
            explainText.setText(explenation[2]);
        }

        if(gameType.equals("normal")){
            gameTypeText.setBackground(getResources().getDrawable(R.drawable.button_normal));
            gameTypeText.setText("NORMAL MAZE");
            explainText.setText(explenation[3]);
        }



        getSupportActionBar().hide();
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        Intent intent = new Intent(InfoScreenGameType.this, MainLevelSelect.class);
        startActivity(intent);
        finish();
    }

    public void infoScreenGameTypeClicked(View v){
        Intent intent = new Intent();

        if(gameType.equals("hidden")){
            intent = new Intent(InfoScreenGameType.this, LevelSelectHidden.class);
        }
        if(gameType.equals("changing")){
            intent = new Intent(InfoScreenGameType.this, LevelPlayChangingWalls.class);
        }
        if(gameType.equals("locked")){
            intent = new Intent(InfoScreenGameType.this, LevelSelectLocked.class);
        }

        if(gameType.equals("normal")){
            intent = new Intent(InfoScreenGameType.this, LevelSelectNormal.class);
        }

        startActivity(intent);
        finish();
    }






}
