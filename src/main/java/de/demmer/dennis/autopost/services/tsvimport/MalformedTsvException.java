package de.demmer.dennis.autopost.services.tsvimport;

public class MalformedTsvException extends Exception {

    public  MalformedTsvException(String errorMessage){
        super(errorMessage);
    }

}
