package com.collosteam.simplesitereader.app.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.collosteam.simplesitereader.R;
import com.collosteam.simplesitereader.app.fragment.ItemFragment;
import com.collosteam.simplesitereader.app.fragment.WebViewFragment;

public class WebViewActivity extends Activity {
    int lesson_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        Intent intent = getIntent();

        if (intent != null && intent.hasExtra(ItemFragment.EXTRAS_LESSON_ID))
            lesson_id = intent.getIntExtra(ItemFragment.EXTRAS_LESSON_ID, -1);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, WebViewFragment.newInstance(lesson_id))
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.web_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
