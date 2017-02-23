package com.shertech.mynotes;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener{

    private List<displaynote> noteList = new ArrayList<>();  // Main content is here
    private RecyclerView recyclerView; // Layout's recyclerview
    private nodesAdapter nAdapter; // Data to recyclerview adapter
    share s;
    displaynote d;
    private int n=0;
    String prev="@&@$";
    int B_REQ,x;
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
        d=new displaynote();
        msp=new share();

        builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.dinfo);
        builder.setTitle(R.string.infoT);
        builder.setNegativeButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            return;
            }
        });
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
        s=loadFile();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.addNote:
                Toast.makeText(this, "new note created", Toast.LENGTH_SHORT).show();
                Intent in = new Intent(MainActivity.this, eNode.class).putExtra(share.class.getName(),msp);
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
    private share loadFile(){
        Log.d(TAG, "maloadFile: Loading JSON File");

        try {
            InputStream ss = getApplicationContext().openFileInput("count.json");
            JsonReader r = new JsonReader(new InputStreamReader(ss,getString(R.string.encoding)));
            r.beginObject();
            while (r.hasNext()){
                String n = r.nextName();
                if(n.equals("counter")){
                    x=B_REQ=r.nextInt();
                }else{
                    r.skipValue();
                }
            }r.endObject();
            r.close();
            ss.close();
            for (int i = 0;i<=B_REQ;i++){
                InputStream is = getApplicationContext().openFileInput(getString(R.string.file_name));
                JsonReader reader = new JsonReader(new InputStreamReader(is, getString(R.string.encoding)));
                reader.beginObject();
                while (reader.hasNext()) {
                    String name = reader.nextName();
                    if (name.equals("etTitle"+i)) {
                        d.setTitle(reader.nextString());
                    }else if (name.equals("etNode"+i)) {
                        d.setDescription(reader.nextString());}
                    else if (name.equals("tvTime"+i)) {
                        d.setName(reader.nextString());
                    }else{
                        reader.skipValue();
                    }
                }

                reader.endObject();
                if (d.getTitle()!="" &&d.getTitle()!=prev) {
                    noteList.add(d);
                    nAdapter.notifyDataSetChanged();
                    prev=d.getTitle();
                }
            }

        } catch (FileNotFoundException e) {
            Toast.makeText(this, getString(R.string.no_file), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return s;
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
        displaynote d = noteList.get(pos);
        Toast.makeText(v.getContext(), "LONG " + d.toString(), Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    protected void onPause() {
        super.onPause();

        if(B_REQ==0 || B_REQ==x){
            return;
        }else{
            saveProduct();
        }

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
            FileOutputStream fos = getApplicationContext().openFileOutput("count.json", Context.MODE_PRIVATE);

            JsonWriter writer = new JsonWriter(new OutputStreamWriter(fos, getString(R.string.encoding)));
            writer.setIndent(" ");
            writer.beginObject();
            writer.name("counter").value(B_REQ);
            writer.name("prev").value(prev);
            writer.endObject();
            writer.close();

            Toast.makeText(this, getString(R.string.count), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.getStackTrace();
        }
    }
}
