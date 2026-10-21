package com.zlatev.tournament_management_system.repository.scoring;

import com.zlatev.tournament_management_system.entity.model.scoring.ScoringRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ScoringRuleRepository extends JpaRepository<ScoringRule, UUID> {
}
