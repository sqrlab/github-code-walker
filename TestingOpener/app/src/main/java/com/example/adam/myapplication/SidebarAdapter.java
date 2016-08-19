package com.example.adam.myapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

/**
 * Created by Adamin on 2016-08-17.
 *
 * Listview adapater for our Sidebar
 */


public class SidebarAdapter extends BaseAdapter{
    GitFile[] filesList;
    Context context;
    WebView webView;
    GetReposFile getReposFile;
    SelectHighlight selectHighlight;
    private static LayoutInflater inflater=null;

    public SidebarAdapter(Context context, WebView webview, GitFile[] filesList){
        this.context = context;
        this.webView = webview;
        this.filesList = filesList;

        getReposFile = new GetReposFile(context);
        selectHighlight = new SelectHighlight();

        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount(){
        return filesList.length;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class Holder{
        TextView tv;
        ImageView img;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = new Holder();
        View rowView;
        Log.d("POSITION", "" + position);
        String filename = filesList[position].getName();
        String urlname = filesList[position].getRawUrl();
        String path = filesList[position].getPath();
        boolean isDir = filesList[position].isDirectory();

        rowView = inflater.inflate(R.layout.file_list, null);
        rowView.setBackgroundColor(Color.WHITE);
        holder.img = (ImageView) rowView.findViewById(R.id.icon);
        holder.tv = (TextView) rowView.findViewById(R.id.filename);

        holder.tv.setText(filesList[position].getAdjustedText());

        if (isDir){
            Log.d("DIRECTORY", filename);
            holder.img.setImageResource(R.drawable.foldericon);
            Log.d("PATH PATH PATH",path);
            rowView.setOnClickListener(new DirClickListener(context, path){
                @Override
                public void onClick(View v) {
                    GetReposFile g = new GetReposFile(context);
                    g.execute(fullpath);
                }
            });

        }else{
            Log.d("FILE",filename);
            holder.img.setImageResource(R.drawable.fileicon);
            //Draw file Icon
            ShowCode showCode = new ShowCode(context, webView);
            //Create the showcode

            //make it a clicker
            rowView.setOnClickListener(new HighlightListener(selectHighlight, filename, urlname, showCode){
                public void onClick(View v){
                    selector.highlight(v);
                    showCode.openIt(rawUrl);

                    Intent intent = new Intent();
                    intent.setAction("ActionBarUpdate");
                    intent.putExtra("title", name);
                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                }
            });

        }

        return rowView;
    }
}
