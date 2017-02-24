package com.shertech.mynotes;

import android.content.Context;
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
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class eNode extends AppCompatActivity {

    TextView tvTime;
    EditText etNote;
    EditText etTitle;
    share msp;
    Calendar calendar;
    String title;
    static int n=999999,x=99999;
    SimpleDateFormat simpleDateFormat;
    private static final String TAG = "eNode";
    Button btClear;
    AlertDialog dialog;
    AlertDialog.Builder builder;

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
            msp = (share) intent.getSerializableExtra(share.class.getName());}
        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss z");
        Log.d(TAG, "onCreate: ");
        if (msp==null){
            doAsyncLoad();
            if (msp != null) { // null means no file was loaded
                if(msp.getTitle()!=null && !msp.getTitle().matches("") || !etTitle.getText().toString().matches("")) {
                /*if(x== msp.getFname() && x!=99999){
                n=msp.getFname();}
                else{
                    n=x;
                }*/
                    tvTime.setText(msp.getName());
                    etNote.setText(msp.getDescription());
                    etTitle.setText(msp.getTitle());
                    title = etTitle.getText().toString();
                }else {
                    calendar = Calendar.getInstance();
                    tvTime.setText(simpleDateFormat.format(calendar.getTime()));
                }

            }
            Log.d(TAG, "oncreateResume: ");
        }else{
            tvTime.setText(msp.getName());
            etNote.setText(msp.getDescription());
            etTitle.setText(msp.getTitle());
            title = etTitle.getText().toString();
        }
    }
    private void doAsyncLoad() {
        AsyncLoaderTask alt = new AsyncLoaderTask(this,msp);
        alt.execute();
    }

    protected void setMSP(share s){
        msp=s;
    }

    /*public void clear(View view){
        etNote.setText("");
        msp.setDescription(etNote.getText().toString());
        msp.setCompare("");
        saveProduct();
    }*/

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
                CHECK();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (msp.getTitle()!=null && !msp.getTitle().matches("")&& !etTitle.getText().toString().matches("")){
            if(!(msp.getDescription().matches(msp.getCompare())) || !etNote.getText().toString().matches(msp.getDescription()) || !etTitle.getText().toString().matches(msp.getTitle())){
                calendar = Calendar.getInstance();
                msp.setName(simpleDateFormat.format(calendar.getTime()));
                msp.setDescription(etNote.getText().toString());
                msp.setCompare(etNote.getText().toString());
                msp.setTitle(etTitle.getText().toString());
                /*if(n==999999){
                msp.setFname(x);}
                else {
                    msp.setFname(n);
                }*/
                saveProduct();
            }
            else{
                Log.d(TAG, "onPause: ");
            }
        }
        else{
            if (!etTitle.getText().toString().matches("") ||!etTitle.getText().toString().matches(msp.getTitle())){
                calendar = Calendar.getInstance();
                msp.setName(simpleDateFormat.format(calendar.getTime()));
                msp.setDescription(etNote.getText().toString());
                msp.setCompare(etNote.getText().toString());
                msp.setTitle(etTitle.getText().toString());
                /*if(n==999999){
                    msp.setFname(x);}
                else {
                    msp.setFname(n);
                }*/
                saveProduct();
            }else {
                Log.d(TAG, "onPause: aa");
            }
        }
    }

    private void SAVEPARENT(share msp){
        Intent data = new Intent(this,MainActivity.class);
        data.putExtra(share.class.getName(),msp);
        startActivity(data);

    }
    private void saveProduct() {

        Log.d(TAG, "saveProduct: Saving JSON File");
        try {
            FileOutputStream fos = getApplicationContext().openFileOutput(getString(R.string.file_name), Context.MODE_PRIVATE);

            JsonWriter writer = new JsonWriter(new OutputStreamWriter(fos, getString(R.string.encoding)));
            writer.setIndent(" ");
            writer.beginObject();
           // writer.name("fname").value(msp.getFname());
            writer.name("tvTime").value(msp.getName());
            writer.name("etNode").value(msp.getDescription());
            writer.name("compare").value(msp.getCompare());
            writer.name("etTitle").value(msp.getTitle());
            writer.endObject();
            writer.close();
            Toast.makeText(this, getString(R.string.saved), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.getStackTrace();
        }
    }


   @Override
    public void onBackPressed() {
       if (msp.getTitle()==null || msp.getTitle()==""){
           Intent data = new Intent(this,MainActivity.class);
           startActivity(data);
       }else{
        SAVEPARENT(msp);
       }
        super.onBackPressed();
    }
    private void CHECK(){

        if (msp.getTitle()!=null && !msp.getTitle().matches("")&& !etTitle.getText().toString().matches("")){
            if(!(msp.getDescription().matches(msp.getCompare())) || !etNote.getText().toString().matches(msp.getDescription()) || !etTitle.getText().toString().matches(msp.getTitle())){
                calendar = Calendar.getInstance();
                msp.setName(simpleDateFormat.format(calendar.getTime()));
                msp.setDescription(etNote.getText().toString());
                msp.setCompare(etNote.getText().toString());
                msp.setTitle(etTitle.getText().toString());
                /*if(n==999999){
                msp.setFname(x);}
                else {
                    msp.setFname(n);
                }*/
                Intent data = new Intent(this,MainActivity.class);
                data.putExtra(share.class.getName(),msp);
                startActivity(data);
            }
            else{
                Log.d(TAG, "onPause: ");
                Intent data = new Intent(this,MainActivity.class);
                data.putExtra(share.class.getName(),msp);
                startActivity(data);
            }
        }
        else{
            if (!etTitle.getText().toString().matches("")){
                calendar = Calendar.getInstance();
                msp.setName(simpleDateFormat.format(calendar.getTime()));
                msp.setDescription(etNote.getText().toString());
                msp.setCompare(etNote.getText().toString());
                msp.setTitle(etTitle.getText().toString());
                /*if(n==999999){
                    msp.setFname(x);}
                else {
                    msp.setFname(n);
                }*/
                Intent data = new Intent(this,MainActivity.class);
                data.putExtra(share.class.getName(),msp);
                startActivity(data);
            }else {
                Toast.makeText(this,getString(R.string.no_title),Toast.LENGTH_SHORT).show();
            }
        }

    }
}
