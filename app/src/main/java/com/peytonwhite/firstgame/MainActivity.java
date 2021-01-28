package com.peytonwhite.firstgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    private boolean isMute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);


        setContentView(R.layout.activity_main);


        findViewById(R.id.play).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,ChooseModeActivity.class));
            }
        });

        final SharedPreferences prefs = getSharedPreferences("game",MODE_PRIVATE);

        prefs.getBoolean("isMute",false);

        final ImageView volumeCtrl = findViewById(R.id.volumeCtrl);

        if(isMute)
        {
            volumeCtrl.setImageResource(R.drawable.ic_volume_mute_black_24dp);

        }
        else
        {
            volumeCtrl.setImageResource(R.drawable.ic_volume_up_black_24dp);
        }


        volumeCtrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isMute = !isMute;

                if(isMute)
                {
                    volumeCtrl.setImageResource(R.drawable.ic_volume_mute_black_24dp);

                }
                else
                {
                    volumeCtrl.setImageResource(R.drawable.ic_volume_up_black_24dp);
                }

                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean("isMute",isMute);
                editor.apply();




            }
        });

    }
}
