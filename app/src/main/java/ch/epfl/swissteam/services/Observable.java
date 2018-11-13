package ch.epfl.swissteam.services;

/**
 * Just a simple observable interface
 * Even if java has a class observable, we needed an interface
 * @author Sébastien gachoud
 */
public interface Observable {
    void addObserver(Observer observer);

    void removeObserver(Observer observer);

    void notifyObservers();
}
