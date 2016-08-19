package com.example.adam.myapplication;

import android.graphics.Color;
import android.view.View;

/**
 * Created by Adamin on 2016-08-15.
 */
public class SelectHighlight {
    View past;
    public SelectHighlight(){
        past = null;
    }
    public void highlight(View view){
        if (past != null){
            past.setBackgroundColor(Color.WHITE);
        }
        past = view;
        view.setBackgroundColor(Color.LTGRAY);
    }
}
