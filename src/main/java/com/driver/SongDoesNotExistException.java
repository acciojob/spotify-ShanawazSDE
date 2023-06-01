package com.driver;

public class SongDoesNotExistException extends RuntimeException{
    public SongDoesNotExistException(String s){
        super(s);
    }
}
