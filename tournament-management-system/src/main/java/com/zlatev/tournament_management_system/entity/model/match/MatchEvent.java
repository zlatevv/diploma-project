package com.zlatev.tournament_management_system.entity.model.match;

import com.zlatev.tournament_management_system.entity.model.scoring.ScoringType;
import com.zlatev.tournament_management_system.entity.model.team.Team;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "match_event")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MatchEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Match match;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Team team;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private ScoringType scoringType;

    private Integer matchMinute;
    private Instant createdAt;
}
