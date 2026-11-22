package com.zlatev.tournament_management_system.entity.model.sport;

import com.zlatev.tournament_management_system.entity.model.scoring.ScoringType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "sport")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Sport {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "sport_name", nullable = false)
    private String sportName;

    @OneToMany(mappedBy = "sport", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ScoringType> scoringTypes = new ArrayList<>();
}
