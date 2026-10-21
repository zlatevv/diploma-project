package com.zlatev.tournament_management_system.repository.match;

import com.zlatev.tournament_management_system.entity.model.match.MatchEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MatchEventRepository extends JpaRepository<MatchEvent, UUID> {
}
