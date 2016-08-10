package com.example.adam.myapplication;

import android.app.ActionBar;

import android.content.Context;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;

import android.content.res.ColorStateList;
import android.graphics.Color;

import android.os.AsyncTask;

import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alorma.github.basesdk.client.BaseClient;
import com.alorma.github.basesdk.client.StoreCredentials;
import com.alorma.github.basesdk.client.credentials.GithubDeveloperCredentials;
import com.alorma.github.basesdk.client.credentials.SimpleDeveloperCredentialsProvider;
import com.alorma.github.sdk.bean.dto.response.Content;
import com.alorma.github.sdk.bean.info.FileInfo;
import com.alorma.github.sdk.bean.info.RepoInfo;
import com.alorma.github.sdk.services.repo.GetRepoContentsClient;
import com.unnamed.b.atv.model.TreeNode;
import com.unnamed.b.atv.view.AndroidTreeView;

import org.w3c.dom.Text;

import java.io.BufferedReader;
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
    private Button button;
    private WebView webView;
    private LinearLayout scrollView;
    final String INTENT_PARM = "RepoInformation";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Opening Repo Files
        getReposFile getRepo = new getReposFile();
        getRepo.execute("app");

        //Textview Code area;
        webView = (WebView) findViewById(R.id.web);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        LocalBroadcastManager.getInstance(this).registerReceiver(br, new IntentFilter(INTENT_PARM));


        //HARDCODED STARTING PAGE
        openIt("https://raw.githubusercontent.com/Adam-Anthony/JAX-/master/Java/BackEnd.java");

    }

    private String grabFile(String url) {
        return "filedata";
    }

    private String grabName(String url) {
        return "name";
    }

    private boolean isFolder(String url) {
        return true;
    }


    //Broadcast reciever to grab information from the RepoInfo
    private BroadcastReceiver br = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("BR", "hit");

            //Reset the linearlayout's view.
            LinearLayout linearLayout = (LinearLayout) findViewById(R.id.fileList);
            linearLayout.removeAllViewsInLayout();

            //Returning back to the beggining
            TextView backOne = new TextView(MainActivity.this);
            backOne.setText("...");
            backOne.setPadding(4,15,4,15);

            backOne.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getReposFile backApp = new getReposFile();
                    backApp.execute("app");
                }
            });
            linearLayout.addView(backOne);

            int i = 0;
            boolean moreExtras = intent.hasExtra("Content0");
            while (moreExtras) {
                //Grabbing each ArrayExtra one by one.
                String[] info = intent.getStringArrayExtra("Content" + String.valueOf(i));

                //Setting up variables as they are stored
                String name = info[0];
                String url = info[1];
                String type = info[2];
                TextView sidebarPiece = new TextView(MainActivity.this);
                sidebarPiece.setHorizontallyScrolling(true);
                sidebarPiece.setPadding(4,15,4,15);
                //File
                if (type.compareTo("Folder")!=0) {
                    try {
                        pullURLInfo pullURL = new pullURLInfo();
                        String rawURL = pullURL.execute(url).get();
                        //t.setClickable(true);
                        Log.d("clicker", "making");

                        sidebarPiece.setText(name);
                        sidebarPiece.setOnClickListener(new NavClickListener(rawURL) {
                            @Override
                            public void onClick(View view) {
                                openIt(url);

                            }
                        });

                        Log.d("clicker", "made");
                    } catch (ExecutionException | InterruptedException ei) {
                        Log.d("ExecutionErr", ei.toString());
                    }
                    //end File
                } else {
                    //Folder
                    sidebarPiece.setText(name);
                    sidebarPiece.setTextColor(Color.BLUE);
                    sidebarPiece.setOnClickListener(new NavClickListener(info[1]) {
                        @Override
                        public void onClick(View v) {
                            getReposFile grf = new getReposFile();
                            grf.execute(url);
                        }
                    });
                }

                //Add at the end
                linearLayout.addView(sidebarPiece);

                i++;
                moreExtras = intent.hasExtra("Content"+String.valueOf(i));
            }
            //The amount is set at 4
            //ToDo: set dynamic size by going until there isn't any more content.
        }
    };

    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(br);
        super.onDestroy();
    }

    public void openIt(String a) {
        //Creating the Asynch task
        //String a: URL of download_file

        getCode coder = new getCode();
        //Calling asynch.

        coder.execute(a);
    }


//

    /*
    public void hide(View view){
        scrollView.setVisibility(view.GONE);
    }
    */

    //
    // Grabbing the repository information so we can actually access the folders and files
    //
    protected class getReposFile extends AsyncTask<String, Void, Void> {

        protected Void doInBackground(String... params) {
            //Storing credentials
            StoreCredentials credentials = new StoreCredentials(getApplicationContext());

            //Token
            credentials.storeToken(getString(R.string.token));

            SimpleDeveloperCredentialsProvider credentialsProvider = new SimpleDeveloperCredentialsProvider(null, null, null);

            //Checking in credentials
            GithubDeveloperCredentials.getInstance();
            GithubDeveloperCredentials.init(credentialsProvider);

            Log.d("Background","Started");

            //Setting up the repo info
            final RepoInfo repoInfo = new RepoInfo();

            //Repo owner name and Repository name
            repoInfo.owner="omkarmoghe";
            repoInfo.name="Pokemap";
            //Repo Owner name and Repository name


            //File info based on the repository information
            final FileInfo fileInfo = new FileInfo();
            fileInfo.repoInfo=repoInfo;

            //Grabbing the Repo Content
            GetRepoContentsClient contentsClient = new GetRepoContentsClient(getApplicationContext(), repoInfo, params[0]);
            //Folder Name

            contentsClient.setOnResultCallback(new BaseClient.OnResultCallback<List<Content>>() {
                //OK Response
                @Override
                public void onResponseOk ( final List<Content> contents, Response response){
                    //List<Content>:
                    //Response:

                    try {
                        final FileInfo fileInfo1 = new FileInfo();
                        fileInfo1.repoInfo = repoInfo;
                        fileInfo1.name = contents.get(0).name;
                        fileInfo1.content = contents.get(0).git_url;
                        fileInfo1.path = contents.get(0).path;
                        fileInfo1.head = null;

                        Log.d("api", response.getUrl());


                        String file;
                        Intent contentIntent = new Intent();
                        contentIntent.setAction(INTENT_PARM);

                        for (int i = 0; i < contents.size(); i++) {
                            file = "Folder";
                            String second;
                            if (contents.get(i).isFile()) {
                                file = "File";
                                Log.d("XXXXX","xxx");
                                second = contents.get(i).git_url;
                                Log.d("XXXXX",second);
                            }else{
                                second = contents.get(i).path;
                            }
                            Log.d("FileType", file);

                            String[] filler = new String[3];
                            filler[0] = contents.get(i).name;
                            filler[1] = second;
                            filler[2] = file;

                            contentIntent.putExtra("Content" + String.valueOf(i), filler);

                            Log.d("Name", contents.get(i).name);

                        }
                        Log.d("Intent", "Sending");

                        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(contentIntent);

                        Log.d("Intent", "Sent");
                        //String x = contents.get(0).name;
                        /* Intent sending.
                        Intent intent = new Intent(getApplicationContext(), getReposFile.class);
                        intent.putExtra("val", x);
                        startActivity(intent);
                        */


                        //Send intent with Strings to be caught?
                    } catch (Exception e) {

                    }
                }
                //OK Response

                //Fail Response
                @Override
                public void onFail (RetrofitError retrofitError){

                }
                //Fail Response
            });
            //Repo Content grabbed.

            contentsClient.execute();
            //Executing the contentsClient.


            Log.d("background","Finished");

            return null;

        }

        protected void onPostExecute() {

            /* Intent recieving
            Intent intent = getIntent();
            String str = intent.getStringExtra("var");
            */
            finish();
            //FileInfo fileInfo = new FileInfo();
            //Create a file info
        }
    }
    ////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////
    //An off main thread class to grab information from the internet
    protected class pullURLInfo extends AsyncTask<String, Void, String>{
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

//
// AsynchTask for reading the characters from the file  
//
    protected class getCode extends AsyncTask<String, String, String>{
        @Override
        protected String doInBackground(String... params) {
            try {

                //Params[0] is the url of the download_file
                URL url = new URL(params[0]);

                //Split up the url based on .'s
                String a = params[0];
                String[] cut = a.split("\\.");
                

                //Grab the file type, used in setting the syntax highlighting, from the last cut[]
                String type = "java";
                type = cut[cut.length-1];
                
                BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
                String code = "";
                String str;
                
                //Opening Tags
                code += "<!DOCTYPE html><html><head>" +
                        "<link rel='stylesheet' type='text/css' href='highlight/styles/github.css'>" +
                        "<script src='highlight/highlight.pack.js' type='text/javascript'></script>" +
                        "<script src='highlightnumber/src/highlightjs-line-numbers.js' type='text/javascript'></script>" +
                        "<script>hljs.initHighlightingOnLoad();</script>" + 
                        "<script>hljs.initLineNumbersOnLoad();</script></head>"  +
                        "<body><pre style='word-wrap: break-word; white-space: pre-wrap;'><code class='"+type+"'>";
                
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
            webView.loadDataWithBaseURL("file:///android_asset/",val, "text/html", "utf-8",null);
        }
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

