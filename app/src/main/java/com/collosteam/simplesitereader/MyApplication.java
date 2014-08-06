package com.collosteam.simplesitereader;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.collosteam.simplesitereader.api.NotSameCountItemsArray;
import com.collosteam.simplesitereader.api.Utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * Created by Collos on 7/28/2014.
 */
public class MyApplication extends Application {

    //http://api.collosteam.com/get_all_lessons.php

    public static final boolean DEBUG = true;
    private static final String TAG = "{MyApplication}";

    @Override
    public void onCreate() {
        super.onCreate();
        runInBack();
    }

    //Для отладки
    private void runInBack() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //Генерация БД
                if (DEBUG)
                    try {
                        //Utils.SiteFactory(getApplicationContext());

                        String json = Utils.getJSONLessons();

                        writeStringAsFile(getApplicationContext(),json, "json.txt");
                    //    System.out.print(json);

                    }catch (IOException e) {
                        e.printStackTrace();
                    }
            }
        }).start();
    }




    public static void writeStringAsFile(Context context, final String fileContents, String fileName) {

        try {
            FileWriter out = new FileWriter(new File(context.getFilesDir(), fileName));
            out.write(fileContents);
            out.close();
        } catch (IOException e) {
            Log.e(TAG, "" , e);
        }
    }





}
