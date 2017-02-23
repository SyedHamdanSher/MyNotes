package com.shertech.mynotes;

/**
 * Created by lastwalker on 2/20/17.
 */

public class displaynote {

    private String name;
    private String description;
   // private String compare;
    private String title;
    //private int fname;

    displaynote(){
        name="";
        description="";
        title="";
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
    /*public int getFname() {
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
    }*/

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
