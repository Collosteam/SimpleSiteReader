package com.collosteam.simplesitereader.api.data;

import java.io.Serializable;

/**
 * Created by Collos on 7/12/2014.
 */
public interface Person extends Serializable{

    public static final long serialVersionUID = 0L;

    public String getLogin();

    public String getEmail();

    public String getPass();

}
