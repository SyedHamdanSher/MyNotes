package com.shertech.mynotes;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.JsonWriter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class eNode extends AppCompatActivity {

    TextView tvTime;
    EditText etNote;
    EditText etTitle;
    private share msp;
    private String a, b,time;
    Calendar calendar;
    String title;
    private static final int B_REQ = 1;
    SimpleDateFormat simpleDateFormat;
    private static final String TAG = "eNode";
    Button btClear;
    String Initial,Initiale;
    private static final int RESULT = 2;
    private static int Flag=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_e_node);
        tvTime= (TextView) findViewById(R.id.tvTime);
        etNote=(EditText) findViewById(R.id.etNote);
        etTitle=(EditText) findViewById(R.id.etTitle);
        btClear=(Button) findViewById(R.id.btClear);
        etNote.post(new Runnable() {
            @Override
            public void run() {
                etNote.setSelection(etNote.getText().length());
            }
        });
        title = etTitle.getText().toString();
        Intent intent = getIntent();
        if (intent.hasExtra(share.class.getName())) {
            msp = (share) intent.getSerializableExtra(share.class.getName());
            Initial = msp.getTitle();
            Initiale = msp.getDescription();

            if(msp!=null ) {

                a = new String(msp.getTitle());
                b = new String(msp.getDescription());
                //Log.d(TAG, "onCreate: "+tC+" - "+msp.getTitle()+" "+nC+" - "+ msp.getQuickNotes());
            }
        }
        /*calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss z");
        Log.d(TAG, "onCreate: ");
        //doAsyncLoad();*/
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (msp != null) {
            etTitle.setText(msp.getTitle());
            if(msp.getTitle().length()>0) {
                etTitle.setSelection(msp.getTitle().length());
            }
            etNote.setText(msp.getDescription());
            tvTime.setText(msp.getName());
            if(msp.getDescription().length()>0) {
                etNote.setSelection(msp.getDescription().length());
            }
        }
        Log.d(TAG, "onResume: ");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        outState.putString("Initial",Initial);
        outState.putString("Initiale",Initiale);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Initial=savedInstanceState.getString("Initial");
        Initiale=savedInstanceState.getString("Initiale");
    }
    /*private void doAsyncLoad() {
        AsyncLoaderTask alt = new AsyncLoaderTask(this,msp);
        alt.execute();
    }*/

    //public void setMSP(share s){
    //  msp=s;
    //}


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.enodesave, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.saveNoteIC:
                if(!(a.equals(etTitle.getText().toString())&&b.equals(etNote.getText().toString()))) {
                    msp.setTitle(etTitle.getText().toString());
                    msp.setDescription(etNote.getText().toString());
                    DateFormat df = new SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss z");
                    String date = df.format(Calendar.getInstance().getTime());
                    msp.setName(date);
                }/*else if(!etTitle.getText().toString().equals("")){
                    msp.setTitle(etTitle.getText().toString());
                    msp.setDescription(etNote.getText().toString());
                    DateFormat df = new SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss z");
                    String date = df.format(Calendar.getInstance().getTime());
                    msp.setName(date);
                }*/
                Intent data = new Intent();
                data.putExtra("USER_TEXT", msp);
                setResult(RESULT, data);
                //startActivity(data);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if(!(a.equals(etTitle.getText().toString())&&b.equals(etNote.getText().toString()))) {
            msp.setTitle(etTitle.getText().toString());
            msp.setDescription(etNote.getText().toString());
            DateFormat df = new SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss z");
            String date = df.format(Calendar.getInstance().getTime());
            msp.setName(date);
        }
    }

    private void SAVEPARENT(){
        String str = etTitle.getText().toString();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if(!(a.equals(etTitle.getText().toString())&&b.equals(etNote.getText().toString()))) {
                    msp.setTitle(etTitle.getText().toString());
                    msp.setDescription(etNote.getText().toString());
                    DateFormat df = new SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss z");
                    String date = df.format(Calendar.getInstance().getTime());
                    msp.setName(date);
                }
                Intent data = new Intent();
                data.putExtra("USER_TEXT", msp);
                setResult(RESULT, data);
                finish();
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                share note = null;
                Intent data = new Intent();
                data.putExtra("USER_TEXT", note);
                setResult(RESULT, data);
                finish();
            }
        });
        builder.setTitle("Your Note is not saved!");
        builder.setMessage("Save note '"+str+ "'");

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    @Override
    public void onBackPressed() {
        if(!(a.equals(etTitle.getText().toString())&&b.equals(etNote.getText().toString()))) {
            SAVEPARENT();
        }
        else if(!etTitle.getText().toString().equals(Initial)||!etNote.getText().toString().equals(Initiale)){
            SAVEPARENT();
        }else{
            super.onBackPressed();
            finish();
        }
    }
}
