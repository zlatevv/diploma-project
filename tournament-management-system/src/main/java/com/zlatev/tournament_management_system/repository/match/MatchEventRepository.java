package com.zlatev.tournament_management_system.repository.match;

import com.zlatev.tournament_management_system.entity.model.match.MatchEvent;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MatchEventRepository extends JpaRepository<MatchEvent, UUID> {
    @EntityGraph(attributePaths = {"team", "scoringType"})
    List<MatchEvent> findByMatchIdOrderByCreatedAtAsc(UUID matchId);

    Optional<MatchEvent> findTopByMatchIdOrderByCreatedAtDesc(UUID matchId);

    @Query("""
            select coalesce(sum(e.scoringType.points), 0)
            from MatchEvent e
            where e.match.id = :matchId and e.team.id = :teamId
            """)
    int sumPoints(@Param("matchId") UUID matchId, @Param("teamId") UUID teamId);
}
