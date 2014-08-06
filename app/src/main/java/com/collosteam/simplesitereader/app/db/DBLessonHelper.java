package com.collosteam.simplesitereader.app.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 *
 */
public class DBLessonHelper extends SQLiteOpenHelper implements LessonsColumns{


    private static final String DATABASE_NAME = "my_db";
    private static final int DATABASE_VERSION = 2;
    public static final String TABLE = "lessons" ;
    private static final String SQL_CREATE_ENTRIES = "CREATE TABLE IF NOT EXISTS "
            + TABLE + "("
            + _ID + " INTEGER UNIQUE PRIMARY KEY AUTOINCREMENT, "
            + TITLE + " TEXT NOT NULL UNIQUE, "
            + FAVORITE + " INTEGER, "
            + STATUS + " INTEGER, "
            + URL_STR + " TEXT, "
            + LAST_OPEN + " INTEGER, "
            + "UNIQUE (" + TITLE + ") ON CONFLICT IGNORE);" ;

    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + TABLE ;

    public DBLessonHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
// Удаляем предыдущую таблицу при апгрейде
        db.execSQL(SQL_DELETE_ENTRIES);
// Создаём новый экземпляр таблицы
        onCreate(db);
    }

}
