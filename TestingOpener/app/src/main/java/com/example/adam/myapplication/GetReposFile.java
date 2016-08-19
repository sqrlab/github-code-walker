package com.example.adam.myapplication;

/**
 * Created by Adamin on 2016-08-19.
 */

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.alorma.github.basesdk.client.BaseClient;
import com.alorma.github.basesdk.client.StoreCredentials;
import com.alorma.github.basesdk.client.credentials.GithubDeveloperCredentials;
import com.alorma.github.basesdk.client.credentials.SimpleDeveloperCredentialsProvider;
import com.alorma.github.sdk.bean.dto.response.Content;
import com.alorma.github.sdk.bean.info.FileInfo;
import com.alorma.github.sdk.bean.info.RepoInfo;
import com.alorma.github.sdk.services.repo.GetRepoContentsClient;

import java.util.List;

import retrofit.RetrofitError;
import retrofit.client.Response;

public class GetReposFile extends AsyncTask<String, Void, Void> {
    final String INTENT_PARM = "RepoInformation";
    Context context;
    public GetReposFile(Context context){
        this.context = context;
    }
    protected Void doInBackground(String... params) {
        //Storing credentials
        StoreCredentials credentials = new StoreCredentials(context);

        //Token
        credentials.storeToken(context.getString(R.string.token));

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
        GetRepoContentsClient contentsClient = new GetRepoContentsClient(context, repoInfo, params[0]);
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

                    Log.d("api", response.getUrl());

                    String file;
                    Intent contentIntent = new Intent();

                    contentIntent.putExtra("size", contents.size());
                    contentIntent.putExtra("path", contents.get(0).path);

                    for (int i = 0; i < contents.size(); i++) {
                        file = "Folder";
                        if (contents.get(i).isFile()) {
                            file = "File";
                        }
                        Log.d("FileType", file);

                        String[] filler = new String[3];
                        filler[0] = contents.get(i).name;
                        filler[1] = contents.get(i).url;
                        filler[2] = file;

                        contentIntent.putExtra("Content" + String.valueOf(i), filler);
                        contentIntent.setAction(INTENT_PARM);

                        Log.d("Name", contents.get(i).name);

                    }
                    Log.d("Intent", "Sending");

                    LocalBroadcastManager.getInstance(context).sendBroadcast(contentIntent);

                    Log.d("Intent", "Sent");
                    //String x = contents.get(0).name;
                    // /* Intent sending.
                    // Intent intent = new Intent(getApplicationContext(), getReposFile.class);
                    // intent.putExtra("val", x);
                    // startActivity(intent);
                    // */


                    //Send intent with Strings to be caught?
                } catch (Exception e) {
                    e.printStackTrace();
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
        //Executing the contentsClient.

        contentsClient.execute();
        Log.d("background", "Finished");

        return null;

    }
}
