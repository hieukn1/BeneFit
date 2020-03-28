package com.example.prohieu.setting;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Toast;

import com.example.prohieu.NavigationDrawer;
import com.example.prohieu.R;
import com.example.prohieu.food.Home;
import com.example.prohieu.ui.home.HomeFragment;

public class sound extends AppCompatActivity {
    private AudioManager audioManager;
    private ImageButton backbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound);

        backbutton = (ImageButton) findViewById(R.id.backBtn);
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), NavigationDrawer.class);
                startActivity(intent);
            }
        });

        audioManager = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
    }

    public void UpButton(View view){
        audioManager.adjustVolume(AudioManager.ADJUST_RAISE, AudioManager.FLAG_PLAY_SOUND);
        Toast.makeText(this,"Volume Up", Toast.LENGTH_SHORT).show();
    }

    public void DownButton(View view){
        audioManager.adjustVolume(AudioManager.ADJUST_LOWER, AudioManager.FLAG_PLAY_SOUND);
        Toast.makeText(this,"Volume Down", Toast.LENGTH_SHORT).show();
    }

}