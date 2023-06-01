package com.driver;

public class PlaylistDoesNotExistsException extends RuntimeException{
    public PlaylistDoesNotExistsException(String s){
        super(s);
    }
}
