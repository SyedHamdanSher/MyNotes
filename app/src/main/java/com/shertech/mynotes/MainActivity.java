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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener{

    public List<share> noteList = new ArrayList<>();  // Main content is here
    private RecyclerView recyclerView; // Layout's recyclerview
    private nodesAdapter nAdapter; // Data to recyclerview adapter
    //share s;
    //displaynote d;
    share msp;
    private static final String TAG = "MainActivity";

    AlertDialog dialog;
    AlertDialog.Builder builder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.rvID);

        nAdapter = new nodesAdapter(noteList, this);

        recyclerView.setAdapter(nAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //d=new displaynote();
        Intent intent = getIntent();
        if (intent.hasExtra(share.class.getName())) {
            msp = (share) intent.getSerializableExtra(share.class.getName());
        }

        builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.dinfo);
        builder.setTitle(R.string.infoT);
        builder.setNegativeButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            return;
            }
        });
        if (noteList.size()==0&&msp!=null){
            new MyAsyncTask(this).execute();
            noteList.add(0,msp);
            nAdapter.notifyDataSetChanged();
        }else {
            List<share> flag=noteList;
            new MyAsyncTask(this).execute();
            if(msp!=null&&flag.size()!=0){
            for(share x:flag){
                noteList.add(0,x);
            }
            noteList.add(0,msp);
            nAdapter.notifyDataSetChanged();}
            else{
                if(msp!=null){
                    noteList.add(0,msp);
                    nAdapter.notifyDataSetChanged();
                }else{
                    Log.d(TAG, "onCreate:ADSAD");
                }
            }
        }


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

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.addNote:
                Toast.makeText(this, "new note created", Toast.LENGTH_SHORT).show();
                Intent in = new Intent(MainActivity.this, eNode.class).putExtra(share.class.getName(),new share());
                startActivity(in);
                return true;
            case R.id.dinfo:
                dialog = builder.create();
                dialog.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {
        int pos = recyclerView.getChildLayoutPosition(v);
        Toast.makeText(v.getContext(),"u pressed:"+Integer.toString(pos),Toast.LENGTH_SHORT).show();
        /*Intent intent = new Intent(MainActivity.this, eNode.class);
        intent.putExtra();
        startActivity(intent);*/

        //Toast.makeText(v.getContext(), "SHORT " + s.toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onLongClick(View v) {
        int pos = recyclerView.getChildLayoutPosition(v);
        share msp = noteList.get(pos);
        Toast.makeText(v.getContext(), "LONG " + msp.toString(), Toast.LENGTH_SHORT).show();
        return false;
    }
    public void setNoteList(List<share> NL){
        noteList=NL;
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveProduct();
    }

   /* @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == B_REQ) {
            if (resultCode == RESULT_OK) {
                Log.d(TAG, "onActivityResult: result Code: " + resultCode);
            }

        } else {
            Log.d(TAG, "onActivityResult: Request Code " + requestCode);
        }
    }*/

    private void saveProduct() {
        Log.d(TAG, "saveProduct: Saving JSON File");
        try {
            FileOutputStream fos = getApplicationContext().openFileOutput(getString(R.string.file_name)+"1", Context.MODE_PRIVATE);
            if(noteList!=null){
                if(noteList.size()!=0){
                    writeJsonStream(fos,noteList);
                }else {
                    Toast.makeText(this, getString(R.string.no_file), Toast.LENGTH_SHORT).show();
                }

            Toast.makeText(this, getString(R.string.count), Toast.LENGTH_SHORT).show();}
            else {
                Log.d(TAG, "saveProduct: ");
            }
        } catch (Exception e) {
            e.getStackTrace();
        }
    }
    public void writeJsonStream(FileOutputStream out, List<share> noteList) throws IOException {
            Log.d(TAG, "writeJsonStream: ");
            JsonWriter writer = new JsonWriter(new OutputStreamWriter(out, getString(R.string.encoding)));
            writer.setIndent("  ");
            writeNoteArray(writer, noteList);
            writer.close();
    }

    public void writeNoteArray(JsonWriter writer, List<share> noteList) throws IOException {
            Log.d(TAG, "writeNoteArray: ");
            writer.beginArray();
            for (share note : noteList) {
                writeNote(writer, note);
            }
            writer.endArray();
    }

    public void writeNote(JsonWriter writer, share note) throws IOException {
        writer.beginObject();
        writer.name("Title").value(note.getTitle());
        writer.name("Name").value(note.getName());
        writer.name("Description").value(note.getDescription());
        writer.endObject();
        Log.d(TAG, "writeNote: ");
    }
    class MyAsyncTask extends AsyncTask<List<share>,Void,List<share>> {
        MainActivity ma;
        List<share> NL;


        MyAsyncTask(MainActivity ma) {
            this.ma = ma;
        }

        @Override
        protected List<share> doInBackground(List<share>... params) {
            Log.d(TAG, "maloadFile: Loading JSON File");

            try {
                InputStream ss = getApplicationContext().openFileInput(getString(R.string.file_name) + "1");
                NL = readJsonStream(ss);

            } catch (FileNotFoundException e) {
                Toast.makeText(ma.getApplicationContext(), getString(R.string.no_file), Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return NL;
        }

        public List<share> readJsonStream(InputStream in) throws IOException {
            Log.d(TAG, "readJsonStream: ");
            JsonReader reader = new JsonReader(new InputStreamReader(in, getString(R.string.encoding)));
            try {
                return readNoteArray(reader);
            } finally {
                reader.close();
            }
        }

        public List<share> readNoteArray(JsonReader reader) throws IOException {
            Log.d(TAG, "readNoteArray: ");
            List<share> nL = new ArrayList<>();

            reader.beginArray();
            while (reader.hasNext()) {
                nL.add(readMessage(reader));
            }
            reader.endArray();
            return nL;
        }
        @Override
        protected void onPostExecute(List<share> shares) {
            ma.setNoteList(shares);
        }

        public share readMessage(JsonReader reader) throws IOException {
            Log.d(TAG, "readMessage: ");
            share s = new share();

            reader.beginObject();
            while (reader.hasNext()) {
                String name = reader.nextName();
                if (name.equals("name")) {
                    s.setName(reader.nextString());
                } else if (name.equals("Title")) {
                    s.setTitle(reader.nextString());
                } else if (name.equals("Description")) {
                    s.setDescription(reader.nextString());
                } else {
                    reader.skipValue();
                }
            }
            reader.endObject();
            return s;
        }
    }

}
