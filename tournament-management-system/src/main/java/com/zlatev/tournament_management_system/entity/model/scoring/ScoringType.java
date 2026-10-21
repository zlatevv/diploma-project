package com.zlatev.tournament_management_system.entity.model.scoring;

import com.zlatev.tournament_management_system.entity.model.sport.Sport;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "scoring_type")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScoringType {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;
    private Integer points;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sport_id")
    private Sport sport;
}