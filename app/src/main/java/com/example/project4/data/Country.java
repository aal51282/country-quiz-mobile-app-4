package com.example.project4.data;

public class Country {
    private int id;
    private String name;
    private String continent;

    public Country(int id, String name, String continent) {
        this.id = id;
        this.name = name;
        this.continent = continent;
    }

    // Getters and setters
    public int getId() { return id; }
    public String getName() { return name; }
    public String getContinent() { return continent; }
}