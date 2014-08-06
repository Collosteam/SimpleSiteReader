package com.collosteam.simplesitereader.api.data;

import java.io.Serializable;

/**
 * Created by Collos on 7/30/2014.
 */
public interface Lesson extends Serializable {

    public static final long serialVersionUID = 0L;


    public int getID();
    public String getTitle();
    public String getUrl();

}
