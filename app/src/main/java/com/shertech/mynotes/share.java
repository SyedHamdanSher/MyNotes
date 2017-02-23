package com.shertech.mynotes;

import java.io.Serializable;

/**
 * Created by lastwalker on 2/16/17.
 */

public class share implements Serializable{

    private String name;
    private String description;
    private String compare;
    private String title;
    private int fname;

    share(){
        compare="";
        name="";
        description="";
        title="";
        fname=999999;
    }
    public String getDescription() {
        return description;
    }

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
}
