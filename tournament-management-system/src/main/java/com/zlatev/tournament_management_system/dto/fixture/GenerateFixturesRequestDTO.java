package com.zlatev.tournament_management_system.dto.fixture;

import jakarta.validation.constraints.Positive;

public record GenerateFixturesRequestDTO(
        @Positive Integer numberOfGroups,
        @Positive Integer teamsPerGroup,
        @Positive Integer qualifyingTeamsPerGroup,
        Boolean doubleRoundRobin,
        Boolean playsEachOtherTwice
) {}
