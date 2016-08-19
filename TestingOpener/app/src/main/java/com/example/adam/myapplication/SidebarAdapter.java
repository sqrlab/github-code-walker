package com.example.adam.myapplication;

import android.content.Context;
import android.media.Image;
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

    SelectHighlight selectHighlight;
    private static LayoutInflater inflater=null;

    public SidebarAdapter(MainActivity mainActivity, WebView webview, GitFile[] filesList){
        context = mainActivity;
        this.webView = webview;
        this.filesList = filesList;

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
        holder.img = (ImageView) rowView.findViewById(R.id.icon);
        holder.tv = (TextView) rowView.findViewById(R.id.filename);

        if (isDir){
            Log.d("DIRECTORY", filename);
            holder.img.setImageResource(R.drawable.foldericon);

        }else{
            Log.d("FILE",filename);
            holder.img.setImageResource(R.drawable.fileicon);
            //Draw file Icon

            final ShowCode showCode = new ShowCode(context, webView);
            //Create the showcode

            //make it a clicker
            rowView.setOnClickListener(new HighlightListener(selectHighlight, urlname, showCode){
                public void onClick(View v){
                    selector.highlight(v);
                    showCode.openIt(rawUrl);
                }
            });

        }

        holder.tv.setText(filesList[position].getName());
        return rowView;
    }
}
