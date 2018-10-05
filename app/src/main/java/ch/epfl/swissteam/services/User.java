package ch.epfl.swissteam.services;

import java.util.Arrays;

/**
 * Class representing a user in the database,
 * @author simonwicky
 */
public class User {



    private String name_, surname_, description_;
    private String[] categories_;

    /**
     * Default constructor, needed for database
     */
    public User(){

    }

    /**
     * Create a new user given its specificities
     * @param name_ User's name
     * @param surname_ User's surname
     * @param description_ User's description
     * @param categories_ User's categories of services
     */
    public User(String name_, String surname_, String description_, String... categories_) {
        this.name_ = name_;
        this.surname_ = surname_;
        this.description_ = description_;
        this.categories_ = categories_;
    }

    public String getName_() {
        return name_;
    }

    public String getSurname_() {
        return surname_;
    }

    public String getDescription_() {
        return description_;
    }

    public String[] getCategories() {
        return Arrays.copyOf(categories_, categories_.length);
    }


}
