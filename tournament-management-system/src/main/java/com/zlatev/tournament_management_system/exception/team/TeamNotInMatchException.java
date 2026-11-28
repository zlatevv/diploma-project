package com.zlatev.tournament_management_system.exception.team;

public class TeamNotInMatchException extends RuntimeException {
    public TeamNotInMatchException(String message) {
        super(message);
    }
}
