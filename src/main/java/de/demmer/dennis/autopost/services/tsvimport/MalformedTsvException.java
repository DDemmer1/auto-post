package de.demmer.dennis.autopost.services.tsvimport;

public class MalformedTsvException extends Exception {

    private int row;


    public  MalformedTsvException(String errorMessage, int row){
        super(errorMessage);
        this.row = row;
    }

    public int getRow() {
        return row;
    }
}
