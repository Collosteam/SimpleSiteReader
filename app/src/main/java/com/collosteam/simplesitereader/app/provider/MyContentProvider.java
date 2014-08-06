package com.collosteam.simplesitereader.app.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;

import com.collosteam.simplesitereader.app.db.DBLessonHelper;
import com.collosteam.simplesitereader.app.db.LessonsColumns;

public class MyContentProvider extends ContentProvider implements LessonsColumns {

    // // Uri
    // authority
    static final String AUTHORITY = "com.collosteam.simplesitereader.provider";

    // path
    static final String PATH = "lessons";

    // Общий Uri
    public static final Uri CONTENT_URI = Uri.parse("content://"
            + AUTHORITY + "/" + PATH);
    // Типы данных, набор строк
    static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd."
            + AUTHORITY + "." + PATH;
    // Одна строка
    static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd."
            + AUTHORITY + "." + PATH;
    // UriMatcher, общий Uri
    static final int URI_ALL_LESS = 1;
    // Uri с указанным ID
    static final int URI_LESS_BY_ID = 2;
    // описание и создание UriMatcher
    private static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, PATH, URI_ALL_LESS);
        uriMatcher.addURI(AUTHORITY, PATH + "/#", URI_LESS_BY_ID);
    }

    public static String TAG = "{MyContentProvider}";
    SQLiteOpenHelper dbHelper;
    SQLiteDatabase db;

    public MyContentProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public String getType(Uri uri) {
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Log.d(TAG, "insert - " + uri.toString());

        if (uriMatcher.match(uri) != URI_ALL_LESS)
            throw new IllegalArgumentException("Wrong URI: " + uri);

        //Открываем соеденение с БД
        db = dbHelper.getWritableDatabase();

        long rowID = -1;

        db.beginTransaction();
        try {
            rowID = db.insert(DBLessonHelper.TABLE, null, values);
            db.setTransactionSuccessful();
        } catch (Exception e){

        }finally {
            db.endTransaction();
        }

        Uri resultUri = ContentUris.withAppendedId(CONTENT_URI, rowID);
        // уведомляем ContentResolver, что данные по адресу resultUri изменились
        getContext().getContentResolver().notifyChange(CONTENT_URI, null);

        return resultUri;

    }

    @Override
    public boolean onCreate() {
        dbHelper = new DBLessonHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Log.d(TAG, "query - " + uri.toString());
        // проверяем Uri
        switch (uriMatcher.match(uri)) {
            case URI_ALL_LESS: // общий Uri
                Log.d(TAG, "URI_ALL_LESS");
                // если сортировка не указана, ставим свою - по имени
                if (TextUtils.isEmpty(sortOrder)) {
                    //  sortOrder = TIME + " DESC";
                }
                break;
            case URI_LESS_BY_ID: // Uri с ID
                String id = uri.getLastPathSegment();
                Log.d(TAG, "URI_WORDS_BY_ID " + id);
                // добавляем ID к условию выборки
                if (TextUtils.isEmpty(selection)) {
                    selection = _ID + " = " + id;
                } else {
                    selection = selection + " AND " + _ID + " = " + id;
                }
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
        db = dbHelper.getWritableDatabase();

        Log.d(TAG, "Quary -  " +
                " projection - " + projection +
                " selection - " + selection +
                " selectionArgs - " + selectionArgs +
                " sortOrder - " + sortOrder);
        Cursor cursor = db.query(DBLessonHelper.TABLE, projection, selection,
                selectionArgs, null, null, sortOrder);
        // просим ContentResolver уведомлять этот курсор
        // об изменениях данных в GEO_URI
        cursor.setNotificationUri(getContext().getContentResolver(),
                CONTENT_URI);
        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        Log.d(TAG, "update - " + uri.toString());

        // проверяем Uri
        switch (uriMatcher.match(uri)) {
            case URI_ALL_LESS: // общий Uri
                break;
            case URI_LESS_BY_ID: // Uri с ID
                String id = uri.getLastPathSegment();
                Log.d(TAG, "URI_LESSON_BY_ID " + id);
                // добавляем ID к условию выборки
                if (TextUtils.isEmpty(selection)) {
                    selection = _ID + " = " + id;
                } else {
                    selection = selection + " AND " + _ID + " = " + id;
                }
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
        db = dbHelper.getWritableDatabase();
        int row = -1;
        db.beginTransaction();
        try {

            row = db.update(DBLessonHelper.TABLE, values, selection, selectionArgs);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();

        }

        Log.d(TAG, "update row " + row);

        // уведомляем ContentResolver, что данные по адресу resultUri изменились
        getContext().getContentResolver().notifyChange(uri, null);

        return row;

    }
}
