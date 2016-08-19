package com.example.adam.myapplication;

import android.app.ActionBar;

import android.content.Context;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;

import android.content.res.ColorStateList;
import android.graphics.Color;

import android.os.AsyncTask;

import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;


import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.alorma.github.basesdk.client.BaseClient;
import com.alorma.github.basesdk.client.StoreCredentials;
import com.alorma.github.basesdk.client.credentials.GithubDeveloperCredentials;
import com.alorma.github.basesdk.client.credentials.SimpleDeveloperCredentialsProvider;
import com.alorma.github.sdk.bean.dto.response.Content;
import com.alorma.github.sdk.bean.info.FileInfo;
import com.alorma.github.sdk.bean.info.RepoInfo;
import com.alorma.github.sdk.services.repo.GetRepoContentsClient;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit.RetrofitError;
import retrofit.client.Response;



public class MainActivity extends AppCompatActivity {
    private WebView webView;
    //private LinearLayout scrollView;
    final String INTENT_PARM = "RepoInformation";
    final String INTENT_BAR = "ActionBarUpdate";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setTitle("Code Walker");

        //Opening Repo Files
        GetReposFile g = new GetReposFile(this);
        g.execute("app");

        //Textview Code area;
        webView = (WebView) findViewById(R.id.web);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setBuiltInZoomControls(true);

        LocalBroadcastManager.getInstance(this).registerReceiver(br, new IntentFilter(INTENT_PARM));
        LocalBroadcastManager.getInstance(this).registerReceiver(actionbar, new IntentFilter(INTENT_BAR));
    }

    private BroadcastReceiver actionbar = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            getSupportActionBar().setTitle(intent.getStringExtra("title"));
        }
    };

    //Broadcast reciever to grab information from the RepoInfo
    private BroadcastReceiver br = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("BR", "hit");

            //Reset the linearlayout's view.
            ScrollView scrollView = (ScrollView) findViewById(R.id.scrollView2);
            scrollView.removeAllViewsInLayout();

            int size = intent.getIntExtra("size",1);
            SelectHighlight selectHighlight = new SelectHighlight();
            String path = intent.getStringExtra("path");


            Log.d("Full path",path);

            //back button
            //Returning back to the beggining

            File filePath = new File(path);
            String backPath = filePath.getParentFile().getPath();
            Log.d("Fill path",backPath);

            GitFile[] files;
            String[] urlList;
            int folderAdjust = 0;

            if (backPath.compareTo("app")!=0) {
                String dotdot = filePath.getParentFile().getParentFile().getPath();
                Log.d("NotApp","NotApp");
                folderAdjust = 1;

                files = new GitFile[size+1];
                files[0] = new GitFile();
                files[0].setPath(dotdot);
            }else{
                Log.d("app","app");
                files = new GitFile[size];

            }
            // FileList creator

            int offset;
            for (int i = 0; i < size; i++){
                offset = i + folderAdjust;
                String[] info = intent.getStringArrayExtra("Content" + String.valueOf(i));

                String name = info[0];
                String url = info[1];
                String file = info[2];

                if (file.compareTo("File")==0) {
                    try {
                        Log.d("URL", url);
                        PullURLInfo pullURLInfo = new PullURLInfo();
                        String rawurl = pullURLInfo.execute(url).get();
                        files[offset] = new GitFile(name, rawurl, false);
                    }catch(Exception e){
                        Log.d("Accidental", "Problem");
                        e.printStackTrace();
                        files[offset] = new GitFile("aa", "google.com", true);
                    }
                }else{

                    files[offset] = new GitFile(name,backPath);
                }
            }

            ListView listView = new ListView(MainActivity.this);
            listView.setHorizontalScrollBarEnabled(true);
            listView.setAdapter(new SidebarAdapter(MainActivity.this, webView, files));

            scrollView.addView(listView);

            Log.d("SIZE", ""+size);

        }
    };

    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(br);
        super.onDestroy();
    }


    //Options Item Selected, generated on startup
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}

