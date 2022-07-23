package com.api.auth.exception;

public class EntityException extends RuntimeException{
    private final static String MESSAGE = "Dados informado inválido";

    public EntityException() {
        super(MESSAGE);
    }
    
    public EntityException(String message) {
        super(message);
    }
    
}
