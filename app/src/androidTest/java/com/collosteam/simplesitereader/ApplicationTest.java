package com.collosteam.simplesitereader;

import android.app.Application;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.ApplicationTestCase;

import com.collosteam.simplesitereader.api.Utils;
import com.collosteam.simplesitereader.api.data.LessonImpl;
import com.collosteam.simplesitereader.api.data.Person;
import com.collosteam.simplesitereader.api.data.User;
import com.collosteam.simplesitereader.app.db.DBColumn;
import com.collosteam.simplesitereader.app.db.DBHelper;
import com.google.gson.Gson;

import java.util.List;
import java.util.Map;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> implements DBColumn {
    public ApplicationTest() {
        super(Application.class);
    }

    private final String JSON_LESSON_TEXT = "{\"lessons\":[{\"url\":\"http://startandroid.ru/ru/uroki/vse-uroki-spiskom/4-urok-1-vvedenie.html\",\"title\":\"Урок 1. Введение.\",\"_id\":1},{\"url\":\"http://startandroid.ru/ru/uroki/vse-uroki-spiskom/9-urok-2-ustanovka-i-nastrojka-sredy-razrabotki.html\",\"title\":\"Урок 2. Установка и настройка среды разработки Eclipse\",\"_id\":2},{\"url\":\"http://startandroid.ru/ru/uroki/vse-uroki-spiskom/12-urok-3-sozdanie-avd-pervoe-prilozhenie-struktura-android-proekta.html\",\"title\":\"Урок 3. Создание AVD. Первое приложение. Структура Android-проекта.\",\"_id\":3},{\"url\":\"http://startandroid.ru/ru/uroki/vse-uroki-spiskom/13-urok-4-elementy-ekrana-i-ih-svojstva.html\",\"title\":\"Урок 4. Элементы экрана и их свойства\",\"_id\":4},{\"url\":\"http://startandroid.ru/ru/uroki/vse-uroki-spiskom/14-urok-5-layout-kak-ispolzovat-smena-orientatsii-ekrana.html\",\"title\":\"Урок 5. Layout-файл в Activity. XML представление. Смена ориентации экрана.\",\"_id\":5}]}";

    public void testUserEquals() throws Exception {
        User me = new User("Andrew", "1234", "aaa@bbb.ua");
        User me2 = new User("Andrew", "1234", "aaass@bbbss.ua");

        assertSame(true, me.equals(me2));
        assertSame(true, me.hashCode() == me2.hashCode());

        User you = new User("Stepan", "12345", "aassa@bbssb.ua");
        assertSame(false, me.equals(you));
        assertSame(false, me.hashCode() == you.hashCode());
    }

    public void testSearchUser() throws Exception {
        User me = new User("Andrew", "12345", "Andrew@gmail.com");

        Map<Integer, Person> users = Utils.getUsers();
        assertSame(true, users.containsKey(me.hashCode()));

        User you = new User("Stepan", "12345", "aassa@bbssb.ua");
        assertSame(false, users.containsKey(you.hashCode()));
    }

    public void testCastExeption() throws Exception {

        Object a = "efbdsfujsdjflsd";

        Person person = (Person) a;


    }

    public void testDBTest() throws Exception {
        long rowID = -1L;
        DBHelper helper = new DBHelper(getContext());
        SQLiteDatabase writableDatabase = helper.getWritableDatabase();

        assertNotNull(writableDatabase);

        ContentValues cv = new ContentValues();
        cv.put(NAME, "Vasa");
        cv.put(PASS, "54321");
        cv.put(EMAIL, "vasa@i.ua");

        writableDatabase.beginTransaction();
        try {
          rowID = writableDatabase.insert(DBHelper.TABLE, null, cv);

            assertEquals(rowID>=0,true);

            writableDatabase.setTransactionSuccessful();
        } finally {
            writableDatabase.endTransaction();
        }

        writableDatabase.close();

        assertNotNull(writableDatabase);

        writableDatabase = helper.getWritableDatabase();
        Cursor cursor = writableDatabase.query(DBHelper.TABLE,null,null,null,null,null,null);

        if(cursor.moveToFirst()){

            String name = "";

            name = cursor.getString(cursor.getColumnIndex(NAME));

            assertEquals("Vasa",name);


        }
    }

    public void testJSONParser() throws Exception {
        Gson gson = new Gson();

        String jsonFromSite = Utils.getJSONLessons();

        Utils.LessonListWraper wraper  = gson.fromJson(jsonFromSite, Utils.LessonListWraper.class);
        List<LessonImpl> lessonList = wraper.getLessons();
        assertNotNull(lessonList);
        assertEquals(5,lessonList.size());
        assertEquals("Урок 1. Введение.",lessonList.get(0).getTitle());

    }
}