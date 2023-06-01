package com.driver;

public class UserDoesNotExistException extends RuntimeException{
    public UserDoesNotExistException(String s){
        super(s);
    }
}
