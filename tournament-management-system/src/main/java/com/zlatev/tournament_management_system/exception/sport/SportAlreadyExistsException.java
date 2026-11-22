package com.zlatev.tournament_management_system.exception.sport;

public class SportAlreadyExistsException extends RuntimeException {
    public SportAlreadyExistsException(String message) {
        super(message);
    }
}
