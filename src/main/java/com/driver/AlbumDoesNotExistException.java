package com.driver;

public class AlbumDoesNotExistException extends RuntimeException{
    public AlbumDoesNotExistException(String s){
        super(s);
    }


}
