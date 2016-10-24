package com.focaloid.focaloid_training;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

/**
 * Created by dinil on 08-08-2016.
 */
public class Splash extends Activity{

    public static  int splashtime=4000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_layout);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(Splash.this,LoginActivity.class);
                startActivity(i);

                finish();
            }
        },splashtime);
    }
}
