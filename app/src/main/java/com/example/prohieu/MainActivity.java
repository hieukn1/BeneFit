package com.example.prohieu;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private TextView tv;
    private ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // --------------------avtivity_main
        tv = (TextView) findViewById(R.id.welcomescreen);
        iv = (ImageView) findViewById(R.id.ucilogo);
        Animation myani = AnimationUtils.loadAnimation(this, R.anim.animation1);
        tv.startAnimation(myani);
        iv.startAnimation(myani);
        // --------------------activity_main

        // --------------------activity_loginscreen
        Thread thread = new Thread()
        {
            @Override
            public void run()
            {
                try{
                    sleep(4000);
                    Intent intent = new Intent(getApplicationContext(), Welcome3slide.class);
                    startActivity(intent);
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                super.run();
            }
        };
        thread.start();
        // --------------------activity_loginscreen
    }
}
