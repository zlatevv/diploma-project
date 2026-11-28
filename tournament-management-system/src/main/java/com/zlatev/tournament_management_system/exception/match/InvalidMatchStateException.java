package com.zlatev.tournament_management_system.exception.match;

public class InvalidMatchStateException extends RuntimeException {
    public InvalidMatchStateException(String message) {
        super(message);
    }
}
