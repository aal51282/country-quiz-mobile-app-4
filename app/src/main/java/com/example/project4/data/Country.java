package com.example.project4.data;

/**
 * Represents a country with its ID, name, and continent.
 */
public class Country {
    private int id;
    private String name;
    private String continent;

    /**
     * Constructs a Country object with the given ID, name, and continent.
     * @param id The unique identifier for the country.
     * @param name The name of the country.
     * @param continent The continent to which the country belongs.
     */
    public Country(int id, String name, String continent) {
        this.id = id;
        this.name = name;
        this.continent = continent;
    } // Country constructor

    // Getters and setters

    /**
     * Returns the unique identifier for the country.
     * @return The ID of the country.
     */
    public int getId() {
        return id;
    } // getId

    /**
     * Returns the name of the country.
     * @return The name of the country.
     */
    public String getName() {
        return name;
    } // getName

    /**
     * Returns the continent to which the country belongs.
     * @return The continent of the country.
     */
    public String getContinent() {
        return continent;
    } // getContinent
} // Country