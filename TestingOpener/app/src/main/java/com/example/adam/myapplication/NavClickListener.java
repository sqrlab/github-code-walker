package com.example.adam.myapplication;

import android.view.View;

/**
 * Created by Adamin on 2016-07-27.
 * A custom onClickListener that get's passed a URL on setup
 * Useful in our work because it allows an easy way to set up the buttons
 * in code, with the url value available.
 */
public abstract class NavClickListener implements View.OnClickListener {
    String newFolder;
    public NavClickListener(String newFolder){
        super();
        this.newFolder = newFolder;
    }

    //We don't set the onClick method, so that we can customize it's use in our code.
    //For most of the files, it will just be calling OpenIt(url) in MainActivity

}
