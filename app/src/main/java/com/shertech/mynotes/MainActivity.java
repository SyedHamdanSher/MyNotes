package com.shertech.mynotes;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.JsonReader;
import android.util.JsonWriter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener{


    public List<share> noteList = new ArrayList<>();  // Main content is here
    private RecyclerView recyclerView; // Layout's recyclerview
    private nodesAdapter nAdapter; // Data to recyclerview adapter
    public share msp=null;
    public int notecheck = 1;
    private static final String TAG = "MainActivity";
    private static final int B_REQ = 1; private static final int RESULT = 2;
    int counter=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.rvID);

        nAdapter = new nodesAdapter(noteList, this);

        recyclerView.setAdapter(nAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        notecheck=0;
        //msp = new share(1);
        noteList.clear();
        new AsyncLoaderTask(this).execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    protected void onResume() {
        super.onResume();
        recyclerView.setAdapter(nAdapter);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.addNote:
                counter++;
                notecheck=1;
                msp=null;
                share msp = new share(counter);
                Intent intent = new Intent(MainActivity.this, eNode.class);
                intent.putExtra(share.class.getName(), msp);
                startActivityForResult(intent, B_REQ);
                return true;
            case R.id.dinfo:
                Intent ss = new Intent(MainActivity.this, ApplicationInformation.class);
                startActivity(ss);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {
        int pos = recyclerView.getChildLayoutPosition(v);
        notecheck=1;
        msp=null;
        share m = noteList.get(pos);
        Intent intent = new Intent(MainActivity.this, eNode.class);
        intent.putExtra(share.class.getName(), m);
        startActivityForResult(intent, B_REQ);

    }
    @Override
    protected void onPause() {
        Collections.sort(noteList, noteComparator);
        recyclerView.setAdapter(nAdapter);
        if(noteList.size()!=0){
            writeNotepad(noteList, this);}
        super.onPause();
    }

    @Override
    public boolean onLongClick(View v) {
        int pos = recyclerView.getChildLayoutPosition(v);
        cancel(pos);
        return true;
    }
    public void setNoteList(List<share> NL){
        if(NL!=null){
        noteList.clear();
        noteList.addAll(NL);
            if(msp!=null){
                if(!msp.getTitle().equals("")){
                    int flag=0;
                    for (int i = 0; i < noteList.size(); i++) {
                        if (msp.getID() == noteList.get(i).getID()) {
                        noteList.get(i).setTitle(msp.getTitle());
                        noteList.get(i).setName(msp.getName());
                        Log.d(TAG, "Updatedata:" + msp.getName()+":"+noteList.get(i).getName());
                        noteList.get(i).setDescription(msp.getDescription());
                        flag = 1;
                        break;
                        }
                    }if (flag==0){
                        noteList.add(msp);
                    }
                    Collections.sort(noteList, noteComparator);
                    writeNotepad(noteList, this);
                }
                else
                    Toast.makeText(this,R.string.no_file,Toast.LENGTH_SHORT).show();
            }
            recyclerView.setAdapter(nAdapter);

            for(int i=0;i<noteList.size();i++)
                if(counter<noteList.get(i).getID())
                    counter = noteList.get(i).getID();

            nAdapter.notifyDataSetChanged();

        }else
            Toast.makeText(this,R.string.no_file,Toast.LENGTH_SHORT).show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if ( requestCode == B_REQ ) {
            if ( resultCode == RESULT ) {
                msp = (share) data.getSerializableExtra("USER_TEXT");
                if (msp!= null && notecheck==1) {
                    if (!msp.getTitle().equals("")) {
                        int flag = 0;
                        for (int i = 0; i < noteList.size(); i++) {
                            if (msp.getID() == noteList.get(i).getID()) {
                                noteList.get(i).setTitle(msp.getTitle());
                                noteList.get(i).setName(msp.getName());
                                noteList.get(i).setDescription(msp.getDescription());
                                flag = 1;
                                break;
                            }
                        }
                        if (flag == 0) {
                            noteList.add(msp);
                        }
                        Collections.sort(noteList, noteComparator);
                        writeNotepad(noteList, this);
                    }
                    else
                        Toast.makeText(this, getString(R.string.no_title), Toast.LENGTH_SHORT).show();
                }
                recyclerView.setAdapter(nAdapter);
            } else {
                Log.d(TAG, "onActivityResult: result Code: " + resultCode);
            }
        } else {
            Log.d(TAG, "onActivityResult: Request Code " + requestCode);
        }
    }
    public void cancel(final int pos) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        share note = noteList.get(pos);
        builder.setTitle("Delete note '"+note.getTitle()+"'?");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Log.d(TAG, "onActivityResult: yes  ");
                noteList.remove(pos);
                recyclerView.setAdapter(nAdapter);
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        share x = noteList.get(pos);
        builder.setTitle("Delete note '"+x.getTitle()+"'");
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    public static Comparator<share> noteComparator = new Comparator<share>() {

        @Override
        public int compare(share o1, share o2) {
            Date start = null,end=null;
            //DateFormat df = new SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss z");
            try {
                start = new SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss z").parse(o1.getName());
                end = new SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss z").parse(o2.getName());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Log.d(TAG, "parseJSON: " + o1.getName());

            return end.compareTo(start);
        }

    };
    public void writeNotepad(List<share> qnotes, MainActivity ma) {
        try {
            FileOutputStream fos = ma.getApplicationContext().openFileOutput(getString(R.string.file_name1), Context.MODE_PRIVATE);
            JsonWriter writer = new JsonWriter(new OutputStreamWriter(fos, ma.getString(R.string.encoding)));
            writer.setIndent("  ");
            writeMessagesArray(writer, qnotes);
            writer.close();
        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    public void writeMessagesArray(JsonWriter writer, List<share> qnotes) throws
            IOException {
        writer.beginArray();
        for ( share qnote :qnotes) {
            writeMessage(writer, qnote);
        }
        writer.endArray();
    }
    public void writeMessage(JsonWriter writer, share qNote) throws IOException {
        writer.beginObject();
        writer.name("ID").value(qNote.getID());
        writer.name("Title").value(qNote.getTitle());
        writer.name("Name").value(qNote.getName());
        writer.name("Description").value(qNote.getDescription());
        writer.endObject();
    }
}
