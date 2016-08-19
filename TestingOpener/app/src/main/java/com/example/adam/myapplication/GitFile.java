package com.example.adam.myapplication;

/**
 * Created by Adamin on 2016-08-18.
 */
public class GitFile {
    String name;
    String path;
    String rawUrl;
    boolean isDir;

    private String adjustedText;

    /*
    * Constructors
    */

    // Empty Constructor
    public GitFile(){
        name = "..";
        path = "";
        rawUrl = "";
        isDir = true;
        adjustText();
    }

    // File Constructor
    public GitFile(String name, String rawUrl, boolean isDir){
        this.name = name;
        this.path = "";
        this.rawUrl = rawUrl;
        this.isDir = isDir;
        adjustText();
    }

    //Directory / folder  constructor
    public GitFile(String name, String path){
        this.name = name;
        this.path = path + "/" + name;
        this.rawUrl = "";
        isDir = true;
        adjustText();
    }
    /*
    * Constuctors
    */

    private void adjustText(){
        adjustedText = String.format("%1$-" + 30 + "s", name);
    }

    public void setPath(String path){
        this.path = path;
    }

    public void setRawUrl(String rawUrl){
        this.rawUrl = rawUrl;
    }

    public void setName (String name) { this.name = name; }

    public void setDir (Boolean isDir) {
        this.isDir = isDir;
    }

    public String getName(){
        return name;
    }

    public String getRawUrl(){
        return rawUrl;
    }

    public String getPath(){
        return path;
    }

    public boolean isDirectory(){
        return isDir;
    }

    public String getAdjustedText(){
        return adjustedText;
    }

}
