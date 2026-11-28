package com.zlatev.tournament_management_system.repository.scoring;

import com.zlatev.tournament_management_system.entity.model.scoring.ScoringType;
import com.zlatev.tournament_management_system.entity.model.sport.Sport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Repository
public interface ScoringTypeRepository extends JpaRepository<ScoringType, UUID> {
    List<ScoringType> findBySportId(UUID sportId);
    boolean existsBySportIdAndNameIgnoreCase(UUID sportId, String name);
}
