package com.shertech.mynotes;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.JsonWriter;
import android.util.Log;
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
        doAsyncLoad();
    }

    private void doAsyncLoad() {
        AsyncLoaderTask alt = new AsyncLoaderTask(this,msp);
        alt.execute();
    }

    protected void setMSP(share s){
        msp=s;
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Load the JSON containing the product data - if it exists
        if (msp != null) { // null means no file was loaded
            if(msp.getTitle()!=null && !msp.getTitle().matches("") || !etTitle.getText().toString().matches("")) {
                if(x== msp.getFname() && x!=99999){
                n=msp.getFname();}
                else{
                    n=x;
                }
                tvTime.setText(msp.getName());
                etNote.setText(msp.getDescription());
                etTitle.setText(msp.getTitle());
                title = etTitle.getText().toString();
            }else {
                calendar = Calendar.getInstance();
                tvTime.setText(simpleDateFormat.format(calendar.getTime()));
            }

        }
        Log.d(TAG, "onResume: ");
    }

    public void clear(View view){
        etNote.setText("");
        msp.setDescription(etNote.getText().toString());
        msp.setCompare("");
        saveProduct();
    }

    private share loadFile(){
        Log.d(TAG, "loadFile: Loading JSON File");
        try {
            InputStream is = getApplicationContext().openFileInput(getString(R.string.file_name));
            JsonReader reader = new JsonReader(new InputStreamReader(is, getString(R.string.encoding)));
            reader.beginObject();
            while (reader.hasNext()) {
                String name = reader.nextName();
                if (name.equals("fname")) {
                    msp.setFname(reader.nextInt());
                } else if (name.equals("etTitle"+n)) {
                    msp.setTitle(reader.nextString());
                }else if (name.equals("etNode"+n)) {
                    msp.setDescription(reader.nextString());}
                else if (name.equals("compare"+n)) {
                    msp.setCompare(reader.nextString());}
                else if (name.equals("tvTime"+n)) {
                    msp.setName(reader.nextString());
                }else{
                    reader.skipValue();
                }
            }
            reader.endObject();

        } catch (FileNotFoundException e) {
            Toast.makeText(this, getString(R.string.no_file), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return msp;
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
                if(n==999999){
                msp.setFname(x);}
                else {
                    msp.setFname(n);
                }
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
                if(n==999999){
                    msp.setFname(x);}
                else {
                    msp.setFname(n);
                }
                saveProduct();
            }else {
                Log.d(TAG, "onPause: aa");
            }
        }
    }
    private void saveProduct() {

        Log.d(TAG, "saveProduct: Saving JSON File");
        try {
            FileOutputStream fos = getApplicationContext().openFileOutput(getString(R.string.file_name), Context.MODE_PRIVATE);

            JsonWriter writer = new JsonWriter(new OutputStreamWriter(fos, getString(R.string.encoding)));
            writer.setIndent(" ");
            writer.beginObject();
            writer.name("fname").value(msp.getFname());
            writer.name("tvTime"+n).value(msp.getName());
            writer.name("etNode"+n).value(msp.getDescription());
            writer.name("compare"+n).value(msp.getCompare());
            writer.name("etTitle"+n).value(msp.getTitle());
            writer.endObject();
            writer.close();
            Toast.makeText(this, getString(R.string.saved), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.getStackTrace();
        }
    }


   /* @Override
    public void onBackPressed() {
        Intent data = new Intent();
        data.putExtra("B_REQB", n);
        setResult(RESULT_OK, data);
        super.onBackPressed();
    }*/
}
