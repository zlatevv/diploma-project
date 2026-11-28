package com.zlatev.tournament_management_system.exception.match;

public class MatchNotLiveException extends RuntimeException {
    public MatchNotLiveException(String message) {
        super(message);
    }
}
