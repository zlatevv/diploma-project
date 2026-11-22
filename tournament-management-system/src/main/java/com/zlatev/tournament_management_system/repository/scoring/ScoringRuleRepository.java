package com.zlatev.tournament_management_system.repository.scoring;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ScoringRuleRepository extends JpaRepository<ScoringRule, UUID> {
}
