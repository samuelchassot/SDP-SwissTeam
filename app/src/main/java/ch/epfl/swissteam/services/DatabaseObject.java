package ch.epfl.swissteam.services;

/**
 * Interface which propose some utility methods to interact with the database
 *
 * @author Adrian Baudat
 * @author Julie Giunta
 */
public interface DatabaseObject {

    /**
     * Load an object from the database
     *
     * @return a DatabaseObject
     */
    DatabaseObject load();

    /**
     * Store an object in the database
     */
    void store();
}
