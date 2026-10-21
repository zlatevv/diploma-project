package com.zlatev.tournament_management_system.repository.sport;

import com.zlatev.tournament_management_system.entity.model.sport.Sport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SportRepository extends JpaRepository<Sport, UUID> {
}
