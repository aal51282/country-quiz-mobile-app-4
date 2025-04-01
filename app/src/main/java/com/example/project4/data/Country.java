package com.example.project4.data;

public class Country {
    private int id;
    private String name;
    private String continent;

    public Country(int id, String name, String continent) {
        this.id = id;
        this.name = name;
        this.continent = continent;
    } // Country constructor

    // Getters and setters
    public int getId() {
        return id;
    } // getId
    public String getName() {
        return name;
    } // getName
    public String getContinent() {
        return continent;
    } // getContinent
} // Country