package com.zlatev.tournament_management_system.entity.model.match;

import com.zlatev.tournament_management_system.entity.model.scoring.ScoringType;
import com.zlatev.tournament_management_system.entity.model.team.Team;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "match_event")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MatchEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "match_id")
    private Match match;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "team_id")
    private Team team;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "scoring_type_id")
    private ScoringType scoringType;

    private Integer matchMinute;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @PrePersist
    void onCreate() { createdAt = Instant.now(); }
}
