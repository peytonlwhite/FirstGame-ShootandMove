package com.peytonwhite.firstgame;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ChooseModeActivity extends AppCompatActivity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);


        setContentView(R.layout.choose_game_mode);




        ///set play listeners
        TextView game1 = findViewById(R.id.textViewPlayAirplane);
        TextView game2 = findViewById(R.id.textViewPlayPeyton);

        game1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                TextView highscore = findViewById(R.id.highscoretext);

                final SharedPreferences prefs = getSharedPreferences("game",MODE_PRIVATE);

                highscore.setText("Highscore: " + prefs.getInt("highscore1",0));

                prefs.getBoolean("isMute",false);

                Intent intent = new Intent(ChooseModeActivity.this,GameActivity.class);

                intent.putExtra("game",1);
                startActivity(intent);
            }
        });

        game2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                TextView highscore = findViewById(R.id.highscoretext);

                final SharedPreferences prefs = getSharedPreferences("game",MODE_PRIVATE);

                highscore.setText("Highscore: " + prefs.getInt("highscore2",0));

                prefs.getBoolean("isMute",false);


                Intent intent = new Intent(ChooseModeActivity.this,GameActivity.class);

                intent.putExtra("game",2);
                startActivity(intent);
            }
        });








    }
}
