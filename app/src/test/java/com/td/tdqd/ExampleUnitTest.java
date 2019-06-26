package com.td.tdqd;

import android.util.Log;

import org.junit.Test;

import java.io.File;
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
}