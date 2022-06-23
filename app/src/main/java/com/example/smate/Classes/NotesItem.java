package com.example.smate.Classes;

public class NotesItem {
    String name;
    String url;
//    String branch;
//    String sem;

    public NotesItem() {
    }

//    public NotesItem(String url, String branch, String sem,String name) {
//        this.url = url;
//        this.branch = branch;
//        this.sem = sem;
//        this.name = name;
//    }


    public NotesItem(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

//    public String getBranch() {
//        return branch;
//    }
//
//    public void setBranch(String branch) {
//        this.branch = branch;
//    }
//
//    public String getSem() {
//        return sem;
//    }
//
//    public void setSem(String sem) {
//        this.sem = sem;
//    }
}
