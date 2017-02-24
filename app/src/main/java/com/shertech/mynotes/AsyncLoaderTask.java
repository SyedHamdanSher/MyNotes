package com.shertech.mynotes;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.JsonReader;
import android.util.Log;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by lastwalker on 2/22/17.
 */

public class AsyncLoaderTask extends AsyncTask<share,Void,share> {

    private static final String TAG = "AsyncLoaderTask";
    private eNode eNActivity;
    //private MainActivity mainActivity;
    share msp;
    int n=999999,flag=0;


    public AsyncLoaderTask(eNode ma,share s) {
        eNActivity = ma;
        msp=s;
        flag=1;
        //this.n=n;

    }

   /* public AsyncLoaderTask(MainActivity ma,share s) {
        mainActivity = ma;
    }*/

    @Override
    protected share doInBackground(share... params) {
        Log.d(TAG, "loadFile: Loading JSON File");
        try {
            InputStream is = eNActivity.openFileInput(eNActivity.getString(R.string.file_name));
            JsonReader reader = new JsonReader(new InputStreamReader(is, eNActivity.getString(R.string.encoding)));
            reader.beginObject();
            while (reader.hasNext()) {
                String name = reader.nextName();
                if (name.equals("etTitle")) {
                    msp.setTitle(reader.nextString());
                }else if (name.equals("etNode")) {
                    msp.setDescription(reader.nextString());}
                else if (name.equals("compare")) {
                    msp.setCompare(reader.nextString());}
                else if (name.equals("tvTime")) {
                    msp.setName(reader.nextString());
                }else{
                    reader.skipValue();
                }
            }
            reader.endObject();
        } catch (FileNotFoundException e) {
            Toast.makeText(eNActivity, eNActivity.getString(R.string.no_file), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(share s) {
        eNActivity.setMSP(msp);
    }

}
