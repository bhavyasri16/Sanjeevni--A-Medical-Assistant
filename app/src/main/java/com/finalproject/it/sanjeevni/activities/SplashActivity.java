package com.finalproject.it.sanjeevni.activities;

import android.content.Intent;
import android.os.SystemClock;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class SplashActivity extends AppCompatActivity {

    private static boolean isShowPageStart = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (isShowPageStart){
            SystemClock.sleep(100);//1500
            isShowPageStart = false;
        }
        Intent intent = new Intent(this, WelcomeActivity.class);
        startActivity(intent);
        finish();
    }
}
