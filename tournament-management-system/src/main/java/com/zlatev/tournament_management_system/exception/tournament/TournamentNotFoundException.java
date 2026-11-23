package com.zlatev.tournament_management_system.exception.tournament;

public class TournamentNotFoundException extends RuntimeException {
    public TournamentNotFoundException(String message) {
        super(message);
    }
}
