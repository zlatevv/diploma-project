package com.zlatev.tournament_management_system.scheduler.groupplayoff;

import com.zlatev.tournament_management_system.entity.model.tournament.Tournament;

public record GroupsPlayoffConfig(
        int numberOfGroups,
        int qualifyingTeamsPerGroup,
        int teamsPerGroup,
        boolean teamsPlayEachOtherTwice,
        Tournament tournament
) {}
