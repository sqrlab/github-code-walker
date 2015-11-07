package com.example.adam.tabletviewer;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.alorma.github.basesdk.client.*;
import com.alorma.github.basesdk.ApiClient.*;


import com.alorma.github.basesdk.client.credentials.GithubDeveloperCredentials;
import com.alorma.github.basesdk.client.credentials.SimpleDeveloperCredentialsProvider;
import com.alorma.github.sdk.bean.dto.response.Content;
import com.alorma.github.sdk.bean.info.RepoInfo;
import com.alorma.github.sdk.security.GitHub;
import com.alorma.github.sdk.services.repo.GetRepoContentsClient;
import com.alorma.github.sdk.services.repos.*;

import org.w3c.dom.Text;

import java.io.File;
import java.util.List;

import retrofit.RetrofitError;
import retrofit.client.Client;
import retrofit.client.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView textView = (TextView) findViewById(R.id.codeArea);

        StoreCredentials credentials = new StoreCredentials(getApplicationContext());
        credentials.storeToken("cffa6bf2b4a5151b6d2f7fed99a2e222c19c8f1f");
        SimpleDeveloperCredentialsProvider a = new SimpleDeveloperCredentialsProvider(null, null, null);

        GithubDeveloperCredentials.getInstance();
        GithubDeveloperCredentials.init(a);

        RepoInfo repoInfo = new RepoInfo();
        repoInfo.owner = "Adam-Anthony";
        repoInfo.name = "JAX-";
        //repoInfo.branch = "Java";

        GetRepoContentsClient repoContentsClient = new GetRepoContentsClient(getApplicationContext(), repoInfo);
        repoContentsClient.setStoreCredentials(credentials);
        repoContentsClient.setOnResultCallback(new BaseClient.OnResultCallback<List<Content>>() {
                                                   @Override
                                                   public void onResponseOk(List<Content> contentList, Response response) {
                                                       Log.d("Github", "onResponseOK: " + response);
                                                       textView.setText(response.getUrl());

                                                   }

                                                   @Override
                                                   public void onFail(RetrofitError retrofitError) {
                                                       Log.d("Github", "onFail: " + retrofitError);
                                                   }
                                               }

        );
        repoContentsClient.execute();

    }

    /*
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
    */
}
