package com.dev.myapplication;

public class Contacts {
    private int id;
    private String name;
    private String description;
    private String duratiom;

    private String date;
    private String time;


    Contacts(String name, String desc,String duration,String date,String time) {
        this.name = name;
        this.description = desc;
        this.duratiom = duration;
        this.date = date;
        this.time = time;

    }
    Contacts(int id, String name,String desc,String duration,String date,String time) {
        this.id = id;
        this.name = name;
        this.description = desc;
        this.duratiom = duration;
        this.date = date;
        this.time = time;
    }
    int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDuratiom() {
        return duratiom;
    }

    public void setDuratiom(String duratiom) {
        this.duratiom = duratiom;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
