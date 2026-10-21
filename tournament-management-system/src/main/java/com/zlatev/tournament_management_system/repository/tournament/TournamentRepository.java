package com.zlatev.tournament_management_system.repository.tournament;

import com.zlatev.tournament_management_system.entity.model.tournament.Tournament;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TournamentRepository extends JpaRepository<Tournament, UUID> {
}
