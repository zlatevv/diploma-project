package com.zlatev.tournament_management_system.entity.model.tournament;

import com.zlatev.tournament_management_system.entity.enums.tournament.TournamentFormat;
import com.zlatev.tournament_management_system.entity.enums.tournament.TournamentStatus;
import com.zlatev.tournament_management_system.entity.model.group.Group;
import com.zlatev.tournament_management_system.entity.model.sport.Sport;
import com.zlatev.tournament_management_system.entity.model.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "tournament")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Tournament {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    private TournamentFormat format;

    @Enumerated(EnumType.STRING)
    private TournamentStatus status;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sport_id")
    private Sport sport;

    @OneToMany(mappedBy = "tournament", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Group> groups = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "organizer_id")
    private User organizer;
}