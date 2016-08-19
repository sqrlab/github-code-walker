package com.example.adam.myapplication;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Adamin on 2016-08-18.
 * Param: String String fileURL
 * Grabs the download url from the file
 */
public class PullURLInfo extends AsyncTask<String, Void, String> {
    private Exception e;

    protected String doInBackground(String... paramURL){
        try{
            //Set the url as the given values
            URL url = new URL(paramURL[0]);

            Log.d("realurl", "start");
            //Open a buffered reader for the webpage
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            Log.d("realurl", "Worked");

            String loadedPage = "";
            String TempPage;
            while ((TempPage = in.readLine()) != null) {
                loadedPage += TempPage;
            }
            in.close();

            //Here we parse through the text of the api page to find the download url
            // It's easily identifiable by it's https:\\raw beggining
            Pattern p = Pattern.compile("https:\\/\\/raw[^\"]+");
            Matcher m = p.matcher(loadedPage);
            boolean b = m.find();

            //If we find a match, we procced to return the download url to be used as a refrence
            if (b) {
                String matched = m.group();
                Log.d("Urls?", matched);

                return matched;
            }else{
                //If there wasn't a match for some reason (if a folder got sent here somehow)
                // then we just return the page we just read

                //ToDo: Return a value that doesn't cause exceptions but will show blank instead of the API page
                return paramURL[0];
            }
        }catch (Exception e){
            this.e = e;
            Log.d("AsyncException", e.toString());
            return null;
        }
    }
}