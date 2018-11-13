package ch.epfl.swissteam.services;

/**
 * Simple observer for our observable
 * @author Sébastien gachoud
 */
public interface Observer {
    void update(Observable observable);
}