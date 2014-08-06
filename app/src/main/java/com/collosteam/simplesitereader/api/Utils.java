package com.collosteam.simplesitereader.api;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.collosteam.simplesitereader.api.data.LessonImpl;
import com.collosteam.simplesitereader.api.data.Person;
import com.collosteam.simplesitereader.api.data.User;
import com.collosteam.simplesitereader.app.db.DBLessonHelper;
import com.collosteam.simplesitereader.app.db.LessonsColumns;
import com.google.gson.Gson;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Collos on 7/12/2014.
 */
public class Utils implements LessonsColumns {

    private static final String TAG = "{Utils}";

    private static Map<Integer, Person> users;

    private static String names[] = {
            "Wayan",
            "Made",
            "Nyoman",
            "Ketut",
            "Sam",
            "Bob",
            "Ken",
            "Andrew",
            "Suzanna",
            "George",
    };

    //
    public static Map<Integer, Person> getUsers() {
        int count = names.length;
        if (users == null) {
            users = new HashMap<Integer, Person>(count + 5);

            for (String name : names) {
                Person user = new User(name, "12345", String.format("%s@gmail.com", name));
                users.put(user.hashCode(), user);
            }
        }
        return users;
    }

    //Имитация списка пользователей
    private static List<Person> persons;

    public static List<Person> getPersonsList() {
        int count = names.length;
        if (persons == null) {
            persons = new ArrayList<Person>(count + 5);

            for (String name : names) {
                Person user = new User(name, "12345", String.format("%s@gmail.com", name));
                persons.add(user);
            }
        }
        return persons;
    }


    private static String[] urlsArray = {
            "http://startandroid.ru/ru/uroki/vse-uroki-spiskom/4-urok-1-vvedenie.html",
            "http://startandroid.ru/ru/uroki/vse-uroki-spiskom/9-urok-2-ustanovka-i-nastrojka-sredy-razrabotki.html",
            "http://startandroid.ru/ru/uroki/vse-uroki-spiskom/12-urok-3-sozdanie-avd-pervoe-prilozhenie-struktura-android-proekta.html",
            "http://startandroid.ru/ru/uroki/vse-uroki-spiskom/13-urok-4-elementy-ekrana-i-ih-svojstva.html",
            "http://startandroid.ru/ru/uroki/vse-uroki-spiskom/14-urok-5-layout-kak-ispolzovat-smena-orientatsii-ekrana.html",
    };

    private static String[] titleArray = {
            "Урок 1. Введение.",
            "Урок 2. Установка и настройка среды разработки Eclipse",
            "Урок 3. Создание AVD. Первое приложение. Структура Android-проекта.",
            "Урок 4. Элементы экрана и их свойства",
            "Урок 5. Layout-файл в Activity. XML представление. Смена ориентации экрана.",

    };

    public static void SiteFactory(Context context) throws IOException {


        DBLessonHelper dbLessonHelper = new DBLessonHelper(context);

        SQLiteDatabase sqLiteDatabase = dbLessonHelper.getWritableDatabase();

        try {
            sqLiteDatabase.beginTransaction(); //Начало транзакции

            for(ContentValues cv : getContentValues())
            sqLiteDatabase.insert(DBLessonHelper.TABLE, null, cv);

            sqLiteDatabase.setTransactionSuccessful();// Успешная транзакция
        } finally {
            sqLiteDatabase.endTransaction(); //Завершение транзакции
            sqLiteDatabase.close();  // Закрытие базы данных
        }
    }


    public static ContentValues[] getContentValues() throws IOException {

        NetworkManager networkManager = new NetworkManager();

        Utils.LessonListWraper wraper  =   networkManager.getAllLess();

        List<LessonImpl> lessonList = wraper.getLessons();

        ContentValues[] contentValueses = new ContentValues[lessonList.size()];

        int i = 0;

        for (LessonImpl lesson : lessonList) {
            ContentValues cv = new ContentValues();
            cv.put(_ID, lesson.getID());
            cv.put(TITLE, lesson.getTitle()); //less.get(i).getTitle();
            cv.put(FAVORITE, 0);
            cv.put(STATUS, 0);
            cv.put(LAST_OPEN, -1);
            cv.put(URL_STR, lesson.getUrl());
            Log.d(TAG, " Site insert " + cv.toString());
            contentValueses[i++] = cv;
        }

        return contentValueses;
    }


    public static String getJSONLessons() throws IOException {

        List<LessonImpl> lessonList = new ArrayList<LessonImpl>();
        Document doclist = Jsoup.connect("http://www.startandroid.ru/ru/uroki/vse-uroki-spiskom.html").get();
        Elements newsHeadlines = doclist.select(".list-title>a");
        List<Element> elements = newsHeadlines;
        int i = 1;
        for (Element element : elements ){
            if(!element.attr("href").contains("html")){
                continue;
            }
             LessonImpl lesson = new LessonImpl(i++, element.text(),"http://www.startandroid.ru" + element.attr("href"));
            lessonList.add(lesson);
        }



        Gson gson = new Gson();
        return gson.toJson(new LessonListWraper(lessonList));
    }

    public static class LessonListWraper {

        List<LessonImpl> lessons;

        public LessonListWraper(List<LessonImpl> lessons) {
            this.lessons = lessons;
        }

        public List<LessonImpl> getLessons() {
            return lessons;
        }
    }

}
