package com.example.adam.myapplication;

import android.view.View;

/**
 * Created by Adamin on 2016-07-27.
 */
public abstract class NavClickListener implements View.OnClickListener {
    String url;

    public NavClickListener(String url){
        this.url = url;
    }

}
