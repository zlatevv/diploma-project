package com.zlatev.tournament_management_system.exception.tournament;

public class NotEnoughTeamsException extends RuntimeException {
    public NotEnoughTeamsException(String message) {
        super(message);
    }
}
