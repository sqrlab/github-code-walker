package com.example.adam.myapplication;

import android.app.ActionBar;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Parcel;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.internal.widget.ActionBarOverlayLayout;
import android.support.v7.internal.widget.ActivityChooserModel;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alorma.github.basesdk.client.BaseClient;
import com.alorma.github.basesdk.client.GithubDeveloperCredentialsProvider;
import com.alorma.github.basesdk.client.StoreCredentials;
import com.alorma.github.basesdk.client.credentials.GithubDeveloperCredentials;
import com.alorma.github.basesdk.client.credentials.SimpleDeveloperCredentialsProvider;
import com.alorma.github.sdk.bean.dto.response.Content;
import com.alorma.github.sdk.bean.info.FileInfo;
import com.alorma.github.sdk.bean.info.RepoInfo;
import com.alorma.github.sdk.services.content.GetFileContentClient;
import com.alorma.github.sdk.services.repo.GetRepoContentsClient;

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
    private ScrollView scrollView;
    final String INTENT_PARM =  "RepoInformation";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Button?
        //button = (Button)findViewById(R.id.button);

        //Scrollview?
        scrollView = (ScrollView)findViewById(R.id.scrollView2);

        //Opening Repo Files
        getReposFile getRepo = new getReposFile();
        getRepo.execute();

        //Textview Code area;
        webView = (WebView) findViewById(R.id.web);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        LocalBroadcastManager.getInstance(this).registerReceiver(br,new IntentFilter(INTENT_PARM));


        //HARDCODED STARTING PAGE
        openIt("https://raw.githubusercontent.com/Adam-Anthony/JAX-/master/Java/BackEnd.java");

    }

    private BroadcastReceiver br = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("BR", "hit");

            for (int i=0;i<4;i++) {
                String[] info = intent.getStringArrayExtra("Content" + String.valueOf(i));
                String name = info[0];
                String url = info[1];
                String type = info[2];

                Log.d("recieve", name);
                Log.d("recieve", url);
                TextView t = new TextView(MainActivity.this);
                t.setText(name);
                t.setPadding(4, 10, 10, 4);

                if (type.equals("File")) {

                    try {
                        pullURLInfo pullURL = new pullURLInfo();
                        String a = pullURL.execute(url).get();
                        t.setClickable(true);
                        Log.d("clicker", "making");
                        t.setOnClickListener(new NavClickListener(a) {
                            @Override
                            public void onClick(View v) {
                                openIt(url);
                            }
                        });
                        Log.d("clicker", "made");
                    }catch (ExecutionException | InterruptedException ei){
                        Log.d("ExecutionErr",ei.toString());
                    }
                } else {
                    t.setTextColor(Color.BLUE);
                    t.setClickable(true);
                    /*
                    t.setOnClickListener(new NavClickListener(url) {
                        @Override
                        public void onClick(View v) {
                            try {
                                URL urlObj = new URL(url);
                                BufferedReader in = new BufferedReader(new InputStreamReader(urlObj.openStream()));

                                String loadedPage = "";
                                String tempPage;
                                while ((tempPage = in.readLine()) != null){
                                    loadedPage += tempPage;
                                }
                                in.close();

                                Pattern p = Pattern.compile("\\{[.]*?\\}\\}");
                                Matcher m = p.matcher(url);
                                boolean b = m.find();
                                while (b){
                                    String links = m.group();
                                    Pattern pp = Pattern.compile("\"type\": [^,]+");
                                    Matcher mm = p.matcher(links);
                                    boolean bb = m.find();
                                    if (bb){
                                        String type = m.group();
                                    }else{

                                    }
                                    b = m.find();
                                }
                            }catch (IOException e){
                                    Log.d("Error", e.toString());
                            }
                        }
                    });
                    */

                    //Access URL

                    /*Grab data inside.
                    Name
                    Type?
                        dir
                            "_links":{
                                https:\\/\\/api[.]*?ref=dev
                            }
                        file
                            https:\\/\\/raw[^\"]+
                    */
                    /*
                    Pattern p = Pattern.compile("https:\\/\\/api[.]*?ref=dev");
                    Matcher m = p.matcher(loadedPage);
                    boolean b = m.find();
                    Log.d("Folder",url);
                    */
                }

                LinearLayout linearLayout = (LinearLayout) findViewById(R.id.fileList);
                linearLayout.addView(t);
                Log.d("Added", type);
            }
        }
    };

    protected void onDestroy(){
        LocalBroadcastManager.getInstance(this).unregisterReceiver(br);
        super.onDestroy();
    }

    public void openIt(String a){
        //Creating the Asynch task
        //String a: URL of download_file

        getCode coder = new getCode();
        //Calling asynch.

        coder.execute(a);
    }

//HARD CODED DOWNLOAD FILE URL ACCESS
    public void openA(View view){
        openIt("https://raw.githubusercontent.com/Adam-Anthony/JAX-/master/Java/A%20-%20Run.bat");
    }
    public void openB(View view){
        openIt("https://raw.githubusercontent.com/Adam-Anthony/JAX-/master/Java/BackEnd.java");
    }
    public void openC(View view){
        //openIt("https://raw.githubusercontent.com/Adam-Anthony/JAX-/master/Java/ReadMe.txt");

    }
    public void openD(View view){
        openIt("https://raw.githubusercontent.com/Adam-Anthony/JAX-/master/Java/Test.java");
    }
//HARD CODED DOWNLOAD FILE URL ACCESS

//
    //What's this for?
    public void hide(View view){
        scrollView.setVisibility(view.GONE);
    }

    //
    // Grabbing the repository information so we can actually access the folders and files
    //
    protected class getReposFile extends AsyncTask<Void, Void, Void>{
        //private String[] alps;
        protected Void doInBackground (Void... params){
            //Storing credentials
            StoreCredentials credentials = new StoreCredentials(getApplicationContext());

            //Token
            credentials.storeToken(getString(R.string.token));

            SimpleDeveloperCredentialsProvider credentialsProvider = new SimpleDeveloperCredentialsProvider(null, null, null);

            //Checking in credentials
            GithubDeveloperCredentials.getInstance();
            GithubDeveloperCredentials.init(credentialsProvider);

            Log.d("Background", "Started");

            //Setting up the repo info
            final RepoInfo repoInfo = new RepoInfo();

            //Repo owner name and Repository name
            repoInfo.owner = "omkarmoghe";
            repoInfo.name = "Pokemap";
            //Repo Owner name and Repository name


            //File info based on the repository information
            final FileInfo fileInfo = new FileInfo();
            fileInfo.repoInfo = repoInfo;

            //Grabbing the Repo Content
            GetRepoContentsClient contentsClient = new GetRepoContentsClient(getApplicationContext(),repoInfo, "app");
            //Folder Name

            contentsClient.setOnResultCallback(new BaseClient.OnResultCallback<List<Content>>() {

                //OK Response
                @Override
                public void onResponseOk(final List<Content> contents, Response response) {    
                    //List<Content>:    
                    //Response:         

                    try {
                        final FileInfo fileInfo1 = new FileInfo();
                        fileInfo1.repoInfo = repoInfo;
                        fileInfo1.name = contents.get(0).name;
                        fileInfo1.content = contents.get(0).git_url;
                        fileInfo1.path = contents.get(0).path;
                        fileInfo1.head = null;

                        Log.d("api",response.getUrl());


                        /*
                        URL apiPage = new URL(response.getUrl());
                        BufferedReader in = new BufferedReader(new InputStreamReader(apiPage.openStream()));

                        String loadedPage = "";
                        String TempPage;
                        while ((TempPage = in.readLine()) != null){
                            loadedPage += TempPage;
                        }
                        in.close();

                        Pattern p = Pattern.compile("https:\\/\\/raw[^\"]+");
                        Matcher m = p.matcher(loadedPage);
                        boolean b = m.matches();

                        if ( b ){
                            Log.d("Regex", "Matched")
                        }
                        */

                        //Adding the buttons to the side click bar.

                        //LinearLayout linearLayout = (LinearLayout) findViewById(R.id.fileList);

                        String file;
                        Intent contentIntent = new Intent();
                        contentIntent.setAction(INTENT_PARM);
                        for (int i = 0; i < 4; i++) {
                            file = "Folder";
                            if ( contents.get(i).isFile() ){
                                file = "File";
                            }
                            Log.d("FileType",file);

                            String[] filler = new String[3];
                            filler[0] = contents.get(i).name;
                            filler[1] = contents.get(i).url;
                            filler[2] = file;


                            contentIntent.putExtra("Content" + String.valueOf(i),filler);

                            //TextView tv1 = new TextView(getBaseContext());
                            //tv1.setText(contents.get(i).name);
                            //tv1.setPadding(5, 5, 5, 5);

                            Log.d("Name", contents.get(i).name);

                            /* Attempting to grab the file names
                            tv1.setClickable(true);
                            tv1.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    GetFileContentClient fileContentClient = new GetFileContentClient(getApplication(), fileInfo1);
                                    Content c = fileContentClient.executeSync();
                                    //textView.setText(c.name);
                                }
                            });
                            */

                            //linearLayout.addView(tv1);

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
                public void onFail(RetrofitError retrofitError) {

                }
                //Fail Response
            });
            //Repo Content grabbed.

            contentsClient.execute();
            //Executing the contentsClient.


            Log.d("background", "Finished");
            return null;
        }

        protected void onPostExecute(){

            /* Intent recieving
            Intent intent = getIntent();
            String str = intent.getStringExtra("var");
            */
            Log.d("Post", "Started");
            LinearLayout linearLayout = (LinearLayout) findViewById(R.id.fileList);

            TextView tv1 = new TextView(getBaseContext());
            tv1.setText("ABCDEFGHIJKLMNOP");
            tv1.setTextSize(22);
            tv1.setTextColor(Color.BLACK);
            tv1.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            );
            //Set a text view

            linearLayout.addView(tv1);
            //Add to the linear layout

            Log.d("Post", "Ended");

            finish();
            //FileInfo fileInfo = new FileInfo();
            //Create a file info
        }
    }

    protected class pullURLInfo extends AsyncTask<String, Void, String>{
        private Exception e;

        protected String doInBackground(String... paramURL){
            try{
                URL url = new URL(paramURL[0]);

                Log.d("realurl", "start");
                BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
                Log.d("realurl", "Worked");

                String loadedPage = "";
                String TempPage;
                while ((TempPage = in.readLine()) != null) {
                    loadedPage += TempPage;
                }
                in.close();

                Pattern p = Pattern.compile("https:\\/\\/raw[^\"]+");
                Matcher m = p.matcher(loadedPage);
                boolean b = m.find();
                if (b) {
                    String matched = m.group();
                    Log.d("Urls?", matched);

                    return matched;
                }else{
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
                //ToDo: Read repo url, and display the different files within for selection.

                //Todo: Make the selectable files open their download urls and in this asynch task.

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

