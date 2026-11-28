package com.zlatev.tournament_management_system.exception.tournament;

public class TournamentNotStartedException extends RuntimeException {
    public TournamentNotStartedException(String message) {
        super(message);
    }
}
