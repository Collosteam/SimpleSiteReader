package com.collosteam.simplesitereader.app.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;

    /**
      *
      */
public abstract class AbstractFragmentFactory {

    public abstract int getConteinerID();

    public void setFragmentToConteiner(Activity activity, Fragment fragment){
        activity.getFragmentManager().beginTransaction().replace(getConteinerID(),fragment).commit();
    }

}
