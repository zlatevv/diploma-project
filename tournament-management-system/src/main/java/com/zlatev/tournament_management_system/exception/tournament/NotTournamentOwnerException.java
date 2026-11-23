package com.zlatev.tournament_management_system.exception.tournament;

public class NotTournamentOwnerException extends RuntimeException {
    public NotTournamentOwnerException(String message) {
        super(message);
    }
}
