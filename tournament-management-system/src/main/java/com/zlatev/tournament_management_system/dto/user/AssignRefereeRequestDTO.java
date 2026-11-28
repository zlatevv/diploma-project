package com.zlatev.tournament_management_system.dto.user;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record AssignRefereeRequestDTO(
        @NotNull UUID refereeId
) {}
