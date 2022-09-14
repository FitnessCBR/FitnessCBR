package com.example.cbr__fitness.data;

/**
 * This class is created to match the lables of restrictions to their ID´s.
 * This enables a single query to be sufficient for the user creation.
 */
public class Limitation {

    private String name;

    private int ID;

    public Limitation(String name, int ID) {
        this.name = name;
        this.ID = ID;
    }

    public String getName(){
        return name;
    }
    public int getID(){
        return ID;
    }
}
