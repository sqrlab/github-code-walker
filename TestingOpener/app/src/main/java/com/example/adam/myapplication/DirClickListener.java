package com.example.adam.myapplication;

import android.content.Context;
import android.view.View;

/**
 * Created by Adamin on 2016-08-19.
 */
public abstract class DirClickListener implements View.OnClickListener {
    String fullpath;
    Context context;

    public DirClickListener(Context context, String fullpath) {
        super();
        this.context = context;
        this.fullpath = fullpath;
    }
}