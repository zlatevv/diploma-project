package com.zlatev.tournament_management_system.exception;

import com.zlatev.tournament_management_system.exception.match.*;
import com.zlatev.tournament_management_system.exception.scoring.InvalidScoringData;
import com.zlatev.tournament_management_system.exception.scoring.NoScoreToUndoException;
import com.zlatev.tournament_management_system.exception.scoring.ScoringTypeNotFoundException;
import com.zlatev.tournament_management_system.exception.sport.SportAlreadyExistsException;
import com.zlatev.tournament_management_system.exception.sport.SportDoesNotExistException;
import com.zlatev.tournament_management_system.exception.team.TeamAlreadyExistsException;
import com.zlatev.tournament_management_system.exception.team.TeamNotFoundException;
import com.zlatev.tournament_management_system.exception.team.TeamNotInMatchException;
import com.zlatev.tournament_management_system.exception.tournament.*;
import com.zlatev.tournament_management_system.exception.user.InvalidRefereeException;
import com.zlatev.tournament_management_system.exception.user.UserAlreadyExistsException;
import com.zlatev.tournament_management_system.exception.user.UserNotFoundException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 404 NOT FOUND
    @ExceptionHandler({
            UserNotFoundException.class,
            SportDoesNotExistException.class,
            TeamNotFoundException.class,
            TournamentNotFoundException.class,
            MatchNotFoundException.class,
            ScoringTypeNotFoundException.class
    })
    public ResponseEntity<Map<String, Object>> handleNotFound(RuntimeException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    // 409 CONFLICT
    @ExceptionHandler({
            UserAlreadyExistsException.class,
            SportAlreadyExistsException.class,
            TeamAlreadyExistsException.class,
            TournamentNotEditableException.class,
            NoScoreToUndoException.class,
            InvalidMatchStateException.class,
            MatchNotLiveException.class,
            MatchAlreadyFinishedException.class
    })
    public ResponseEntity<Map<String, Object>> handleConflict(RuntimeException ex) {
        return buildResponse(HttpStatus.CONFLICT, ex.getMessage());
    }

    // 400 BAD REQUEST
    @ExceptionHandler({
            InvalidScoringData.class,
            InvalidNumberOfTeamsException.class,
            NotEnoughTeamsException.class,
            UnsupportedTournamentFormatException.class,
            IllegalArgumentException.class,
            IllegalStateException.class,
            InvalidRefereeException.class,
            TeamNotInMatchException.class
    })
    public ResponseEntity<Map<String, Object>> handleBadRequest(RuntimeException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    // 400 VALIDATION ERRORS (@Valid DTOs)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(
            MethodArgumentNotValidException ex) {

        Map<String, String> fieldErrors = new HashMap<>();

        ex.getBindingResult().getFieldErrors()
                .forEach(err -> fieldErrors.put(
                        err.getField(),
                        err.getDefaultMessage()
                ));

        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", Instant.now().toString());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Validation Failed");
        body.put("errors", fieldErrors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    // 401 UNAUTHORIZED (Wrong email/password)
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Map<String, Object>> handleBadCredentials(
            BadCredentialsException ex) {

        return buildResponse(
                HttpStatus.UNAUTHORIZED,
                "Invalid email or password"
        );
    }

    // 403 FORBIDDEN (Not owner / insufficient role)
    @ExceptionHandler({
            NotTournamentOwnerException.class,
            AccessDeniedException.class,
            NotMatchOfficialException.class
    })
    public ResponseEntity<Map<String, Object>> handleForbidden(RuntimeException ex) {
        return buildResponse(HttpStatus.FORBIDDEN, ex.getMessage());
    }

    // Builds a consistent error response
    private ResponseEntity<Map<String, Object>> buildResponse(
            HttpStatus status,
            String message) {

        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", Instant.now().toString());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", message);

        return ResponseEntity.status(status).body(body);
    }
}
