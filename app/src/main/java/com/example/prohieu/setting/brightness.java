package com.example.prohieu.setting;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSeekBar;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.prohieu.R;

public class brightness extends AppCompatActivity {
    /*private SeekBar seekBar;
    private int brightness;
    private ContentResolver contentResolver;
    private Window window;
    TextView textView;*/
    AppCompatSeekBar seek;
    int brightness;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brightness);

        seek = (AppCompatSeekBar) findViewById(R.id.brightbar);
        try {
            brightness = Settings.System.getInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }

        seek.setKeyProgressIncrement(1);
        seek.setProgress(brightness);

        seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                brightness = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
                Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, brightness);
            }
        });

        /*seekBar = (SeekBar) findViewById(R.id.brightbar);
        textView = (TextView) findViewById(R.id.txtPercentage);
        contentResolver = getContentResolver();
        window = getWindow();
        seekBar.setMax(255);
        seekBar.setKeyProgressIncrement(1);

        try {
            brightness = Settings.System.getInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS);
        } catch (Exception e) {
            Log.e("Error", "cannot access system brightness.");
            e.printStackTrace();
        }

        seekBar.setProgress(brightness);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Settings.System.putInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS, brightness);
                WindowManager.LayoutParams layoutParams = window.getAttributes();
                layoutParams.screenBrightness = brightness / (float) 255;
                window.setAttributes(layoutParams);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress <= 20) {
                    brightness = 20;
                } else {
                    brightness = progress;
                }
                float perc = (brightness / (float) 255) * 100;
                textView.setText((int) perc + "%");
            }
        });*/
    }
}