package com.td.tdqd;

import android.util.Log;

import com.td.tdqd.util.DBserverices;

import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void test1(){

        try {
            File file = new File("C:\\Users\\Administrator\\Documents\\雷电模拟器\\Pictures\\Screenshots/1.txt");
            char[] chars = new char[1024];
            FileReader fileReader = new FileReader(file);
            StringBuffer stringBuffer = new StringBuffer();
            while(fileReader.read(chars)!=-1){
                stringBuffer.append(chars);
            }

            String[] strings = stringBuffer.toString().split("\n");
            System.out.println(strings.length);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void test3() throws Exception {
        System.out.println(15/Double.valueOf(15)*100);
    }

    @Test
    public void test2() throws Exception {


        File file = new File("G:/data.txt");
        char[] chars = new char[1024];
        FileReader fileReader = new FileReader(file);
        StringBuffer stringBuffer = new StringBuffer();
        StringBuffer stringBuffer1 = new StringBuffer();

        int num=0;
        while((num=fileReader.read(chars))!=-1){
            stringBuffer.append(chars,0,num);
            //System.out.print(chars);
            //System.out.println(num);
        }

        //Log.i("-------->",stringBuffer.toString());

        String[] strings = stringBuffer.toString().split("\r\n");
        //System.out.println(strings.length);


        long x = System.currentTimeMillis();
        //INSERT INTO t1(field1,field2) VALUES(v101,v102),(v201,v202),(v301,v302),(v401,v402);
        try {

            int loop = strings.length/100;
            int yu = strings.length%100;
            int index=0;
            for (int i = 0; i < loop; i++) {
                stringBuffer1.append("INSERT into name_tables_copy (name_tables_copy.name,name_tables_copy.name_id) VALUES ");
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
                System.out.println(stringBuffer1.toString());
                new Thread(new DBserverices(stringBuffer1.toString())).start();
                stringBuffer1.setLength(0);
            }


            stringBuffer1.append("INSERT into name_tables_copy (name_tables_copy.name,name_tables_copy.name_id) VALUES ");
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
            System.out.println(stringBuffer1.toString());
            new Thread(new DBserverices(stringBuffer1.toString())).start();
            stringBuffer1=null;


            Thread.sleep(12000);


            DBserverices.closeConnection();



        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(System.currentTimeMillis()-x);
    }
}