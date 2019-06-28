package com.td.tdqd;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.td.tdqd.util.DBserverices;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class MainActivity extends BaseActivity implements View.OnClickListener{


    String dataPath="";
    private String TAG="MainActivity";
    private ProgressBar progressBar;
    private long mExitTime;
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what){
                case 1:
                    Toast.makeText(MainActivity.this,"处理完毕",Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Toast.makeText(MainActivity.this,"文件类型错误",Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    if(msg.arg1==100){
                        progressBar.setProgress(msg.arg1);
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(MainActivity.this,"数据导入完毕",Toast.LENGTH_LONG).show();
                    }else{
                        progressBar.setProgress(msg.arg1);
                        //Log.i("progressBar",String.valueOf(msg.arg1));
                    }
                    break;
                default:break;
            }


        }
    };





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        TextView textView = (TextView)findViewById(R.id.textView2);
        textView.setBackgroundColor(android.graphics.Color.RED);
        textView.setText("Hello "+ getIntent().getStringExtra("username"));
        Button button = (Button)findViewById(R.id.upFileButton);
        button.setOnClickListener(this);

        progressBar = (ProgressBar)findViewById(R.id.progressBar1);
        progressBar.setVisibility(View.GONE);

    }
    @Override
    public boolean onOptionsItemSelected( MenuItem item) {
        switch (item.getItemId()){
            case R.id.exit:
                ActivitysController.finshAll();
                break;
            case R.id.help:
                Toast.makeText(this,"联系作者",Toast.LENGTH_LONG).show();
                break;
            default:
                break;
        }
        return true;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       getMenuInflater().inflate(R.menu.menu,menu);
       return true;
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
            progressBar.setVisibility(View.VISIBLE);
            Uri uri = data.getData();
            dataPath = uri.getPath();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        File file = new File(dataPath);
                        char[] chars = new char[1024];
                        FileReader fileReader = new FileReader(file);
                        StringBuffer stringBuffer = new StringBuffer();
                        StringBuffer stringBuffer1 = new StringBuffer();
                        int num;
                        while((num=fileReader.read(chars))!=-1){
                            stringBuffer.append(chars,0,num);
                            //System.out.print(chars);
                            //System.out.println(num);
                        }

                        String[] strings = stringBuffer.toString().split("\r\n");
                        try {
                            int loop = strings.length/100;
                            int yu = strings.length%100;
                            int index=0;
                            for (int i = 0; i < loop; i++) {
                                stringBuffer1.append("INSERT into name_tables (name_tables.name,name_tables.name_id) VALUES ");
                                for (int j = 0; j < 100; j++) {
                                    String[] strings1 = strings[index].split("----");
                                    if(j==0){
                                        stringBuffer1.append("(\"");
                                    }else{
                                        stringBuffer1.append(",(\"");
                                    }
                                    stringBuffer1.append(strings1[0]);
                                    stringBuffer1.append("\",\"");
                                    stringBuffer1.append(strings1[1]);
                                    stringBuffer1.append("\")");
                                    index++;
                                }
                                //System.out.println(stringBuffer1.toString());
                                new Thread(new DBserverices(stringBuffer1.toString())).start();
                                stringBuffer1.setLength(0);
                            }


                            stringBuffer1.append("INSERT into name_tables (name_tables.name,name_tables.name_id) VALUES ");
                            for (int j = 0; j < yu; j++) {
                                String[] strings1 = strings[index].split("----");
                                if(j==0){
                                    stringBuffer1.append("(\"");
                                }else{
                                    stringBuffer1.append(",(\"");
                                }
                                stringBuffer1.append(strings1[0]);
                                stringBuffer1.append("\",\"");
                                stringBuffer1.append(strings1[1]);
                                stringBuffer1.append("\")");
                                index++;
                            }
                            //System.out.println(stringBuffer1.toString());
                            new Thread(new DBserverices(stringBuffer1.toString())).start();
                            stringBuffer1=null;
                            Double d = Double.valueOf(loop/2);
                            Double d1;

                            for (int i=0;i<loop/2;i++){
                                Message message = handler.obtainMessage();
                                message.what=3;
                                d1=(i/d*100);
                                message.arg1 = d1.intValue();
                                handler.sendMessage(message);
                                Thread.sleep(1000);
                            }
                            Message message = handler.obtainMessage();
                            message.what=3;
                            message.arg1 = 100;
                            handler.sendMessage(message);

                            DBserverices.closeConnection();



                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        /*Message message = handler.obtainMessage();
                        message.what=1;
                        handler.sendMessage(message);*/
                    } catch (Exception e) {
                        Message message = handler.obtainMessage();
                        message.what=2;
                        handler.sendMessage(message);
                        //e.printStackTrace();
                    }
                }
            }).start();
        }
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        //ActivitysController.finshAll();
    }

    public void onBackPressed() {

        if ((System.currentTimeMillis() - mExitTime) > 2000) {
            Toast.makeText(MainActivity.this, "再按一次退出应用", Toast.LENGTH_SHORT).show();
            mExitTime = System.currentTimeMillis();
        } else {
            //用户退出处理
            ActivitysController.finshAll();
        }



    }



}
