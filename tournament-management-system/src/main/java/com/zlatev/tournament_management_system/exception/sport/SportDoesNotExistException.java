package com.zlatev.tournament_management_system.exception.sport;

public class SportDoesNotExistException extends RuntimeException {
    public SportDoesNotExistException(String message) {
        super(message);
    }
}
