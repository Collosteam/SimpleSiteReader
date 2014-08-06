package com.collosteam.simplesitereader.app.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.collosteam.simplesitereader.R;
import com.collosteam.simplesitereader.api.Utils;
import com.collosteam.simplesitereader.api.data.Person;
import com.collosteam.simplesitereader.api.data.User;

import java.util.Map;

import static com.collosteam.simplesitereader.app.activity.MainActivity.EXTRAS_KEY_NAME;
import static com.collosteam.simplesitereader.app.activity.MainActivity.EXTRAS_KEY_PASSW;


/**
 * Simple Login screen
 */
public class LoginActivity extends Activity implements View.OnClickListener {

    private static final String TAG = "{LoginActivity}";

    //Кнопки
    private Button bSignUp;
    private Button bLogin;

    //Поля ввода
    private EditText etName;
    private EditText etPassw;

    /*Constants*/
    private final int SIGN_UP_REQUEST_ID = "SIGN_UP_REQUEST_ID".hashCode();

    /*Animation  */
    private AnimationSet set;

    private SharedPreferences setting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Инициализируем кнопки (создаем объекты)
        bSignUp = (Button) findViewById(R.id.bSignUp);
        bLogin = (Button) findViewById(R.id.bLogin);


        set = (AnimationSet) AnimationUtils.loadAnimation(this, R.anim.click_animation);

        set.setAnimationListener(
         new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                doLogin();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        }
        );

        //Инициализируем поля ввода (создаем объекты)
        etName = (EditText) findViewById(R.id.etName);
        etPassw = (EditText) findViewById(R.id.etPass);

        //Подписываемся на слушание событий
        bSignUp.setOnClickListener(this);
        bLogin.setOnClickListener(this);



        setting = PreferenceManager.getDefaultSharedPreferences(this);

        if (setting.contains(EXTRAS_KEY_NAME)) {

            String name = setting.getString(EXTRAS_KEY_NAME, "");

            etName.setText(name);
        }

        if (setting.contains(EXTRAS_KEY_PASSW)) {

            String pass = setting.getString(EXTRAS_KEY_PASSW, "");

            etPassw.setText(pass);
        }

    /*    SharedPreferences.Editor editor = setting.edit();

        editor.putInt("ID_USER", 666);
        editor.putBoolean("IS_LIVE", true);
        editor.putFloat("weidth", 88.455f);

        editor.commit();*/


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.login, menu);
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

    //Обработчик нажатия в активити
    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            //Обработка нажатия на кнопку Login
            case R.id.bLogin:

                v.startAnimation(set);

            //    doLogin();

                break;

            case R.id.bSignUp:
                startActivityForResult(new Intent(this, SignUpActivity.class), SIGN_UP_REQUEST_ID);
                break;
        }
    }

    private void doLogin() {
        //Инициализируем диалог
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Login...");
        progressDialog.show(); // Запускаем диалог для имитации проверки данных

        //Считываем  значения из полей ввода
        final String userName = etName.getText() != null ? etName.getText().toString() : "";
        final String userPass = etPassw.getText() != null ? etPassw.getText().toString() : "";

                /* Класс Handler имеет метод postDelayed(Runnable run, long delay) позволяющий
                * совершать отлаженый запуск некоего действия помещенного в метод run()*/
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (userName.length() == 0) {
                    etName.setError("Имя не может быть пустым");
                    progressDialog.dismiss();
                }
                if (userPass.length() <= 4) {
                    etPassw.setError("Пароль не может быть короче 4 символов");
                    progressDialog.dismiss();
                }
                if (userName.length() > 0 && userPass.length() >= 4) {
                    /*Для проверки правильности введенных данных используем hash code
                        объекта созданного на основе введенных пользователем значений*/
                    int simpleUserUsedForSearch = new User(userName, userPass, null).hashCode();

                    Map<Integer, Person> integerPersonMap = Utils.getUsers();

                    if (integerPersonMap.containsKey(simpleUserUsedForSearch)) {
                        //Запускаем новое окно в котро отображаем данные о пользоветеле
                        //Данные передаются через экземпляр Intent
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra(EXTRAS_KEY_NAME, userName);
                        intent.putExtra(MainActivity.EXTRAS_KEY_EMAIL,
                                Utils.getUsers().get(simpleUserUsedForSearch).getEmail());
                        progressDialog.dismiss(); //Прекращаем показ диалога

                        //Сохраняем пользователя
                        SharedPreferences.Editor editor = setting.edit();

                        editor.putString(MainActivity.EXTRAS_KEY_NAME, userName);
                        editor.putString(MainActivity.EXTRAS_KEY_PASSW, userPass);

                        editor.commit();

                        startActivity(intent); //Запускаем новую активность

                    } else {
                        progressDialog.dismiss(); //Прекращаем показ диалога
                        Toast.makeText(LoginActivity.this, "Пользователь не найден!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }, 1500);
    }

    /*Проверяем что пришло из активити которое мы вызывали через - startActivityForResult*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SIGN_UP_REQUEST_ID) {
            Log.d(TAG, "Получен результат из SignUpActivity");
            if (resultCode == RESULT_OK) {
                Log.d(TAG, "Получен результат RESULT_OK");
                if (data != null) {
                    if (data.hasExtra(EXTRAS_KEY_NAME)) {
                        etName.setText(data.getStringExtra(EXTRAS_KEY_NAME));
                    }

                    if (data.hasExtra(EXTRAS_KEY_PASSW)) {
                        etPassw.setText(data.getStringExtra(EXTRAS_KEY_PASSW));
                    }
                }

            } else {
                Log.d(TAG, "Получен результат RESULT_CANCELED");
            }
        }
    }
}
