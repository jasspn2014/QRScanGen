package com.aroradevelopers.qrgenscan;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashScreen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen_activity);

        Handler handler = new Handler();

        handler.postDelayed(new Runnable(){
            @Override
            public void run() {
                startMainActivity();
            }
        },1500);
    }

    private void startMainActivity() {
        Intent intent = new Intent(this,MainActivity.class);
        this.finish();
        startActivity(intent);
    }
}
