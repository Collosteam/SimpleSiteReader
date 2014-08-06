package com.collosteam.simplesitereader.api.data;

import com.collosteam.simplesitereader.app.db.LessonsColumns;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Collos on 7/30/2014.
 */
public class LessonImpl implements Lesson,LessonsColumns {

    public LessonImpl(int id, String title, String url_str) {
        this.id = id;
        this.title = title;
        this.url_str = url_str;
    }

    @SerializedName(_ID)
    int id;

    @SerializedName(TITLE)
    String title;

    @SerializedName(URL_STR)
    String url_str;

    @Override
    public int getID() {
        return id;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getUrl() {
        return url_str;
    }
}
