package ch.epfl.swissteam.services.models;

/**
 * Simple observer for our observable
 * @author Sébastien gachoud
 */
public interface Observer {
    void update(Observable observable);
}