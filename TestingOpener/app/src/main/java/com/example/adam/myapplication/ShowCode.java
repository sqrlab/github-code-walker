package com.example.adam.myapplication;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.webkit.WebView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Adamin on 2016-08-18.
 * ShowCode
 * Param: Context c, Webview file
 *
 * GrabCode
 * Param: String URL
 * Display the file's contents on the screen.
 *
 * openIt
 * Param: String
 * Calls GrabCode
 */
public class ShowCode{
    Context context;
    WebView webView;
    public ShowCode(Context context, WebView webView){
        this.webView = webView;
        this.context = context;
    }

    protected class GrabCode extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            Log.d("xXx",params[0]);
            try {

                //Params[0] is the url of the download_file
                Log.d("xXx",params[0]);
                URL url = new URL(params[0]);

                //Split up the url based on .'s
                String a = params[0];
                String[] cut = a.split("\\.");


                //Grab the file type, used in setting the syntax highlighting, from the last cut[]
                String type = "java";
                type = cut[cut.length - 1];

                BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
                String code = "";
                String str;

                //Opening Tags
                code += "<!DOCTYPE html><html><head>" +
                        "<link rel='stylesheet' type='text/css' href='highlight/styles/github.css'>" +
                        "<script src='highlight/highlight.pack.js' type='text/javascript'></script>" +
                        "<script src='highlightnumber/src/highlightjs-line-numbers.js' type='text/javascript'></script>" +
                        "<script>hljs.initHighlightingOnLoad();</script>" +
                        "<script>hljs.initLineNumbersOnLoad();</script></head>" +
                        "<body><pre style='word-wrap: break-word; white-space: pre-wrap;'><code class='" + type + "'>";

                while ((str = in.readLine()) != null) {
                    //Grabbing the characters from the file.
                    code = code + str + "\n";
                }
                in.close();

                //Closing Tags
                code += "</code></pre></body></html>";
                return code;
            } catch (MalformedURLException e) {
                Log.d("x", "Malformed ");
          } catch (IOException e) {
                Log.d("x", "IO");
            }
            return "";
        }

        //Loading the webview with the file data.
        @Override
        protected void onPostExecute(String val) {
        super.onPostExecute(val);
            webView.loadDataWithBaseURL("file:///android_asset/", val, "text/html", "utf-8", null);
        }
    }
    public void openIt(String a){
        GrabCode grabCode = new GrabCode();
        grabCode.execute(a);
    }
}
