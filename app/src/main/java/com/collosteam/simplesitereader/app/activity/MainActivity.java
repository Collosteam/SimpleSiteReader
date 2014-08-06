package com.collosteam.simplesitereader.app.activity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.collosteam.simplesitereader.R;
import com.collosteam.simplesitereader.app.db.DBColumn;
import com.collosteam.simplesitereader.app.db.LessonsColumns;
import com.collosteam.simplesitereader.app.fragment.ItemFragment;
import com.collosteam.simplesitereader.app.provider.MyContentProvider;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class MainActivity extends Activity implements DBColumn, LessonsColumns {

    public static final String EXTRAS_KEY_NAME = "e.name";
    public static final String EXTRAS_KEY_EMAIL = "e.mail";
    public static final String EXTRAS_KEY_PASSW = "e.passw";
    private static final String TAG = "{MainActivity}";

    private String mName;
    private String mMail;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Intent intent = getIntent();

        TextView tvName = (TextView) findViewById(R.id.tvName);
        TextView tvMail = (TextView) findViewById(R.id.tvMail);

        //Проверяем намеринье
        if (intent != null) {
            if (intent.hasExtra(EXTRAS_KEY_NAME)) {
                mName = intent.getStringExtra(EXTRAS_KEY_NAME);
                tvName.setText(mName);
            }
            if (intent.hasExtra(EXTRAS_KEY_EMAIL)) {
                mMail = intent.getStringExtra(EXTRAS_KEY_EMAIL);
                tvMail.setText(mMail);
            }
        }
        //Инициализируем кнопку
        Button buttonGoTo = (Button) findViewById(R.id.buttonGoTo);

        // Создаем слушателя для обработки нажатия
        buttonGoTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ContentValues cv = new ContentValues();
                cv.put(TITLE, "Lessons - " + System.currentTimeMillis());

                getContentResolver().insert(MyContentProvider.CONTENT_URI, cv);

            }
        });

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction().add(R.id.conteiner, new ItemFragment()).commit();
        }

        final String imgURL = "https://www.gstatic.com/images/icons/gplus-64.png";

        final ImageView mImageView = (ImageView) findViewById(R.id.imageView);

        Thread threadNotMain = new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        final Bitmap b = loadImageFromNetwork(imgURL);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mImageView.setImageBitmap(b);
                            }
                        });


                    }
                }
        );

        //threadNotMain.start();

       ProgressBar bar = (ProgressBar) findViewById(R.id.progressBar);

       new ImageLoad(bar,mImageView).execute(imgURL);


    }


    //Загрузка картинки по URL_STR
    private Bitmap loadImageFromNetwork(String imgURL) {

        try {
            java.net.URL url = new URL(imgURL);//Создаем Сыылку на картинку

            HttpURLConnection connection = (HttpURLConnection) url.openConnection(); //Открываем соединенеие

            Bitmap bitmap = BitmapFactory.decodeStream(connection.getInputStream()); // Декодируем поток

            return bitmap;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return null;
        }


    }


    class ImageLoad extends AsyncTask<String, Void, Bitmap> {

        ProgressBar bar;
        ImageView mImageView;

        ImageLoad(ProgressBar bar, ImageView mImageView) {
            this.bar = bar;
            this.mImageView = mImageView;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            bar.setVisibility(View.VISIBLE);

        }

        @Override
        protected Bitmap doInBackground(String... params) {

            String [] entyParams = params;

            String URLIMAGE  = entyParams[0] ;

            Bitmap bitmap = loadImageFromNetwork(URLIMAGE);    // Получить картинку с SD Card!!!

            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);

            mImageView.setImageBitmap(bitmap);
            bar.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
