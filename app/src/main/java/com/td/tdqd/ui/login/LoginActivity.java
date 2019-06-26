package com.td.tdqd.ui.login;

import android.app.Activity;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.td.tdqd.MainActivity;
import com.td.tdqd.R;
import com.td.tdqd.util.WjNetVerify;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{


    private EditText usernameEditText ;
    private EditText passwordEditText ;
    private Button loginButton ;
    private ProgressBar loadingProgressBar;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            loadingProgressBar.setVisibility(View.GONE);

            super.handleMessage(msg);
            HashMap info = (HashMap) msg.obj;
            //TODO 正式发布时，取消注释
            /*if (msg.what == 1) {

                if("success".equals(info.get("infoType").toString())){
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("username",usernameEditText.getText().toString());
                    startActivity(intent);
                }else{
                    Toast.makeText(LoginActivity.this,info.get("message").toString(),Toast.LENGTH_SHORT).show();
                }
            }*/
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.putExtra("username",usernameEditText.getText().toString());
            startActivity(intent);
            //Toast.makeText(LoginActivity.this,"handler",Toast.LENGTH_SHORT);

        }

    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.login);
        loadingProgressBar = findViewById(R.id.loading);




        loginButton.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {


        loadingProgressBar.setVisibility(View.VISIBLE);loadingProgressBar.setVisibility(View.VISIBLE);

        new Thread(new Runnable() {
            @Override
            public void run() {

                WjNetVerify.setAccount(usernameEditText.getText().toString());
                WjNetVerify.setPassword(passwordEditText.getText().toString());
                HashMap map = WjNetVerify.login("1");
                Message message =handler.obtainMessage();
                message.obj=map;
                message.what=1;
                message.arg1=1;
                message.arg2=2;
                handler.sendMessage(message);

            }
        }).start();

    }
}
