package com.shertech.mynotes;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by lastwalker on 2/16/17.
 */

public class share implements Serializable{


    private String name;
    private String description;
    private String compare;
    private String title;
    private int fname,ID;

    share(int c){
        compare="";
        description="";
        DateFormat df = new SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss z");
        String date = df.format(Calendar.getInstance().getTime());
        title="";
        name=date;
        //fname=999999;
        ID = c;
    }
    share(){}

    share(int id,String title,String time,String notes){
        compare="";
        description=notes;
        DateFormat df = new SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss z");
        String date = df.format(Calendar.getInstance().getTime());
        this.title=title;
        name=time;
        //fname=999999;
        ID = id;
    }
    public String getDescription() {
        return description;
    }
    public void setID(int id) {
        this.ID = id;
    }
    public int getID() { return ID;  }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public int getFname() {
        return fname;
    }

    public void setFname(int fname) {
        this.fname = fname;
    }

    public String getCompare() {
        return compare;
    }

    public void setCompare(String compare) {
        this.compare = compare;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return title+"("+name+"),"+description;
    }
}
