package com.zlatev.tournament_management_system.entity.model.group;


import com.zlatev.tournament_management_system.entity.model.team.Team;
import com.zlatev.tournament_management_system.entity.model.tournament.Tournament;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "tournament_group")
@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class Group {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tournament_id")
    private Tournament tournament;

    @ManyToMany
    @JoinTable(name = "tournament_group_team",
            joinColumns = @JoinColumn(name = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "team_id"))
    @Builder.Default
    private List<Team> teams = new ArrayList<>();
}

