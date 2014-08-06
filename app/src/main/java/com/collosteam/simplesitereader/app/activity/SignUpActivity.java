package com.collosteam.simplesitereader.app.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.collosteam.simplesitereader.R;
import com.collosteam.simplesitereader.api.Utils;
import com.collosteam.simplesitereader.api.data.User;

public class SignUpActivity extends Activity implements View.OnClickListener {

    private static final String TAG = "{SignUpActivity}";

    //Кнопки
    private Button bSignUp;

    //Поля ввода
    private EditText etName;
    private EditText etMail;
    private EditText etPassw;
    private EditText etConfPassw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //Инициализируем кнопки (создаем объекты)
        bSignUp = (Button) findViewById(R.id.bSignUp);

        //Инициализируем поля ввода (создаем объекты)
        etName = (EditText) findViewById(R.id.etName);
        etMail = (EditText) findViewById(R.id.etEmail);
        etPassw = (EditText) findViewById(R.id.etPass);
        etConfPassw = (EditText) findViewById(R.id.etConfPass);

        //Подписываемся на слушание событий
        bSignUp.setOnClickListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.sign_up, menu);
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

    @Override
    public void onClick(View v) {

        int id = v.getId();

        switch (id) {
            //Обработка нажатия на кнопку Login
            case R.id.bSignUp:
                //Инициализируем диалог
                final ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Sign up...");
                progressDialog.show();  // Запускаем диалог для имитации проверки данных

                //Считываем  значения из полей ввода
                final String userName = etName.getText() != null ? etName.getText().toString() : "";
                final String userEmail = etMail.getText() != null ? etMail.getText().toString() : "";
                final String userPass = etPassw.getText() != null ? etPassw.getText().toString() : "";
                final String userConfPass = etConfPassw.getText() != null ? etConfPassw.getText().toString() : "";

                /* Класс Handler имеет метод postDelayed(Runnable run, long delay) позволяющий
                * совершать отлаженый запуск некоего действия помещенного в метод run()*/
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        if (userName.length() == 0) {
                            etName.setError("Имя не может быть пустым");
                            progressDialog.dismiss();
                        }

                        if (!userEmail.contains("@")) {
                            etMail.setError("Неправильный email");
                            progressDialog.dismiss();
                        }

                        if (userPass.length() <= 4) {
                            etPassw.setError("Пароль не может быть короче 4 символов");
                            progressDialog.dismiss();
                        }

                        if (userConfPass.length() <= 4) {
                            etConfPassw.setError("Пароль не может быть короче 4 символов");
                            progressDialog.dismiss();
                        }

                        if (!userConfPass.equals(userPass)) {
                            etConfPassw.setError("Пароли должны совпадать");
                            progressDialog.dismiss();
                        }

                        if (userName.length() > 0
                                && userPass.length() >= 4
                                && userEmail.contains("@")
                                && etConfPassw.length() >= 4
                                && userConfPass.equals(userPass)) {
                            /*Для проверки правильности введенных данных используем hash code
                                объекта созданного на основе введенных пользователем значений*/
                            User user = new User(userName, userPass, userEmail);
                            int simpleUserUsedForSearch = user.hashCode();
                            if (Utils.getUsers().containsKey(simpleUserUsedForSearch)) {
                                progressDialog.dismiss(); //Прекращаем показ диалога
                                Toast.makeText(SignUpActivity.this, "Данный пользователь уже существует", Toast.LENGTH_SHORT).show();
                            } else {
                                progressDialog.dismiss(); //Прекращаем показ диалога
                                Toast.makeText(SignUpActivity.this, "Новый пользователь создан!", Toast.LENGTH_SHORT).show();

                                //Запихиваем нашего нового юзера в импровизированное хранилище
                                Utils.getUsers().put(simpleUserUsedForSearch,user);

                                //Запускаем новое окно в котро отображаем данные о пользоветеле
                                //Данные передаются через экземпляр Intent
                                Intent data = new Intent();

                                data.putExtra(MainActivity.EXTRAS_KEY_NAME, userName);
                                data.putExtra(MainActivity.EXTRAS_KEY_PASSW, userPass);

                                setResult(RESULT_OK, data);
                                onBackPressed();
                            }
                        }
                    }
                }, 1500);
                break;
        }
    }
}
