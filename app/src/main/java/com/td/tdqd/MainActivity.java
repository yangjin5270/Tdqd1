package com.td.tdqd;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView textView = (TextView)findViewById(R.id.textView2);
        textView.setBackgroundColor(android.graphics.Color.RED);
        textView.setText("Hello "+ getIntent().getStringExtra("username"));
        Button button = (Button)findViewById(R.id.upFileButton);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.upFileButton:
                selectFile();
                break;
                default:
                    break;
        }
    }

    private void selectFile(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");//设置类型，我这里是任意类型，任意后缀的可以这样写。
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent,1);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode != Activity.RESULT_OK) {
            Log.i( "------->",String.valueOf(resultCode));
            super.onActivityResult(requestCode, resultCode, data);
            return;
        }
        if (requestCode == 1) {
            Uri uri = data.getData();
            Log.i( "------->",uri.getPath());
            Log.i("------->",uri.getPath());
            Log.i("------->",uri.getScheme());
            Log.i("------->",uri.getHost());
            try {
                File file = new File(uri.getPath());
                char[] chars = new char[1024];
                FileReader fileReader = new FileReader(file);
                StringBuffer stringBuffer = new StringBuffer();
                while(fileReader.read(chars)!=-1){
                    stringBuffer.append(chars);
                }
                //Log.i("-------->",stringBuffer.toString());
                String[] strings = stringBuffer.toString().split("\n");
                Log.i("Array size",String.valueOf(strings.length));
                Log.i("String",strings[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        super.onActivityResult(requestCode, resultCode, data);

    }

    private  String getRealPathFromUri_AboveApi19(Context context, Uri uri) {
        String filePath = null;
        String wholeID = DocumentsContract.getDocumentId(uri);

        // 使用':'分割
        String id = wholeID.split(":")[1];

        String[] projection = {MediaStore.Images.Media.DATA};
        String selection = MediaStore.Images.Media._ID + "=?";
        String[] selectionArgs = {id};

        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,//
                projection, selection, selectionArgs, null);
        int columnIndex = cursor.getColumnIndex(projection[0]);
        if (cursor.moveToFirst()) filePath = cursor.getString(columnIndex);
        cursor.close();
        return filePath;
    }
}