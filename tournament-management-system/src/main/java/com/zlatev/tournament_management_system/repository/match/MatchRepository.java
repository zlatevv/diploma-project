package com.zlatev.tournament_management_system.repository.match;

import com.zlatev.tournament_management_system.entity.model.match.Match;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MatchRepository extends JpaRepository<Match, UUID> {
}
