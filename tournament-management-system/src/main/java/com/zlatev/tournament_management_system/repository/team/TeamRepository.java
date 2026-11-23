package com.zlatev.tournament_management_system.repository.team;

import com.zlatev.tournament_management_system.entity.model.team.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TeamRepository extends JpaRepository<Team, UUID> {
    Integer countByTournamentId(UUID id);

    void deleteByTournamentId(UUID tournamentId);

    ArrayList<Team> findByTournamentId(UUID tournamentId);

    Optional<Team> findByIdAndTournamentId(UUID teamId, UUID tournamentId);

    boolean existsByNameAndTournamentId(String name, UUID tournamentId);
}
