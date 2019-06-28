package com.td.tdqd;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {


    private String TAG="BaseActivity";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Log.i(TAG,getClass().getSimpleName());
        ActivitysController.addActivity(this);
    }


    protected void onDestroy(){
        super.onDestroy();
        ActivitysController.removeActivity(this);
    }
}
