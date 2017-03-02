package com.shertech.mynotes;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.JsonReader;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * Created by lastwalker on 2/22/17.
 */

public class AsyncLoaderTask extends AsyncTask<List<share>,Void,List<share>> {

    private static final String TAG = "AsyncLoaderTask";
    private MainActivity mainActivity;
    private int count;

    public AsyncLoaderTask(MainActivity ma) {
        mainActivity = ma;
    }

    @Override
    protected List<share> doInBackground(List<share>... params) {
        List<share> json = null;
        try{
            json = readNotes(mainActivity);
            //readNotepad(mainActivity);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return json;
    }
    public List<share> readNotes(MainActivity ma) throws IOException{
        List<share> json = null;

        try {
            json = readNotepad(ma);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return json;
    }


    @Override
    protected void onPostExecute(List<share> s)
    {if( s != null ){
        Collections.sort(s, noteComparator);
        mainActivity.notecheck=1;
        mainActivity.setNoteList(s);
        super.onPostExecute(s);
    }else{
        mainActivity.setNoteList(s);
    }}
    public List<share> readNotepad(MainActivity ma) throws IOException {
        InputStream is = ma.getApplicationContext().openFileInput(ma.getString(R.string.file_name1));
        JsonReader reader = new JsonReader(new InputStreamReader(is, "UTF-8"));
        try {
            return readMessagesArray(reader);
        } finally {
            reader.close();
        }}
    public List<share> readMessagesArray(JsonReader reader) throws IOException {
        List<share> messages = new ArrayList<share>();

        reader.beginArray();
        while (reader.hasNext()) {
            messages.add(readMessage(reader));
        }
        reader.endArray();
        return messages;
    }
    public share readMessage(JsonReader reader) throws IOException {
        share qNotes = new share();
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("ID")) {
                qNotes.setID(Integer.parseInt(reader.nextString()));
            } else if (name.equals("Title")) {
                qNotes.setTitle(reader.nextString());
            }else if (name.equals("Name")) {
                qNotes.setName(reader.nextString());
            } else if (name.equals("Description")) {
                qNotes.setDescription(reader.nextString());
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return qNotes;
    }

    public static Comparator<share> noteComparator = new Comparator<share>() {

        @Override
        public int compare(share quickNotes, share t1) {
            //quickNotes.getTime();
            Date start = null,end=null;
            DateFormat df = new SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss z");
            try {
                start = new SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss z").parse(quickNotes.getName());
                end = new SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss z").parse(t1.getName());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Log.d(TAG, "parseJSON: " + quickNotes.getName());

            if( end.compareTo(start) == 0) {
                return (t1.getID() - quickNotes.getID());
            }
            else
                return end.compareTo(start);
        }

    };
}
