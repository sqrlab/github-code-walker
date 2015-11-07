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
import android.widget.LinearLayout;
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

import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends AppCompatActivity {

    private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getReposFile getRepo = new getReposFile();
        getRepo.execute();

        //Textview Code area;
        textView = (TextView) findViewById(R.id.text);
        textView.setMovementMethod(new ScrollingMovementMethod());

        //Creating the Asynch task
        getCode coder = new getCode();
        //Calling asynch.
        coder.execute("https://raw.githubusercontent.com/Adam-Anthony/JAX-/master/Java/BackEnd.java");


    }

    public void openIt(String a){
        //Creating the Asynch task
        getCode coder = new getCode();
        //Calling asynch.
        coder.execute(a);
    }

    public void openA(View view){
        openIt("https://raw.githubusercontent.com/Adam-Anthony/JAX-/master/Java/A%20-%20Run.bat");
    }
    public void openB(View view){
        openIt("https://raw.githubusercontent.com/Adam-Anthony/JAX-/master/Java/BackEnd.java");
    }
    public void openC(View view){
        openIt("https://raw.githubusercontent.com/Adam-Anthony/JAX-/master/Java/ReadMe.txt");
    }
    public void openD(View view){
        openIt("https://raw.githubusercontent.com/Adam-Anthony/JAX-/master/Java/Test.java");
    }

    protected class getReposFile extends AsyncTask<Void, Void, Void>{
        //private String[] alps;
        protected Void doInBackground (Void... params){
            StoreCredentials credentials = new StoreCredentials(getApplicationContext());
            credentials.storeToken(getString(R.string.token));
            SimpleDeveloperCredentialsProvider credentialsProvider = new SimpleDeveloperCredentialsProvider(null, null, null);

            GithubDeveloperCredentials.getInstance();
            GithubDeveloperCredentials.init(credentialsProvider);

            final RepoInfo repoInfo = new RepoInfo();
            repoInfo.owner = "Adam-Anthony";
            repoInfo.name = "JAX-";

            final FileInfo fileInfo = new FileInfo();
            fileInfo.repoInfo = repoInfo;


            GetRepoContentsClient contentsClient = new GetRepoContentsClient(getApplicationContext(),repoInfo, "Java");
            contentsClient.setOnResultCallback(new BaseClient.OnResultCallback<List<Content>>() {
                @Override
                public void onResponseOk(final List<Content> contents, Response response) {
                    try {
                        final FileInfo fileInfo1 = new FileInfo();
                        fileInfo1.repoInfo = repoInfo;
                        fileInfo1.name = contents.get(0).name;
                        fileInfo1.content = contents.get(0).content;
                        fileInfo1.path = contents.get(0).path;
                        for (int i = 0; i < 3; i++) {
                            LinearLayout linearLayout = (LinearLayout) findViewById(R.id.fileList);
                            TextView tv1 = new TextView(getApplicationContext());
                            tv1.setText(contents.get(i).name);
                            tv1.setPadding(5, 5, 5, 5);
                            tv1.setClickable(true);
                            tv1.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    GetFileContentClient fileContentClient = new GetFileContentClient(getApplication(), fileInfo1);
                                    fileContentClient.execute();
                                    textView.setText("abcdefghijklmnopq");
                                }
                            });
                            linearLayout.addView(tv1);
                        }

                        String x = contents.get(0).name;
                        /* Intent sending.
                        Intent intent = new Intent(getApplicationContext(), getReposFile.class);
                        intent.putExtra("val", x);
                        startActivity(intent);
                        */
                        //Send intent with Strings to be caught?
                    } catch (Exception e) {

                    }
                }

                @Override
                public void onFail(RetrofitError retrofitError) {

                }
            });
            contentsClient.execute();

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
            linearLayout.addView(tv1);
            FileInfo fileInfo = new FileInfo();
        }
    }

    protected class getCode extends AsyncTask<String, String, String>{
        @Override
        protected String doInBackground(String... params) {
            try {
                //ToDo: Read repo url, and display the different files within for selection.

                //Todo: Make the selectable files open their download urls and in this asynch task.

                URL url = new URL(params[0]);
                BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
                String code = "";
                String str;
                while ((str = in.readLine()) != null) {
                    code = code + str + "\n";
                }
                in.close();
                return code;
            } catch (MalformedURLException e) {
                Log.d("x", "Malformed ");
            } catch (IOException e) {
                Log.d("x", "IO");
            }
            return "";
        }

        @Override
        protected void onPostExecute(String val) {
            super.onPostExecute(val);
            textView.setText(val);
        }
    }
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
