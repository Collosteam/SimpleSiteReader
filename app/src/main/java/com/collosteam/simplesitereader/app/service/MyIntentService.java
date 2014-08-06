package com.collosteam.simplesitereader.app.service;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;

import com.collosteam.simplesitereader.api.Utils;
import com.collosteam.simplesitereader.app.provider.MyContentProvider;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * helper methods.
 */
public class MyIntentService extends IntentService {


    public MyIntentService() {
        super("MyIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        ContentValues[] contentValueses = null;
        try {
            contentValueses = Utils.getContentValues();
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (ContentValues cv : contentValueses)
            getContentResolver().insert(MyContentProvider.CONTENT_URI, cv);

        stopSelf();
    }


}
