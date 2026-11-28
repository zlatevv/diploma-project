package com.zlatev.tournament_management_system.exception.match;

public class MatchAlreadyFinishedException extends RuntimeException {
    public MatchAlreadyFinishedException(String message) {
        super(message);
    }
}
