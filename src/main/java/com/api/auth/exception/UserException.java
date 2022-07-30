package com.api.auth.exception;

public class UserException extends RuntimeException{
    private final static String MESSAGE = "Dados informado inválido";

    public UserException() {
        super(MESSAGE);
    }
    
    public UserException(String message) {
        super(message);
    }
    
}
