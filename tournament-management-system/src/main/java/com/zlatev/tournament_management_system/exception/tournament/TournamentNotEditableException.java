package com.zlatev.tournament_management_system.exception.tournament;

public class TournamentNotEditableException extends RuntimeException {
    public TournamentNotEditableException(String message) {
        super(message);
    }
}
