package com.example.adam.myapplication;

import com.unnamed.b.atv.model.TreeNode;

/**
 * Created by Adamin on 2016-08-03.
 */
public abstract class NavTreeListener implements TreeNode.TreeNodeClickListener {
    String url = "";

    public NavTreeListener(String url){
        this.url = url;
    }
}
