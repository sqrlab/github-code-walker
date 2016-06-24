package com.example.adam.myapplication;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Parcel;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.internal.widget.ActivityChooserModel;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends AppCompatActivity {
    private Button button;
    private WebView webView;
    private ScrollView scrollView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Button?
        button = (Button)findViewById(R.id.button);

        //Scrollview?
        scrollView = (ScrollView)findViewById(R.id.scrollView2);

        //Opening Repo Files
        getReposFile getRepo = new getReposFile();
        getRepo.execute();

        //Textview Code area;
        webView = (WebView) findViewById(R.id.web);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        //HARDCODED STARTING PAGE
        openIt("https://raw.githubusercontent.com/Adam-Anthony/JAX-/master/Java/BackEnd.java");

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
            credentials.storeToken(getString(R.string.token));
            SimpleDeveloperCredentialsProvider credentialsProvider = new SimpleDeveloperCredentialsProvider(null, null, null);

            //Checking in credentials
            GithubDeveloperCredentials.getInstance();
            GithubDeveloperCredentials.init(credentialsProvider);

            //Setting up the repo info
            final RepoInfo repoInfo = new RepoInfo();

            //Repo owner name and Repository name
            repoInfo.owner = "Adam-Anthony";
            repoInfo.name = "JAX-";

            //File info based on the repository information
            final FileInfo fileInfo = new FileInfo();
            fileInfo.repoInfo = repoInfo;

            //Grabbing the Repo Content
            GetRepoContentsClient contentsClient = new GetRepoContentsClient(getApplicationContext(),repoInfo, "Java");
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
                        fileInfo1.content = contents.get(0).content;
                        fileInfo1.path = contents.get(0).path;
                        fileInfo1.head = null;


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

                        String file = "Folder";
                        if ( contents.get(0).isFile() ){
                            file = "File";
                        }
                        Log.d("FileType",file);

                        //Adding the buttons to the side click bar.
                        for (int i = 0; i < 3; i++) {
                            LinearLayout linearLayout = (LinearLayout) findViewById(R.id.fileList);
                            TextView tv1 = new TextView(getApplicationContext());
                            tv1.setText(contents.get(i).name);
                            tv1.setPadding(5, 5, 5, 5);


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
                            linearLayout.addView(tv1);
                            */
                        }

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

            return null;
        }

        protected void onPostExecute(){

            /* Intent recieving
            Intent intent = getIntent();
            String str = intent.getStringExtra("var");
            */
            LinearLayout linearLayout = (LinearLayout) findViewById(R.id.fileList);

            TextView tv1 = new TextView(getApplication());
            tv1.setText("ABCDEFGHIJKLMNOP");
            //Set a text view

            linearLayout.addView(tv1);
            //Add to the linear layout

            FileInfo fileInfo = new FileInfo();
            //Create a file info
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
