package ch.epfl.swissteam.services;

public class Utility {
    public static class IllegalCallException extends Exception{
        IllegalCallException(String m){
            super(m);
        }
    }
}
